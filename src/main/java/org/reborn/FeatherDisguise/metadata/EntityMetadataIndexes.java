package org.reborn.FeatherDisguise.metadata;

// a mega-bullshit class for indexing stupid mojank metadata values
public class EntityMetadataIndexes {

    // this was handy: https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Entity_metadata?oldid=2767652
    // + just looking at the decompiled nms code for all the entities and cross-checking to make sure all the values were correct

    // region Indexes

    // region Entity

    public static final int ENTITY_GENERIC;
    public static final int ENTITY_AIR_SUPPLY;
    public static final int ENTITY_NAMETAG;
    public static final int ENTITY_SHOW_NAMETAG;
    public static final int ENTITY_IS_SILENT;

    // endregion

    // region LivingEntity

    public static final int LIVING_ENTITY_HEALTH;
    public static final int LIVING_ENTITY_POTION_EFFECT_COLOR;
    public static final int LIVING_ENTITY_POTION_EFFECT_AMBIENT;
    public static final int LIVING_ENTITY_ARROWS_IN_ENTITY;
    public static final int LIVING_ENTITY_HAS_AI;

    // endregion

    // region AgeableEntity

    public static final int AGEABLE_ENTITY_AGE;

    // endregion

    // region TameableEntity

    public static final int TAMEABLE_ENTITY_GENERIC;
    public static final int TAMEABLE_ENTITY_OWNER_NAME;

    // endregion

    // region ArmorStand

    public static final int ARMOR_STAND_GENERIC;
    public static final int ARMOR_STAND_HEAD_POSITION;
    public static final int ARMOR_STAND_BODY_POSITION;
    public static final int ARMOR_STAND_LEFT_ARM_POSITION;
    public static final int ARMOR_STAND_RIGHT_ARM_POSITION;
    public static final int ARMOR_STAND_LEFT_LEG_POSITION;
    public static final int ARMOR_STAND_RIGHT_LEG_POSITION;

    // endregion

    // region Player

    public static final int PLAYER_SKIN_FLAGS;
    public static final int PLAYER_CAPE_BIT;
    public static final int PLAYER_ABSORPTION_HEARTS;
    public static final int PLAYER_SCORE;

    // endregion

    // region Horse

    public static final int HORSE_GENERIC;
    public static final int HORSE_TYPE;
    public static final int HORSE_COLOR_STYLE;
    public static final int HORSE_OWNER_NAME;
    public static final int HORSE_ARMOR_TYPE;

    // endregion

    // region Bat

    public static final int BAT_IS_HANGING;

    // endregion

    // region Ocelot

    public static final int OCELOT_TYPE;

    // endregion

    // region Wolf

    public static final int WOLF_GENERIC;
    public static final int WOLF_HEALTH;
    public static final int WOLF_BEGGING;
    public static final int WOLF_COLLAR_COLOR;

    // endregion

    // region Pig

    public static final int PIG_HAS_SADDLE;

    // endregion

    // region Rabbit

    public static final int RABBIT_TYPE;

    // endregion

    // region Sheep

    public static final int SHEEP_WOOL_DATA;

    // endregion

    // region Villager

    public static final int VILLAGER_TYPE;

    // endregion

    // region Enderman

    public static final int ENDERMAN_CARRIED_BLOCK;
    public static final int ENDERMAN_CARRIED_BLOCK_DATA;
    public static final int ENDERMAN_IS_SCREAMING;

    // endregion

    // region Zombie

    public static final int ZOMBIE_IS_CHILD;
    public static final int ZOMBIE_IS_VILLAGER;
    public static final int ZOMBIE_IS_CONVERTING;

    // endregion

    // region Blaze

    public static final int BLAZE_IS_ON_FIRE;

    // endregion

    // region Spider

    public static final int SPIDER_IS_CLIMBING; // wtf is this LMAO

    // endregion

    // region Creeper

    public static final int CREEPER_STATE;
    public static final int CREEPER_IS_POWERED;

    // endregion

    // region Ghast

    public static final int GHAST_IS_SCREAMING;

    // endregion

    // region Slime

    public static final int SLIME_SIZE;

    // endregion

    // region Skeleton

    public static final int SKELETON_TYPE;

    // endregion

    // region Witch

    public static final int WITCH_IS_AGGRESSIVE;

    // endregion

    // region Iron Gholem

    public static final int IRON_GOLEM_IS_PLAYER_CREATED;

    // endregion

    // region WitherBoss

    public static final int WITHER_BOSS_CENTER_HEAD_TARGET;
    public static final int WITHER_BOSS_LEFT_HEAD_TARGET;
    public static final int WITHER_BOSS_RIGHT_HEAD_TARGET;
    public static final int WITHER_BOSS_INVULNERABILITY_TIME;

    // endregion

    // region Guardian

    public static final int GUARDIAN_GENERIC;
    public static final int GUARDIAN_TARGET_ENTITY;

    // endregion

    // endregion

