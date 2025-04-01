package org.reborn.FeatherDisguise.wrapper;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class DisguiseMetadataException extends RuntimeException {

    public DisguiseMetadataException(@NotNull final String reason) {
        super(reason);
    }
}
