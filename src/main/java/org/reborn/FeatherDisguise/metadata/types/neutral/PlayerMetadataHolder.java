package org.reborn.FeatherDisguise.metadata.types.neutral;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class PlayerMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityPlayer>> {

    public PlayerMetadataHolder() {
        super(EntityType.PLAYER);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.removeIndex((byte) 15); // LivingEntityBase within the NMS uses this for "AI", but players don't have AI. to avoid client crashes, always remove this index for players
        this.setMask((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, (byte) 0); // makes the bitmask 0, which is the default for the player skin flags
        this.setCapeBit(false);
        this.setAbsorptionHearts(this.getAbsorptionHearts());
        this.setScore(this.getScore());
    }

    public boolean isCapeEnabled() {
        return this.getMaskBit((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_CAPE.getBitValue());
    }

    public void setCapeEnabled(boolean allowCape) {
        this.setMaskBit(EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_CAPE.getBitValue(), allowCape);
    }

    public boolean isJacketEnabled() {
        return this.getMaskBit((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_JACKET.getBitValue());
    }

    public void setJacketEnabled(boolean allowJacket) {
        this.setMaskBit(EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_JACKET.getBitValue(), allowJacket);
    }

    public boolean isLeftSleeveEnabled() {
        return this.getMaskBit((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_LEFT_SLEEVE.getBitValue());
    }

    public void setLeftSleeveEnabled(boolean allowLeftSleeve) {
        this.setMaskBit(EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_LEFT_SLEEVE.getBitValue(), allowLeftSleeve);
    }

    public boolean isRightSleeveEnabled() {
        return this.getMaskBit((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_RIGHT_SLEEVE.getBitValue());
    }

    public void setRightSleeveEnabled(boolean allowRightSleeve) {
        this.setMaskBit(EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_RIGHT_SLEEVE.getBitValue(), allowRightSleeve);
    }

    public boolean isLeftLegEnabled() {
        return this.getMaskBit((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_LEFT_LEG_PANTS.getBitValue());
    }

    public void setLeftLegEnabled(boolean allowLeftLegPants) {
        this.setMaskBit(EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_LEFT_LEG_PANTS.getBitValue(), allowLeftLegPants);
    }

    public boolean isRightLegEnabled() {
        return this.getMaskBit((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_RIGHT_LEG_PANTS.getBitValue());
    }

    public void setRightLegEnabled(boolean allowRightLegPants) {
        this.setMaskBit(EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_RIGHT_LEG_PANTS.getBitValue(), allowRightLegPants);
    }

    public boolean isHatEnabled() {
        return this.getMaskBit((byte) EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_HAT.getBitValue());
    }

    public void setHatEnabled(boolean allowHat) {
        this.setMaskBit(EntityMetadataIndexes.PLAYER_SKIN_FLAGS, PlayerEntityBitMaskType.HAS_HAT.getBitValue(), allowHat);
    }

    @ApiStatus.Internal
    private byte getInternalCapeBit() {
        return this.getIndex((byte) EntityMetadataIndexes.PLAYER_CAPE_BIT, (byte) 0);
    }

    @ApiStatus.Experimental // i have no idea wtf this is supposed to do clientside, there's no code for it???
    public void setCapeBit(boolean hasCape) {
        this.setIndex((byte) EntityMetadataIndexes.PLAYER_CAPE_BIT, EntityDataTypes.BYTE, (byte) (hasCape ? 0x02 : 0)); // the wiki says 0x02, but they might just be pulling this out of their ass?
    }

    public float getAbsorptionHearts() {
        return this.getIndex((byte) EntityMetadataIndexes.PLAYER_ABSORPTION_HEARTS, 0f);
    }

    public void setAbsorptionHearts(float amount) {
        this.setIndex((byte) EntityMetadataIndexes.PLAYER_ABSORPTION_HEARTS, EntityDataTypes.FLOAT, amount);
    }

    public int getScore() {
        return this.getIndex((byte) EntityMetadataIndexes.PLAYER_SCORE, 0); // boooo u suck at the game!
    }

    public void setScore(int score) {
        this.setIndex((byte) EntityMetadataIndexes.PLAYER_SCORE, EntityDataTypes.INT, score);
    }

    // this is my hacky way to synchronise the nms player metadata with our holder system, since
    // packetevents for some reason doesn't come with any sort of feature like this in-built into their metadata system
    public void cheekySynchroniseNMSPlayerMetadataToHolder(@NotNull final EntityPlayer nmsPlayer) {

        // base entity
        this.setOnFire(nmsPlayer.isBurning());
        this.setSneaking(nmsPlayer.isSneaking());
        this.setSprinting(nmsPlayer.isSprinting());
        this.setPerformingAction((nmsPlayer.getDataWatcher().getByte(0) & 1 << EntityBitMaskType.IS_DOING_ACTION.getBitID()) != 0); // spigot provides no method LOL, so just raw dog it
        this.setInvisible(nmsPlayer.isInvisible());

        this.setCustomName(nmsPlayer.getCustomName());
        this.setCustomNameVisible(nmsPlayer.getCustomNameVisible());
        this.setSilent(nmsPlayer.R());

        // living entity
        this.setHealth(nmsPlayer.getHealth());
        this.setPotionEffectColorID(nmsPlayer.getDataWatcher().getInt(EntityMetadataIndexes.LIVING_ENTITY_POTION_EFFECT_COLOR));
        this.setPotionEffectAmbient(nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.LIVING_ENTITY_POTION_EFFECT_AMBIENT) > 0);
        this.setArrowsStuck(0); // why would we even bother with this lmao

        // player entity
        this.setCapeEnabled((nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.PLAYER_SKIN_FLAGS) & PlayerEntityBitMaskType.HAS_CAPE.getParkMask()) == PlayerEntityBitMaskType.HAS_CAPE.getParkMask());
        this.setJacketEnabled((nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.PLAYER_SKIN_FLAGS) & PlayerEntityBitMaskType.HAS_JACKET.getParkMask()) == PlayerEntityBitMaskType.HAS_JACKET.getParkMask());
        this.setLeftSleeveEnabled((nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.PLAYER_SKIN_FLAGS) & PlayerEntityBitMaskType.HAS_LEFT_SLEEVE.getParkMask()) == PlayerEntityBitMaskType.HAS_LEFT_SLEEVE.getParkMask());
        this.setRightSleeveEnabled((nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.PLAYER_SKIN_FLAGS) & PlayerEntityBitMaskType.HAS_RIGHT_SLEEVE.getParkMask()) == PlayerEntityBitMaskType.HAS_RIGHT_SLEEVE.getParkMask());
        this.setLeftLegEnabled((nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.PLAYER_SKIN_FLAGS) & PlayerEntityBitMaskType.HAS_LEFT_LEG_PANTS.getParkMask()) == PlayerEntityBitMaskType.HAS_LEFT_LEG_PANTS.getParkMask());
        this.setRightLegEnabled((nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.PLAYER_SKIN_FLAGS) & PlayerEntityBitMaskType.HAS_RIGHT_LEG_PANTS.getParkMask()) == PlayerEntityBitMaskType.HAS_RIGHT_LEG_PANTS.getParkMask());
        this.setRightLegEnabled((nmsPlayer.getDataWatcher().getByte(EntityMetadataIndexes.PLAYER_SKIN_FLAGS) & PlayerEntityBitMaskType.HAS_HAT.getParkMask()) == PlayerEntityBitMaskType.HAS_HAT.getParkMask());
        this.setCapeBit(false); // this seems to be accurate to nms?
        this.setAbsorptionHearts(nmsPlayer.getAbsorptionHearts());
        this.setScore(53915); // b e p i s
    }

    @Getter @AllArgsConstructor
    public enum PlayerEntityBitMaskType {
        HAS_CAPE(0, (byte) 0x01),
        HAS_JACKET(1, (byte) 0x02),
        HAS_LEFT_SLEEVE(2, (byte) 0x04),
        HAS_RIGHT_SLEEVE(3, (byte) 0x08),
        HAS_LEFT_LEG_PANTS(4, (byte) 0x10),
        HAS_RIGHT_LEG_PANTS(5, (byte) 0x20),
        HAS_HAT(6, (byte) 0x40);

        private final int bitID;
        private final byte bitValue;

        public int getParkMask() {
            return 1 << bitID; // copied from decomped src (EnumPlayerModelParts.class)
        }
    }

}
