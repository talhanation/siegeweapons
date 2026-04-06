package com.talhanation.siegeweapons.entities.projectile;

import com.talhanation.siegeweapons.config.SiegeWeaponsServerConfig;
import com.talhanation.siegeweapons.entities.AbstractVehicleEntity;
import com.talhanation.siegeweapons.init.ModEntityTypes;
import com.talhanation.siegeweapons.init.ModItems;
import com.talhanation.siegeweapons.init.ModSounds;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BallistaProjectile extends AbstractArrow {
    //Arrow
    public static BallistaProjectile factory(EntityType<? extends BallistaProjectile> entityType, Level level) {
        return new BallistaProjectile(entityType, level);
    }

    public BallistaProjectile(Level world, LivingEntity owner, double d1, double d2, double d3) {
        super(ModEntityTypes.BALLISTA_PROJECTILE.get(), d1, d2, d3, world);
        setOwner(owner);
        setCritArrow(true);
        setKnockback(1);
        setPierceLevel((byte) 1);
        setBaseDamage(SiegeWeaponsServerConfig.ballistaProjectileDamage.get());
    }
    public boolean wasShot = false;
    public int counter = 0;
    private float rotation;

    public float getAccuracy(){
        return 0F;
    };
    public BallistaProjectile(EntityType<? extends BallistaProjectile> type, Level world) {
        super(type, world);
    }

    public float getProjectileRotation(float partialTicks){
        return rotation + getProjectileRotationAmount() * partialTicks;
    }

    public float getProjectileRotationAmount(){
        return inGround ? 0F : 0.055F;
    }

    public void updateProjectileRotation() {
        rotation += getProjectileRotationAmount();
    }

    @Override
    public void tick() {
        super.tick();

        if (wasShot){
            counter++;
        }

        if (counter > 3200){
            this.discard();
        }
        updateProjectileRotation();

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if(!level().isClientSide()){
            BlockState state = level().getBlockState(blockHitResult.getBlockPos());
            for (int i = 0; i < 200; ++i) {
                double d0 = this.random.nextGaussian() * 0.03D;
                double d1 = this.random.nextGaussian() * 0.03D;
                double d2 = this.random.nextGaussian() * 0.03D;
                double d3 = 20.0D;
                double x = this.getX(1.0D) - d0 * d3;
                double y = this.getY() - d1 * d3  + i * 0.012;
                double z = this.getRandomZ(2.0D) - d2 * d3;
                ((ServerLevel) level()).sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, state),
                        x, y, z,
                        1, 0, 0, 0, 0.05
                );
            }
        }
        super.onHitBlock(blockHitResult);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ModItems.BALLISTA_PROJECTILE_ITEM.get().getDefaultInstance();
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if(this.level().isClientSide()) this.hitParticles();
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        if (!this.level().isClientSide()) {

            Entity hitEntity = hitResult.getEntity();
            Entity ownerEntity = this.getOwner();
            if(ownerEntity == null) return;


            boolean isCanceled = false;

            if(hitEntity.isVehicle()){
                if(hitEntity.getControllingPassenger() != null && ownerEntity.getTeam() != null && ownerEntity.isAlliedTo(hitEntity.getControllingPassenger()) && !ownerEntity.getTeam().isAllowFriendlyFire())
                    isCanceled = true;
            }
            else if (ownerEntity instanceof LivingEntity && ownerEntity.getTeam() != null && ownerEntity.isAlliedTo(hitEntity) && !ownerEntity.getTeam().isAllowFriendlyFire()){
                    isCanceled = true;
            }

            if(isCanceled){
                this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
                this.setYRot(this.getYRot() + 180.0F);
                this.yRotO += 180.0F;
                if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                    if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }

                    this.discard();
                }
                return;
            }
        }
        super.onHitEntity(hitResult);
    }

    public void hitParticles(){

    }
    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(@NotNull DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }
}
