package org.reborn.FeatherDisguise.metadata;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MetadataIndex {

    public static final int STARTING_METADATA_INDEX_ID = 0;     // ground-zero mother frackers

    // region Static abstract keys

    @NotNull public static final List<MetadataObject<?>> ENTITY_METADATA_KEYS = new ArrayList<MetadataObject<?>>(5) {{
        add(EntityMetadata.GENERIC_FLAGS);
        add(EntityMetadata.AIR_TICKS);
        add(EntityMetadata.NAMETAG);
        add(EntityMetadata.NAMETAG_VISIBLE);
        add(EntityMetadata.IS_SILENT);
    }};

    @NotNull public static final List<MetadataObject<?>> LIVING_ENTITY_METADATA_KEYS = new ArrayList<MetadataObject<?>>(5) {{
        add(LivingEntityMetadata.HEALTH);
        add(LivingEntityMetadata.POTION_EFFECT_COLOR_ID);
        add(LivingEntityMetadata.IS_POTION_EFFECT_AMBIENT);
        add(LivingEntityMetadata.ARROWS_STUCK);
        add(LivingEntityMetadata.IS_AI_DISABLED);
    }};

    // endregion

    // region Modal

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class EntityMetadata {

        @NotNull public static final MetadataObject<Byte> GENERIC_FLAGS;                                                // 0
        @NotNull public static final MetadataObject<Integer> AIR_TICKS;                                                 // 1
        @NotNull public static final MetadataObject<String> NAMETAG;                                                    // 2
        @NotNull public static final MetadataObject<Byte> NAMETAG_VISIBLE;                                              // 3
        @NotNull public static final MetadataObject<Byte> IS_SILENT;                                                    // 4

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder();

            GENERIC_FLAGS = builder.add((byte) 0);
            AIR_TICKS = builder.add(300);
            NAMETAG = builder.add("");
            NAMETAG_VISIBLE = builder.add(setByteFlag(false));
            IS_SILENT = builder.add(setByteFlag(true));

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class LivingEntityMetadata {

        @NotNull public static final MetadataObject<Float> HEALTH;                                                      // 6
        @NotNull public static final MetadataObject<Integer> POTION_EFFECT_COLOR_ID;                                    // 7
        @NotNull public static final MetadataObject<Byte> IS_POTION_EFFECT_AMBIENT;                                     // 8
        @NotNull public static final MetadataObject<Byte> ARROWS_STUCK;                                                 // 9
        @NotNull public static final MetadataObject<Byte> IS_AI_DISABLED;                                               // 15

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(EntityMetadata.SCHEMA);

            HEALTH = builder.offset(2, 1.0f);       // offset the index: 4 (+2) -> 6
            POTION_EFFECT_COLOR_ID = builder.add(0);
            IS_POTION_EFFECT_AMBIENT = builder.add(setByteFlag(false));
            ARROWS_STUCK = builder.add((byte) 0);
            IS_AI_DISABLED = builder.offset(6, setByteFlag(false));         // offset the index: 9 (+6) -> 15

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class AgedEntityMetadata {

        @NotNull public static final MetadataObject<Byte> IS_BABY;                                                      // 12

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(LivingEntityMetadata.SCHEMA);

            IS_BABY = builder.setAndApply(12, (byte) 0);

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class TamedEntityMetadata {

        @NotNull public static final MetadataObject<Byte> GENERIC_FLAGS;                                                // 16
        @NotNull public static final MetadataObject<String> OWNER_NAME;                                                 // 17

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(AgedEntityMetadata.SCHEMA);

            GENERIC_FLAGS = builder.setAndApply(16, (byte) 0);
            OWNER_NAME = builder.add("");

            SCHEMA = builder.build();
        }
    }

    // region Modal Entity

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class AbstractHorseMetadata {

        @NotNull public static final MetadataObject<Integer> GENERIC_FLAGS;                                             // 16
        @NotNull public static final MetadataObject<Byte> TYPE;                                                         // 19
        @NotNull public static final MetadataObject<Integer> COLOR_STYLE;                                               // 20
        @NotNull public static final MetadataObject<String> OWNER_NAME;                                                 // 21
        @NotNull public static final MetadataObject<Integer> ARMOR_TYPE;                                                // 22

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(AgedEntityMetadata.SCHEMA);

            GENERIC_FLAGS = builder.setAndApply(16, 0);
            TYPE = builder.offset(3, (byte) 0);
            COLOR_STYLE = builder.add(0);
            OWNER_NAME = builder.add("");
            ARMOR_TYPE = builder.add(0);

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class AbstractGuardianMetadata {

        @NotNull public static final MetadataObject<Integer> GENERIC_FLAGS;                                             // 16
        @NotNull public static final MetadataObject<Integer> TARGET_ENTITY_ID;                                          // 17

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(LivingEntityMetadata.SCHEMA);

            GENERIC_FLAGS = builder.setAndApply(16, 0);
            TARGET_ENTITY_ID = builder.add(0);

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class AbstractSkeletonMetadata {

        @NotNull public static final MetadataObject<Byte> TYPE;                                                         // 13

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(LivingEntityMetadata.SCHEMA);

            TYPE = builder.setAndApply(13, (byte) 0);

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class AbstractZombieMetadata {

        @NotNull public static final MetadataObject<Byte> IS_BABY;                                                      // 12
        @NotNull public static final MetadataObject<Byte> IS_VILLAGER;                                                  // 13
        @NotNull public static final MetadataObject<Byte> IS_CONVERTING;                                                // 14

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(LivingEntityMetadata.SCHEMA);

            IS_BABY = builder.setAndApply(12, setByteFlag(false));
            IS_VILLAGER = builder.add(setByteFlag(false));
            IS_CONVERTING = builder.add(setByteFlag(false));

            SCHEMA = builder.build();
        }
    }

    // endregion

    // endregion

    // region Object & Non-Living

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class ArmorStandMetadata {

        @NotNull public static final MetadataObject<Byte> GENERIC_FLAGS;                                                // 10
        @NotNull public static final MetadataObject<Vector3f> HEAD_POSE;                                                // 11
        @NotNull public static final MetadataObject<Vector3f> BODY_POSE;                                                // 12
        @NotNull public static final MetadataObject<Vector3f> LEFT_ARM_POSE;                                            // 13
        @NotNull public static final MetadataObject<Vector3f> RIGHT_ARM_POSE;                                           // 14
        @NotNull public static final MetadataObject<Vector3f> LEFT_LEG_POSE;                                            // 15
        @NotNull public static final MetadataObject<Vector3f> RIGHT_LEG_POSE;                                           // 16

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(LivingEntityMetadata.SCHEMA);       // starts at 15, inherited from LIVING_ENTITY
            builder.remove(15); // LivingEntityBase within NMS uses this for "AI", but armor-stands don't have AI. to avoid client crashes, always remove this index first

            GENERIC_FLAGS = builder.setAndApply(10, (byte) 0);
            HEAD_POSE = builder.add(new Vector3f(0.0f, 0.0f, 0.0f));
            BODY_POSE = builder.add(new Vector3f(0.0f, 0.0f, 0.0f));
            LEFT_ARM_POSE = builder.add(new Vector3f(-10.0f, 0.0f, -10.0f));
            RIGHT_ARM_POSE = builder.add(new Vector3f(-15.0f, 0.0f, 10.0f));
            LEFT_LEG_POSE = builder.add(new Vector3f(-1.0f, 0.0f, -1.0f));
            RIGHT_LEG_POSE = builder.add(new Vector3f(1.0f, 0.0f, 1.0f));

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class ItemEntityMetadata {

        @NotNull public static final MetadataObject<ItemStack> ITEM_STACK;                                              // 10

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(EntityMetadata.SCHEMA);

            ITEM_STACK = builder.setAndApply(10, new ItemStack(Blocks.STONE, 0));

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class ArrowMetadata {

        @NotNull public static final MetadataObject<Byte> IS_CRITICAL;                                                  // 16

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(EntityMetadata.SCHEMA);

            IS_CRITICAL = builder.setAndApply(16, (byte) 0);

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class WitherSkullMetadata {

        @NotNull public static final MetadataObject<Byte> IS_CHARGED;                                                   // 10

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(EntityMetadata.SCHEMA);

            IS_CHARGED = builder.setAndApply(10, setByteFlag(false));

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class ItemFrameMetadata {

        @NotNull public static final MetadataObject<Integer> ITEM_SLOT;                                                 // 8
        @NotNull public static final MetadataObject<Byte> ROTATION;                                                     // 9

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(EntityMetadata.SCHEMA);

            ITEM_SLOT = builder.setAndApply(8, 5);
            ROTATION = builder.add((byte) 0);

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class FireworkMetadata {

        @NotNull public static final MetadataObject<Integer> ITEM_STACK;                                                // 8

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(EntityMetadata.SCHEMA);

            ITEM_STACK = builder.setAndApply(8, 5);

            SCHEMA = builder.build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class BoatMetadata {

        @NotNull public static final MetadataObject<Integer> TIME_SINCE_HIT;                                            // 17
        @NotNull public static final MetadataObject<Integer> FORWARD_DIRECTION;                                         // 18
        @NotNull public static final MetadataObject<Float> DAMAGE_TAKEN;                                                // 19

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(EntityMetadata.SCHEMA);

            TIME_SINCE_HIT = builder.setAndApply(17, 0);
            FORWARD_DIRECTION = builder.add(1);
            DAMAGE_TAKEN = builder.add(0.0f);

            SCHEMA = builder.build();
        }
    }

    // endregion

    // region Player

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class PlayerMetadata {

        @NotNull public static final MetadataObject<Byte> GENERIC_FLAGS;                                                // 10
        @NotNull public static final MetadataObject<Byte> CAPE_BIT;                                                     // 16
        @NotNull public static final MetadataObject<Float> ABSORPTION_HEARTS;                                           // 17
        @NotNull public static final MetadataObject<Integer> SCORE;                                                     // 18

        @NotNull public static final MetadataSchema SCHEMA;

        static {

            final MetadataSchema.Builder builder = MetadataSchema.builder().inherit(LivingEntityMetadata.SCHEMA);       // starts at 15, inherited from LIVING_ENTITY
            builder.remove(15); // LivingEntityBase within NMS uses this for "AI", but players don't have AI. to avoid client crashes, always remove this index first

            GENERIC_FLAGS = builder.setAndApply(10, (byte) 0);
            CAPE_BIT = builder.offset(6, (byte) 0);
            ABSORPTION_HEARTS = builder.add(0.0f);
            SCORE = builder.add(0);

            SCHEMA = builder.build();
        }
    }

    // endregion

    // region Helpful

    // a lot of 1_8 metadata fields have stupid byte flags instead
    // of booleans. this helpful utility just lets us declare using booleans
    // but returns the correct byte value for the metadata field.
    public static byte setByteFlag(final boolean flag) {
        return flag ? (byte) 1 : (byte) 0;
    }

    // endregion
}
