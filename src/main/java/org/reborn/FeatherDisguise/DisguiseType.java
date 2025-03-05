package org.reborn.FeatherDisguise;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.types.hostile.*;
import org.reborn.FeatherDisguise.types.neutral.*;
import org.reborn.FeatherDisguise.types.passive.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@RequiredArgsConstructor
public enum DisguiseType {

    // Pre-Release
    CREEPER(DisguiseCreeper.class),
    SKELETON(DisguiseSkeleton.class),
    SPIDER(DisguiseSpider.class),
    ZOMBIE(DisguiseZombie.class),
    SLIME(DisguiseSlime.class),
    GHAST(null),
    ENDERMAN(DisguiseEnderman.class),
    PIG(DisguisePig.class),
    CAVE_SPIDER(DisguiseCaveSpider.class),
    SHEEP(DisguiseSheep.class),
    COW(DisguiseCow.class),
    SILVERFISH(null),
    CHICKEN(DisguiseChicken.class),
    SQUID(DisguiseSquid.class),
    WOLF(DisguiseWolf.class),
    GIANT(null),
    ZOMBIE_PIGMAN(DisguiseZombiePigman.class),

    // Adventure Update (1.0)
    MOOSHROOM(null),
    VILLAGER(DisguiseVillager.class),
    SNOW_GOLEM(DisguiseSnowman.class),
    MAGMA_CUBE(DisguiseMagmaCube.class),
    BLAZE(DisguiseBlaze.class),

    // (1.2)
    IRON_GOLEM(DisguiseIronGolem.class),
    OCELOT(null),

    // Pretty Scary Update (1.4)
    BAT(DisguiseBat.class),
    WITCH(DisguiseWitch.class),
    WITHER_BOSS(DisguiseWitherBoss.class),
    WITHER_SKELETON(DisguiseWitherSkeleton.class),
    ZOMBIE_VILLAGER(null),

    // Horse Update (1.6)
    HORSE(null),
    MULE(null),
    DONKEY(DisguiseDonkey.class),
    SKELETON_HORSE(DisguiseSkeletonHorse.class),
    ZOMBIE_HORSE(DisguiseZombieHorse.class),

    // Bountiful Update (1.8)
    GUARDIAN(DisguiseGuardian.class),
    ELDER_GUARDIAN(DisguiseElderGuardian.class),
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
