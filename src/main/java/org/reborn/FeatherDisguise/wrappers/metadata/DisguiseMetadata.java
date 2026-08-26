package org.reborn.FeatherDisguise.wrappers.metadata;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.util.Teardown;

import java.util.EnumMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.NONE)
public class DisguiseMetadata implements Teardown {

    private Map<EntityMetadataType, Boolean> entityMetadataTypeFlags = internalGenerateMap();


    public void addMetadataFlag(@NotNull final EntityMetadataType entityMetadataType, final boolean flag) {
        this.metadataFlag(entityMetadataType, flag);
    }

    @SuppressWarnings("UnusedReturnValue")
    @NotNull public DisguiseMetadata metadataFlag(@NotNull final EntityMetadataType entityMetadataType, final boolean flag) {
        this.internalAddMetadataFlag(entityMetadataType, flag);
        return this;
    }

    public boolean isMetadataTypeFlagged(@NotNull final EntityMetadataType entityMetadataType) {
        return this.entityMetadataTypeFlags != null && this.entityMetadataTypeFlags.containsKey(entityMetadataType);
    }

    public boolean doesMetadataTypeMatchFlag(@NotNull final EntityMetadataType entityMetadataType, final boolean flag) {
        return this.isMetadataTypeFlagged(entityMetadataType) == flag;
    }

    @ApiStatus.Internal
    private void internalAddMetadataFlag(@NotNull final EntityMetadataType entityMetadataType, final boolean flag) {
        if (this.entityMetadataTypeFlags == null) {
            this.entityMetadataTypeFlags = internalGenerateMap();
        }

        this.entityMetadataTypeFlags.put(entityMetadataType, flag);
    }

    @ApiStatus.Internal
    @NotNull private static EnumMap<EntityMetadataType, Boolean> internalGenerateMap() {
        return new EnumMap<>(EntityMetadataType.class);
    }

    public void clearFlags() {
        if (this.entityMetadataTypeFlags == null) return;
        this.entityMetadataTypeFlags.clear();
    }

    @Override
    public void teardown() {
        if (this.entityMetadataTypeFlags != null && !this.entityMetadataTypeFlags.isEmpty()) {
            this.entityMetadataTypeFlags.clear();
        }

        this.entityMetadataTypeFlags = null;
    }
}
