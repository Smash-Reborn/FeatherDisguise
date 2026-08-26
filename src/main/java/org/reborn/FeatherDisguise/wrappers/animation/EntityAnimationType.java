package org.reborn.FeatherDisguise.wrappers.animation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum EntityAnimationType {

    // forces the entity to play its main hand swing animation (if applicable)
    SWING_MAIN_HAND(0),

    // (only plays animation for boats & minecarts) forces the entity to modify clientside damage animation vars
    ENTITY_HURT(1),

    // (only for player entities) forces the player to play the "wake-up" animation
    WAKE_UP_FROM_BED(2),

    // isn't properly handled client-side, will do nothing
    UNUSED_INVALID(3),

    // renders combat "crit" particles at entity position
    COMBAT_CRIT_PARTICLES(4),

    // renders combat "magic_crit" particles at entity position
    COMBAT_MAGIC_CRIT_PARTICLES(5);

    private final int animationID;
}
