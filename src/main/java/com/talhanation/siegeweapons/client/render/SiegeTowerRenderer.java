package com.talhanation.siegeweapons.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.client.models.SiegeTowerModel;
import com.talhanation.siegeweapons.entities.SiegeTowerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SiegeTowerRenderer extends EntityRenderer<SiegeTowerEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Main.MOD_ID, "textures/entity/siegetower.png");

    public SiegeTowerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.8F;
    }

    @Override
    public ResourceLocation getTextureLocation(SiegeTowerEntity entity) {
        return TEXTURE;
    }
    private final SiegeTowerModel<SiegeTowerEntity> model = new SiegeTowerModel<>();
    @Override
    public void render(SiegeTowerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0D, -0.25D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180F - entityYaw));
        poseStack.scale(-1.1F, -1.1F, 1.1F);

        poseStack.translate(0.0D, -1.7D,-0.5D);

        this.model.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);

        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
