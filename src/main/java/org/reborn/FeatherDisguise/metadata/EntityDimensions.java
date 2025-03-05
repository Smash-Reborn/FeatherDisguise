package org.reborn.FeatherDisguise.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor @Getter @Setter
public class EntityDimensions implements Cloneable {

    private float width;
    private float height;
    private float eyeHeight;

    public EntityDimensions(final float width, final float height) {
        this(width, height, defaultEyeHeight(height));
    }

    public EntityDimensions() {
        this(0.5f, 0.5f);
    }

    public static float defaultEyeHeight(float height) {
        return height * 0.85f;
    }

    @NotNull public EntityDimensions clone() {
        try {
            return (EntityDimensions) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }
}
