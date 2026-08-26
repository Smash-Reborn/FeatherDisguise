package org.reborn.FeatherDisguise.wrappers.metadata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reborn.FeatherDisguise.metadata.MetadataIndex;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum EntityMetadataType {

    ENTITY_ON_FIRE(MetadataIndex.EntityMetadata.GENERIC_FLAGS.getIndex(), (byte) 0x01); // todo link the bitmask to the MetadataHolder enums

    private final int metadataIndexID;
    private final byte metadataBitmaskID;

    EntityMetadataType(final int metadataIndexID) {
        this.metadataIndexID = metadataIndexID;
        this.metadataBitmaskID = 0x00;
    }
}
