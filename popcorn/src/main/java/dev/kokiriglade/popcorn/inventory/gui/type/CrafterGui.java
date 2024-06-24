package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.util.InventoryBased;
import dev.kokiriglade.popcorn.inventory.gui.type.util.NamedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a gui in the form of a crafter.
 *
 * @since 3.0.0
 */
public class CrafterGui extends NamedGui implements InventoryBased {

    /**
     * Represents the inventory component for the input
     */
    @NonNull
    private InventoryComponent inputComponent = new InventoryComponent(3, 3);

    /**
     * Represents the inventory component for the player inventory
     */
    @NonNull
    private InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * Constructs a new crafter gui.
     *
     * @param title the title/name of this gui
     * @since 3.0.0
     */
    public CrafterGui(@NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new crafter gui for the given {@code plugin}.
     *
     * @param title the title/name of this gui
     * @param plugin the owning plugin of this gui
     * @see #CrafterGui(Component)
     * @since 3.0.0
     */
    public CrafterGui(@NonNull Component title, @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(@NonNull HumanEntity humanEntity) {
        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getInputComponent().display(getInventory(), 0);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        humanEntity.openInventory(getInventory());
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public CrafterGui copy() {
        CrafterGui gui = new CrafterGui(getTitle(), super.plugin);

        gui.inputComponent = inputComponent.copy();
        gui.playerInventoryComponent = playerInventoryComponent.copy();

        gui.setOnTopClick(this.onTopClick);
        gui.setOnBottomClick(this.onBottomClick);
        gui.setOnGlobalClick(this.onGlobalClick);
        gui.setOnOutsideClick(this.onOutsideClick);
        gui.setOnClose(this.onClose);

        return gui;
    }

    @Override
    public void click(@NonNull InventoryClickEvent event) {
        int rawSlot = event.getRawSlot();

        if (rawSlot >= 0 && rawSlot <= 8) {
            getInputComponent().click(this, event, rawSlot);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 9);
        }
    }

    @NonNull
    @Override
    public Inventory getInventory() {
        if (this.inventory == null) {
            this.inventory = createInventory();
        }

        return inventory;
    }

    @Contract(pure = true)
    @Override
    public boolean isPlayerInventoryUsed() {
        return getPlayerInventoryComponent().hasItem();
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.CRAFTER, getTitle());

        addInventory(inventory, this);

        return inventory;
    }

    @Contract(pure = true)
    @Override
    public int getViewerCount() {
        return getInventory().getViewers().size();
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public List<HumanEntity> getViewers() {
        return new ArrayList<>(getInventory().getViewers());
    }

    /**
     * Gets the inventory component representing the input.
     *
     * @return the input component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getInputComponent() {
        return inputComponent;
    }

    /**
     * Gets the inventory component representing the player inventory.
     *
     * @return the player inventory component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getPlayerInventoryComponent() {
        return playerInventoryComponent;
    }

}
