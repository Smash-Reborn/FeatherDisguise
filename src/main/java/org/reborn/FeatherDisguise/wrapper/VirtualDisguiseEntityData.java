package org.reborn.FeatherDisguise.wrapper;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.types.AbstractMetadataHolder;

import java.util.UUID;

public class VirtualDisguiseEntityData<T extends AbstractMetadataHolder<?>> {

    @ApiStatus.Internal
    @NotNull private final BlankEntity blank;

    @Getter @NotNull private final T metadataHolder;

    public VirtualDisguiseEntityData(@NotNull final T metadataHolder, @NotNull final org.bukkit.World bukkitWorld) {
        this.blank = new BlankEntity(((CraftWorld) bukkitWorld).getHandle());
        this.metadataHolder = metadataHolder;
    }

    public int getVirtualID() {
        return this.blank.getId();
    }

    @NotNull public UUID getVirtualUUID() {
        return this.blank.getUniqueID();
    }

    // warning: just use the getters above for vars you want.
    //          the only reason this is included is that some
    //          packets don't provide the correct constructors we want,
    //          and they usually take the actual entity object.
    //          this is more-so for convenience, but not in a good way...
    @ApiStatus.Experimental
    @NotNull public Entity getVirtualEntity() {
        return this.blank;
    }

    /*
     * in modern versions, there are easier ways to get the entityID and entityUUID.
     * unfortunately, older spigot is even more ass in this regard, so we need
     * to generate blank entities to increment the id counter & fetch a uuid.
     *
     * cringe but we don't have much of a choice if we want to remain spigot compatible
     */
    private static class BlankEntity extends Entity {

        public BlankEntity(@NotNull final World world) {
            super(world);
            // thankfully constructing this is barely a thing to notice, so we can ignore it
        }

        @Override
        protected void h() { }

        @Override
        protected void a(NBTTagCompound nbtTagCompound) { }

        @Override
        protected void b(NBTTagCompound nbtTagCompound) { }
    }
}
