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
 * Represents a gui in the form of a blast furnace
 *
 * @since 3.0.0
 */
public class BlastFurnaceGui extends NamedGui implements InventoryBased {

    /**
     * Represents the inventory component for the ingredient
     */
    private @NonNull InventoryComponent ingredientComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the fuel
     */
    private @NonNull InventoryComponent fuelComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the output
     */
    private @NonNull InventoryComponent outputComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the player inventory
     */
    private @NonNull InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * Constructs a new GUI
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public BlastFurnaceGui(final @NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new blast furnace gui for the given {@code plugin}.
     *
     * @param title  the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #BlastFurnaceGui(Component)
     * @since 3.0.0
     */
    public BlastFurnaceGui(final @NonNull Component title, final @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(final @NonNull HumanEntity humanEntity) {
        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getIngredientComponent().display(getInventory(), 0);
        getFuelComponent().display(getInventory(), 1);
        getOutputComponent().display(getInventory(), 2);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            final HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        humanEntity.openInventory(getInventory());
    }

    @Contract(pure = true)
    @Override
    public @NonNull BlastFurnaceGui copy() {
        final BlastFurnaceGui gui = new BlastFurnaceGui(getTitle(), super.plugin);

        gui.ingredientComponent = ingredientComponent.copy();
        gui.fuelComponent = fuelComponent.copy();
        gui.outputComponent = outputComponent.copy();
        gui.playerInventoryComponent = playerInventoryComponent.copy();

        gui.setOnTopClick(this.onTopClick);
        gui.setOnBottomClick(this.onBottomClick);
        gui.setOnGlobalClick(this.onGlobalClick);
        gui.setOnOutsideClick(this.onOutsideClick);
        gui.setOnClose(this.onClose);

        return gui;
    }

    @Override
    public void click(final @NonNull InventoryClickEvent event) {
        final int rawSlot = event.getRawSlot();

        if (rawSlot == 0) {
            getIngredientComponent().click(this, event, 0);
        } else if (rawSlot == 1) {
            getFuelComponent().click(this, event, 0);
        } else if (rawSlot == 2) {
            getOutputComponent().click(this, event, 0);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 3);
        }
    }

    @Override
    public @NonNull Inventory getInventory() {
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

    @Contract(pure = true)
    @Override
    public @NonNull Inventory createInventory() {
        return Bukkit.createInventory(this, InventoryType.BLAST_FURNACE, getTitle());
    }

    @Contract(pure = true)
    @Override
    public int getViewerCount() {
        return getInventory().getViewers().size();
    }

    @Contract(pure = true)
    @Override
    public @NonNull List<HumanEntity> getViewers() {
        return new ArrayList<>(getInventory().getViewers());
    }

    /**
     * Gets the inventory component representing the ingredient
     *
     * @return the ingredient component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getIngredientComponent() {
        return ingredientComponent;
    }

    /**
     * Gets the inventory component representing the fuel
     *
     * @return the fuel component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getFuelComponent() {
        return fuelComponent;
    }

    /**
     * Gets the inventory component representing the output
     *
     * @return the output component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getOutputComponent() {
        return outputComponent;
    }

    /**
     * Gets the inventory component representing the player inventory
     *
     * @return the player inventory component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getPlayerInventoryComponent() {
        return playerInventoryComponent;
    }

}
