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

        EntityType<?> entityType = cachedEntityTypeMap.get(entity.getClass());
        if (entityType != null) return entityType;
        // hopefully this is all it will ever have to get too

        // really really really stupid af thing here, but i can't think of any other way to handle this atm.
        // the problem is when custom nms entities exist (eg: MyCustomPotion extends EntityPotion) the above
        // check will fail, because the cache is only storing the exact class reference. how the nms tracker
        // got around this was its instanceof checks for all the entities. entities store no enum or identifying
        // object, so im not sure how to correctly determine if a custom entity extends a cached entity type.

        // so ill fold and do the super cringe instanceof block for now, but this 100000% needs to be fixed eventually.
        // remember: this part of the code only calls if it's a custom nms entity

        if (entity instanceof EntityArmorStand) {entityType = EntityType.ARMOR_STAND;}
        else if (entity instanceof EntityCreeper) {entityType = EntityType.CREEPER;}
        else if (entity instanceof EntitySkeleton) {entityType = EntityType.SKELETON;}
        else if (entity instanceof EntityCaveSpider) {entityType = EntityType.CAVE_SPIDER;} // needs to come before spider else unreachable
        else if (entity instanceof EntitySpider) {entityType = EntityType.SPIDER;}
        else if (entity instanceof EntityGiantZombie) {entityType = EntityType.GIANT;}
        else if (entity instanceof EntityPigZombie) {entityType = EntityType.ZOMBIE_PIGMAN;} // needs to come before zombie else unreachable
        else if (entity instanceof EntityZombie) {entityType = EntityType.ZOMBIE;}
        else if (entity instanceof EntityMagmaCube) {entityType = EntityType.MAGMA_CUBE;} // needs to come before slime else unreachable
        else if (entity instanceof EntitySlime) {entityType = EntityType.SLIME;}
        else if (entity instanceof EntityGhast) {entityType = EntityType.GHAST;}
        else if (entity instanceof EntityEnderman) {entityType = EntityType.ENDERMAN;}
        else if (entity instanceof EntitySilverfish) {entityType = EntityType.SILVERFISH;}
        else if (entity instanceof EntityBlaze) {entityType = EntityType.BLAZE;}
        else if (entity instanceof EntityWither) {entityType = EntityType.WITHER_BOSS;}
        else if (entity instanceof EntityBat) {entityType = EntityType.BAT;}
        else if (entity instanceof EntityWitch) {entityType = EntityType.WITCH;}
        else if (entity instanceof EntityEndermite) {entityType = EntityType.ENDERMITE;}
        else if (entity instanceof EntityGuardian) {entityType = EntityType.GUARDIAN;}
        else if (entity instanceof EntityPig) {entityType = EntityType.PIG;}
        else if (entity instanceof EntitySheep) {entityType = EntityType.SHEEP;}
        else if (entity instanceof EntityMushroomCow) {entityType = EntityType.MUSHROOM_COW;} // needs to come before cow else unreachable
        else if (entity instanceof EntityCow) {entityType = EntityType.COW;}
        else if (entity instanceof EntityChicken) {entityType = EntityType.CHICKEN;}
        else if (entity instanceof EntitySquid) {entityType = EntityType.SQUID;}
        else if (entity instanceof EntityWolf) {entityType = EntityType.WOLF;}
        else if (entity instanceof EntitySnowman) {entityType = EntityType.SNOWMAN;}
        else if (entity instanceof EntityOcelot) {entityType = EntityType.OCELOT;}
        else if (entity instanceof EntityIronGolem) {entityType = EntityType.IRON_GOLEM;}
        else if (entity instanceof EntityHorse) {entityType = EntityType.HORSE;}
        else if (entity instanceof EntityRabbit) {entityType = EntityType.RABBIT;}
        else if (entity instanceof EntityVillager) {entityType = EntityType.VILLAGER;}
        else if (entity instanceof EntityPlayer) {entityType = EntityType.PLAYER;}

        else if (entity instanceof EntityItem) {entityType = EntityType.ITEM_TILE;}
        else if (entity instanceof EntityExperienceOrb) {entityType = EntityType.EXPERIENCE_ORB;}
        else if (entity instanceof EntityEgg) {entityType = EntityType.EGG;}
        else if (entity instanceof EntityLeash) {entityType = EntityType.LEASH_HITCH;}
        else if (entity instanceof EntityPainting) {entityType = EntityType.PAINTING;}
        else if (entity instanceof EntityArrow) {entityType = EntityType.ARROW;}
        else if (entity instanceof EntitySnowball) {entityType = EntityType.SNOWBALL;}
        else if (entity instanceof EntityLargeFireball) {entityType = EntityType.LARGE_FIREBALL;}
        else if (entity instanceof EntitySmallFireball) {entityType = EntityType.SMALL_FIREBALL;}
        else if (entity instanceof EntityEnderPearl) {entityType = EntityType.ENDER_PEARL;}
        else if (entity instanceof EntityEnderSignal) {entityType = EntityType.EYE_OF_ENDER;}
        else if (entity instanceof EntityPotion) {entityType = EntityType.POTION;}
        else if (entity instanceof EntityThrownExpBottle) {entityType = EntityType.EXPERIENCE_BOTTLE;}
        else if (entity instanceof EntityItemFrame) {entityType = EntityType.ITEM_FRAME;}
        else if (entity instanceof EntityWitherSkull) {entityType = EntityType.WITHER_SKULL;}
        else if (entity instanceof EntityTNTPrimed) {entityType = EntityType.PRIMED_TNT;}
        else if (entity instanceof EntityFallingBlock) {entityType = EntityType.FALLING_BLOCK;}
        else if (entity instanceof EntityFireworks) {entityType = EntityType.FIREWORKS;}
        else if (entity instanceof EntityBoat) {entityType = EntityType.BOAT;}
        else if (entity instanceof EntityMinecartRideable) {entityType = EntityType.MINECART_RIDEABLE;}
        else if (entity instanceof EntityMinecartChest) {entityType = EntityType.MINECART_CHEST;}
        else if (entity instanceof EntityMinecartFurnace) {entityType = EntityType.MINECART_FURNACE;}
        else if (entity instanceof EntityMinecartTNT) {entityType = EntityType.MINECART_TNT;}
        else if (entity instanceof EntityMinecartHopper) {entityType = EntityType.MINECART_HOPPER;}
        else if (entity instanceof EntityMinecartMobSpawner) {entityType = EntityType.MINECART_MOB_SPAWNER;}
        else if (entity instanceof EntityMinecartCommandBlock) {entityType = EntityType.MINECART_COMMAND_BLOCK;}
        else if (entity instanceof EntityEnderCrystal) {entityType = EntityType.ENDER_CRYSTAL;}
        else if (entity instanceof EntityFishingHook) {entityType = EntityType.FISHING_HOOK;}

        return entityType;
    }

    @Override
    public void teardown() {
        if (!cachedEntityTypeMap.isEmpty()) {
            cachedEntityTypeMap.clear();
        }

        cachedEntityTypeMap = null;
    }
}
