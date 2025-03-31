package org.reborn.FeatherDisguise.metadata;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_8_R3.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.util.ITeardown;

import java.util.HashMap;

/*
 * i absolutely refuse to do a stupid for loop, switch case or cancer "else if instanceof" checks for entities.
 * we have our own custom EntityType<> class, and we are gonna utilise it to the fullest potential.
 * mem storage for this is piss easy, and it will be referenced so frequently by the custom tracker it makes
 * sense to make it as fast as it possibly can be. direct reference to the hashmap will always be superior to the other mojank methods.
 */
public class CachedEntityTypes implements ITeardown {

    @ApiStatus.Internal
    private HashMap<Class<? extends Entity>, EntityType<?>> cachedEntityTypeMap;

    public CachedEntityTypes() {
        this.cachedEntityTypeMap = new HashMap<>(60);

        this.cachedEntityTypeMap.put(EntityArmorStand.class, EntityType.ARMOR_STAND);
        this.cachedEntityTypeMap.put(EntityCreeper.class, EntityType.CREEPER);
        this.cachedEntityTypeMap.put(EntitySkeleton.class, EntityType.SKELETON);
        //this.cachedEntityTypeMap.put(EntitySkeleton.class, EntityType.WITHER_SKELETON); // cannot do duplicates, but this shouldn't be a problem, as the original tracker didn't care about subtypes (handled this via metadata in 1.8)
        this.cachedEntityTypeMap.put(EntitySpider.class, EntityType.SPIDER);
        this.cachedEntityTypeMap.put(EntityGiantZombie.class, EntityType.GIANT);
        this.cachedEntityTypeMap.put(EntityZombie.class, EntityType.ZOMBIE);
        this.cachedEntityTypeMap.put(EntitySlime.class, EntityType.SLIME);
        this.cachedEntityTypeMap.put(EntityGhast.class, EntityType.GHAST);
        this.cachedEntityTypeMap.put(EntityPigZombie.class, EntityType.ZOMBIE_PIGMAN);
        this.cachedEntityTypeMap.put(EntityEnderman.class, EntityType.ENDERMAN);
        this.cachedEntityTypeMap.put(EntityCaveSpider.class, EntityType.CAVE_SPIDER);
        this.cachedEntityTypeMap.put(EntitySilverfish.class, EntityType.SILVERFISH);
        this.cachedEntityTypeMap.put(EntityBlaze.class, EntityType.BLAZE);
        this.cachedEntityTypeMap.put(EntityMagmaCube.class, EntityType.MAGMA_CUBE);
        this.cachedEntityTypeMap.put(EntityWither.class, EntityType.WITHER_BOSS);
        this.cachedEntityTypeMap.put(EntityBat.class, EntityType.BAT);
        this.cachedEntityTypeMap.put(EntityWitch.class, EntityType.WITCH);
        this.cachedEntityTypeMap.put(EntityEndermite.class, EntityType.ENDERMITE);
        this.cachedEntityTypeMap.put(EntityGuardian.class, EntityType.GUARDIAN);
        this.cachedEntityTypeMap.put(EntityPig.class, EntityType.PIG);
        this.cachedEntityTypeMap.put(EntitySheep.class, EntityType.SHEEP);
        this.cachedEntityTypeMap.put(EntityCow.class, EntityType.COW);
        this.cachedEntityTypeMap.put(EntityChicken.class, EntityType.CHICKEN);
        this.cachedEntityTypeMap.put(EntitySquid.class, EntityType.SQUID);
        this.cachedEntityTypeMap.put(EntityWolf.class, EntityType.WOLF);
        this.cachedEntityTypeMap.put(EntityMushroomCow.class, EntityType.MUSHROOM_COW);
        this.cachedEntityTypeMap.put(EntitySnowman.class, EntityType.SNOWMAN);
        this.cachedEntityTypeMap.put(EntityOcelot.class, EntityType.OCELOT);
        this.cachedEntityTypeMap.put(EntityIronGolem.class, EntityType.IRON_GOLEM);
        this.cachedEntityTypeMap.put(EntityHorse.class, EntityType.HORSE);
        this.cachedEntityTypeMap.put(EntityRabbit.class, EntityType.RABBIT);
        this.cachedEntityTypeMap.put(EntityVillager.class, EntityType.VILLAGER);
        this.cachedEntityTypeMap.put(EntityPlayer.class, EntityType.PLAYER);

        this.cachedEntityTypeMap.put(EntityItem.class, EntityType.ITEM_TILE);
        this.cachedEntityTypeMap.put(EntityExperienceOrb.class, EntityType.EXPERIENCE_ORB);
        this.cachedEntityTypeMap.put(EntityEgg.class, EntityType.EGG);
        this.cachedEntityTypeMap.put(EntityLeash.class, EntityType.LEASH_HITCH);
        this.cachedEntityTypeMap.put(EntityPainting.class, EntityType.PAINTING);
        this.cachedEntityTypeMap.put(EntityArrow.class, EntityType.ARROW);
        this.cachedEntityTypeMap.put(EntitySnowball.class, EntityType.SNOWBALL);
        this.cachedEntityTypeMap.put(EntityLargeFireball.class, EntityType.LARGE_FIREBALL);
        this.cachedEntityTypeMap.put(EntitySmallFireball.class, EntityType.SMALL_FIREBALL);
        this.cachedEntityTypeMap.put(EntityEnderPearl.class, EntityType.ENDER_PEARL);
        this.cachedEntityTypeMap.put(EntityEnderSignal.class, EntityType.EYE_OF_ENDER);
        this.cachedEntityTypeMap.put(EntityPotion.class, EntityType.POTION);
        this.cachedEntityTypeMap.put(EntityThrownExpBottle.class, EntityType.EXPERIENCE_BOTTLE);
        this.cachedEntityTypeMap.put(EntityItemFrame.class, EntityType.ITEM_FRAME);
        this.cachedEntityTypeMap.put(EntityWitherSkull.class, EntityType.WITHER_SKULL);
        this.cachedEntityTypeMap.put(EntityTNTPrimed.class, EntityType.PRIMED_TNT);
        this.cachedEntityTypeMap.put(EntityFallingBlock.class, EntityType.FALLING_BLOCK);
        this.cachedEntityTypeMap.put(EntityFireworks.class, EntityType.FIREWORKS);
        this.cachedEntityTypeMap.put(EntityBoat.class, EntityType.BOAT);
        this.cachedEntityTypeMap.put(EntityMinecartRideable.class, EntityType.MINECART_RIDEABLE);
        this.cachedEntityTypeMap.put(EntityMinecartChest.class, EntityType.MINECART_CHEST);
        this.cachedEntityTypeMap.put(EntityMinecartFurnace.class, EntityType.MINECART_FURNACE);
        this.cachedEntityTypeMap.put(EntityMinecartTNT.class, EntityType.MINECART_TNT);
        this.cachedEntityTypeMap.put(EntityMinecartHopper.class, EntityType.MINECART_HOPPER);
        this.cachedEntityTypeMap.put(EntityMinecartMobSpawner.class, EntityType.MINECART_MOB_SPAWNER);
        this.cachedEntityTypeMap.put(EntityMinecartCommandBlock.class, EntityType.MINECART_COMMAND_BLOCK);
        this.cachedEntityTypeMap.put(EntityEnderCrystal.class, EntityType.ENDER_CRYSTAL);
        this.cachedEntityTypeMap.put(EntityFishingHook.class, EntityType.FISHING_HOOK);
    }

    @Nullable public EntityType<?> getEntityTypeViaReference(@NotNull final Entity entity) {
        Preconditions.checkNotNull(cachedEntityTypeMap, "Cached entity type map is empty or invalid. Unable to fetch entity type");
        return cachedEntityTypeMap.get(entity.getClass());
    }

    @Override
    public void teardown() {
        if (!cachedEntityTypeMap.isEmpty()) {
            cachedEntityTypeMap.clear();
        }

        cachedEntityTypeMap = null;
    }
}
