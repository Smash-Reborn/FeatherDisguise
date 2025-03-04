package org.reborn.FeatherDisguise.metadata;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.jetbrains.annotations.NotNull;

/*
 * this exists because the 1.8 EntityTypes NMS class is dogshit when it comes to getting information about registered nms entities.
 * however, we are on 1.8, so none of these fields will change. so instead ive backported the EntityType<> class from modern spigot for easy usage here.
 */
public class EntityType<T extends Entity> {

    @NotNull private final Class<? extends Entity> entityClass;

    @Getter @NotNull private final org.bukkit.entity.EntityType bukkitEntityType; // just in case we need this lol

    @Getter @NotNull private final String entityName;

    @Getter private final int entityReferenceID;

    @Getter @NotNull private final EntityDimensions entityDimensions;


    @NotNull public static final EntityType<EntityArmorStand> ARMOR_STAND;
    @NotNull public static final EntityType<EntityCreeper> CREEPER;
    @NotNull public static final EntityType<EntitySkeleton> SKELETON;
    @NotNull public static final EntityType<EntitySkeleton> WITHER_SKELETON;
    @NotNull public static final EntityType<EntitySpider> SPIDER;
    @NotNull public static final EntityType<EntityGiantZombie> GIANT;
    @NotNull public static final EntityType<EntityZombie> ZOMBIE;
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

    public static <T extends Entity> EntityType<T> register(final Class<T> entity, final org.bukkit.entity.EntityType entityType, final String entityName, final int entityReferenceID, final EntityDimensions entityDimensions) {
        return new EntityType<>(entity, entityType, entityName, entityReferenceID, entityDimensions);
    }

    public EntityType(@NotNull final Class<T> entityClass, @NotNull final org.bukkit.entity.EntityType entityType, @NotNull final String entityName, final int entityReferenceID, @NotNull final EntityDimensions entityDimensions) {
        this.entityClass = entityClass;
        this.bukkitEntityType = entityType;
        this.entityName = entityName;
        this.entityReferenceID = entityReferenceID;
        this.entityDimensions = entityDimensions;
    }

