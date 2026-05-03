package com.talhanation.siegeweapons.entities;

import com.talhanation.siegeweapons.ModTexts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TransportCartEntity extends AbstractInventoryVehicleEntity {

    private static final EntityDataAccessor<Integer> CARGO = SynchedEntityData.defineId(TransportCartEntity.class, EntityDataSerializers.INT);

    public TransportCartEntity(EntityType<? extends AbstractInventoryVehicleEntity> entityType, Level world) {
        super(entityType, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

    }

    @Override
    public boolean itemInteraction(Player player, InteractionHand interactionHand) {
        return super.itemInteraction(player, interactionHand);
    }

    @Override
    public Vec3 getDriverPosition() {
        double f = 0F;
        double d = 0F;
        return (new Vec3(f, 0.0D, 0.0D + d)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
    }

    @Override
    public int getMaxPassengerSize() {
        return 1;
    }

    @Override
    public int getMaxSpeedInKmH() {
        return 15;//TODO: CONFIG
    }

    @Override
    public double getMaxHealth() {
        return 200;//TODO: CONFIG
    }

    public Component getVehicleTypeName(){
        return ModTexts.CATAPULT;
    }

    @Override
    public void openGUI(Player player) {

    }

    protected void positionRider(Entity rider, MoveFunction moveFunction) {
        if (this.hasPassenger(rider)) {
            Vec3 vec = getDriverPosition();

            moveFunction.accept(rider, this.getX() + vec.x, this.getY(), this.getZ() + vec.z);
        }
    }

    @Override
    public boolean shouldRiderSit() {

        return false;
    }
}
