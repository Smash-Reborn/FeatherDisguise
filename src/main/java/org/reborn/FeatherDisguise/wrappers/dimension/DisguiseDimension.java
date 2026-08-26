package org.reborn.FeatherDisguise.wrappers.dimension;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter @Setter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class DisguiseDimension {

    // dimensions wrapper for the entity that is disguised (player, etc)
    @NotNull private EntityDimension disguisedEntityDimensions;

    // dimensions wrapper for the visible entity that player client(s) see
    @NotNull private EntityDimension visibleDisguiseEntityDimensions;
}
