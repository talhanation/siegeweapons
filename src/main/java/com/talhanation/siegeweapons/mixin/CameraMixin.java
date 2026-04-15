package com.talhanation.siegeweapons.mixin;

import com.talhanation.siegeweapons.entities.BallistaEntity;
import com.talhanation.siegeweapons.entities.IShootingWeapon;
import com.talhanation.siegeweapons.entities.SiegeTowerEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow private Vec3 position;
    @Shadow private float xRot;
    @Shadow private float yRot;

    @Inject(method = "setup", at = @At("HEAD"), cancellable = true)
    public void onAimingWithBallista(BlockGetter blockGetter, Entity renderViewEntity, boolean isThirdPerson, boolean isMirror, float partialTicks, CallbackInfo ci) {

        if (renderViewEntity instanceof Player player) {
            if (player.getVehicle() instanceof BallistaEntity ballista && ballista.getShowTrajectory()) {

                double x = Mth.lerp(partialTicks, ballista.xo, ballista.getX());
                double y = Mth.lerp(partialTicks, ballista.yo, ballista.getY());
                double z = Mth.lerp(partialTicks, ballista.zo, ballista.getZ());

                float yaw = Mth.rotLerp(partialTicks, ballista.yRotO, ballista.getYRot());
                float pitch = Mth.lerp(partialTicks, ballista.xRotO, ballista.getXRot());

                double yawRad = Math.toRadians(yaw);

                Vec3 rotatedOffset = new Vec3(
                        -Math.sin(yawRad) * 0.15,
                        1.6,
                        Math.cos(yawRad) * 0.15
                );

                this.position = new Vec3(x, y, z).add(rotatedOffset);
                this.xRot = pitch;
                this.yRot = yaw;

                ci.cancel();
            }
            else if(player.getVehicle() instanceof SiegeTowerEntity siegeTower && siegeTower.getShowTrajectory()){
                double x = Mth.lerp(partialTicks, siegeTower.xo, siegeTower.getX());
                double y = Mth.lerp(partialTicks, siegeTower.yo, siegeTower.getY());
                double z = Mth.lerp(partialTicks, siegeTower.zo, siegeTower.getZ());

                float yaw = Mth.rotLerp(partialTicks, siegeTower.yRotO, siegeTower.getYRot());
                float pitch = Mth.lerp(partialTicks, siegeTower.xRotO, siegeTower.getXRot());

                double yawRad = Math.toRadians(yaw);

                Vec3 rotatedOffset = new Vec3(
                        -Math.sin(yawRad) * 0.15,
                        1.6,
                        Math.cos(yawRad) * 0.15
                );

                this.position = new Vec3(x, y + 7.5, z).add(rotatedOffset);
                this.xRot = pitch;
                this.yRot = yaw;

                ci.cancel();
            }
        }

    }
}