    static {
        ENTITY_GENERIC = 0; // ground zero mother-fuckers                                                                       // 0
        ENTITY_AIR_SUPPLY = getIncrementedMetadataIndex(ENTITY_GENERIC);                                                        // 1
        ENTITY_NAMETAG = getIncrementedMetadataIndex(ENTITY_AIR_SUPPLY);                                                        // 2
        ENTITY_SHOW_NAMETAG = getIncrementedMetadataIndex(ENTITY_NAMETAG);                                                      // 3
        ENTITY_IS_SILENT = getIncrementedMetadataIndex(ENTITY_SHOW_NAMETAG);                                                    // 4

        LIVING_ENTITY_HEALTH = 6;                                                                                               // 6
        LIVING_ENTITY_POTION_EFFECT_COLOR = getIncrementedMetadataIndex(LIVING_ENTITY_HEALTH);                                  // 7
        LIVING_ENTITY_POTION_EFFECT_AMBIENT = getIncrementedMetadataIndex(LIVING_ENTITY_POTION_EFFECT_COLOR);                   // 8
        LIVING_ENTITY_ARROWS_IN_ENTITY = getIncrementedMetadataIndex(LIVING_ENTITY_POTION_EFFECT_AMBIENT);                      // 9
        LIVING_ENTITY_HAS_AI = 15;                                                                                              // 15

        AGEABLE_ENTITY_AGE = 12;                                                                                                // 12
        TAMEABLE_ENTITY_GENERIC = 16;                                                                                           // 16
        TAMEABLE_ENTITY_OWNER_NAME = getIncrementedMetadataIndex(TAMEABLE_ENTITY_GENERIC);                                      // 17

        ARMOR_STAND_GENERIC = 10;
        ARMOR_STAND_HEAD_POSITION = getIncrementedMetadataIndex(ARMOR_STAND_GENERIC);
        ARMOR_STAND_BODY_POSITION = getIncrementedMetadataIndex(ARMOR_STAND_HEAD_POSITION);
        ARMOR_STAND_LEFT_ARM_POSITION = getIncrementedMetadataIndex(ARMOR_STAND_BODY_POSITION);
        ARMOR_STAND_RIGHT_ARM_POSITION = getIncrementedMetadataIndex(ARMOR_STAND_LEFT_ARM_POSITION);
        ARMOR_STAND_LEFT_LEG_POSITION = getIncrementedMetadataIndex(ARMOR_STAND_RIGHT_ARM_POSITION);
        ARMOR_STAND_RIGHT_LEG_POSITION = getIncrementedMetadataIndex(ARMOR_STAND_LEFT_LEG_POSITION);

        PLAYER_SKIN_FLAGS = 10;
        PLAYER_CAPE_BIT = 16;
        PLAYER_ABSORPTION_HEARTS = getIncrementedMetadataIndex(PLAYER_CAPE_BIT);
        PLAYER_SCORE = getIncrementedMetadataIndex(PLAYER_ABSORPTION_HEARTS);

        HORSE_GENERIC = 16;
        HORSE_TYPE = 19;
        HORSE_COLOR_STYLE = getIncrementedMetadataIndex(HORSE_TYPE);
        HORSE_OWNER_NAME = getIncrementedMetadataIndex(HORSE_COLOR_STYLE);
        HORSE_ARMOR_TYPE = getIncrementedMetadataIndex(HORSE_OWNER_NAME);

        BAT_IS_HANGING = 16;

        OCELOT_TYPE = 18;

        WOLF_GENERIC = 16;
        WOLF_HEALTH = 18;
        WOLF_BEGGING = getIncrementedMetadataIndex(WOLF_HEALTH);
        WOLF_COLLAR_COLOR = getIncrementedMetadataIndex(WOLF_BEGGING);

        PIG_HAS_SADDLE = 16;

        RABBIT_TYPE = 18;

        SHEEP_WOOL_DATA = 16;

        VILLAGER_TYPE = 16;

        ENDERMAN_CARRIED_BLOCK = 16;
        ENDERMAN_CARRIED_BLOCK_DATA = getIncrementedMetadataIndex(ENDERMAN_CARRIED_BLOCK);
        ENDERMAN_IS_SCREAMING = getIncrementedMetadataIndex(ENDERMAN_CARRIED_BLOCK_DATA);

        ZOMBIE_IS_CHILD = 12;
        ZOMBIE_IS_VILLAGER = getIncrementedMetadataIndex(ZOMBIE_IS_CHILD);
        ZOMBIE_IS_CONVERTING = getIncrementedMetadataIndex(ZOMBIE_IS_VILLAGER);

        BLAZE_IS_ON_FIRE = 16;

        SPIDER_IS_CLIMBING = 16;

        CREEPER_STATE = 16;
        CREEPER_IS_POWERED = getIncrementedMetadataIndex(CREEPER_STATE);

        GHAST_IS_SCREAMING = 16;

        SLIME_SIZE = 16;

        SKELETON_TYPE = 13;

        WITCH_IS_AGGRESSIVE = 21;

        IRON_GOLEM_IS_PLAYER_CREATED = 16;

        WITHER_BOSS_CENTER_HEAD_TARGET = 17;
        WITHER_BOSS_RIGHT_HEAD_TARGET = getIncrementedMetadataIndex(WITHER_BOSS_CENTER_HEAD_TARGET);
        WITHER_BOSS_LEFT_HEAD_TARGET = getIncrementedMetadataIndex(WITHER_BOSS_RIGHT_HEAD_TARGET);
        WITHER_BOSS_INVULNERABILITY_TIME = getIncrementedMetadataIndex(WITHER_BOSS_LEFT_HEAD_TARGET);

        GUARDIAN_GENERIC = 16;
        GUARDIAN_TARGET_ENTITY = getIncrementedMetadataIndex(GUARDIAN_GENERIC);
    }

    /** Gets the previous index and returns an incremented {@code integer} by {@code +1}. **/
    private static int getIncrementedMetadataIndex(final int previousIndex) {
        return previousIndex + 1;
    }

}
