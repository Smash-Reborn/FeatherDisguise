package org.reborn.FeatherDisguise.metadata.modal;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.types.AbstractMetadataHolder;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;

public class LivingEntityMetadataHolder<E extends EntityType<?>> extends AbstractMetadataHolder<E> {

    public LivingEntityMetadataHolder(@NotNull E entityType) {
        super(entityType);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata(); // construct the base Entity metadata
        this.setHealth(this.getHealth());
        this.setPotionEffectColorID(this.getPotionEffectColorID());
        this.setPotionEffectAmbient(this.isPotionEffectAmbient());
        this.setArrowsStuck(this.getArrowsStuck());
        this.setAIDisabled(true);
    }

    public float getHealth() {
        return this.getIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_HEALTH, 1.0f);
    }

    public void setHealth(float health) {
        this.setIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_HEALTH, EntityDataTypes.FLOAT, health);
    }

    public int getPotionEffectColorID() {
        return this.getIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_POTION_EFFECT_COLOR, 0);
    }

    public void setPotionEffectColorID(int colorID) {
        this.setIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_POTION_EFFECT_COLOR, EntityDataTypes.INT, colorID);
    }

    @ApiStatus.Internal
    private byte getInternalIsPotionEffectAmbient() {
        return this.getIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_POTION_EFFECT_AMBIENT, (byte) 0);
    }

    public boolean isPotionEffectAmbient() {
        return this.getInternalIsPotionEffectAmbient() >= 1;
    }

    public void setPotionEffectAmbient(boolean isAmbient) {
        this.setIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_POTION_EFFECT_AMBIENT, EntityDataTypes.BYTE, (byte) (isAmbient ? 1 : 0));
    }

    public byte getArrowsStuck() {
        return this.getIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_ARROWS_IN_ENTITY, (byte) 0);
    }

    public void setArrowsStuck(int arrowsStuck) {
        this.setIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_ARROWS_IN_ENTITY, EntityDataTypes.BYTE, (byte) arrowsStuck);
    }

    @ApiStatus.Internal
    private byte getInternalIsAIDisabled() {
        return this.getIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_HAS_AI, (byte) 0);
    }

    public boolean isAIDisabled() {
        return this.getInternalIsAIDisabled() != 0;
    }

    public void setAIDisabled(boolean isDisabled) {
        this.setIndex((byte) EntityMetadataIndexes.LIVING_ENTITY_HAS_AI, EntityDataTypes.BYTE, (byte) (isDisabled ? 1 : 0));
    }

}
