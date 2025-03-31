package org.reborn.FeatherDisguise.metadata;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/*
 * this exists because the 1.8 EntityTypes NMS class is dogshit when it comes to getting information about registered nms entities.
 * however, we are on 1.8, so none of these fields will change. so instead ive backported the EntityType<> class from modern spigot for easy usage here.
 */
public class EntityType<T extends Entity> {

    // NMS entity class
    @NotNull private final Class<? extends Entity> entityClass;

    // Bukkit EntityType
    @Getter @NotNull private final org.bukkit.entity.EntityType bukkitEntityType; // just in case we need this lol

    // Defined entity key-name (ripped from decomp)
    @Getter @NotNull private final String entityName;

    // LivingEntities -> taken from EntityTypes<> class, Non-Living Entities -> taken from EntityTrackerEntry<> class (used for spawning packet entityTypeID)
    @Getter private final int entityReferenceID;

    // entityTracker update frequency
    @Getter private final int trackerUpdateFrequency;

    // entityTracker allow sending velocity packets
    @Getter private final boolean trackerAllowVelocityUpdates;

    // Defined entity dimensions (ripped from decomp)
    @Getter @NotNull private final EntityDimensions entityDimensions;

    @ApiStatus.Internal
    private static final int DEFAULT_OR_UNKNOWN_ENTITY_REF_ID = -1;

    // region Living Entities

    @NotNull public static final EntityType<EntityArmorStand> ARMOR_STAND;
    @NotNull public static final EntityType<EntityCreeper> CREEPER;
    @NotNull public static final EntityType<EntitySkeleton> SKELETON;
    @NotNull public static final EntityType<EntitySkeleton> WITHER_SKELETON;
    @NotNull public static final EntityType<EntitySpider> SPIDER;
    @NotNull public static final EntityType<EntityGiantZombie> GIANT;
    @NotNull public static final EntityType<EntityZombie> ZOMBIE;
    @NotNull public static final EntityType<EntityZombie> ZOMBIE_VILLAGER;
    @NotNull public static final EntityType<EntitySlime> SLIME;
    @NotNull public static final EntityType<EntityGhast> GHAST;
    @NotNull public static final EntityType<EntityPigZombie> ZOMBIE_PIGMAN;
    @NotNull public static final EntityType<EntityEnderman> ENDERMAN;
    @NotNull public static final EntityType<EntityCaveSpider> CAVE_SPIDER;
    @NotNull public static final EntityType<EntitySilverfish> SILVERFISH;
    @NotNull public static final EntityType<EntityBlaze> BLAZE;
    @NotNull public static final EntityType<EntityMagmaCube> MAGMA_CUBE;
    @NotNull public static final EntityType<EntityWither> WITHER_BOSS;
    @NotNull public static final EntityType<EntityBat> BAT;
    @NotNull public static final EntityType<EntityWitch> WITCH;
    @NotNull public static final EntityType<EntityEndermite> ENDERMITE;
    @NotNull public static final EntityType<EntityGuardian> GUARDIAN;
    @NotNull public static final EntityType<EntityGuardian> ELDER_GUARDIAN;
    @NotNull public static final EntityType<EntityPig> PIG;
    @NotNull public static final EntityType<EntitySheep> SHEEP;
    @NotNull public static final EntityType<EntityCow> COW;
    @NotNull public static final EntityType<EntityChicken> CHICKEN;
    @NotNull public static final EntityType<EntitySquid> SQUID;
    @NotNull public static final EntityType<EntityWolf> WOLF;
    @NotNull public static final EntityType<EntityMushroomCow> MUSHROOM_COW;
    @NotNull public static final EntityType<EntitySnowman> SNOWMAN;
    @NotNull public static final EntityType<EntityOcelot> OCELOT;
    @NotNull public static final EntityType<EntityIronGolem> IRON_GOLEM;
    @NotNull public static final EntityType<EntityHorse> HORSE;
    @NotNull public static final EntityType<EntityHorse> DONKEY;
    @NotNull public static final EntityType<EntityHorse> MULE;
    @NotNull public static final EntityType<EntityHorse> ZOMBIE_HORSE;
    @NotNull public static final EntityType<EntityHorse> SKELETON_HORSE;
    @NotNull public static final EntityType<EntityRabbit> RABBIT;
    @NotNull public static final EntityType<EntityVillager> VILLAGER;
    @NotNull public static final EntityType<EntityPlayer> PLAYER;

    // endregion

    // region Non-Living Entities

