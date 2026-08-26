package org.reborn.FeatherDisguise.wrappers.animation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum EntityStatusAnimationType {

    // for spawner minecarts, resets some internal spawner value
    // for rabbit, calls createRunningParticles() and set some ticker field vars, usually for handling rabbit animations
    RESET_MINECART_TIMER_OR_RESET_RABBIT_JUMP_ANIMATION(1),

    // runs hurt animation for living entity
    LIVING_ENTITY_HURT_ANIMATION(2),

    // runs death animation for living entity
    LIVING_ENTITY_DEATH_ANIMATION(3),

    // makes iron golem entity raise its arms for 10 ticks (just plays the animation really)
    IRON_GOLEM_ARMS_RAISE(4),

    // both of these work similarly. they call the overloaded method for EntityTameable class. each id calls its respective particle effects
    TAMING_ANIMAL_HEARTS(6),
    TAMING_ANIMAL_SMOKE(7),

    // makes wolf entity start shaking its body (usually the animation played after leaving water)
    WOLF_SHAKING(8),

    // calls onItemUseFinish() for EntityPlayer classes. method runs the item finished() method, usually for things like eating food or items breaking
    EATING_ACCEPTED(9),

    // for tnt minecarts, calls ignite() method which sets an 80 tick fuse and also plays the tnt_primed sound. if fuse tick reaches 0 calls explodeCart()
    // for sheep, it just sets the eating_grass tick timer to 40, meaning it'll play the grass eating animation
    SHEEP_EATS_GRASS_OR_MINECART_TNT_IGNITION(10),

    // makes iron golem hold a rose for 400 ticks (once this time passes, it reverts back to normal)
    IRON_GOLEM_HOLDING_ROSE(11),

    // overloaded method for EntityVillager class. calls respective particle effects specifically for villager interactions
    VILLAGER_TAMING_HEARTS(12),
    VILLAGER_ANGRY_CLOUDS(13),
    VILLAGER_HAPPY_SPARKLE(14),

    // runs witch ambient particle effects (purple sparkles)
    WITCH_MAGIC_SPARKLE(15),

    // if the zombie metadata flag for isSilent() is false, plays the zombie converting sound
    ZOMBIE_CONVERTING_TO_VILLAGER(16),

    // runs firework exploding animation. uses whatever firework itemstack metadata is present for the entity
    FIREWORKS_EXPLODING(17),

    // overloaded method for AnimalEntity class. just displays the mating heart particle effects
    ANIMATION_IN_LOVE_HEARTS(18),

    // forces squid rotation to be 0.0 on the client
    RESET_SQUID_ROTATION(19),

    // overloaded method for livingEntity class. calls spawnExplosionParticle() to clients to display an explosion effect
    // (if a LivingEntity subclass overloads the method and doesn't call super(), it's likely the explosion effect won't play)
    EXPLOSION_PARTICLES_LIVING_ENTITIES(20),

    // plays special GuardianSound() and effect to viewers
    GUARDIAN_SOUND(21),

    // idk some debug related shit for EntityPlayer classes
    ENABLE_REDUCED_DEBUG_FOR_PLAYERS(22),
    DISABLE_REDUCED_DEBUG_FOR_PLAYERS(23);

    private final int animationID;
}
