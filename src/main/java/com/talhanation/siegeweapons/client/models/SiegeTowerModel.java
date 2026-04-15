package com.talhanation.siegeweapons.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.talhanation.siegeweapons.entities.SiegeTowerEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SiegeTowerModel<T extends SiegeTowerEntity> extends EntityModel<T> {
	private final ModelPart SiegeTower;
	private final ModelPart wheelsFront;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart wheelsBack;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart ramp;
	private final ModelPart ladders;
	private final ModelPart ladder2;
	private final ModelPart ladder;
	private final ModelPart body;

	public SiegeTowerModel() {
		ModelPart root = createBodyLayer().bakeRoot();
		this.SiegeTower = root.getChild("SiegeTower");
		this.wheelsFront = this.SiegeTower.getChild("wheelsFront");
		this.bone = this.wheelsFront.getChild("bone");
		this.bone2 = this.wheelsFront.getChild("bone2");
		this.wheelsBack = this.SiegeTower.getChild("wheelsBack");
		this.bone3 = this.wheelsBack.getChild("bone3");
		this.bone4 = this.wheelsBack.getChild("bone4");
		this.ramp = this.SiegeTower.getChild("ramp");
		this.ladders = this.SiegeTower.getChild("ladders");
		this.ladder2 = this.ladders.getChild("ladder2");
		this.ladder = this.ladders.getChild("ladder");
		this.body = this.SiegeTower.getChild("body");
	}



	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition SiegeTower = partdefinition.addOrReplaceChild("SiegeTower", CubeListBuilder.create(), PartPose.offset(0.0F, 23.65F, 4.0F));

		PartDefinition wheelsFront = SiegeTower.addOrReplaceChild("wheelsFront", CubeListBuilder.create().texOffs(28, 214).addBox(0.0F, -1.0F, -1.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 214).addBox(-30.0F, -1.0F, -1.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -20.0F));

		PartDefinition bone = wheelsFront.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(30.0F, -0.15F, 32.35F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone2 = wheelsFront.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(-22.0F, -0.15F, 32.35F));

		PartDefinition cube_r4 = bone2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r5 = bone2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r6 = bone2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition wheelsBack = SiegeTower.addOrReplaceChild("wheelsBack", CubeListBuilder.create().texOffs(28, 214).addBox(0.0F, -1.0F, -1.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 214).addBox(-30.0F, -1.0F, -1.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 20.0F));

		PartDefinition bone3 = wheelsBack.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(30.0F, -0.15F, 32.35F));

		PartDefinition cube_r7 = bone3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r8 = bone3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r9 = bone3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone4 = wheelsBack.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 218).addBox(-6.0F, -3.375F, -40.85F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.001F)), PartPose.offset(-22.0F, -0.15F, 32.35F));

		PartDefinition cube_r10 = bone4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.25F, -32.675F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r11 = bone4.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.375F, -32.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r12 = bone4.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 218).addBox(-2.5F, -3.5F, -9.0F, 4.0F, 7.0F, 17.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.5F, -0.225F, -32.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition ramp = SiegeTower.addOrReplaceChild("ramp", CubeListBuilder.create(), PartPose.offset(-23.0F, -97.0F, -22.0F));

		PartDefinition cube_r13 = ramp.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(36, 138).addBox(-1.0F, -2.0F, -44.0F, 4.0F, 4.0F, 40.0F, new CubeDeformation(0.0F))
				.texOffs(36, 138).addBox(-1.0F, -45.0F, -44.0F, 4.0F, 4.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(47.0F, -3.0F, -1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r14 = ramp.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(21, 123).addBox(-3.0F, -4.0F, -48.0F, 4.0F, 4.0F, 55.0F, new CubeDeformation(0.0F))
				.texOffs(21, 123).addBox(-47.0F, -4.0F, -48.0F, 4.0F, 4.0F, 55.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(46.0F, -7.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r15 = ramp.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(9, 182).addBox(-21.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.0F, -43.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r16 = ramp.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -43.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r17 = ramp.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -27.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r18 = ramp.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(9, 182).addBox(-21.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.0F, -27.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r19 = ramp.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(14, 188).addBox(-22.0F, -2.0F, 5.0F, 20.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.0F, -17.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r20 = ramp.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(16, 188).addBox(-20.0F, -2.0F, 5.0F, 20.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -17.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r21 = ramp.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(14, 188).addBox(-22.0F, -2.0F, 5.0F, 20.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.0F, -17.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r22 = ramp.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(9, 182).addBox(-21.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.0F, -27.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r23 = ramp.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(9, 182).addBox(-21.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.0F, -43.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r24 = ramp.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -43.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r25 = ramp.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(16, 188).addBox(-20.0F, -2.0F, 5.0F, 20.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -17.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r26 = ramp.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -27.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition ladders = SiegeTower.addOrReplaceChild("ladders", CubeListBuilder.create(), PartPose.offset(-15.0F, -11.0F, 16.0F));

		PartDefinition ladder2 = ladders.addOrReplaceChild("ladder2", CubeListBuilder.create().texOffs(11, 193).addBox(-30.0F, -2.0F, -1.0F, 30.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-30.0F, -2.0F, -14.0F, 30.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-74.0F, -2.0F, -14.0F, 30.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-74.0F, -2.0F, -1.0F, 30.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-118.0F, -2.0F, -14.0F, 30.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-118.0F, -2.0F, -1.0F, 30.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition cube_r27 = ladder2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(16, 195).addBox(3.0F, -2.0F, 100.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 56.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 12.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -1.0F, -19.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r28 = ladder2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(16, 195).addBox(3.0F, -2.0F, 100.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 56.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 12.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, -19.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r29 = ladder2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(16, 195).addBox(3.0F, -2.0F, 100.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 56.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 12.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -1.0F, -19.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition ladder = ladders.addOrReplaceChild("ladder", CubeListBuilder.create().texOffs(27, 193).addBox(-14.0F, -2.0F, -1.0F, 14.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(27, 193).addBox(-14.0F, -2.0F, -14.0F, 14.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(27, 193).addBox(-58.0F, -2.0F, -14.0F, 14.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(27, 193).addBox(-58.0F, -2.0F, -1.0F, 14.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(27, 193).addBox(-102.0F, -2.0F, -1.0F, 14.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(27, 193).addBox(-102.0F, -2.0F, -14.0F, 14.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition cube_r30 = ladder.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(16, 195).addBox(3.0F, -2.0F, 100.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 56.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 195).addBox(3.0F, -2.0F, 12.0F, 22.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -1.0F, -19.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition body = SiegeTower.addOrReplaceChild("body", CubeListBuilder.create().texOffs(7, 189).addBox(-9.0F, -2.0F, -1.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(-28, -14).addBox(-5.0F, 54.0F, 0.0F, 16.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(184, 126).addBox(-33.0F, 130.0F, -9.0F, 4.0F, 4.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(184, 126).addBox(11.0F, 130.0F, -9.0F, 4.0F, 4.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(0, 182).addBox(-30.0F, 128.0F, 1.0F, 21.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 182).addBox(-30.0F, 128.0F, 17.0F, 21.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 182).addBox(-9.0F, 128.0F, 1.0F, 21.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(8, 189).addBox(-9.0F, 128.0F, -8.0F, 20.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-29.0F, 128.0F, -8.0F, 20.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-29.0F, 128.0F, 33.0F, 20.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(8, 189).addBox(-9.0F, 128.0F, 33.0F, 20.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 182).addBox(-9.0F, 128.0F, 17.0F, 21.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, 87.0F, -1.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, 87.0F, -1.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, 87.0F, 9.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, 87.0F, 9.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, 87.0F, 19.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, 87.0F, 19.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-9.0F, 87.0F, 29.0F, 21.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-30.0F, 87.0F, 29.0F, 21.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-30.0F, 42.0F, 29.0F, 21.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-9.0F, 42.0F, 29.0F, 21.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, 42.0F, 19.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, 42.0F, 19.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, 42.0F, 9.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, 42.0F, 9.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, 42.0F, -1.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, 42.0F, -1.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-9.0F, -2.0F, 29.0F, 21.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(11, 193).addBox(-30.0F, -2.0F, 29.0F, 21.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, -2.0F, 19.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, -2.0F, 19.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-9.0F, -2.0F, 9.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, -2.0F, 9.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(7, 189).addBox(-30.0F, -2.0F, -1.0F, 21.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -140.0F, -17.0F));

		PartDefinition cube_r31 = body.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 3.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r32 = body.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 3.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r33 = body.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 3.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r34 = body.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 3.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r35 = body.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(4, 178).addBox(-22.0F, -2.0F, -5.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, -13.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r36 = body.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(6, 178).addBox(-20.0F, -2.0F, -5.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -13.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r37 = body.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(17, 189).addBox(-20.0F, -2.0F, 6.0F, 20.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 35.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r38 = body.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(15, 189).addBox(-22.0F, -2.0F, 6.0F, 20.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 35.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r39 = body.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(8, 182).addBox(-22.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 51.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r40 = body.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 51.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r41 = body.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(31, 133).addBox(-3.0F, -4.0F, -45.0F, 4.0F, 4.0F, 45.0F, new CubeDeformation(0.0F))
				.texOffs(31, 133).addBox(49.0F, -4.0F, -45.0F, 4.0F, 4.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-34.0F, 117.0F, -5.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r42 = body.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(28, 130).addBox(-3.0F, -4.0F, -49.0F, 4.0F, 4.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-34.0F, 117.0F, -8.0F, 0.0F, 1.5708F, 3.1416F));

		PartDefinition cube_r43 = body.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(28, 130).addBox(-3.0F, -4.0F, -49.0F, 4.0F, 4.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-34.0F, 86.0F, -8.0F, 0.0F, 1.5708F, 3.1416F));

		PartDefinition cube_r44 = body.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(28, 130).addBox(-3.0F, -4.0F, -49.0F, 4.0F, 4.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-34.0F, 47.0F, -8.0F, 0.0F, 1.5708F, 3.1416F));

		PartDefinition cube_r45 = body.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(31, 133).addBox(-3.0F, -4.0F, -45.0F, 4.0F, 4.0F, 45.0F, new CubeDeformation(0.0F))
				.texOffs(31, 133).addBox(49.0F, -4.0F, -45.0F, 4.0F, 4.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-34.0F, 86.0F, -5.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r46 = body.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(27, 129).addBox(-3.0F, -4.0F, -45.0F, 4.0F, 4.0F, 49.0F, new CubeDeformation(0.0F))
				.texOffs(27, 129).addBox(-55.0F, -4.0F, -45.0F, 4.0F, 4.0F, 49.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, 43.0F, -5.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r47 = body.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(0, 102).addBox(-3.0F, -4.0F, -45.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
				.texOffs(0, 102).addBox(41.0F, -4.0F, -45.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 23.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r48 = body.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 51.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r49 = body.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 35.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r50 = body.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 19.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r51 = body.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(6, 178).addBox(-20.0F, -2.0F, -5.0F, 18.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, -13.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r52 = body.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(8, 178).addBox(-18.0F, -2.0F, -5.0F, 18.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, -13.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r53 = body.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 19.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r54 = body.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 35.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r55 = body.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 51.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r56 = body.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(0, 102).addBox(-3.0F, -4.0F, -45.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
				.texOffs(0, 102).addBox(41.0F, -4.0F, -45.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 23.0F, 35.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r57 = body.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(8, 178).addBox(-18.0F, -2.0F, -5.0F, 18.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -13.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r58 = body.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(6, 178).addBox(-20.0F, -2.0F, -5.0F, 18.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -13.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r59 = body.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 19.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r60 = body.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 19.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r61 = body.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 35.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r62 = body.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 35.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r63 = body.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 51.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r64 = body.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 51.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r65 = body.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 67.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r66 = body.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 67.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r67 = body.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 83.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r68 = body.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 83.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r69 = body.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 99.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r70 = body.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 99.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r71 = body.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 115.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r72 = body.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 67.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r73 = body.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(8, 182).addBox(-22.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 67.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r74 = body.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 83.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r75 = body.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(8, 182).addBox(-22.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 83.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r76 = body.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 99.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r77 = body.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(9, 182).addBox(-21.0F, -2.0F, -1.0F, 19.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 99.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r78 = body.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 115.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r79 = body.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(8, 182).addBox(-22.0F, -2.0F, -1.0F, 20.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, 115.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r80 = body.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 115.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r81 = body.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 83.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r82 = body.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 67.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r83 = body.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 83.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r84 = body.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 67.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r85 = body.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 99.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r86 = body.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(13, 182).addBox(-17.0F, -2.0F, -1.0F, 17.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 99.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r87 = body.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(10, 182).addBox(-20.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 115.0F, 19.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r88 = body.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(12, 182).addBox(-18.0F, -2.0F, -1.0F, 18.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.0F, 115.0F, 35.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r89 = body.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(0, 102).addBox(-3.0F, -4.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
				.texOffs(0, 102).addBox(-47.0F, -4.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 92.0F, 35.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r90 = body.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(0, 102).addBox(-3.0F, -4.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F))
				.texOffs(0, 102).addBox(-47.0F, -4.0F, -38.0F, 4.0F, 4.0F, 76.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 92.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(SiegeTowerEntity entity, float partialTicks, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		wheelsFront.xRot = entity.getWheelRotation(partialTicks);
		wheelsBack.xRot  = wheelsFront.xRot;
		ramp.xRot        = -entity.getRampAngle(partialTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		SiegeTower.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}