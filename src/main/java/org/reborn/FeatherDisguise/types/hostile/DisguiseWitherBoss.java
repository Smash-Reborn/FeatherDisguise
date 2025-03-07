package org.reborn.FeatherDisguise.types.hostile;

import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.WitherBossMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

import java.util.Collections;
import java.util.List;

public class DisguiseWitherBoss extends AbstractDisguise<WitherBossMetadataHolder> {

    public DisguiseWitherBoss(@NotNull Player player) {
        super(DisguiseType.WITHER_BOSS, new WitherBossMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.WITHER_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.WITHER_DEATH;
    }

    public double getSquidRelatedEntityYOffset() {
        return 0.65d;
    }

    @NotNull public List<PacketWrapper<?>> extraPacketsToProvideDuringEntitySpawning() {
        return Collections.singletonList(new WrapperPlayServerUpdateAttributes(
                this.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID(),
                Collections.singletonList(new WrapperPlayServerUpdateAttributes.Property(
                        Attributes.MAX_HEALTH, 69.0f, Collections.singletonList(
                                new WrapperPlayServerUpdateAttributes.PropertyModifier(
                                        Attributes.MAX_HEALTH.getName(), 31.0f,
                                        WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.ADDITION))))));
        // send a max health attribute packet too so the boss-bar is filled completely client-side
    }
}
