package org.reborn.FeatherDisguise.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class EntityDimensions {

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
}
