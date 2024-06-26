package dev.kokiriglade.popcorn.builder.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link ItemMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings({"unused"})
public final class ItemBuilder extends AbstractItemBuilder<ItemBuilder, ItemMeta> {

    private ItemBuilder(final @NonNull ItemStack itemStack, final @Nullable ItemMeta itemMeta) {
        super(itemStack, itemMeta != null
            ? itemMeta
            : Objects.requireNonNull(
            Bukkit.getItemFactory().getItemMeta(itemStack.getType())
        ));
    }

    /**
     * Creates an {@code ItemBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code ItemBuilder}
     * @since 1.0.0
     */
    public static @NonNull ItemBuilder of(final @NonNull ItemStack itemStack) {
        return new ItemBuilder(itemStack, itemStack.getItemMeta());
    }

    /**
     * Creates an {@code ItemBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code ItemBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item
     * @since 1.0.0
     */
    public static @NonNull ItemBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return ItemBuilder.of(getItem(material));
    }

}
