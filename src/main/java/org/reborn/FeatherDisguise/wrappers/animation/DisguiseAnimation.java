package org.reborn.FeatherDisguise.wrappers.animation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.util.Teardown;

import java.util.EnumMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.NONE)
public class DisguiseAnimation implements Teardown {

    private Map<EntityAnimationType, Boolean> entityAnimationTypeFlags = internalGenerateMap();


    public void addAnimationFlag(@NotNull final EntityAnimationType entityAnimationType, final boolean flag) {
        this.animationFlag(entityAnimationType, flag);
    }

    @SuppressWarnings("UnusedReturnValue")
    @NotNull public DisguiseAnimation animationFlag(@NotNull final EntityAnimationType entityAnimationType, final boolean flag) {
        this.internalAddAnimationFlag(entityAnimationType, flag);
        return this;
    }

    public boolean isAnimationTypeFlagged(@NotNull final EntityAnimationType entityAnimationType) {
        return this.entityAnimationTypeFlags != null && this.entityAnimationTypeFlags.containsKey(entityAnimationType);
    }

    public boolean doesAnimationTypeMatchFlag(@NotNull final EntityAnimationType entityAnimationType, final boolean flag) {
        return this.isAnimationTypeFlagged(entityAnimationType) == flag;
    }

    @ApiStatus.Internal
    private void internalAddAnimationFlag(@NotNull final EntityAnimationType entityAnimationType, final boolean flag) {
        if (this.entityAnimationTypeFlags == null) {
            this.entityAnimationTypeFlags = internalGenerateMap();
        }

        this.entityAnimationTypeFlags.put(entityAnimationType, flag);
    }

    @ApiStatus.Internal
    @NotNull private static EnumMap<EntityAnimationType, Boolean> internalGenerateMap() {
        return new EnumMap<>(EntityAnimationType.class);
    }

    public void clearFlags() {
        if (this.entityAnimationTypeFlags == null) return;
        this.entityAnimationTypeFlags.clear();
    }

    @Override
    public void teardown() {
        if (this.entityAnimationTypeFlags != null && !this.entityAnimationTypeFlags.isEmpty()) {
            this.entityAnimationTypeFlags.clear();
        }

        this.entityAnimationTypeFlags = null;
    }
}
