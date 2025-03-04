package org.reborn.FeatherDisguise;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.types.hostile.DisguiseWitherSkeleton;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@RequiredArgsConstructor
public enum DisguiseType {

    // Pre-Release
    CREEPER(null),
    SKELETON(null),
    SPIDER(null),
    ZOMBIE(null),
    SLIME(null),
    GHAST(null),
    ENDERMAN(null),
    PIG(null),
    CAVE_SPIDER(null),
    SHEEP(null),
    COW(null),
    SILVERFISH(null),
    CHICKEN(null),
    SQUID(null),
    WOLF(null),
    GIANT(null),

    // Adventure Update (1.0)
    MOOSHROOM(null),
    VILLAGER(null),
    SNOW_GOLEM(null),
    MAGMA_CUBE(null),
    BLAZE(null),

    // (1.2)
    IRON_GOLEM(null),
    OCELOT(null),

    // Pretty Scary Update (1.4)
    BAT(null),
    WITCH(null),
    WITHER_BOSS(null),
    WITHER_SKELETON(DisguiseWitherSkeleton.class),
    ZOMBIE_VILLAGER(null),

    // Horse Update (1.6)
    HORSE(null),
    MULE(null),
    DONKEY(null),
    SKELETON_HORSE(null),
    ZOMBIE_HORSE(null),

    // Bountiful Update (1.8)
    GUARDIAN(null),
    ELDER_GUARDIAN(null),
    RABBIT(null),
    ENDERMITE(null),
    KILLER_BUNNY(null)
    ;

    @Nullable private final Class<? extends AbstractDisguise<?>> disguiseClass;

    /** @return {@link AbstractDisguise} object based on the {@link DisguiseType}.
     * If the {@code type} is {@code null} the method will instead return {@link Optional#empty()}.
     * **/
    @NotNull public Optional<AbstractDisguise<?>> generateDisguiseObjectFromType(@NotNull Player player) {
        if (this.disguiseClass == null) return Optional.empty();
        try {
            return Optional.of(this.disguiseClass.getDeclaredConstructor(Player.class).newInstance(player));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
            return Optional.empty();
        }
    }
}
