package dev.kokiriglade.popcorn.inventory.gui.type.abstraction;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An enchanting table inventory
 *
 * @since 3.0.0
 */
public abstract class EnchantingTableInventory {

    /**
     * The inventory holder
     */
    protected @NonNull InventoryHolder inventoryHolder;

    /**
     * Creates a new enchanting table inventory for the specified inventory holder
     *
     * @param inventoryHolder the inventory holder
     * @since 3.0.0
     */
    public EnchantingTableInventory(final @NonNull InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    /**
     * Opens the inventory for the specified player
     *
     * @param player the player to open the inventory for
     * @param title  the title of the inventory
     * @param items  the top items
     * @since 3.0.0
     */
    public abstract void openInventory(@NonNull Player player, @NonNull Component title, @Nullable ItemStack[] items);

    /**
     * Sends the top items to the inventory for the specified player.
     *
     * @param player the player for which to open the enchanting table
     * @param items  the items to send
     * @since 3.0.0
     */
    public abstract void sendItems(@NonNull Player player, @Nullable ItemStack[] items);

    /**
     * Clears the cursor of the specified player
     *
     * @param player the player to clear the cursor of
     * @since 3.0.0
     */
    public abstract void clearCursor(@NonNull Player player);

}
