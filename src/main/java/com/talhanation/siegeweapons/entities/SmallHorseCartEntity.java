package com.talhanation.siegeweapons.entities;

import com.talhanation.siegeweapons.ModTexts;
import com.talhanation.siegeweapons.math.Kalkuel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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

    // FIX 1: 1 Block weiter weg (war 2.1F)
    private static final float HORSE_DISTANCE = 3.1F;
    private static final int INVENTORY_SIZE = 54;
    private static final int INVENTORY_COLUMNS = 9;

    public SmallHorseCartEntity(EntityType<? extends AbstractInventoryVehicleEntity> entityType, Level world) {
        super(entityType, world);
    }

    ///////////////////////////////////TICK/////////////////////////////////////////

    @Override
    public void tick() {
        super.tick(); // ruft control(), move(), etc. auf

        AbstractHorse horse = getAttachedHorse();
        if (horse == null) return;

        // NoAI aufrechterhalten (überlebt Chunk-Reload durch NBT, aber zur Sicherheit)
        if (!horse.isNoAi()) horse.setNoAi(true);

        // FIX 1: Pferd-Animation antreiben — setDeltaMovement + setOnGround
        // damit LivingEntity.updateWalkAnimation() die Laufanimation korrekt berechnet
        float cartSpeed = Math.abs(getSpeed());
        horse.setOnGround(this.onGround());

        if (cartSpeed > 0.005F) {
            double mx = Kalkuel.calculateMotionX(cartSpeed, this.getYRot());
            double mz = Kalkuel.calculateMotionZ(cartSpeed, this.getYRot());
            horse.setDeltaMovement(mx, 0D, mz);
        } else {
            horse.setDeltaMovement(Vec3.ZERO);
        }

        // FIX 1: Hufschlag-Sound abhängig von Geschwindigkeit
        if (!getCommandSenderWorld().isClientSide() && cartSpeed > 0.005F && this.onGround()) {
            // Interval: bei Maxspeed ~4 Ticks, bei Minspeed ~9 Ticks
            int interval = Math.max(4, (int) (9 - cartSpeed * 25));
            if (tickCount % interval == 0) {
                getCommandSenderWorld().playSound(
                        null,
                        horse.getX(), horse.getY(), horse.getZ(),
                        SoundEvents.HORSE_GALLOP,
                        SoundSource.NEUTRAL,
                        0.5F,
                        0.8F + random.nextFloat() * 0.4F
                );
            }
        }
    }

    ////////////////////////////////////CONTROL////////////////////////////////////

    @Override
    public void control(Entity driver, float xRot, float yRot) {
        if (driver == null || !hasAttachedHorse()) {
            setSpeed(Kalkuel.subtractToZero(getSpeed(), 0.002F));
            setForward(false);
            setBackward(false);
            setDeltaMovement(
                    Kalkuel.calculateMotionX(getSpeed(), this.getYRot()),
                    getDeltaMovement().y,
                    Kalkuel.calculateMotionZ(getSpeed(), this.getYRot())
            );
            return;
        }

        // Geschwindigkeit: nur bei Tastendruck ändern, kein automatisches Abbremsen
        float maxSpeed = getMaxSpeedInKmH() / (60F * 1.15F);
        float speed = getSpeed();

        if (isForward()) speed = Math.min(speed + 0.008F, maxSpeed);
        if (isBackward()) speed = Math.max(speed - 0.006F, 0F);

        setSpeed(speed);

        AbstractHorse horse = getAttachedHorse();
        if (horse != null) {
            // FIX 2: Das Pferd dreht sich GRADUELL in Richtung des Spielers —
            // das Cart folgt dem Pferd, NICHT direkt dem Spieler.
            // Nur wenn das Cart sich bewegt kann gelenkt werden (kein Spinnen im Stand).
            float horseYaw = horse.getYRot();
            float playerYaw = driver.getYRot();
            float angleDelta = Mth.wrapDegrees(playerYaw - horseYaw);

            // Max. Lenkwinkel pro Tick: bei Fahrt 2.5°/Tick = ~50°/sek, im Stand 0
            float maxTurnRate = speed > 0.003F ? 2.5F : 0F;
            angleDelta = Mth.clamp(angleDelta, -maxTurnRate, maxTurnRate);

            float newHorseYaw = horseYaw + angleDelta;
            horse.setYRot(newHorseYaw);
            horse.yHeadRot = newHorseYaw;
            horse.yBodyRot = newHorseYaw;
            horse.setXRot(0F);

            // Cart-Rotation folgt dem Pferd (nie direkt dem Spieler)
            this.setYRot(newHorseYaw);
            this.setXRot(0F);
        }

        setDeltaMovement(
                Kalkuel.calculateMotionX(getSpeed(), this.getYRot()),
                getDeltaMovement().y,
                Kalkuel.calculateMotionZ(getSpeed(), this.getYRot())
        );
    }

    ////////////////////////////////////PASSENGERS////////////////////////////////////

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
        if (entity instanceof Player) return getPassengers().stream().noneMatch(e -> e instanceof Player);
        return false;
    }

    // FIX 3: Spieler sitzt (Sitting-Animation aktiv)
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
            // Pferd wird vor dem Cart platziert (zieht das Cart)
            double frontX = Kalkuel.calculateMotionX(HORSE_DISTANCE, this.getYRot());
            double frontZ = Kalkuel.calculateMotionZ(HORSE_DISTANCE, this.getYRot());
            moveFunction.accept(rider, this.getX() + frontX, this.getY(), this.getZ() + frontZ);

        } else {
            // FIX 3: Spieler sitzt 2 Blöcke vorne (Richtung Pferd / Kutschbock)
            double riderX = Kalkuel.calculateMotionX(1.8F, this.getYRot());
            double riderZ = Kalkuel.calculateMotionZ(1.8F, this.getYRot());
            double riderY = this.getY() + this.getPassengersRidingOffset();
            moveFunction.accept(rider, this.getX() + riderX, riderY, this.getZ() + riderZ);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(@NotNull LivingEntity passenger) {
        double sideX = Kalkuel.calculateMotionX(1.0F, this.getYRot() + 90F);
        double sideZ = Kalkuel.calculateMotionZ(1.0F, this.getYRot() + 90F);
        return new Vec3(this.getX() + sideX, this.getY(), this.getZ() + sideZ);
    }

    ////////////////////////////////////INTERACTION////////////////////////////////////

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

    ////////////////////////////////////HORSE MANAGEMENT////////////////////////////////////

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

        // Pferd seitlich neben dem Cart absetzen
        double sideX = Kalkuel.calculateMotionX(-1.5F, this.getYRot() + 90F);
        double sideZ = Kalkuel.calculateMotionZ(-1.5F, this.getYRot() + 90F);
        horse.setPos(this.getX() + sideX, this.getY(), this.getZ() + sideZ);
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

    ////////////////////////////////////INVENTORY////////////////////////////////////

    @Override
    public int getInventorySize() { return INVENTORY_SIZE; }

    @Override
    public int getInventoryColumns() { return INVENTORY_COLUMNS; }

    @Override
    public void openGUI(Player player) {
        if (!getCommandSenderWorld().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(
                    serverPlayer,
                    new SimpleMenuProvider(
                            (id, inv, p) -> new com.talhanation.siegeweapons.inventory.SmallHorseCartMenu(id, this, inv),
                            this.getDisplayName()
                    ),
                    buf -> buf.writeUUID(this.getUUID())
            );
        }
    }

    ////////////////////////////////////DATA////////////////////////////////////

    @Override
    protected void defineSynchedData() { super.defineSynchedData(); }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) { super.addAdditionalSaveData(compoundTag); }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) { super.readAdditionalSaveData(compoundTag); }

    ////////////////////////////////////ABSTRACT IMPL////////////////////////////////////

    @Override
    public Vec3 getDriverPosition() {
        double riderX = Kalkuel.calculateMotionX(1.8F, this.getYRot());
        double riderZ = Kalkuel.calculateMotionZ(1.8F, this.getYRot());
        return new Vec3(riderX, 0.0D, riderZ);
    }

    @Override
    public int getMaxPassengerSize() { return 2; }

    @Override
    public int getMaxSpeedInKmH() { return 12; }

    @Override
    public double getMaxHealth() { return 150; }

    @Override
    public Component getVehicleTypeName() { return ModTexts.SMALL_HORSE_CART; }
}
