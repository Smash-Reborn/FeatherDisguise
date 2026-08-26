package org.reborn.FeatherDisguise.metadata;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class MetadataSchema {

    private final Map<Integer, MetadataObject<?>> metadataObjectKeys;


    // region Accessors

    @NotNull public Map<Integer, MetadataObject<?>> getMappedObjects() {
        return Collections.unmodifiableMap(this.metadataObjectKeys);
    }

    @NotNull public Collection<MetadataObject<?>> getMetadataObjectsAsCollection() {
        return Collections.unmodifiableCollection(this.metadataObjectKeys.values());
    }

    @NotNull public List<MetadataObject<?>> getMetadataObjectsAsList() {
        return new ArrayList<>(this.getMetadataObjectsAsCollection());
    }

    // endregion

    // region Builder

    @NotNull public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class Builder {

        private final Map<Integer, MetadataObject<?>> keys = new HashMap<>();
        private int index = MetadataIndex.STARTING_METADATA_INDEX_ID;   // 0

        @NotNull public <T> MetadataObject<T> set(final int newIndex, @NotNull final T defaultValue) {
            final MetadataObject<T> key = new MetadataObject<>(newIndex, defaultValue);
            keys.put(newIndex, key);
            return key;
        }

        @NotNull public <T> MetadataObject<T> setAndApply(final int newIndex, @NotNull final T defaultValue) {
            index = newIndex;
            return this.set(newIndex, defaultValue);
        }

        @NotNull public <T> MetadataObject<T> setSupplier(final int newIndex, @NotNull final Supplier<T> defaultValueSupplier) {
            final MetadataObject<T> key = new MetadataObject<>(newIndex, defaultValueSupplier);
            keys.put(newIndex, key);
            return key;
        }

        @NotNull public <T> MetadataObject<T> add(@NotNull final T defaultValue) {
            return this.set(index++, defaultValue);
        }

        @NotNull public <T> MetadataObject<T> addSupplier(@NotNull final Supplier<T> defaultValueSupplier) {
            return this.setSupplier(index++, defaultValueSupplier);
        }

        @NotNull public <T> MetadataObject<T> offset(final int offsetIndex, @NotNull final T defaultValue) {
            index = index + offsetIndex;
            return this.set(index, defaultValue);
        }

        @NotNull public <T> MetadataObject<T> offsetSupplier(final int offsetIndex, @NotNull final Supplier<T> defaultValueSupplier) {
            index = index + offsetIndex;
            return this.setSupplier(index, defaultValueSupplier);
        }

        public void remove(final int index) {
            keys.remove(index);
        }

        @NotNull public Builder inherit(@NotNull final MetadataSchema parentSchema) {
            for (final Map.Entry<Integer, MetadataObject<?>> entry : parentSchema.getMappedObjects().entrySet()) {

                final int index = entry.getKey();
                final MetadataObject<?> metadata = entry.getValue();

                keys.put(index, metadata);

                // ensure child fields start after the highest inherited index.
                // using <= avoids the off-by-one case when we see n-1 before n.
                if (this.index <= index) {
                    this.index = index + 1;
                }
            }

            return this;
        }

        @NotNull public MetadataSchema build() {
            return new MetadataSchema(Collections.unmodifiableMap(keys));
        }
    }

    // endregion
}