    @NotNull public static final EntityType<EntityItem> ITEM_TILE;
    @NotNull public static final EntityType<EntityExperienceOrb> EXPERIENCE_ORB;
    @NotNull public static final EntityType<EntityEgg> EGG;
    @NotNull public static final EntityType<EntityLeash> LEASH_HITCH;
    @NotNull public static final EntityType<EntityPainting> PAINTING;
    @NotNull public static final EntityType<EntityArrow> ARROW;
    @NotNull public static final EntityType<EntitySnowball> SNOWBALL;
    @NotNull public static final EntityType<EntityLargeFireball> LARGE_FIREBALL;
    @NotNull public static final EntityType<EntitySmallFireball> SMALL_FIREBALL;
    @NotNull public static final EntityType<EntityEnderPearl> ENDER_PEARL;
    @NotNull public static final EntityType<EntityEnderSignal> EYE_OF_ENDER;
    @NotNull public static final EntityType<EntityPotion> POTION;
    @NotNull public static final EntityType<EntityThrownExpBottle> EXPERIENCE_BOTTLE;
    @NotNull public static final EntityType<EntityItemFrame> ITEM_FRAME;
    @NotNull public static final EntityType<EntityWitherSkull> WITHER_SKULL;
    @NotNull public static final EntityType<EntityTNTPrimed> PRIMED_TNT;
    @NotNull public static final EntityType<EntityFallingBlock> FALLING_BLOCK;
    @NotNull public static final EntityType<EntityFireworks> FIREWORKS;
    @NotNull public static final EntityType<EntityBoat> BOAT;
    @NotNull public static final EntityType<EntityMinecartRideable> MINECART_RIDEABLE;      // WHY TF MOJANK WHY U MAKE SO MANY SEPARATE COPY PASTED MINECART VARIANTS
    @NotNull public static final EntityType<EntityMinecartChest> MINECART_CHEST;
    @NotNull public static final EntityType<EntityMinecartFurnace> MINECART_FURNACE;
    @NotNull public static final EntityType<EntityMinecartTNT> MINECART_TNT;
    @NotNull public static final EntityType<EntityMinecartHopper> MINECART_HOPPER;
    @NotNull public static final EntityType<EntityMinecartMobSpawner> MINECART_MOB_SPAWNER;
    @NotNull public static final EntityType<EntityMinecartCommandBlock> MINECART_COMMAND_BLOCK;
    @NotNull public static final EntityType<EntityEnderCrystal> ENDER_CRYSTAL;
    @NotNull public static final EntityType<EntityFishingHook> FISHING_HOOK;

    // endregion

    public static <T extends Entity> EntityType<T> register(final Class<T> entity, final org.bukkit.entity.EntityType entityType, final String entityName,
                                                            final int entityReferenceID, final int trackerUpdateFrequency, final boolean trackerAllowVelocityUpdates, final EntityDimensions entityDimensions) {
        return new EntityType<>(entity, entityType, entityName, entityReferenceID, trackerUpdateFrequency, trackerAllowVelocityUpdates, entityDimensions);
    }

    public EntityType(@NotNull final Class<T> entityClass, @NotNull final org.bukkit.entity.EntityType entityType, @NotNull final String entityName,
                      final int entityReferenceID, final int trackerUpdateFrequency, final boolean trackerAllowVelocityUpdates, @NotNull final EntityDimensions entityDimensions) {
        this.entityClass = entityClass;
        this.bukkitEntityType = entityType;
        this.entityName = entityName;
        this.entityReferenceID = entityReferenceID;
        this.trackerUpdateFrequency = trackerUpdateFrequency;
        this.trackerAllowVelocityUpdates = trackerAllowVelocityUpdates;
        this.entityDimensions = entityDimensions;
    }

