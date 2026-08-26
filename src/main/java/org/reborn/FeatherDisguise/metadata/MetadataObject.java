package org.reborn.FeatherDisguise.metadata;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Getter
public final class MetadataObject<T> {

    private final int index;

    @NotNull private final Supplier<T> defaultObject;

    public MetadataObject(final int index, @NotNull final Supplier<T> defaultObject) {
        this.index = index;
        this.defaultObject = defaultObject;
    }

    public MetadataObject(final int index, @NotNull final T defaultObject) {
        this(index, () -> defaultObject);
    }

    @NotNull public T getDefaultObject() {
        return this.defaultObject.get();
    }
}
