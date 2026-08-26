package org.reborn.FeatherDisguise.wrappers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.util.VanillaSoundData;
import org.reborn.FeatherDisguise.wrappers.animation.DisguiseAnimation;
import org.reborn.FeatherDisguise.wrappers.dimension.DisguiseDimension;
import org.reborn.FeatherDisguise.wrappers.metadata.DisguiseMetadata;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PUBLIC)
public class DisguiseFlags<T extends Entity> {

    // corresponding entity that the object will be disguised as
    @NotNull private final EntityType<T> entityType;

    // dimensions which determine properties such as width, height, eye height, etc
    // (stored for both the disguised object and the visual disguise entity)
    @NotNull private final DisguiseDimension dimensions;

    // flagged animations which are allowed to be played for the disguise
    @NotNull private final DisguiseAnimation animations;

    // flagged metadata indexes which are allowed to be synchronized with the disguise
    // (disguised object <-> disguise entity) (base entity, living entity, etc)
    @NotNull private final DisguiseMetadata metadata;

    @Builder.Default private final boolean renderItemsInHandSlot = false;

    @Builder.Default private final boolean renderItemsInArmorSlots = false;

    @Builder.Default private final boolean renderArmSwingAnimation = false;

    @Builder.Default private final boolean headRotationYawLocked = false;

    @Builder.Default private final boolean headRotationPitchLocked = false;

    @Builder.Default private final boolean ignoringRotationPackets = false;

    @Builder.Default private final boolean checkForIsGroundedState = true;

    @Builder.Default private final boolean armorStandForSynchronousMovement = false;

    @Builder.Default @Nullable private final Vector armorStandPositionOffset = null;

    @ApiStatus.Experimental
    @Builder.Default private final boolean customPacketClientPayload = false;

    @Builder.Default @Nullable private final VanillaSoundData hurtSound = null;

    @Builder.Default @Nullable private final VanillaSoundData deathSound = null;

    @Builder.Default @Nullable private final Runnable onHurtAction = null;

    @Builder.Default @Nullable private final Runnable onDeathAction = null;

    @Builder.Default @Nullable List<Packet<?>> auxiliaryOnSpawnDisguisePackets = null;

    @Builder.Default @Nullable List<Packet<?>> auxiliaryOnDestroyDisguisePackets = null;
}