    static { // these are exactly copied from nms EntityTypes.class
        ARMOR_STAND = register(EntityArmorStand.class, org.bukkit.entity.EntityType.ARMOR_STAND, "ArmorStand", 30, 3, true, new EntityDimensions(0.5f, 1.975f, 1.7775f));
        CREEPER = register(EntityCreeper.class, org.bukkit.entity.EntityType.CREEPER, "Creeper", 50, 3, true, new EntityDimensions(0.6f, 1.8f));
        SKELETON = register(EntitySkeleton.class, org.bukkit.entity.EntityType.SKELETON, "Skeleton", 51, 3, true, new EntityDimensions(0.6f, 1.95f, 1.74f));
        WITHER_SKELETON = register(EntitySkeleton.class, org.bukkit.entity.EntityType.SKELETON, "Skeleton", 51, 3, true, new EntityDimensions(0.72f, 2.535f, 2.1f));        // has to use SKELETON data
        SPIDER = register(EntitySpider.class, org.bukkit.entity.EntityType.SPIDER, "Spider", 52, 3, true, new EntityDimensions(1.4f, 0.9f, 0.65f));
        GIANT = register(EntityGiantZombie.class, org.bukkit.entity.EntityType.GIANT, "Giant", 53, 3, true, new EntityDimensions(3.6f, 12.0f, 10.44f));
        ZOMBIE = register(EntityZombie.class, org.bukkit.entity.EntityType.ZOMBIE, "Zombie", 54, 3, true, new EntityDimensions(0.6f, 1.95f, 1.74f));
        ZOMBIE_VILLAGER = register(EntityZombie.class, org.bukkit.entity.EntityType.ZOMBIE, "Zombie", 54, 3, true, new EntityDimensions(0.6f, 1.95f, 1.74f));                // has to use ZOMBIE data
        SLIME = register(EntitySlime.class, org.bukkit.entity.EntityType.SLIME, "Slime", 55, 3, true, new EntityDimensions(0.51000005f, 0.51000005f, 0.325f));
        GHAST = register(EntityGhast.class, org.bukkit.entity.EntityType.GHAST, "Ghast", 56, 3, true, new EntityDimensions(4.0f, 4.0f, 2.6f));
        ZOMBIE_PIGMAN = register(EntityPigZombie.class, org.bukkit.entity.EntityType.PIG_ZOMBIE, "PigZombie", 57, 3, true, new EntityDimensions(0.6f, 1.95f, 1.79f));
        ENDERMAN = register(EntityEnderman.class, org.bukkit.entity.EntityType.ENDERMAN, "Enderman", 58, 3, true, new EntityDimensions(0.6f, 2.9f, 2.55f));
        CAVE_SPIDER = register(EntityCaveSpider.class, org.bukkit.entity.EntityType.CAVE_SPIDER, "CaveSpider", 59, 3, true, new EntityDimensions(0.7f, 0.5f, 0.45f));
        SILVERFISH = register(EntitySilverfish.class, org.bukkit.entity.EntityType.SILVERFISH, "Silverfish", 60, 3, true, new EntityDimensions(0.4f, 0.3f, 0.13f));
        BLAZE = register(EntityBlaze.class, org.bukkit.entity.EntityType.BLAZE, "Blaze", 61, 3, true, new EntityDimensions(0.6f, 1.8f));
        MAGMA_CUBE = register(EntityMagmaCube.class, org.bukkit.entity.EntityType.MAGMA_CUBE, "LavaSlime", 62, 3, true, new EntityDimensions(0.51000005f, 0.51000005f, 0.325f));
        WITHER_BOSS = register(EntityWither.class, org.bukkit.entity.EntityType.WITHER, "WitherBoss", 64,3, false,  new EntityDimensions(0.9f, 3.5f));
        BAT = register(EntityBat.class, org.bukkit.entity.EntityType.BAT, "Bat", 65, 3, false, new EntityDimensions(0.5f, 0.9f, 0.45f));
        WITCH = register(EntityWitch.class, org.bukkit.entity.EntityType.WITCH, "Witch", 66, 3, true, new EntityDimensions(0.6f, 1.95f, 1.62f));
        ENDERMITE = register(EntityEndermite.class, org.bukkit.entity.EntityType.ENDERMITE, "Endermite", 67, 3, true, new EntityDimensions(0.4f, 0.3f, 0.13f));
        GUARDIAN = register(EntityGuardian.class, org.bukkit.entity.EntityType.GUARDIAN, "Guardian", 68, 3, true, new EntityDimensions(0.85f, 0.85f, 0.425f));
        ELDER_GUARDIAN = register(EntityGuardian.class, org.bukkit.entity.EntityType.GUARDIAN, "Guardian", 68, 3, true, new EntityDimensions(1.9975f, 1.9975f, 0.99875f));         // has to use GUARDIAN data
        PIG = register(EntityPig.class, org.bukkit.entity.EntityType.PIG, "Pig", 90, 3, true, new EntityDimensions(0.9f, 0.9f, 0.86875f));
        SHEEP = register(EntitySheep.class, org.bukkit.entity.EntityType.SHEEP, "Sheep", 91, 3, true, new EntityDimensions(0.9f, 1.3f, 1.235f));
        COW = register(EntityCow.class, org.bukkit.entity.EntityType.COW, "Cow", 92, 3, true, new EntityDimensions(0.9f, 1.3f));
        CHICKEN = register(EntityChicken.class, org.bukkit.entity.EntityType.CHICKEN, "Chicken", 93, 3, true, new EntityDimensions(0.4f, 0.7f, 0.644f));
        SQUID = register(EntitySquid.class, org.bukkit.entity.EntityType.SQUID, "Squid", 94, 3, true, new EntityDimensions(0.8f, 0.8f, 0.4f));
        WOLF = register(EntityWolf.class, org.bukkit.entity.EntityType.WOLF, "Wolf", 95, 3, true, new EntityDimensions(0.6f, 0.8f, 0.68f));
        MUSHROOM_COW = register(EntityMushroomCow.class, org.bukkit.entity.EntityType.MUSHROOM_COW, "MushroomCow", 96, 3, true, new EntityDimensions(0.9f, 1.3f));
        SNOWMAN = register(EntitySnowman.class, org.bukkit.entity.EntityType.SNOWMAN, "SnowMan", 97, 3, true, new EntityDimensions(0.7f, 1.9f, 1.7f));
        OCELOT = register(EntityOcelot.class, org.bukkit.entity.EntityType.OCELOT, "Ozelot", 98, 3, true, new EntityDimensions(0.6f, 0.7f));
        IRON_GOLEM = register(EntityIronGolem.class, org.bukkit.entity.EntityType.IRON_GOLEM, "VillagerGolem", 99, 3, true, new EntityDimensions(1.4f, 2.9f, 2.55f));
        HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true, new EntityDimensions(1.4f, 1.6f, 1.52f));
        DONKEY = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        MULE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        ZOMBIE_HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        SKELETON_HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        RABBIT = register(EntityRabbit.class, org.bukkit.entity.EntityType.RABBIT, "Rabbit", 101, 3, true, new EntityDimensions(0.6f, 0.7f));
        VILLAGER = register(EntityVillager.class, org.bukkit.entity.EntityType.VILLAGER, "Villager", 120, 3, true, new EntityDimensions(0.6f, 1.8f));
        PLAYER = register(EntityPlayer.class, org.bukkit.entity.EntityType.PLAYER, "Player", DEFAULT_OR_UNKNOWN_ENTITY_REF_ID, 2, false, new EntityDimensions(0.6f, 1.8f));              // technically don't use this data, but parody it anyways

