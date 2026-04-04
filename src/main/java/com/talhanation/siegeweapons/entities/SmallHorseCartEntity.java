package com.talhanation.siegeweapons.entities;

import com.talhanation.siegeweapons.ModTexts;
import com.talhanation.siegeweapons.math.Kalkuel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;

public class SmallHorseCartEntity extends AbstractInventoryVehicleEntity {

    // ── Konfiguration ─────────────────────────────────────────────────────────

    private static final float HORSE_DISTANCE = 3.1F;
    private static final int   INVENTORY_SIZE    = 54;
    private static final int   INVENTORY_COLUMNS = 9;

    /** Maximaler Lenkwinkel in Grad (= max. Kopf-Offset des Pferdes). */
    private static final float MAX_STEERING  = 30F;
    /** Grad pro Tick, die beim Drücken von Links/Rechts aufgebaut werden. */
    private static final float INPUT_RATE    = 4F;
    /** Grad pro Tick, die beim Loslassen wieder abgebaut werden (zurück auf 0). */
    private static final float RETURN_RATE   = 3F;
    /** Faktor: steeringAngle → Cart-Drehrate pro Tick.
     *  Bei MAX_STEERING (30°) → 30 * 0.1 = 3°/Tick. */
    private static final float STEER_TO_TURN = 0.1F;

    // ── Synced Data ───────────────────────────────────────────────────────────

    /**
     * Aktueller Lenkwinkel (–MAX_STEERING … +MAX_STEERING).
     * Negativ = links, Positiv = rechts.
     * Synced zu allen Clients damit das Pferde-Kopf-Rendering überall korrekt ist.
     */
    private static final EntityDataAccessor<Float> STEERING_ANGLE =
            SynchedEntityData.defineId(SmallHorseCartEntity.class, EntityDataSerializers.FLOAT);

    public SmallHorseCartEntity(EntityType<? extends AbstractInventoryVehicleEntity> entityType, Level world) {
        super(entityType, world);
    }

