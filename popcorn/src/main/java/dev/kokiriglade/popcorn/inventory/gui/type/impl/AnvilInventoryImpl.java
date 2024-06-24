package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.AnvilInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.util.CustomInventoryUtil;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

/**
 * Internal anvil inventory
 *
 * @since 3.0.0
 */
public class AnvilInventoryImpl extends AnvilInventory {

    public AnvilInventoryImpl(@NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public Inventory openInventory(@NonNull Player player, @NonNull Component title, @Nullable org.bukkit.inventory.ItemStack[] items) {
        int itemAmount = items.length;

        if (itemAmount != 3) {
            throw new IllegalArgumentException(
                "The amount of items for an anvil should be 3, but is '" + itemAmount + "'"
            );
        }

        ServerPlayer serverPlayer = getServerPlayer(player);

        CraftEventFactory.handleInventoryCloseEvent(serverPlayer, InventoryCloseEvent.Reason.OPEN_NEW);

        serverPlayer.containerMenu = serverPlayer.inventoryMenu;

        net.minecraft.network.chat.Component message = PaperAdventure.asVanilla(title);

        ContainerAnvilImpl containerAnvil = new ContainerAnvilImpl(serverPlayer, message);

        Inventory inventory = containerAnvil.getBukkitView().getTopInventory();

        inventory.setItem(0, items[0]);
        inventory.setItem(1, items[1]);
        inventory.setItem(2, items[2]);

        int containerId = containerAnvil.getContainerId();

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.ANVIL, message));
        serverPlayer.containerMenu = containerAnvil;
        serverPlayer.initMenu(containerAnvil);

        return inventory;
    }

    @Override
    public void sendItems(@NonNull Player player, @Nullable org.bukkit.inventory.ItemStack[] items) {
        NonNullList<ItemStack> nmsItems = CustomInventoryUtil.convertToNMSItems(items);
        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = serverPlayer.containerMenu.containerId;
        int state = serverPlayer.containerMenu.incrementStateId();
        ItemStack cursor = CraftItemStack.asNMSCopy(player.getItemOnCursor());
        ServerPlayerConnection playerConnection = serverPlayer.connection;

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, nmsItems, cursor));
    }

    @Override
    public void sendFirstItem(@NonNull Player player, org.bukkit.inventory.@Nullable ItemStack item) {
        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = serverPlayer.containerMenu.containerId;
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, state, 0, nmsItem));
    }

    @Override
    public void sendSecondItem(@NonNull Player player, org.bukkit.inventory.@Nullable ItemStack item) {
        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = serverPlayer.containerMenu.containerId;
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, state, 1, nmsItem));
    }

    @Override
    public void sendResultItem(@NonNull Player player, org.bukkit.inventory.@Nullable ItemStack item) {
        sendResultItem(player, CraftItemStack.asNMSCopy(item));
    }

    /**
     * Sends the result item to the specified player with the given item
     *
     * @param player the player to send the result item to
     * @param item the result item
     * @since 3.0.0
     */
    private void sendResultItem(@NonNull Player player, @NonNull ItemStack item) {
        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = serverPlayer.containerMenu.containerId;
        int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, state, 2, item));
    }

    @Override
    public void clearResultItem(@NonNull Player player) {
        sendResultItem(player, ItemStack.EMPTY);
    }

    @Override
    public void setCursor(@NonNull Player player, org.bukkit.inventory.@NonNull ItemStack item) {
        setCursor(player, CraftItemStack.asNMSCopy(item));
    }

    /**
     * Sets the cursor of the given player
     *
     * @param player the player to set the cursor
     * @param item the item to set the cursor to
     * @since 3.0.0
     */
    private void setCursor(@NonNull Player player, @NonNull ItemStack item) {
        ServerPlayer serverPlayer = getServerPlayer(player);
        int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(-1, state, -1, item));
    }

    @Override
    public void clearCursor(@NonNull Player player) {
        ServerPlayer serverPlayer = getServerPlayer(player);
        int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(-1, state, -1, ItemStack.EMPTY));
    }

    /**
     * Gets the server player associated to this player
     *
     * @param player the player to get the server player from
     * @return the server player
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    private ServerPlayer getServerPlayer(@NonNull Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * A custom container anvil for responding to item renaming
     *
     * @since 3.0.0
     */
    private class ContainerAnvilImpl extends AnvilMenu {

        /**
         * Creates a new custom anvil container for the specified player
         *
         * @param serverPlayer the player for whom this anvil container is
         * @param title the title of the inventory
         * @since 3.0.0
         */
        public ContainerAnvilImpl(@NonNull ServerPlayer serverPlayer, net.minecraft.network.chat.Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory(),
                ContainerLevelAccess.create(serverPlayer.getCommandSenderWorld(), new BlockPos(0, 0, 0)));

            this.checkReachable = false;
            this.cost.set(AnvilInventoryImpl.super.cost);

            setTitle(title);

            Slot originalSlot = this.slots.get(2);

            this.slots.set(2, new Slot(originalSlot.container, originalSlot.index, originalSlot.x, originalSlot.y) {
                @Override
                public void onTake(net.minecraft.world.entity.player.@NonNull Player player, @NonNull ItemStack stack) {
                    originalSlot.onTake(player, stack);
                }
            });
        }

        @Override
        public boolean setItemName(@Nullable String name) {
            name = name == null ? "" : name;

            /* Only update if the name is actually different. This may be called even if the name is not different,
               particularly when putting an item in the first slot. */
            if (!name.equals(AnvilInventoryImpl.super.observableText.get())) {
                AnvilInventoryImpl.super.observableText.set(name);
            }

            //the client predicts the output result, so we broadcast the state again to override it
            broadcastFullState();
            return true; //no idea what this is for
        }

        @Override
        public void createResult() {}

        @Override
        public void removed(net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {}

        @Override
        protected void clearContainer(net.minecraft.world.entity.player.@NonNull Player player,
                                      @NonNull Container inventory) {}

        @Override
        protected void onTake(net.minecraft.world.entity.player.@NonNull Player player, @NonNull ItemStack stack) {}

        public int getContainerId() {
            return this.containerId;
        }
    }

}