        ITEM_TILE = register(EntityItem.class, org.bukkit.entity.EntityType.DROPPED_ITEM, "Item", 2, 20, true, new EntityDimensions(0.25f, 0.25f));
        EXPERIENCE_ORB = register(EntityExperienceOrb.class, org.bukkit.entity.EntityType.EXPERIENCE_ORB, "XPOrb", DEFAULT_OR_UNKNOWN_ENTITY_REF_ID, 3, true, new EntityDimensions(0.5f, 0.5f));
        EGG = register(EntityEgg.class, org.bukkit.entity.EntityType.EGG, "ThrownEgg", 62, 10, true, new EntityDimensions(0.25f, 0.25f));
        LEASH_HITCH = register(EntityLeash.class, org.bukkit.entity.EntityType.LEASH_HITCH, "LeashKnot", 77, Integer.MAX_VALUE, false, new EntityDimensions(0.5f, 0.5f));
        PAINTING = register(EntityPainting.class, org.bukkit.entity.EntityType.PAINTING, "Painting", DEFAULT_OR_UNKNOWN_ENTITY_REF_ID, Integer.MAX_VALUE, false, new EntityDimensions(0.5f, 0.5f));
        ARROW = register(EntityArrow.class, org.bukkit.entity.EntityType.ARROW, "Arrow", 60, 20, false, new EntityDimensions(0.5f, 0.5f));
        SNOWBALL = register(EntitySnowball.class, org.bukkit.entity.EntityType.SNOWBALL, "Snowball", 61,10, true,  new EntityDimensions(0.25f, 0.25f));
        LARGE_FIREBALL = register(EntityLargeFireball.class, org.bukkit.entity.EntityType.FIREBALL, "Fireball", 63, 10, false, new EntityDimensions(1.0f, 1.0f));
        SMALL_FIREBALL = register(EntitySmallFireball.class, org.bukkit.entity.EntityType.SMALL_FIREBALL, "SmallFireball", 64, 10, false, new EntityDimensions(0.3125f, 0.3125f));
        ENDER_PEARL = register(EntityEnderPearl.class, org.bukkit.entity.EntityType.ENDER_PEARL, "ThrownEnderpearl", 65, 10, true, new EntityDimensions(0.25f, 0.25f));
        EYE_OF_ENDER = register(EntityEnderSignal.class, org.bukkit.entity.EntityType.ENDER_SIGNAL, "EyeOfEnderSignal", 72, 4, true, new EntityDimensions(0.25f, 0.25f));
        POTION = register(EntityPotion.class, org.bukkit.entity.EntityType.SPLASH_POTION, "ThrownPotion", 73, 10, true, new EntityDimensions(0.25f, 0.25f));
        EXPERIENCE_BOTTLE = register(EntityThrownExpBottle.class, org.bukkit.entity.EntityType.THROWN_EXP_BOTTLE, "ThrownExpBottle", 75, 10, true, new EntityDimensions(0.25f, 0.25f));
        ITEM_FRAME = register(EntityItemFrame.class, org.bukkit.entity.EntityType.ITEM_FRAME, "ItemFrame", 71, Integer.MAX_VALUE, false, new EntityDimensions(0.5f, 0.5f));
        WITHER_SKULL = register(EntityWitherSkull.class, org.bukkit.entity.EntityType.WITHER_SKULL, "WitherSkull", 66, 10, false, new EntityDimensions(0.3125f, 0.3125f));
        PRIMED_TNT = register(EntityTNTPrimed.class, org.bukkit.entity.EntityType.PRIMED_TNT, "PrimedTnt", 50, 10, true, new EntityDimensions(0.98f, 0.98f));
        FALLING_BLOCK = register(EntityFallingBlock.class, org.bukkit.entity.EntityType.FALLING_BLOCK, "FallingSand", 70, 20, true, new EntityDimensions(1.0f, 1.0f));
        FIREWORKS = register(EntityFireworks.class, org.bukkit.entity.EntityType.FIREWORK, "FireworksRocketEntity", 76, 10, true, new EntityDimensions(0.25f, 0.25f));
        BOAT = register(EntityBoat.class, org.bukkit.entity.EntityType.BOAT, "Boat", 1, 3, true, new EntityDimensions(1.5f, 0.6f));
        MINECART_RIDEABLE = register(EntityMinecartRideable.class, org.bukkit.entity.EntityType.MINECART, EntityMinecartAbstract.EnumMinecartType.RIDEABLE.b(), 10, 3, true, new EntityDimensions(0.98f, 0.7f));
        MINECART_CHEST = register(EntityMinecartChest.class, org.bukkit.entity.EntityType.MINECART_CHEST, EntityMinecartAbstract.EnumMinecartType.CHEST.b(), 10, 3, true, new EntityDimensions(0.98f, 0.7f));
        MINECART_FURNACE = register(EntityMinecartFurnace.class, org.bukkit.entity.EntityType.MINECART_FURNACE, EntityMinecartAbstract.EnumMinecartType.FURNACE.b(), 10, 3, true, new EntityDimensions(0.98f, 0.7f));
        MINECART_TNT = register(EntityMinecartTNT.class, org.bukkit.entity.EntityType.MINECART_TNT, EntityMinecartAbstract.EnumMinecartType.TNT.b(), 10, 3, true, new EntityDimensions(0.98f, 0.7f));
        MINECART_HOPPER = register(EntityMinecartHopper.class, org.bukkit.entity.EntityType.MINECART_HOPPER, EntityMinecartAbstract.EnumMinecartType.HOPPER.b(), 10, 3, true, new EntityDimensions(0.98f, 0.7f));
        MINECART_MOB_SPAWNER = register(EntityMinecartMobSpawner.class, org.bukkit.entity.EntityType.MINECART_MOB_SPAWNER, EntityMinecartAbstract.EnumMinecartType.SPAWNER.b(), 10, 3, true, new EntityDimensions(0.98f, 0.7f));
        MINECART_COMMAND_BLOCK = register(EntityMinecartCommandBlock.class, org.bukkit.entity.EntityType.MINECART_COMMAND, EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK.b(), 10, 3, true, new EntityDimensions(0.98f, 0.7f));
        ENDER_CRYSTAL = register(EntityEnderCrystal.class, org.bukkit.entity.EntityType.ENDER_CRYSTAL, "EnderCrystal", 51, Integer.MAX_VALUE, false, new EntityDimensions(2.0f, 2.0f));
        FISHING_HOOK = register(EntityFishingHook.class, org.bukkit.entity.EntityType.FISHING_HOOK, "FishingHook", 90, 5, true, new EntityDimensions(0.25f, 0.25f));
    }

}
