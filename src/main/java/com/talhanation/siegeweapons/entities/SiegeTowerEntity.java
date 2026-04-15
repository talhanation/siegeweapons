package com.talhanation.siegeweapons.entities;

import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.ModTexts;
import com.talhanation.siegeweapons.math.Kalkuel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SiegeTowerEntity extends AbstractInventoryVehicleEntity implements IShootingWeapon {

    private static final EntityDataAccessor<Float>   RAMP_ANGLE     = SynchedEntityData.defineId(SiegeTowerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> RAMP_DEPLOYED  = SynchedEntityData.defineId(SiegeTowerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TRIGGERING     = SynchedEntityData.defineId(SiegeTowerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IDEAL_POSITION = SynchedEntityData.defineId(SiegeTowerEntity.class, EntityDataSerializers.BOOLEAN);

    /** Maximaler Rampenwinkel: -80° in Radiant. */
    private static final float RAMP_MAX_ANGLE = -(float) Math.toRadians(80);
    /** Ausfahrrate: 0 → RAMP_MAX_ANGLE in 7s = 140 Ticks. */
    private static final float RAMP_RATE      = -RAMP_MAX_ANGLE / (7 * 20);

    /** Höhe der Ebenen über dem Boden in Blöcken. Anpassbar. */
    private static final float FLOOR_1_HEIGHT = 0.65F;
    private static final float FLOOR_2_HEIGHT = FLOOR_1_HEIGHT + 3.00F;
    private static final float FLOOR_3_HEIGHT = FLOOR_2_HEIGHT + 3.00F;
    private static final float FLOOR_4_HEIGHT = FLOOR_3_HEIGHT + 3.00F;

    /** Lokale Offsets der 5 Plätze pro Ebene (Mitte, VL, VR, HL, HR). */
    private static final double[] SLOT_FWD  = {0.0,  0.75,  0.15, -0.75, -0.75};
    private static final double[] SLOT_SIDE = {0.0, -0.75,  0.75, -0.75,  0.75};

    /** Suchdistanz nach der Mauer vorwärts in Blöcken. */
    private static final int WALL_SEARCH_DIST = 6;
    /** Minimale / maximale Mauererhöhung in Blöcken. */
    private static final int WALL_MIN_HEIGHT  = 3;
    private static final int WALL_MAX_HEIGHT  = 15;

    private boolean showTrajectory;
    private float   prevRampAngle = 0F;
    private int     dismountTimer  = 0;

    public SiegeTowerEntity(EntityType<? extends SiegeTowerEntity> entityType, Level world) {
        super(entityType, world);
    }

    // ─── DATA ─────────────────────────────────────────────────────────────────

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RAMP_ANGLE,     0F);
        this.entityData.define(RAMP_DEPLOYED,  false);
        this.entityData.define(TRIGGERING,     false);
        this.entityData.define(IDEAL_POSITION, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("rampAngle",     getRampAngle());
        tag.putBoolean("rampDeployed", isRampDeployed());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setRampAngle(tag.getFloat("rampAngle"));
        setRampDeployed(tag.getBoolean("rampDeployed"));
    }

    // ─── TICK ─────────────────────────────────────────────────────────────────

    @Override
    public void tick() {
        super.tick();

        prevRampAngle = getRampAngle();

        if (getCommandSenderWorld().isClientSide()) return;

        tickRamp();
        tickIdealPosition();
        tickDismount();

        if (getControllingPassenger() == null && isTriggering()) {
            setTriggering(false);
        }
    }

    private void tickRamp() {
        float angle = getRampAngle();
        if (isTriggering()) {
            if (angle > RAMP_MAX_ANGLE) {
                float newAngle = Math.max(angle - RAMP_RATE, RAMP_MAX_ANGLE);
                setRampAngle(newAngle);
                if (newAngle <= RAMP_MAX_ANGLE) setRampDeployed(true);
            }
        } else {
            if (angle < 0F) {
                float newAngle = Math.min(angle + RAMP_RATE, 0F);
                setRampAngle(newAngle);
                if (newAngle >= 0F) setRampDeployed(false);
            } else {
                setRampDeployed(false);
            }
        }
    }

    private void tickIdealPosition() {
        setIdealPosition(findWallTarget() != null);
    }

    private void tickDismount() {
        if (!isRampDeployed() || !isIdealPosition()) {
            dismountTimer = 0;
            return;
        }
        dismountTimer++;
        if (dismountTimer >= 20) {
            dismountTimer = 0;
            dismountLastPassenger();
        }
    }

    private void dismountLastPassenger() {
        List<Entity> passengers = this.getPassengers();
        for (int i = passengers.size() - 1; i >= 1; i--) {
            Entity passenger = passengers.get(i);
            BlockPos target = findWallTarget();
            if (target != null) {
                passenger.stopRiding();
                passenger.teleportTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
                break;
            }
        }
    }

    /**
     * Sucht den Auftreffpunkt auf einer Mauer 6–15 Blöcke hoch direkt vor dem Turm.
     * Gibt die begehbare Oberfläche zurück, oder null wenn keine Mauer passt.
     */
    private BlockPos findWallTarget() {
        Level level = getCommandSenderWorld();
        Vec3 forward = this.getForward().scale(2);
        for (int dist = 1; dist <= WALL_SEARCH_DIST; dist++) {
            double cx = this.getX() + forward.x * dist;
            double cz = this.getZ() + forward.z * dist;
            for (int h = WALL_MIN_HEIGHT; h <= WALL_MAX_HEIGHT; h++) {
                BlockPos pos      = BlockPos.containing(cx, this.getY() + h, cz);
                BlockPos above    = pos.above();
                BlockState state  = level.getBlockState(pos);
                BlockState stateA = level.getBlockState(above);
                if (!state.isAir() && state.isSolidRender(level, pos) && stateA.isAir()) {
                    return above;
                }
            }
        }
        return null;
    }

    // ─── CONTROL ──────────────────────────────────────────────────────────────

    @Override
    public void control(Entity driver, float xRot, float yRot) {
        float speed = Kalkuel.subtractToZero(getSpeed(), getRollResistance());
        if (driver != null) {
            float maxSpeed = getMaxSpeedInKmH() / (60F * 1.15F);
            if (isForward())  speed = Math.min(speed + 0.01F, maxSpeed);
            if (isBackward()) speed = Math.max(speed - 0.008F, -maxSpeed * 0.5F);
            deltaRotation = 0;
            if (isLeft())  --deltaRotation;
            if (isRight()) ++deltaRotation;
            this.setXRot(xRot);
            this.setYRot(yRot + this.deltaRotation * 0.38F);
        } else {
            setForward(false);
            setBackward(false);
            setLeft(false);
            setRight(false);
        }
        this.setSpeed(speed);
        setDeltaMovement(
                Kalkuel.calculateMotionX(getSpeed(), getYRot()),
                getDeltaMovement().y,
                Kalkuel.calculateMotionZ(getSpeed(), getYRot())
        );
    }

    private float getRollResistance() {
        return 0.005F;
    }

    // ─── IShootingWeapon ──────────────────────────────────────────────────────

    @Override public void shootWeapon()      {}
    @Override public void playShootSound()   {}
    @Override public void playLoadingSound() {}

    @Override
    public void updateTrigger(boolean trigger, LivingEntity livingEntity) {
        boolean needsUpdate = false;
        if (this.isTriggering() != trigger) {
            this.setTriggering(trigger);
            needsUpdate = true;
        }
        if (getCommandSenderWorld().isClientSide && needsUpdate && livingEntity instanceof Player) {
            Main.SIMPLE_CHANNEL.sendToServer(
                    new com.talhanation.siegeweapons.network.MessageLoadAndShootWeapon(trigger, livingEntity.getUUID()));
        }
    }

    @Override public void    setShowTrajectory(boolean v) { this.showTrajectory = v; }
    @Override public boolean getShowTrajectory()          { return showTrajectory; }

    // ─── openGUI ──────────────────────────────────────────────────────────────

    @Override
    public void openGUI(Player player) {
        if (player instanceof ServerPlayer sp) {
            NetworkHooks.openScreen(sp, new SimpleMenuProvider(
                    (id, inv, p) -> new com.talhanation.siegeweapons.inventory.VehicleInventoryMenu(id, this, inv),
                    this.getDisplayName()
            ), buf -> buf.writeUUID(this.getUUID()));
        } else {
            Main.SIMPLE_CHANNEL.sendToServer(
                    new com.talhanation.siegeweapons.network.MessageOpenGUI(this));
        }
    }

    // ─── GETTER / SETTER ──────────────────────────────────────────────────────

    public float getRampAngle()                { return entityData.get(RAMP_ANGLE); }
    public void  setRampAngle(float a)         { entityData.set(RAMP_ANGLE, a); }

    /** Smooth interpolierte Variante für die Client-Animation. */
    public float getRampAngle(float partialTicks) {
        return Mth.lerp(partialTicks, prevRampAngle, getRampAngle());
    }

    public boolean isRampDeployed()            { return entityData.get(RAMP_DEPLOYED); }
    public void    setRampDeployed(boolean b)  { entityData.set(RAMP_DEPLOYED, b); }

    public boolean isTriggering()              { return entityData.get(TRIGGERING); }
    public void    setTriggering(boolean t)    { entityData.set(TRIGGERING, t); }

    public boolean isIdealPosition()           { return entityData.get(IDEAL_POSITION); }
    public void    setIdealPosition(boolean b) { entityData.set(IDEAL_POSITION, b); }

    // ─── PASSENGERS ───────────────────────────────────────────────────────────

    @Override
    protected void positionRider(@NotNull Entity rider, @NotNull MoveFunction fn) {
        if (!this.hasPassenger(rider)) return;
        List<Entity> passengers = this.getPassengers();
        int idx = passengers.indexOf(rider);
        if (idx == 0) {
            Vec3 pos = getDriverPosition();
            fn.accept(rider, this.getX() + pos.x, this.getY() + getPassengersRidingOffset(), this.getZ() + pos.z);
            return;
        }
        int floorIdx = (idx - 1) / 5;
        int slotIdx  = (idx - 1) % 5;
        float[] floors = {FLOOR_1_HEIGHT, FLOOR_2_HEIGHT, FLOOR_3_HEIGHT, FLOOR_4_HEIGHT};
        float floorY = floors[Math.min(floorIdx, floors.length - 1)];
        Vec3 offset  = computeFloorOffset(slotIdx);
        fn.accept(rider, this.getX() + offset.x, this.getY() + floorY, this.getZ() + offset.z);
    }

    /**
     * Berechnet den weltkoordinierten XZ-Offset eines Passagierplatzes relativ
     * zur Entity-Position. slotIdx: 0=Mitte, 1=VL, 2=VR, 3=HL, 4=HR.
     */
    private Vec3 computeFloorOffset(int slotIdx) {
        Vec3 forward = this.getForward();
        Vec3 right   = new Vec3(forward.z, 0, -forward.x);
        return forward.scale(SLOT_FWD[slotIdx]).add(right.scale(SLOT_SIDE[slotIdx]));
    }

    /**
     * Fügt eine Entity auf den ersten freien Nicht-Fahrer-Platz ein.
     * Wird z.B. von Recruits-KI aufgerufen.
     */
    public boolean boardPassenger(Entity entity) {
        if (this.getPassengers().isEmpty()) return false;
        if (this.getPassengers().size() >= getMaxPassengerSize()) return false;
        return entity.startRiding(this);
    }

    @Override
    protected boolean tryRiding(Entity entity) {
        if (this.getCommandSenderWorld().isClientSide()) return false;
        if (this.getPassengers().isEmpty() && !isAllowedDriver(entity)) return false;
        if (entity.startRiding(this)) {
            entity.setYRot(this.getYRot());
            entity.setXRot(this.getXRot());
            return true;
        }
        return false;
    }

    private static boolean isAllowedDriver(Entity entity) {
        if (entity instanceof Player) return true;
        ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return type != null && type.equals(new ResourceLocation("recruits", "siege_engineer"));
    }

    @Override
    public Vec3 getDriverPosition() {
        double f = -2.0F;
        double d = -0.75F;
        return new Vec3(f, 0D, d).yRot(-this.getYRot() * (float) (Math.PI / 180F) - (float) (Math.PI / 2F));
    }

    @Override
    public double getPassengersRidingOffset() { return 0.0D; }

    // ─── ABSTRACT IMPL ────────────────────────────────────────────────────────

    @Override public int       getMaxPassengerSize() { return 21; }
    @Override public int       getMaxSpeedInKmH()    { return 6; }
    @Override public double    getMaxHealth()        { return 2000D; }
    @Override public Component getVehicleTypeName()  { return ModTexts.SIEGE_TOWER; }
}
