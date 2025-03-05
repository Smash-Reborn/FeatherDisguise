package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityWither;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class WitherBossMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityWither>> {

    public WitherBossMetadataHolder() {
        super(EntityType.WITHER_BOSS);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.clearAllHeadTargets(); // all head targets set to 0
        this.setInvulnerabilityTime(this.getInvulnerabilityTime());
    }

    public int getCenterHeadTargetID() {
        return this.getIndex((byte) EntityMetadataIndexes.WITHER_BOSS_CENTER_HEAD_TARGET, 0);
    }

    public boolean doesCenterHeadValidTarget() {
        return this.getCenterHeadTargetID() != 0;
    }

    public void setCenterHeadTargetID(@Nullable Integer targetID) {
        this.setIndex((byte) EntityMetadataIndexes.WITHER_BOSS_CENTER_HEAD_TARGET, EntityDataTypes.INT, targetID != null ? targetID : 0);
    }

    public int getLeftHeadTargetID() {
        return this.getIndex((byte) EntityMetadataIndexes.WITHER_BOSS_LEFT_HEAD_TARGET, 0);
    }

    public boolean doesLeftHeadValidTarget() {
        return this.getLeftHeadTargetID() != 0;
    }

    public void setLeftHeadTargetID(@Nullable Integer targetID) {
        this.setIndex((byte) EntityMetadataIndexes.WITHER_BOSS_LEFT_HEAD_TARGET, EntityDataTypes.INT, targetID != null ? targetID : 0);
    }

    public int getRightHeadTargetID() {
        return this.getIndex((byte) EntityMetadataIndexes.WITHER_BOSS_RIGHT_HEAD_TARGET, 0);
    }

    public boolean doesRightHeadValidTarget() {
        return this.getRightHeadTargetID() != 0;
    }

    public void setRightHeadTargetID(@Nullable Integer targetID) {
        this.setIndex((byte) EntityMetadataIndexes.WITHER_BOSS_RIGHT_HEAD_TARGET, EntityDataTypes.INT, targetID != null ? targetID : 0);
    }

    public void clearAllHeadTargets() {
        this.setCenterHeadTargetID(null);
        this.setLeftHeadTargetID(null);
        this.setRightHeadTargetID(null);
    }

    public void setAllHeadTargets(int targetID) {
        this.setCenterHeadTargetID(targetID);
        this.setLeftHeadTargetID(targetID);
        this.setRightHeadTargetID(targetID);
    }

    public int getInvulnerabilityTime() {
        return this.getIndex((byte) EntityMetadataIndexes.WITHER_BOSS_INVULNERABILITY_TIME, 0);
    }

    public void setInvulnerabilityTime(int invTime) {
        this.setIndex((byte) EntityMetadataIndexes.WITHER_BOSS_INVULNERABILITY_TIME, EntityDataTypes.INT, invTime);
    }
}