    static {
        ARMOR_STAND = register(EntityArmorStand.class, org.bukkit.entity.EntityType.ARMOR_STAND, "ArmorStand", 30, new EntityDimensions(0.5f, 1.975f, 1.7775f));
        CREEPER = register(EntityCreeper.class, org.bukkit.entity.EntityType.CREEPER, "Creeper", 50, new EntityDimensions(0.6f, 1.8f));
        SKELETON = register(EntitySkeleton.class, org.bukkit.entity.EntityType.SKELETON, "Skeleton", 51, new EntityDimensions(0.6f, 1.95f, 1.74f));
        WITHER_SKELETON = register(EntitySkeleton.class, org.bukkit.entity.EntityType.SKELETON, "Skeleton", 51, new EntityDimensions(0.72f, 2.535f, 2.1f));        // has to use SKELETON data
        SPIDER = register(EntitySpider.class, org.bukkit.entity.EntityType.SPIDER, "Spider", 52, new EntityDimensions(1.4f, 0.9f, 0.65f));
        GIANT = register(EntityGiantZombie.class, org.bukkit.entity.EntityType.GIANT, "Giant", 53, new EntityDimensions(3.6f, 12.0f, 10.44f));
        ZOMBIE = register(EntityZombie.class, org.bukkit.entity.EntityType.ZOMBIE, "Zombie", 54, new EntityDimensions(0.6f, 1.95f, 1.74f));
        SLIME = register(EntitySlime.class, org.bukkit.entity.EntityType.SLIME, "Slime", 55, new EntityDimensions(0.52f, 0.52f, 0.325f));           // todo scalable within entityDimensions shit
        GHAST = register(EntityGhast.class, org.bukkit.entity.EntityType.GHAST, "Ghast", 56, new EntityDimensions(4.0f, 4.0f, 2.6f));
        ZOMBIE_PIGMAN = register(EntityPigZombie.class, org.bukkit.entity.EntityType.PIG_ZOMBIE, "PigZombie", 57, new EntityDimensions(0.6f, 1.95f, 1.79f));
        ENDERMAN = register(EntityEnderman.class, org.bukkit.entity.EntityType.ENDERMAN, "Enderman", 58, new EntityDimensions(0.6f, 2.9f, 2.55f));
        CAVE_SPIDER = register(EntityCaveSpider.class, org.bukkit.entity.EntityType.CAVE_SPIDER, "CaveSpider", 59, new EntityDimensions(0.7f, 0.5f, 0.45f));
        SILVERFISH = register(EntitySilverfish.class, org.bukkit.entity.EntityType.SILVERFISH, "Silverfish", 60, new EntityDimensions(0.4f, 0.3f, 0.13f));
        BLAZE = register(EntityBlaze.class, org.bukkit.entity.EntityType.BLAZE, "Blaze", 61, new EntityDimensions(0.6f, 1.8f));
        MAGMA_CUBE = register(EntityMagmaCube.class, org.bukkit.entity.EntityType.MAGMA_CUBE, "LavaSlime", 62, new EntityDimensions(0.52f, 0.52f, 0.325f));
        WITHER_BOSS = register(EntityWither.class, org.bukkit.entity.EntityType.WITHER, "WitherBoss", 64, new EntityDimensions(0.9f, 3.5f));
        BAT = register(EntityBat.class, org.bukkit.entity.EntityType.BAT, "Bat", 65, new EntityDimensions(0.5f, 0.9f, 0.45f));
        WITCH = register(EntityWitch.class, org.bukkit.entity.EntityType.WITCH, "Witch", 66, new EntityDimensions(0.6f, 1.95f, 1.62f));
        ENDERMITE = register(EntityEndermite.class, org.bukkit.entity.EntityType.ENDERMITE, "Endermite", 67, new EntityDimensions(0.4f, 0.3f, 0.13f));
        GUARDIAN = register(EntityGuardian.class, org.bukkit.entity.EntityType.GUARDIAN, "Guardian", 68, new EntityDimensions(0.85f, 0.85f, 0.425f));
        ELDER_GUARDIAN = register(EntityGuardian.class, org.bukkit.entity.EntityType.GUARDIAN, "Guardian", 68, new EntityDimensions(1.9975f, 1.9975f, 0.99875f));         // has to use GUARDIAN data
        PIG = register(EntityPig.class, org.bukkit.entity.EntityType.PIG, "Pig", 90, new EntityDimensions(0.9f, 0.9f, 0.86875f));
        SHEEP = register(EntitySheep.class, org.bukkit.entity.EntityType.SHEEP, "Sheep", 91, new EntityDimensions(0.9f, 1.3f, 1.235f));
        COW = register(EntityCow.class, org.bukkit.entity.EntityType.COW, "Cow", 92, new EntityDimensions(0.9f, 1.3f));
        CHICKEN = register(EntityChicken.class, org.bukkit.entity.EntityType.CHICKEN, "Chicken", 93, new EntityDimensions(0.4f, 0.7f, 0.644f));
        SQUID = register(EntitySquid.class, org.bukkit.entity.EntityType.SQUID, "Squid", 94, new EntityDimensions(0.8f, 0.8f, 0.4f));
        WOLF = register(EntityWolf.class, org.bukkit.entity.EntityType.WOLF, "Wolf", 95, new EntityDimensions(0.6f, 0.8f, 0.68f));
        MUSHROOM_COW = register(EntityMushroomCow.class, org.bukkit.entity.EntityType.MUSHROOM_COW, "MushroomCow", 96, new EntityDimensions(0.9f, 1.3f));
        SNOWMAN = register(EntitySnowman.class, org.bukkit.entity.EntityType.SNOWMAN, "SnowMan", 97, new EntityDimensions(0.7f, 1.9f, 1.7f));
        OCELOT = register(EntityOcelot.class, org.bukkit.entity.EntityType.OCELOT, "Ozelot", 98, new EntityDimensions(0.6f, 0.7f));
        IRON_GOLEM = register(EntityIronGolem.class, org.bukkit.entity.EntityType.IRON_GOLEM, "VillagerGolem", 99, new EntityDimensions(1.4f, 2.9f, 2.55f));
        HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, new EntityDimensions(1.4f, 1.6f, 1.52f));
        DONKEY = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        MULE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        ZOMBIE_HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        SKELETON_HORSE = register(EntityHorse.class, org.bukkit.entity.EntityType.HORSE, "EntityHorse", 100, new EntityDimensions(1.4f, 1.6f, 1.52f));         // has to use HORSE data
        RABBIT = register(EntityRabbit.class, org.bukkit.entity.EntityType.RABBIT, "Rabbit", 101, new EntityDimensions(0.6f, 0.7f));
        VILLAGER = register(EntityVillager.class, org.bukkit.entity.EntityType.VILLAGER, "Villager", 120, new EntityDimensions(0.6f, 1.8f));
        PLAYER = register(EntityPlayer.class, org.bukkit.entity.EntityType.PLAYER, "Player", -1, new EntityDimensions(0.6f, 1.8f));              // technically don't use this data, but parody it anyways
    }

}
