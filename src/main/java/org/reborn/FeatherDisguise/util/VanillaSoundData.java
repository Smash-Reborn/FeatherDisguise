package org.reborn.FeatherDisguise.util;

import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class VanillaSoundData {

    // bukkit-api sound type
    private final Sound soundType;

    // converted bukkit -> raw nms sound string (used with packet(s))
    @Nullable private final String soundString;

    private final float volume;
    private final float pitch;

    private static final float DEFAULT_VOLUME = 1.5f;
    private static final float DEFAULT_PITCH = 0.8f;

    public VanillaSoundData(@NotNull final Sound sound, final float volume, final float pitch) {
        this.soundType = sound;
        this.soundString = this.getRawSoundStringFromSoundType(sound);
        this.volume = volume;
        this.pitch = pitch;
    }

    public VanillaSoundData(@NotNull final String soundString, final float volume, final float pitch) {
        this.soundType = null;
        this.soundString = soundString;
        this.volume = volume;
        this.pitch = pitch;
    }

    @NotNull public static VanillaSoundData defaultEntity(@NotNull final Sound sound) {
        return pitchedEntity(sound, DEFAULT_PITCH);
    }

    @NotNull public static VanillaSoundData defaultEntity(@NotNull final String soundString) {
        return pitchedEntity(soundString, DEFAULT_PITCH);
    }

    @NotNull public static VanillaSoundData pitchedEntity(@NotNull final Sound sound, final float pitch) {
        return new VanillaSoundData(sound, DEFAULT_VOLUME, pitch);
    }

    @NotNull public static VanillaSoundData pitchedEntity(@NotNull final String soundString, final float pitch) {
        return new VanillaSoundData(soundString, DEFAULT_VOLUME, pitch);
    }

    @Nullable public String getRawSoundStringFromSoundType(@NotNull final Sound sound) {
        return CraftSound.getSound(sound);
    }
}
