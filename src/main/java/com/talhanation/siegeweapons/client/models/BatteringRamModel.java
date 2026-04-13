package com.talhanation.siegeweapons.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.talhanation.siegeweapons.entities.BatteringRamEntity;
import com.talhanation.siegeweapons.entities.CatapultEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BatteringRamModel<T extends BatteringRamEntity> extends EntityModel<T> {
	private final ModelPart BatteringRam;
	private final ModelPart wheelsFront;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart wheelsBack;
	private final ModelPart bone5;
	private final ModelPart bone6;
	private final ModelPart ram;

	public BatteringRamModel() {
		ModelPart root = createBodyLayer().bakeRoot();
		this.BatteringRam = root.getChild("BatteringRam");
		this.wheelsFront = this.BatteringRam.getChild("wheelsFront");
		this.bone = this.wheelsFront.getChild("bone");
		this.bone2 = this.wheelsFront.getChild("bone2");
		this.wheelsBack = this.BatteringRam.getChild("wheelsBack");
		this.bone5 = this.wheelsBack.getChild("bone5");
		this.bone6 = this.wheelsBack.getChild("bone6");
		this.ram = this.BatteringRam.getChild("ram");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition BatteringRam = partdefinition.addOrReplaceChild("BatteringRam", CubeListBuilder.create().texOffs(184, 190).addBox(14.0F, -30.0F, -34.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(184, 190).addBox(14.0F, -30.0F, 30.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(184, 190).addBox(-18.0F, -30.0F, 30.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(184, 190).addBox(-18.0F, -30.0F, -34.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 102).addBox(-2.0F, -42.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
		.texOffs(160, 102).addBox(14.0F, -34.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
		.texOffs(160, 102).addBox(-18.0F, -34.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
		.texOffs(160, 102).addBox(-18.0F, -10.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
		.texOffs(160, 102).addBox(14.0F, -10.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = BatteringRam.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 182).addBox(-30.0F, -2.0F, -1.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 182).addBox(-30.0F, -2.0F, 20.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 182).addBox(-30.0F, -2.0F, 41.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 182).addBox(-30.0F, -2.0F, -22.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -43.0F, -18.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r2 = BatteringRam.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 182).addBox(0.0F, -2.0F, -1.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 182).addBox(0.0F, -2.0F, 20.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 182).addBox(0.0F, -2.0F, 41.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 182).addBox(0.0F, -2.0F, -22.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -43.0F, -18.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition wheelsFront = BatteringRam.addOrReplaceChild("wheelsFront", CubeListBuilder.create().texOffs(-1, 214).addBox(-24.0F, -1.0F, -1.0F, 48.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -32.0F));

		PartDefinition bone = wheelsFront.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(24.0F, -0.15F, 32.35F));

		PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone2 = wheelsFront.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(-16.0F, -0.15F, 32.35F));

		PartDefinition cube_r6 = bone2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r7 = bone2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r8 = bone2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition wheelsBack = BatteringRam.addOrReplaceChild("wheelsBack", CubeListBuilder.create().texOffs(-1, 214).addBox(-24.0F, -1.0F, -1.0F, 48.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 32.0F));

		PartDefinition bone5 = wheelsBack.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(24.0F, -0.15F, 32.35F));

		PartDefinition cube_r9 = bone5.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r10 = bone5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r11 = bone5.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone6 = wheelsBack.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(-16.0F, -0.15F, 32.35F));

		PartDefinition cube_r12 = bone6.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r13 = bone6.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r14 = bone6.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition ram = BatteringRam.addOrReplaceChild("ram", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 124.0F, -45.0F, 12.0F, 12.0F, 90.0F, new CubeDeformation(0.0F))
		.texOffs(184, 182).addBox(-20.0F, 128.0F, 1.0F, 40.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -150.0F, 0.0F));

		PartDefinition cube_r15 = ram.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 200).addBox(-6.0F, 2.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 110.0F, 35.0F, 3.1416F, 0.0436F, -3.1416F));

		PartDefinition ropeback_r1 = ram.addOrReplaceChild("ropeback_r1", CubeListBuilder.create().texOffs(0, 200).addBox(-6.0F, 2.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 110.0F, -29.0F, 3.1416F, 0.0F, -3.1416F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(BatteringRamEntity entity, float partialTicks, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		wheelsFront.xRot = entity.getWheelRotation(partialTicks);
		wheelsBack.xRot = wheelsFront.xRot;

		ram.xRot = -entity.getRamAngle(partialTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		BatteringRam.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}