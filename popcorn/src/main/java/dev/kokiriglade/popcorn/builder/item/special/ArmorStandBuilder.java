package dev.kokiriglade.popcorn.builder.item.special;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import dev.kokiriglade.popcorn.builder.item.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link ArmorStandMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class ArmorStandBuilder extends AbstractItemBuilder<ArmorStandBuilder, ArmorStandMeta> {

    private ArmorStandBuilder(final @NonNull ItemStack itemStack, final @NonNull ArmorStandMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates an {@code ArmorStandBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code ArmorStandBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull ArmorStandBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new ArmorStandBuilder(itemStack, castMeta(itemStack.getItemMeta(), ArmorStandMeta.class));
    }

    /**
     * Creates an {@code ArmorStandBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code ArmorStandBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull ArmorStandBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return ArmorStandBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code ArmorStandBuilder} of type {@link Material#ARMOR_STAND}. A convenience method.
     *
     * @return instance of {@code ArmorStandBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull ArmorStandBuilder ofArmorStand() throws IllegalArgumentException {
        return ofType(Material.ARMOR_STAND);
    }

    /**
     * Gets whether the armor stand is invisible.
     *
     * @return whether the armor stand is invisible
     * @since 1.0.0
     */
    public boolean invisible() {
        return this.itemMeta.isInvisible();
    }

    /**
     * Sets whether the armor stand is invisible.
     *
     * @param invisible whether the armor stand is invisible
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull ArmorStandBuilder invisible(final boolean invisible) {
        this.itemMeta.setInvisible(invisible);
        return this;
    }

    /**
     * Gets whether the armor stand is small.
     *
     * @return whether the armor stand is small
     * @since 1.0.0
     */
    public boolean small() {
        return this.itemMeta.isSmall();
    }

    /**
     * Sets whether the armor stand is small.
     *
     * @param small whether the armor stand is small
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull ArmorStandBuilder small(final boolean small) {
        this.itemMeta.setSmall(small);
        return this;
    }

    /**
     * Gets whether the armor stand shows its arms.
     *
     * @return whether the armor stand shows arms
     * @since 1.0.0
     */
    public boolean showArms() {
        return this.itemMeta.shouldShowArms();
    }

    /**
     * Sets whether the armor stand shows its arms.
     *
     * @param showArms whether the armor stand shows arms
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull ArmorStandBuilder showArms(final boolean showArms) {
        this.itemMeta.setShowArms(showArms);
        return this;
    }

    /**
     * Gets whether the armor stand has no base plate.
     *
     * @return whether the armor stand has no base plate
     * @since 1.0.0
     */
    public boolean noBasePlate() {
        return this.itemMeta.hasNoBasePlate();
    }

    /**
     * Sets whether the armor stand has no base plate.
     *
     * @param noBasePlate whether the armor stand has no base plate
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull ArmorStandBuilder noBasePlate(final boolean noBasePlate) {
        this.itemMeta.setNoBasePlate(noBasePlate);
        return this;
    }

    /**
     * Gets whether the armor stand is a marker.
     *
     * @return whether the armor stand is a marker
     * @since 1.0.0
     */
    public boolean marker() {
        return this.itemMeta.isMarker();
    }

    /**
     * Sets whether the armor stand is a marker.
     *
     * @param marker whether the armor stand is a marker
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull ArmorStandBuilder marker(final boolean marker) {
        this.itemMeta.setMarker(marker);
        return this;
    }

}