    // ─── DATA ─────────────────────────────────────────────────────────────────

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STEERING_ANGLE, 0F);
    }

    public float getSteeringAngle() {
        return entityData.get(STEERING_ANGLE);
    }

    public void setSteeringAngle(float angle) {
        entityData.set(STEERING_ANGLE, angle);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    // ─── TICK ─────────────────────────────────────────────────────────────────

    @Override
    public void tick() {
        super.tick();

        AbstractHorse horse = getAttachedHorse();
        if (horse == null) return;

        if (!horse.isNoAi()) horse.setNoAi(true);

        float cartSpeed = Math.abs(getSpeed());

        // deltaMovement am Pferd für Sound-Logik setzen
        if (cartSpeed > 0.005F) {
            horse.setDeltaMovement(
                    Kalkuel.calculateMotionX(cartSpeed, this.getYRot()),
                    0D,
                    Kalkuel.calculateMotionZ(cartSpeed, this.getYRot())
            );
        } else {
            horse.setDeltaMovement(Vec3.ZERO);
        }

        // Hufschlag-Sound
        if (!getCommandSenderWorld().isClientSide() && cartSpeed > 0.005F && this.onGround()) {
            int interval = Math.max(8, (int) (22 - cartSpeed * 80));
            if (tickCount % interval == 0) {
                getCommandSenderWorld().playSound(
                        null,
                        horse.getX(), horse.getY(), horse.getZ(),
                        SoundEvents.HORSE_GALLOP,
                        SoundSource.NEUTRAL,
                        0.4F,
                        0.85F + random.nextFloat() * 0.3F
                );
            }
        }
    }

    // ─── CONTROL ──────────────────────────────────────────────────────────────

    /**
     * Lenkung mit Aufbau/Abbau-Mechanik:
     *
     *  • Links-Taste  → steeringAngle sinkt (richtung –MAX_STEERING)
     *  • Rechts-Taste → steeringAngle steigt (richtung +MAX_STEERING)
     *  • Keine Taste  → steeringAngle kehrt graduell zu 0 zurück
     *
     *  Die Cart-Drehrate pro Tick = steeringAngle * STEER_TO_TURN
     *  Der Pferde-Kopf zeigt in Richtung steeringAngle (sichtbarer Lenkindikator).
     */
    @Override
    public void control(Entity driver, float xRot, float yRot) {
        if (driver == null || !hasAttachedHorse()) {
            setSpeed(Kalkuel.subtractToZero(getSpeed(), 0.002F));
            setSteeringAngle(Kalkuel.subtractToZero(getSteeringAngle(), RETURN_RATE));
            setForward(false);
            setBackward(false);
            setDeltaMovement(
                    Kalkuel.calculateMotionX(getSpeed(), this.getYRot()),
                    getDeltaMovement().y,
                    Kalkuel.calculateMotionZ(getSpeed(), this.getYRot())
            );
            return;
        }

        // ── Geschwindigkeit (persistent, kein Roll-Widerstand beim Fahren) ───
        float maxSpeed = getMaxSpeedInKmH() / (60F * 1.15F);
        float speed = getSpeed();

        if (isForward())  speed = Math.min(speed + 0.008F, maxSpeed);
        if (isBackward()) speed = Math.max(speed - 0.006F, 0F);

        setSpeed(speed);

        // ── Lenkwinkel aufbauen / abbauen ─────────────────────────────────────
        float steering = getSteeringAngle();

        if (isLeft()) {
            steering = Math.max(steering - INPUT_RATE, -MAX_STEERING);
        } else if (isRight()) {
            steering = Math.min(steering + INPUT_RATE, MAX_STEERING);
        } else {
            // Lenkung kehrt zu Geradeaus zurück
            if (steering > 0f)      steering = Math.max(0f, steering - RETURN_RATE);
            else if (steering < 0f) steering = Math.min(0f, steering + RETURN_RATE);
        }

        setSteeringAngle(steering);

        // ── Cart-Rotation proportional zum Lenkwinkel ─────────────────────────
        float newYRot = this.getYRot() + steering * STEER_TO_TURN;
        this.setYRot(newYRot);
        this.setXRot(0F);

        // ── Pferd: Körper folgt Cart, Kopf zeigt Lenkrichtung ─────────────────
        // yHeadRot wird auch hier gesetzt (server-side sync für andere Spieler).
        // Zusätzlich setzt ClientRenderEvents.onRenderLivingPre die Werte
        // kurz vor dem Render-Call – das eliminiert Sync-Latenz vollständig.
        AbstractHorse horse = getAttachedHorse();
        if (horse != null) {
            horse.setYRot(newYRot);
            horse.yBodyRot = newYRot + steering/2;
            horse.yHeadRot = newYRot + steering;   // Kopf = Körper + Lenkwinkel
            horse.setXRot(0F);
            horse.setIsJumping(false);
            horse.setTemper(0);
            horse.setSpeed(speed);
        }

        setDeltaMovement(
                Kalkuel.calculateMotionX(speed, newYRot),
                getDeltaMovement().y,
                Kalkuel.calculateMotionZ(speed, newYRot)
        );
    }

    // ─── PASSENGERS ───────────────────────────────────────────────────────────

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : getPassengers()) {
            if (passenger instanceof Player player) return player;
        }
        return null;
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity entity) {
        if (entity instanceof AbstractHorse) return !hasAttachedHorse();
        if (entity instanceof Player)        return getPassengers().stream().noneMatch(e -> e instanceof Player);
        return false;
    }

    @Override
    public boolean shouldRiderSit() {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.8D;
    }

    @Override
    protected void positionRider(@NotNull Entity rider, @NotNull MoveFunction moveFunction) {
        if (!this.hasPassenger(rider)) return;

        if (rider instanceof AbstractHorse) {
            double hx = Kalkuel.calculateMotionX(HORSE_DISTANCE, this.getYRot());
            double hz = Kalkuel.calculateMotionZ(HORSE_DISTANCE, this.getYRot());
            moveFunction.accept(rider, this.getX() + hx, this.getY(), this.getZ() + hz);
        } else {
            double rx = Kalkuel.calculateMotionX(1.8F, this.getYRot());
            double rz = Kalkuel.calculateMotionZ(1.8F, this.getYRot());
            double ry = this.getY() + this.getPassengersRidingOffset();
            moveFunction.accept(rider, this.getX() + rx, ry, this.getZ() + rz);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(@NotNull LivingEntity passenger) {
        double sx = Kalkuel.calculateMotionX(1.5F, this.getYRot() + 90F);
        double sz = Kalkuel.calculateMotionZ(1.5F, this.getYRot() + 90F);
        return new Vec3(this.getX() + sx, this.getY(), this.getZ() + sz);
    }

    // ─── INTERACTION ──────────────────────────────────────────────────────────

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        if (this.itemInteraction(player, interactionHand)) return InteractionResult.SUCCESS;

        if (!this.getCommandSenderWorld().isClientSide()) {
            if (player.isSecondaryUseActive()) {
                if (hasAttachedHorse()) {
                    detachHorse();
                    player.displayClientMessage(
                            Component.translatable("entity.siegeweapons.small_horse_cart.horse_detached"), true);
                } else {
                    this.openGUI(player);
                }
                return InteractionResult.SUCCESS;
            }

            if (!hasAttachedHorse()) {
                AbstractHorse nearestHorse = findNearestHorse(3.5D);
                if (nearestHorse != null) {
                    attachHorse(nearestHorse);
                    player.displayClientMessage(
                            Component.translatable("entity.siegeweapons.small_horse_cart.horse_attached"), true);
                    return InteractionResult.SUCCESS;
                }
            }

            if (getPassengers().stream().noneMatch(e -> e instanceof Player)) {
                return this.tryRiding(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        }

        return InteractionResult.PASS;
    }

    // ─── HORSE MANAGEMENT ─────────────────────────────────────────────────────

    @Nullable
    public AbstractHorse getAttachedHorse() {
        for (Entity passenger : getPassengers()) {
            if (passenger instanceof AbstractHorse horse) return horse;
        }
        return null;
    }

    public boolean hasAttachedHorse() {
        return getAttachedHorse() != null;
    }

    private void attachHorse(AbstractHorse horse) {
        horse.setNoAi(true);
        horse.startRiding(this);
    }

    public void detachHorse() {
        AbstractHorse horse = getAttachedHorse();
        if (horse == null) return;

        horse.stopRiding();
        horse.setNoAi(false);
        horse.setDeltaMovement(Vec3.ZERO);
        setSteeringAngle(0F);

        double sx = Kalkuel.calculateMotionX(-1.5F, this.getYRot() + 90F);
        double sz = Kalkuel.calculateMotionZ(-1.5F, this.getYRot() + 90F);
        horse.setPos(this.getX() + sx, this.getY(), this.getZ() + sz);
        horse.setYRot(this.getYRot());
    }

    @Nullable
    private AbstractHorse findNearestHorse(double radius) {
        return this.getCommandSenderWorld()
                .getEntitiesOfClass(AbstractHorse.class,
                        this.getBoundingBox().inflate(radius),
                        h -> h.isAlive() && !h.isVehicle())
                .stream()
                .min(Comparator.comparingDouble(h -> h.distanceToSqr(this)))
                .orElse(null);
    }

    // ─── INVENTORY ────────────────────────────────────────────────────────────

    @Override public int getInventorySize()    { return INVENTORY_SIZE; }
    @Override public int getInventoryColumns() { return INVENTORY_COLUMNS; }

    @Override
    public void openGUI(Player player) {
        if (!getCommandSenderWorld().isClientSide() && player instanceof ServerPlayer sp) {
            NetworkHooks.openScreen(
                    sp,
                    new SimpleMenuProvider(
                            (id, inv, p) -> new com.talhanation.siegeweapons.inventory.SmallHorseCartMenu(id, this, inv),
                            this.getDisplayName()
                    ),
                    buf -> buf.writeUUID(this.getUUID())
            );
        }
    }

    // ─── ABSTRACT IMPL ────────────────────────────────────────────────────────

    @Override
    public Vec3 getDriverPosition() {
        return new Vec3(
                Kalkuel.calculateMotionX(1.8F, this.getYRot()),
                0.0D,
                Kalkuel.calculateMotionZ(1.8F, this.getYRot())
        );
    }

    @Override public int      getMaxPassengerSize() { return 2; }
    @Override public int      getMaxSpeedInKmH()    { return 22; }
    @Override public double   getMaxHealth()        { return 250; }
    @Override public Component getVehicleTypeName() { return ModTexts.SMALL_HORSE_CART; }
}
