package org.reborn.FeatherDisguise.metadata.modal;

import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityType;

public class AgedEntityMetadataHolder<E extends EntityType<?>> extends LivingEntityMetadataHolder<E> {

    public AgedEntityMetadataHolder(@NotNull E entityType) {
        super(entityType);
    }
}
