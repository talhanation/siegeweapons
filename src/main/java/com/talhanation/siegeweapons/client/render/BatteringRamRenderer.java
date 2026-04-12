package com.talhanation.siegeweapons.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.client.models.BatteringRamModel;
import com.talhanation.siegeweapons.entities.BatteringRamEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BatteringRamRenderer extends EntityRenderer<BatteringRamEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Main.MOD_ID, "textures/entity/battering_ram.png");

    public BatteringRamRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.8F;
    }

    @Override
    public ResourceLocation getTextureLocation(BatteringRamEntity entity) {
        return TEXTURE;
    }
    private final BatteringRamModel<BatteringRamEntity> model = new BatteringRamModel<>();
    @Override
    public void render(BatteringRamEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0D, -0.3D, 0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180F - entityYaw));
        poseStack.scale(-1.3F, -1.3F, 1.3F);

        this.model.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);

        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
