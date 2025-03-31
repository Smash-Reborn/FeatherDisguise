package org.reborn.FeatherDisguise.tracker;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class IllegalEntityTrackingException extends RuntimeException {

    public IllegalEntityTrackingException(@NotNull final String reason) {
        super(reason);
    }
}
