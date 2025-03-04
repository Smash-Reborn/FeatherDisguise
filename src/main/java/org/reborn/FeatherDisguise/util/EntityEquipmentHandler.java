package org.reborn.FeatherDisguise.util;

import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter @Getter
@NoArgsConstructor @Log4j2
public class EntityEquipmentHandler {

    @NotNull private ItemStack mainHandItemstack = new ItemStack(Material.AIR);

    //@NotNull private ItemStack offHandItemstack = new ItemStack(Material.AIR);    // doesn't exist for 1.8 (actually based af)

    @NotNull private ItemStack helmetItemstack = new ItemStack(Material.AIR);

    @NotNull private ItemStack chestplateItemstack = new ItemStack(Material.AIR);

    @NotNull private ItemStack leggingsItemstack = new ItemStack(Material.AIR);

    @NotNull private ItemStack bootsItemstack = new ItemStack(Material.AIR);

    public void setItemInSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack, boolean updateItemCache,
                              final int entityID, @NotNull final List<Player> playersReceivingPacket) {

        this.setItemToSlotAndSendPacketToViewingClients(
                Collections.singletonList(new EquipmentData(equipmentSlot, itemStack, updateItemCache)), entityID, playersReceivingPacket);
    }

    public void clearAllSlots(final int entityID, @NotNull final List<Player> playersReceivingPacket) {
        final List<EquipmentData> equipmentData = new ArrayList<>(6);
        equipmentData.add(new EquipmentData(EquipmentSlot.MAIN_HAND, new ItemStack(Material.AIR), true));
        //equipmentData.add(new EquipmentData(EquipmentSlot.OFF_HAND, new ItemStack(Material.AIR), true));
        equipmentData.add(new EquipmentData(EquipmentSlot.HELMET, new ItemStack(Material.AIR), true));
        equipmentData.add(new EquipmentData(EquipmentSlot.CHEST_PLATE, new ItemStack(Material.AIR), true));
        equipmentData.add(new EquipmentData(EquipmentSlot.LEGGINGS, new ItemStack(Material.AIR), true));
        equipmentData.add(new EquipmentData(EquipmentSlot.BOOTS, new ItemStack(Material.AIR), true));

        this.setItemToSlotAndSendPacketToViewingClients(equipmentData, entityID, playersReceivingPacket);
    }

    public void clearArmorSlots(final int entityID, @NotNull final List<Player> playerReceivingPacket) {
        final List<EquipmentData> equipmentData = new ArrayList<>(4);
        equipmentData.add(new EquipmentData(EquipmentSlot.HELMET, new ItemStack(Material.AIR), true));
        equipmentData.add(new EquipmentData(EquipmentSlot.CHEST_PLATE, new ItemStack(Material.AIR), true));
        equipmentData.add(new EquipmentData(EquipmentSlot.LEGGINGS, new ItemStack(Material.AIR), true));
        equipmentData.add(new EquipmentData(EquipmentSlot.BOOTS, new ItemStack(Material.AIR), true));

        this.setItemToSlotAndSendPacketToViewingClients(equipmentData, entityID, playerReceivingPacket);
    }

    @ApiStatus.Internal
    private void setItemToSlotAndSendPacketToViewingClients(@NotNull final List<EquipmentData> equipmentData, final int entityID, @NotNull final List<Player> playersReceivingPacket) {
        if (equipmentData.isEmpty() || playersReceivingPacket.isEmpty()) return;

        final List<Equipment> packetEquipmentList = new ArrayList<>(equipmentData.size());

        // loop through our equipment data, add to the list and if
        // we have marked it, modify the item cache to reflect the new item
        for (final EquipmentData eqData : equipmentData) {
            packetEquipmentList.add(new Equipment(eqData.getEquipmentSlot(), SpigotConversionUtil.fromBukkitItemStack(eqData.getItemStack())));

            if (eqData.isUpdateItemCache()) {
                switch (eqData.getEquipmentSlot()) {
                    case MAIN_HAND:
                        this.setMainHandItemstack(eqData.getItemStack());
                    //case OFF_HAND -> this.setOffHandItemstack(eqData.getItemStack());
                    case HELMET:
                        this.setHelmetItemstack(eqData.getItemStack());
                    case CHEST_PLATE:
                        this.setChestplateItemstack(eqData.getItemStack());
                    case LEGGINGS:
                        this.setLeggingsItemstack(eqData.getItemStack());
                    case BOOTS:
                        this.setBootsItemstack(eqData.getItemStack());
                }
            }
        }

        final WrapperPlayServerEntityEquipment equipmentPacket = new WrapperPlayServerEntityEquipment(entityID, packetEquipmentList);
        playersReceivingPacket.forEach(viewer -> PacketUtil.sendPacketEventsPacket(viewer, equipmentPacket, true));     // don't tamper with my equipment bitch
    }

    // this was a record in project-ob1, but since those spicy things don't exist in java 8 ig we just have to settle for classes instead
    @ApiStatus.Internal @AllArgsConstructor @Getter
    private static class EquipmentData {

        @NotNull private final EquipmentSlot equipmentSlot;
        @NotNull private final ItemStack itemStack;
        private final boolean updateItemCache;
    }

}
