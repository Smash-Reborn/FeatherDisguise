package org.reborn.FeatherDisguise.wrappers.dimension;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

@Getter
public final class EntityDimension implements Cloneable {

    private float width;
    @Setter private float height;
    @Setter private float eyeHeight;

    private float precalculatedHalfWidth;


    // region Constructors

    public EntityDimension(final float width, final float height, final float eyeHeight) {
        this.width = width;
        this.height = height;
        this.eyeHeight = eyeHeight;
        this.recalculateHalfWidth();
    }

    public EntityDimension(final float width, final float height) {
        this(width, height, defaultEyeHeight(height));
    }

    public EntityDimension() {
        this(0.5f, 0.5f);
    }

    // endregion

    // region Accessors

    public static float defaultEyeHeight(float height) {
        return height * 0.85f;
    }

    public void setWidth(final float width) {
        this.width = width;
        this.recalculateHalfWidth();
    }

    public void recalculateHalfWidth() {
        this.precalculatedHalfWidth = this.width / 2.0f;
    }

    @NotNull public AxisAlignedBB boundingBoxAtPosition(@NotNull final Location position) {
        return this.boundingBoxAtPosition(position.getX(), position.getY(), position.getZ());
    }

    @NotNull public AxisAlignedBB boundingBoxAtPosition(@NotNull final Vector position) {
        return this.boundingBoxAtPosition(position.getX(), position.getY(), position.getZ());
    }

    @NotNull public AxisAlignedBB boundingBoxAtPosition(final double posX, final double posY, final double posZ) {
        return new AxisAlignedBB(
                posX - this.precalculatedHalfWidth, posY, posZ - this.precalculatedHalfWidth,
                posX + this.precalculatedHalfWidth, posY + this.height, posZ + this.precalculatedHalfWidth);
    }

    public double[] cuboidAtPosition(@NotNull final Location position) {
        return this.cuboidAtPosition(position.getX(), position.getY(), position.getZ());
    }

    public double[] cuboidAtPosition(@NotNull final Vector position) {
        return this.cuboidAtPosition(position.getX(), position.getY(), position.getZ());
    }

    public double[] cuboidAtPosition(final double posX, final double posY, final double posZ) {
        return new double[]{
                posX - this.precalculatedHalfWidth,
                posY,
                posZ - this.precalculatedHalfWidth,
                posX + this.precalculatedHalfWidth,
                posY + this.height,
                posZ + this.precalculatedHalfWidth
        };
    }

    // endregion

    @NotNull public EntityDimension clone() {
        try {
            return (EntityDimension) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
