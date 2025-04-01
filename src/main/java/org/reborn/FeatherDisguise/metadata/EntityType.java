package org.reborn.FeatherDisguise.metadata;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/*
 * this exists because the 1.8 EntityTypes NMS class is dogshit when it comes to getting information about registered nms entities.
 * however, we are on 1.8, so none of these fields will change. so instead ive backported the EntityType<> class from modern spigot for easy usage here.
 */
@Getter
public class EntityType<T extends Entity> {

    // NMS entity class
    @NotNull private final Class<? extends Entity> entityClass;

    // Bukkit EntityType
    @NotNull private final org.bukkit.entity.EntityType bukkitEntityType; // just in case we need this lol

    // Defined entity key-name (ripped from decomp)
    @NotNull private final String entityName;

    // LivingEntities -> taken from EntityTypes<> class, Non-Living Entities -> taken from EntityTrackerEntry<> class (used for spawning packet entityTypeID)
    private final int entityReferenceID;

    // entityTracker update frequency
    private final int trackerUpdateFrequency;

    // entityTracker allow sending velocity packets
    private final boolean trackerAllowVelocityUpdates;

    // Defined entity dimensions (ripped from decomp)
    @NotNull private final EntityDimensions entityDimensions;

    // Entity -> Packet<> (returns packet used for spawning entity) (1.8 has many different types for some reason loool)
    @NotNull private final Function<T, Packet<?>> spawningPacket;

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
                                                            final int entityReferenceID, final int trackerUpdateFrequency, final boolean trackerAllowVelocityUpdates,
                                                            final EntityDimensions entityDimensions, final Function<T, Packet<?>> spawningPacket) {

        return new EntityType<>(entity, entityType, entityName, entityReferenceID, trackerUpdateFrequency, trackerAllowVelocityUpdates, entityDimensions, spawningPacket);
    }

    public EntityType(@NotNull final Class<T> entityClass, @NotNull final org.bukkit.entity.EntityType entityType, @NotNull final String entityName,
                      final int entityReferenceID, final int trackerUpdateFrequency, final boolean trackerAllowVelocityUpdates,
                      @NotNull final EntityDimensions entityDimensions, @NotNull final Function<T, Packet<?>> spawningPacket) {

        this.entityClass = entityClass;
        this.bukkitEntityType = entityType;
        this.entityName = entityName;
        this.entityReferenceID = entityReferenceID;
        this.trackerUpdateFrequency = trackerUpdateFrequency;
        this.trackerAllowVelocityUpdates = trackerAllowVelocityUpdates;
        this.entityDimensions = entityDimensions;
        this.spawningPacket = spawningPacket;
    }

    @SuppressWarnings("unchecked")
    public Packet<?> getSpawningPacketFromEntity(@NotNull final Entity entity) {
        return spawningPacket.apply((T) entity);
    }

    static { // these are exactly copied from nms EntityTypes.class
        ARMOR_STAND = register(EntityArmorStand.class, org.bukkit.entity.EntityType.ARMOR_STAND, "ArmorStand", 30, 3, true,
                new EntityDimensions(0.5f, 1.975f, 1.7775f), entityArmorStand -> new PacketPlayOutSpawnEntity(entityArmorStand, 78));
        CREEPER = register(EntityCreeper.class, org.bukkit.entity.EntityType.CREEPER, "Creeper", 50, 3, true,
                new EntityDimensions(0.6f, 1.8f), PacketPlayOutSpawnEntityLiving::new);
        SKELETON = register(EntitySkeleton.class, org.bukkit.entity.EntityType.SKELETON, "Skeleton", 51, 3, true,
                new EntityDimensions(0.6f, 1.95f, 1.74f), PacketPlayOutSpawnEntityLiving::new);
        WITHER_SKELETON = register(EntitySkeleton.class, org.bukkit.entity.EntityType.SKELETON, "Skeleton", 51, 3, true,
                new EntityDimensions(0.72f, 2.535f, 2.1f), PacketPlayOutSpawnEntityLiving::new);        // has to use SKELETON data
        SPIDER = register(EntitySpider.class, org.bukkit.entity.EntityType.SPIDER, "Spider", 52, 3, true,
                new EntityDimensions(1.4f, 0.9f, 0.65f), PacketPlayOutSpawnEntityLiving::new);
        GIANT = register(EntityGiantZombie.class, org.bukkit.entity.EntityType.GIANT, "Giant", 53, 3, true,
                new EntityDimensions(3.6f, 12.0f, 10.44f), PacketPlayOutSpawnEntityLiving::new);
        ZOMBIE = register(EntityZombie.class, org.bukkit.entity.EntityType.ZOMBIE, "Zombie", 54, 3, true,
                new EntityDimensions(0.6f, 1.95f, 1.74f), PacketPlayOutSpawnEntityLiving::new);
        ZOMBIE_VILLAGER = register(EntityZombie.class, org.bukkit.entity.EntityType.ZOMBIE, "Zombie", 54, 3, true,
                new EntityDimensions(0.6f, 1.95f, 1.74f), PacketPlayOutSpawnEntityLiving::new);                // has to use ZOMBIE data
        SLIME = register(EntitySlime.class, org.bukkit.entity.EntityType.SLIME, "Slime", 55, 3, true,
                new EntityDimensions(0.51000005f, 0.51000005f, 0.325f), PacketPlayOutSpawnEntityLiving::new);
        GHAST = register(EntityGhast.class, org.bukkit.entity.EntityType.GHAST, "Ghast", 56, 3, true,
                new EntityDimensions(4.0f, 4.0f, 2.6f), PacketPlayOutSpawnEntityLiving::new);
        ZOMBIE_PIGMAN = register(EntityPigZombie.class, org.bukkit.entity.EntityType.PIG_ZOMBIE, "PigZombie", 57, 3, true,
                new EntityDimensions(0.6f, 1.95f, 1.79f), PacketPlayOutSpawnEntityLiving::new);
        ENDERMAN = register(EntityEnderman.class, org.bukkit.entity.EntityType.ENDERMAN, "Enderman", 58, 3, true,
                new EntityDimensions(0.6f, 2.9f, 2.55f), PacketPlayOutSpawnEntityLiving::new);
        CAVE_SPIDER = register(EntityCaveSpider.class, org.bukkit.entity.EntityType.CAVE_SPIDER, "CaveSpider", 59, 3, true,
                new EntityDimensions(0.7f, 0.5f, 0.45f), PacketPlayOutSpawnEntityLiving::new);
        SILVERFISH = register(EntitySilverfish.class, org.bukkit.entity.EntityType.SILVERFISH, "Silverfish", 60, 3, true,
                new EntityDimensions(0.4f, 0.3f, 0.13f), PacketPlayOutSpawnEntityLiving::new);
        BLAZE = register(EntityBlaze.class, org.bukkit.entity.EntityType.BLAZE, "Blaze", 61, 3, true,
                new EntityDimensions(0.6f, 1.8f), PacketPlayOutSpawnEntityLiving::new);
        MAGMA_CUBE = register(EntityMagmaCube.class, org.bukkit.entity.EntityType.MAGMA_CUBE, "LavaSlime", 62, 3, true,
                new EntityDimensions(0.51000005f, 0.51000005f, 0.325f), PacketPlayOutSpawnEntityLiving::new);
        WITHER_BOSS = register(EntityWither.class, org.bukkit.entity.EntityType.WITHER, "WitherBoss", 64,3, false,
                new EntityDimensions(0.9f, 3.5f), PacketPlayOutSpawnEntityLiving::new);
        BAT = register(EntityBat.class, org.bukkit.entity.EntityType.BAT, "Bat", 65, 3, false,
                new EntityDimensions(0.5f, 0.9f, 0.45f), PacketPlayOutSpawnEntityLiving::new);
        WITCH = register(EntityWitch.class, org.bukkit.entity.EntityType.WITCH, "Witch", 66, 3, true,
                new EntityDimensions(0.6f, 1.95f, 1.62f), PacketPlayOutSpawnEntityLiving::new);
        ENDERMITE = register(EntityEndermite.class, org.bukkit.entity.EntityType.ENDERMITE, "Endermite", 67, 3, true,
                new EntityDimensions(0.4f, 0.3f, 0.13f), PacketPlayOutSpawnEntityLiving::new);
        GUARDIAN = register(EntityGuardian.class, org.bukkit.entity.EntityType.GUARDIAN, "Guardian", 68, 3, true,
                new EntityDimensions(0.85f, 0.85f, 0.425f), PacketPlayOutSpawnEntityLiving::new);
        ELDER_GUARDIAN = register(EntityGuardian.class, org.bukkit.entity.EntityType.GUARDIAN, "Guardian", 68, 3, true,
                new EntityDimensions(1.9975f, 1.9975f, 0.99875f), PacketPlayOutSpawnEntityLiving::new);         // has to use GUARDIAN data
        PIG = register(EntityPig.class, org.bukkit.entity.EntityType.PIG, "Pig", 90, 3, true,
                new EntityDimensions(0.9f, 0.9f, 0.86875f), PacketPlayOutSpawnEntityLiving::new);
        SHEEP = register(EntitySheep.class, org.bukkit.entity.EntityType.SHEEP, "Sheep", 91, 3, true,
                new EntityDimensions(0.9f, 1.3f, 1.235f), PacketPlayOutSpawnEntityLiving::new);
        COW = register(EntityCow.class, org.bukkit.entity.EntityType.COW, "Cow", 92, 3, true,
                new EntityDimensions(0.9f, 1.3f), PacketPlayOutSpawnEntityLiving::new);
        CHICKEN = register(EntityChicken.class, org.bukkit.entity.EntityType.CHICKEN, "Chicken", 93, 3, true,
                new EntityDimensions(0.4f, 0.7f, 0.644f), PacketPlayOutSpawnEntityLiving::new);
        SQUID = register(EntitySquid.class, org.bukkit.entity.EntityType.SQUID, "Squid", 94, 3, true,
                new EntityDimensions(0.8f, 0.8f, 0.4f), PacketPlayOutSpawnEntityLiving::new);
        WOLF = register(EntityWolf.class, org.bukkit.entity.EntityType.WOLF, "Wolf", 95, 3, true,
                new EntityDimensions(0.6f, 0.8f, 0.68f), PacketPlayOutSpawnEntityLiving::new);
        MUSHROOM_COW = register(EntityMushroomCow.class, org.bukkit.entity.EntityType.MUSHROOM_COW, "MushroomCow", 96, 3, true,
                new EntityDimensions(0.9f, 1.3f), PacketPlayOutSpawnEntityLiving::new);
        SNOWMAN = register(EntitySnowman.class, org.bukkit.entity.EntityType.SNOWMAN, "SnowMan", 97, 3, true,
                new EntityDimensions(0.7f, 1.9f, 1.7f), PacketPlayOutSpawnEntityLiving::new);
        OCELOT = register(EntityOcelot.class, org.bukkit.entity.EntityType.OCELOT, "Ozelot", 98, 3, true,
                new EntityDimensions(0.6f, 0.7f), PacketPlayOutSpawnEntityLiving::new);
        IRON_GOLEM = register(EntityIronGolem.class, org.bukkit.entity.EntityType.IRON_GOLEM, "VillagerGolem", 99, 3, true,
                new EntityDimensions(1.4f, 2.9f, 2.55f), PacketPlayOutSpawnEntityLiving::new);
        HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true,
                new EntityDimensions(1.4f, 1.6f, 1.52f), PacketPlayOutSpawnEntityLiving::new);
        DONKEY = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true,
                new EntityDimensions(1.4f, 1.6f, 1.52f), PacketPlayOutSpawnEntityLiving::new);         // has to use HORSE data
        MULE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true,
                new EntityDimensions(1.4f, 1.6f, 1.52f), PacketPlayOutSpawnEntityLiving::new);         // has to use HORSE data
        ZOMBIE_HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true,
                new EntityDimensions(1.4f, 1.6f, 1.52f), PacketPlayOutSpawnEntityLiving::new);         // has to use HORSE data
        SKELETON_HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, 3, true,
                new EntityDimensions(1.4f, 1.6f, 1.52f), PacketPlayOutSpawnEntityLiving::new);         // has to use HORSE data
        RABBIT = register(EntityRabbit.class, org.bukkit.entity.EntityType.RABBIT, "Rabbit", 101, 3, true,
                new EntityDimensions(0.6f, 0.7f), PacketPlayOutSpawnEntityLiving::new);
        VILLAGER = register(EntityVillager.class, org.bukkit.entity.EntityType.VILLAGER, "Villager", 120, 3, true,
                new EntityDimensions(0.6f, 1.8f), PacketPlayOutSpawnEntityLiving::new);
        PLAYER = register(EntityPlayer.class, org.bukkit.entity.EntityType.PLAYER, "Player", DEFAULT_OR_UNKNOWN_ENTITY_REF_ID, 2, false,
                new EntityDimensions(0.6f, 1.8f), PacketPlayOutNamedEntitySpawn::new);

        ITEM_TILE = register(EntityItem.class, org.bukkit.entity.EntityType.DROPPED_ITEM, "Item", 2, 20, true,
                new EntityDimensions(0.25f, 0.25f), itemTileEntity -> new PacketPlayOutSpawnEntity(itemTileEntity, 2, 1));
        EXPERIENCE_ORB = register(EntityExperienceOrb.class, org.bukkit.entity.EntityType.EXPERIENCE_ORB, "XPOrb", DEFAULT_OR_UNKNOWN_ENTITY_REF_ID, 3, true,
                new EntityDimensions(0.5f, 0.5f), PacketPlayOutSpawnEntityExperienceOrb::new);
        EGG = register(EntityEgg.class, org.bukkit.entity.EntityType.EGG, "ThrownEgg", 62, 10, true,
                new EntityDimensions(0.25f, 0.25f), entityEgg -> new PacketPlayOutSpawnEntity(entityEgg, 62));
        LEASH_HITCH = register(EntityLeash.class, org.bukkit.entity.EntityType.LEASH_HITCH, "LeashKnot", 77, Integer.MAX_VALUE, false,
                new EntityDimensions(0.5f, 0.5f), EntityType::leashHitchPacket);
        PAINTING = register(EntityPainting.class, org.bukkit.entity.EntityType.PAINTING, "Painting", DEFAULT_OR_UNKNOWN_ENTITY_REF_ID, Integer.MAX_VALUE, false,
                new EntityDimensions(0.5f, 0.5f), PacketPlayOutSpawnEntityPainting::new);
        ARROW = register(EntityArrow.class, org.bukkit.entity.EntityType.ARROW, "Arrow", 60, 20, false,
                new EntityDimensions(0.5f, 0.5f), EntityType::arrowPacket);
        SNOWBALL = register(EntitySnowball.class, org.bukkit.entity.EntityType.SNOWBALL, "Snowball", 61,10, true,
                new EntityDimensions(0.25f, 0.25f), entitySnowball -> new PacketPlayOutSpawnEntity(entitySnowball, 61));
        LARGE_FIREBALL = register(EntityLargeFireball.class, org.bukkit.entity.EntityType.FIREBALL, "Fireball", 63, 10, false,
                new EntityDimensions(1.0f, 1.0f), entityLargeFireball -> abstractFireballPacket(entityLargeFireball, 63));
        SMALL_FIREBALL = register(EntitySmallFireball.class, org.bukkit.entity.EntityType.SMALL_FIREBALL, "SmallFireball", 64, 10, false,
                new EntityDimensions(0.3125f, 0.3125f), entitySmallFireball -> abstractFireballPacket(entitySmallFireball, 64));
        ENDER_PEARL = register(EntityEnderPearl.class, org.bukkit.entity.EntityType.ENDER_PEARL, "ThrownEnderpearl", 65, 10, true,
                new EntityDimensions(0.25f, 0.25f), entityEnderPearl -> new PacketPlayOutSpawnEntity(entityEnderPearl, 65));
        EYE_OF_ENDER = register(EntityEnderSignal.class, org.bukkit.entity.EntityType.ENDER_SIGNAL, "EyeOfEnderSignal", 72, 4, true,
                new EntityDimensions(0.25f, 0.25f), entityEnderSignal -> new PacketPlayOutSpawnEntity(entityEnderSignal, 72));
        POTION = register(EntityPotion.class, org.bukkit.entity.EntityType.SPLASH_POTION, "ThrownPotion", 73, 10, true,
                new EntityDimensions(0.25f, 0.25f), entityPotion -> new PacketPlayOutSpawnEntity(entityPotion, 73, entityPotion.getPotionValue()));
        EXPERIENCE_BOTTLE = register(EntityThrownExpBottle.class, org.bukkit.entity.EntityType.THROWN_EXP_BOTTLE, "ThrownExpBottle", 75, 10, true,
                new EntityDimensions(0.25f, 0.25f), entityThrownExpBottle -> new PacketPlayOutSpawnEntity(entityThrownExpBottle, 75));
        ITEM_FRAME = register(EntityItemFrame.class, org.bukkit.entity.EntityType.ITEM_FRAME, "ItemFrame", 71, Integer.MAX_VALUE, false,
                new EntityDimensions(0.5f, 0.5f), EntityType::itemFramePacket);
        WITHER_SKULL = register(EntityWitherSkull.class, org.bukkit.entity.EntityType.WITHER_SKULL, "WitherSkull", 66, 10, false,
                new EntityDimensions(0.3125f, 0.3125f), entityWitherSkull -> abstractFireballPacket(entityWitherSkull, 66));
        PRIMED_TNT = register(EntityTNTPrimed.class, org.bukkit.entity.EntityType.PRIMED_TNT, "PrimedTnt", 50, 10, true,
                new EntityDimensions(0.98f, 0.98f), entityTNTPrimed -> new PacketPlayOutSpawnEntity(entityTNTPrimed, 50));
        FALLING_BLOCK = register(EntityFallingBlock.class, org.bukkit.entity.EntityType.FALLING_BLOCK, "FallingSand", 70, 20, true,
                new EntityDimensions(1.0f, 1.0f), entityFallingBlock -> new PacketPlayOutSpawnEntity(entityFallingBlock, 70, Block.getCombinedId(entityFallingBlock.getBlock())));
        FIREWORKS = register(EntityFireworks.class, org.bukkit.entity.EntityType.FIREWORK, "FireworksRocketEntity", 76, 10, true,
                new EntityDimensions(0.25f, 0.25f), entityFireworks -> new PacketPlayOutSpawnEntity(entityFireworks, 76));
        BOAT = register(EntityBoat.class, org.bukkit.entity.EntityType.BOAT, "Boat", 1, 3, true,
                new EntityDimensions(1.5f, 0.6f), entityBoat -> new PacketPlayOutSpawnEntity(entityBoat, 1));
        MINECART_RIDEABLE = register(EntityMinecartRideable.class, org.bukkit.entity.EntityType.MINECART, EntityMinecartAbstract.EnumMinecartType.RIDEABLE.b(), 10, 3, true,
                new EntityDimensions(0.98f, 0.7f), EntityType::abstractMinecartPacket);
        MINECART_CHEST = register(EntityMinecartChest.class, org.bukkit.entity.EntityType.MINECART_CHEST, EntityMinecartAbstract.EnumMinecartType.CHEST.b(), 10, 3, true,
                new EntityDimensions(0.98f, 0.7f), EntityType::abstractMinecartPacket);
        MINECART_FURNACE = register(EntityMinecartFurnace.class, org.bukkit.entity.EntityType.MINECART_FURNACE, EntityMinecartAbstract.EnumMinecartType.FURNACE.b(), 10, 3, true,
                new EntityDimensions(0.98f, 0.7f), EntityType::abstractMinecartPacket);
        MINECART_TNT = register(EntityMinecartTNT.class, org.bukkit.entity.EntityType.MINECART_TNT, EntityMinecartAbstract.EnumMinecartType.TNT.b(), 10, 3, true,
                new EntityDimensions(0.98f, 0.7f), EntityType::abstractMinecartPacket);
        MINECART_HOPPER = register(EntityMinecartHopper.class, org.bukkit.entity.EntityType.MINECART_HOPPER, EntityMinecartAbstract.EnumMinecartType.HOPPER.b(), 10, 3, true,
                new EntityDimensions(0.98f, 0.7f), EntityType::abstractMinecartPacket);
        MINECART_MOB_SPAWNER = register(EntityMinecartMobSpawner.class, org.bukkit.entity.EntityType.MINECART_MOB_SPAWNER, EntityMinecartAbstract.EnumMinecartType.SPAWNER.b(), 10, 3, true,
                new EntityDimensions(0.98f, 0.7f), EntityType::abstractMinecartPacket);
        MINECART_COMMAND_BLOCK = register(EntityMinecartCommandBlock.class, org.bukkit.entity.EntityType.MINECART_COMMAND, EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK.b(), 10, 3, true,
                new EntityDimensions(0.98f, 0.7f), EntityType::abstractMinecartPacket);
        ENDER_CRYSTAL = register(EntityEnderCrystal.class, org.bukkit.entity.EntityType.ENDER_CRYSTAL, "EnderCrystal", 51, Integer.MAX_VALUE, false,
                new EntityDimensions(2.0f, 2.0f), entityEnderCrystal -> new PacketPlayOutSpawnEntity(entityEnderCrystal, 51));
        FISHING_HOOK = register(EntityFishingHook.class, org.bukkit.entity.EntityType.FISHING_HOOK, "FishingHook", 90, 5, true,
                new EntityDimensions(0.25f, 0.25f), entityFishingHook -> new PacketPlayOutSpawnEntity(entityFishingHook, 90, entityFishingHook.owner != null ? entityFishingHook.owner.getId() : entityFishingHook.getId()));
    }

    // region Spawning Packet functions

    @ApiStatus.Internal
    @NotNull private static PacketPlayOutSpawnEntity arrowPacket(@NotNull final EntityArrow arrowEntity) {
        final Entity arrowShooter = arrowEntity.shooter;
        int ARROW_ID = 0; // default is 0, but this actually causes the client to fuck up. so we will ensure 0 is never used (this is only a problem if the arrow is the first entity ever spawned within a world)

        if (arrowShooter != null) {ARROW_ID = arrowShooter.getId();}
        if (ARROW_ID == 0 && arrowEntity.getId() != 0) {ARROW_ID = arrowEntity.getId();}
        if (ARROW_ID == 0) {ARROW_ID = 53915;}
        // data IDs of 0 for arrows cause them to glitch and incorrectly do clientside physics. normally, the spigot tracker would try to
        // use the shooters ID for data, else just use the arrowID itself. however if the arrow is the first entity spawned in the world,
        // this causes the arrows to behave wrong and visually flicker on the client. to resolve this edge case, we do the logic above to ensure that problem never occurs

        return new PacketPlayOutSpawnEntity(arrowEntity, ARROW.entityReferenceID, ARROW_ID);
    }

    @ApiStatus.Internal
    @NotNull private static PacketPlayOutSpawnEntity abstractFireballPacket(@NotNull final EntityFireball abstractFireballEntity, final int fireballEntityID) {
        return new PacketPlayOutSpawnEntity(abstractFireballEntity, fireballEntityID, abstractFireballEntity.shooter != null ? abstractFireballEntity.shooter.getId() : 0);
    }

    @ApiStatus.Internal
    @NotNull private static PacketPlayOutSpawnEntity abstractMinecartPacket(@NotNull final EntityMinecartAbstract abstractMinecartEntity) {
        return new PacketPlayOutSpawnEntity(abstractMinecartEntity, 10, abstractMinecartEntity.s().a());
    }

    @ApiStatus.Internal
    @NotNull private static PacketPlayOutSpawnEntity leashHitchPacket(@NotNull final EntityLeash leashEntity) {
        final PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(leashEntity, LEASH_HITCH.entityReferenceID);
        final BlockPosition blockPosition = leashEntity.getBlockPosition();

        spawnPacket.a(MathHelper.d((float) (blockPosition.getX() * 32.0))); // blockPosX
        spawnPacket.b(MathHelper.d((float) (blockPosition.getY() * 32.0))); // blockPosY
        spawnPacket.c(MathHelper.d((float) (blockPosition.getZ() * 32.0))); // blockPosZ

        return spawnPacket;
    }

    @ApiStatus.Internal
    @NotNull private static PacketPlayOutSpawnEntity itemFramePacket(@NotNull final EntityItemFrame itemFrameEntity) {
        final PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(itemFrameEntity, ITEM_FRAME.entityReferenceID, itemFrameEntity.direction.b());
        final BlockPosition blockPosition = itemFrameEntity.getBlockPosition();

        spawnPacket.a(MathHelper.d((float) (blockPosition.getX() * 32.0))); // blockPosX
        spawnPacket.b(MathHelper.d((float) (blockPosition.getY() * 32.0))); // blockPosY
        spawnPacket.c(MathHelper.d((float) (blockPosition.getZ() * 32.0))); // blockPosZ

        return spawnPacket;
    }

    // endregion

}
