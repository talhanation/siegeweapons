package com.talhanation.siegeweapons.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.talhanation.siegeweapons.entities.SmallHorseCartEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SmallHorseCartModel<T extends SmallHorseCartEntity> extends EntityModel<T> {
	private final ModelPart SmallCart;
	private final ModelPart wheels;
	private final ModelPart rightWheel;
	private final ModelPart leftWheel;

	public SmallHorseCartModel() {
		ModelPart root = createBodyLayer().bakeRoot();
		this.SmallCart = root.getChild("SmallCart");
		this.wheels = this.SmallCart.getChild("wheels");
		this.rightWheel = this.wheels.getChild("rightWheel");
		this.leftWheel = this.wheels.getChild("leftWheel");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition SmallCart = partdefinition.addOrReplaceChild("SmallCart", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -3.0F, -16.0F, 24.0F, 2.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(88, 74).addBox(-10.0F, -11.0F, 14.0F, 20.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(88, 74).addBox(-10.0F, -11.0F, -16.0F, 20.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 74).addBox(8.0F, -5.0F, -36.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 74).addBox(-10.0F, -5.0F, -36.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-12.0F, -11.0F, -16.0F, 2.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 4.0F));

		PartDefinition cube_r1 = SmallCart.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-1.0F, -4.0F, -16.0F, 2.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, -7.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition wheels = SmallCart.addOrReplaceChild("wheels", CubeListBuilder.create().texOffs(48, 96).addBox(-30.0F, -0.85F, -1.35F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(14.0F, -0.15F, 0.35F));

		PartDefinition rightWheel = wheels.addOrReplaceChild("rightWheel", CubeListBuilder.create().texOffs(0, 124).addBox(-1.0F, -1.025F, -6.525F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 7.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, -8.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-27.5F, 0.175F, -0.825F));

		PartDefinition cube_r2 = rightWheel.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 140).addBox(-1.5F, -3.5F, -9.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 6.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -0.35F, 0.825F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r3 = rightWheel.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 140).addBox(-1.5F, -3.5F, -9.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 6.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.525F, 0.475F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r4 = rightWheel.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 140).addBox(-1.5F, -3.5F, -9.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 6.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -0.35F, 0.15F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r5 = rightWheel.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 124).addBox(-1.0F, 2.0F, -8.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.975F, 3.475F, -1.5708F, 0.0F, 0.0F));

		PartDefinition leftWheel = wheels.addOrReplaceChild("leftWheel", CubeListBuilder.create().texOffs(0, 124).addBox(-1.0F, -1.025F, -6.525F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 7.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, -8.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.175F, -0.825F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r6 = leftWheel.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 140).addBox(-1.5F, -3.5F, -9.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 6.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -0.35F, 0.825F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r7 = leftWheel.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 140).addBox(-1.5F, -3.5F, -9.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 6.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.525F, 0.475F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r8 = leftWheel.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 140).addBox(-1.5F, -3.5F, -9.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F))
		.texOffs(0, 140).addBox(-1.5F, -3.5F, 6.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -0.35F, 0.15F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r9 = leftWheel.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 124).addBox(-1.0F, 2.0F, -8.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.975F, 3.475F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(SmallHorseCartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		wheels.xRot = entity.getWheelRotation(limbSwing);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		SmallCart.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}