package com.talhanation.siegeweapons.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.client.models.BallistaModel;
import com.talhanation.siegeweapons.entities.BallistaEntity;
import com.talhanation.siegeweapons.math.Kalkuel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class BallistaRenderer extends EntityRenderer<BallistaEntity> {

    private static final ResourceLocation[] TEXTURE = {
            new ResourceLocation(Main.MOD_ID,"textures/entity/ballista.png")
    };
    public BallistaRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(BallistaEntity entity) {
        return TEXTURE[0];
    }
    private final BallistaModel<BallistaEntity> model = new BallistaModel<>();
    double translation = -1.5D;

    @Override
    public void render(BallistaEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();

        // Transformations for the ballista model
        poseStack.mulPose(Axis.YN.rotationDegrees(180));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, translation, 0.0D);

        // Update the ballista model's rotation
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        this.model.setCrossBowRotation(entity.isForward() || entity.isBackward(), pitch, entityYaw);
        this.model.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);

        // Render the ballista model
        VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(this.model.renderType(getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        if(entity.getShowTrajectory()){
            float yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());

            double yawRad = Math.toRadians(yaw);
            double pitchRad = Math.toRadians(pitch);

            Vec3 forward = new Vec3(
                    Math.sin(yawRad),
                    0,
                    Math.cos(yawRad)
            );

            List<Vec3> trajectory = calculateTrajectory(forward, -pitchRad, entity.projectileSpeed, 3000, 0, 0.15);
            // Render the trajectory line
            VertexConsumer lineVertexConsumer = multiBufferSource.getBuffer(RenderType.LINES);
            renderBallistaTrajectory(poseStack, lineVertexConsumer, trajectory, 1.0f, 0.0f, 0.0f, 100.0f); // Red line
        }

        poseStack.popPose();
    }
    public static List<Vec3> calculateTrajectory(Vec3 forward, double yShootVec, float initialVelocity, int steps, double heightOffset, double lateralOffset) {

        List<Vec3> trajectory = new ArrayList<>();

        double gravityPerTick = 0.06;

        // ORIGINAL Verhalten beibehalten
        Vec3 direction = new Vec3(forward.x, yShootVec, forward.z)
                .normalize()
                .scale(-1);

        Vec3 side = new Vec3(-forward.z, 0, forward.x)
                .normalize()
                .scale(lateralOffset);

        Vec3 velocity = direction.scale(initialVelocity);

        // Start exakt wie vorher bei t=0
        Vec3 pos = Vec3.ZERO;

        for (int i = 0; i < steps; i++) {

            // Punkt VOR Bewegung hinzufügen (wie t=0 Fall)
            trajectory.add(pos.add(side).add(0, heightOffset, 0));

            // Dann Tick-Simulation
            pos = pos.add(velocity);
            velocity = velocity.add(0, gravityPerTick, 0);
        }

        return trajectory;
    }



    public static void renderBallistaTrajectory(PoseStack poseStack, VertexConsumer vertexConsumer, List<Vec3> points, float r, float g, float b, float alpha) {
        PoseStack.Pose pose = poseStack.last();

        for (int i = 0; i < points.size() - 1; i++) {
            Vec3 p1 = points.get(i);
            Vec3 p2 = points.get(i + 1);

            vertexConsumer.vertex(pose.pose(), (float) p1.x, (float) p1.y, (float) p1.z)
                    .color(r, g, b, alpha)
                    .normal(pose.normal(), 0.0F, 1.0F, 0.0F) // Richtung normalisieren
                    .endVertex();

            vertexConsumer.vertex(pose.pose(), (float) p2.x, (float) p2.y, (float) p2.z)
                    .color(r, g, b, alpha)
                    .normal(pose.normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();
        }
    }


}
