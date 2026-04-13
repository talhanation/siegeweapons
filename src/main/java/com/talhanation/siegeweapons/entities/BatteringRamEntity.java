package com.talhanation.siegeweapons.entities;

import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.ModTexts;
import com.talhanation.siegeweapons.init.ModSounds;
import com.talhanation.siegeweapons.math.Kalkuel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BatteringRamEntity extends AbstractInventoryVehicleEntity implements IShootingWeapon {

    // ── Synced Data ────────────────────────────────────────────────────────────

    private static final EntityDataAccessor<Float>   RAM_ANGLE  = SynchedEntityData.defineId(BatteringRamEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float>   CHARGE     = SynchedEntityData.defineId(BatteringRamEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> STATE      = SynchedEntityData.defineId(BatteringRamEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> TRIGGERING = SynchedEntityData.defineId(BatteringRamEntity.class, EntityDataSerializers.BOOLEAN);

    // ── Konstanten ────────────────────────────────────────────────────────────

    /** Winkel in Radiant wenn der Balken vollständig zurückgezogen ist. */
    private static final float CHARGE_ANGLE    = -0.65F;
    /** Winkel in Radiant beim Aufprall (vorwärts). */
    private static final float IMPACT_ANGLE    =  0.30F;
    /** Geschwindigkeit beim Aufladen (Balken zurückziehen), Radiant/Tick. */
    private static final float CHARGE_RATE     =  0.012F;
    /** Geschwindigkeit beim Schlag (Balken vorwärts), Radiant/Tick. */
    private static final float SWING_RATE      =  0.14F;
    /** Geschwindigkeit beim Zurückfedern nach dem Schlag, Radiant/Tick. */
    private static final float RETURN_RATE     =  0.04F;
    /** Maximale Ladezeit in Ticks (60 = 3 Sekunden für vollen Schlag). */
    private static final int   MAX_CHARGE_TIME = 60;
    /** Basis-Schaden (ohne Aufladung). */
    private static final float BASE_DAMAGE     = 15F;
    /** Bonus-Schaden bei voller Aufladung. */
    private static final float BONUS_DAMAGE    = 45F;
    /** Schlag-Reichweite vor dem Ram in Blöcken. */
    private static final float HIT_REACH       = 2.5F;
    /** Breite der Trefferfläche in Blöcken. */
    private static final float HIT_WIDTH       = 2.0F;

    private boolean showTrajectory; // unused bei Ram, aber Interface erfordert es
    private LivingEntity lastDriver;
    private int chargeTime = 0;
    private boolean impactDealt = false; // Schaden nur einmal pro Schlag austeilen
    private float prevRamAngle = 0F;     // für partialTick-Interpolation (client-seitig)

    public BatteringRamEntity(EntityType<? extends BatteringRamEntity> entityType, Level world) {
        super(entityType, world);
    }

    // ─── DATA ─────────────────────────────────────────────────────────────────

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RAM_ANGLE,  0F);
        this.entityData.define(CHARGE,     0F);
        this.entityData.define(STATE,      RamState.IDLE.getIndex());
        this.entityData.define(TRIGGERING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("ramAngle", getRamAngle());
        tag.putInt("ramState", getState().getIndex());
        tag.putFloat("ramCharge", getCharge());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setRamAngle(tag.getFloat("ramAngle"));
        setState(RamState.fromIndex(tag.getInt("ramState")));
        setCharge(tag.getFloat("ramCharge"));
    }

    // ─── TICK ─────────────────────────────────────────────────────────────────

    @Override
    public void tick() {
        super.tick();

        prevRamAngle = getRamAngle(); // vor dem return speichern – läuft auf Client & Server

        if (getCommandSenderWorld().isClientSide()) return; // Logik nur server-seitig

        LivingEntity driver = getControllingPassenger();
        if (driver != null) lastDriver = driver;

        RamState state = getState();
        float angle = getRamAngle();
        boolean triggering = isTriggering();

        switch (state) {
            case IDLE -> {
                // Warten auf Leertaste
                if (triggering) {
                    setState(RamState.CHARGING);
                    chargeTime = 0;
                    impactDealt = false;
                }
            }

            case CHARGING -> {
                // Leertaste gehalten: Balken wird zurückgezogen
                if (triggering) {
                    chargeTime = Math.min(chargeTime + 1, MAX_CHARGE_TIME);
                    float newAngle = Math.max(angle - CHARGE_RATE, CHARGE_ANGLE);
                    setRamAngle(newAngle);
                    setCharge((float) chargeTime / MAX_CHARGE_TIME);

                    // Krach-Geräusch während Aufladen (Balken ächzt)
                    if (chargeTime % 15 == 1) {
                        this.getCommandSenderWorld().playSound(null,
                                this.getX(), this.getY() + 2, this.getZ(),
                                ModSounds.CATAPULT_LOADED.get(), // Holzächzen, passend
                                this.getSoundSource(), 0.6F,
                                0.7F + random.nextFloat() * 0.3F);
                    }
                } else {
                    // Leertaste losgelassen → Schlag!
                    setState(RamState.SWINGING);
                    impactDealt = false;
                    // Aufprall-Sound kündigt sich an
                    this.getCommandSenderWorld().playSound(null,
                            this.getX(), this.getY() + 2, this.getZ(),
                            ModSounds.CATAPULT_SHOT.get(),
                            this.getSoundSource(), 4F,
                            0.8F + random.nextFloat() * 0.2F);
                }
            }

            case SWINGING -> {
                // Balken schwingt schnell nach vorne
                float newAngle = Math.min(angle + SWING_RATE, IMPACT_ANGLE);
                setRamAngle(newAngle);

                // Aufprall-Moment: wenn Balken Null-Linie (senkrecht) passiert
                if (!impactDealt && angle >= 0F) {
                    dealRamDamage();
                    impactDealt = true;
                }

                // Endposition erreicht → Cooldown
                if (newAngle >= IMPACT_ANGLE) {
                    setState(RamState.COOLDOWN);
                    setCharge(0F);
                    chargeTime = 0;
                }
            }

            case COOLDOWN -> {
                // Balken federt langsam zurück auf Ausgangsposition
                if (angle > 0F) {
                    setRamAngle(Math.max(angle - RETURN_RATE, 0F));
                } else {
                    setState(RamState.IDLE);
                    setRamAngle(0F);
                }
            }
        }

        // Sicherheits-Reset wenn kein Fahrer mehr vorhanden
        if (driver == null && (state == RamState.CHARGING)) {
            setState(RamState.IDLE);
            setRamAngle(0F);
            setCharge(0F);
            chargeTime = 0;
        }
    }

    // ─── SCHADEN ──────────────────────────────────────────────────────────────

    private void dealRamDamage() {
        float charge = getCharge();
        float damage = BASE_DAMAGE + charge * BONUS_DAMAGE;

        // Hitbox: AABB vor dem Ram
        Vec3 forward = this.getForward();
        Vec3 center = this.position().add(forward.scale(HIT_REACH));

        AABB hitBox = new AABB(
                center.x - HIT_WIDTH, this.getY() - 0.5,  center.z - HIT_WIDTH,
                center.x + HIT_WIDTH, this.getY() + 2.5,  center.z + HIT_WIDTH
        );

        // Entities schaden
        List<LivingEntity> targets = this.getCommandSenderWorld()
                .getEntitiesOfClass(LivingEntity.class, hitBox,
                        e -> !e.isPassengerOfSameVehicle(this));

        DamageSource dmgSource = this.damageSources().generic();
        for (LivingEntity target : targets) {
            target.hurt(dmgSource, damage);
            // Entities wegschleudern
            Vec3 knockback = forward.scale(0.8F + charge * 0.8F);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
        }

        // Block-Schaden: nur bei voller Aufladung
        if (charge >= 1.0F) {
            applyBlockDamage(forward, charge);
        }

        // Aufprall-Sound + Erschütterung
        this.getCommandSenderWorld().playSound(null,
                this.getX(), this.getY() + 1, this.getZ(),
                ModSounds.SIEGEWEAPON_HIT.get(),
                this.getSoundSource(), 5F,
                0.7F + random.nextFloat() * 0.3F);
    }

    private void applyBlockDamage(Vec3 forward, float charge) {
        Level level = this.getCommandSenderWorld();

        // range=1 – leicht weniger als vorher (kein range=2 mehr)
        int range = 1;
        BlockPos centerPos = BlockPos.containing(
                this.getX() + forward.x * (HIT_REACH + 0.5),
                this.getY() + 1,
                this.getZ() + forward.z * (HIT_REACH + 0.5)
        );

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    BlockPos pos = centerPos.offset(dx, dy, dz);
                    var state = level.getBlockState(pos);
                    float hardness = state.getDestroySpeed(level, pos);

                    // Härteschwelle fest auf 2.5 (z.B. Holz/Stein ja, Obsidian nein)
                    if (!state.isAir() && hardness >= 0 && hardness < 2.5F) {
                        level.destroyBlock(pos, true, lastDriver);
                    }
                }
            }
        }
    }

    // ─── CONTROL: Katapult-ähnlich, aber agiler ────────────────────────────────

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

            float newYRot = yRot + this.deltaRotation * 0.38F;
            this.setXRot(xRot);
            this.setYRot(newYRot);
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

    @Override
    public void shootWeapon() {
        // Schlag wird intern über State-Machine ausgelöst, nicht über shootWeapon()
    }

    @Override
    public void playShootSound() {

    }

    @Override
    public void playLoadingSound() {

    }

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

    @Override
    public void setShowTrajectory(boolean v){
        this.showTrajectory = v;
    }

    @Override
    public boolean getShowTrajectory(){
        return showTrajectory;
    }

    // ─── GETTER / SETTER ──────────────────────────────────────────────────────

    public float getRamAngle() {
        return entityData.get(RAM_ANGLE);
    }
    public void  setRamAngle(float a){
        entityData.set(RAM_ANGLE, a);
    }

    /** Interpolierte Variante für smooth client-seitige Animation. */
    public float getRamAngle(float partialTicks) {
        return Mth.lerp(partialTicks, prevRamAngle, getRamAngle());
    }

    public float getCharge()   {
        return entityData.get(CHARGE);
    }
    public void  setCharge(float c){
        entityData.set(CHARGE, c);
    }

    public boolean isTriggering()  {
        return entityData.get(TRIGGERING);
    }
    public void setTriggering(boolean t){
        entityData.set(TRIGGERING, t);
    }

    public RamState getState(){
        return RamState.fromIndex(entityData.get(STATE));
    }
    public void setState(RamState s){
        entityData.set(STATE, s.getIndex());
    }

    // ─── PASSENGERS ───────────────────────────────────────────────────────────

    @Override
    protected void positionRider(@NotNull Entity rider, @NotNull MoveFunction fn) {
        if (!this.hasPassenger(rider)) return;
        Vec3 pos = getDriverPosition();
        fn.accept(rider, this.getX() + pos.x, this.getY() + getPassengersRidingOffset(), this.getZ() + pos.z);
    }

    @Override
    public Vec3 getDriverPosition() {
        double f = -2.0F;
        double d = -0.75F;
        return new Vec3(f, 0D, d).yRot(-this.getYRot() * (float)(Math.PI / 180F) - (float)(Math.PI / 2F));
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0D;
    }

    // ─── ABSTRACT IMPL ────────────────────────────────────────────────────────

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

    @Override public int getMaxPassengerSize(){
        return 1;
    }
    @Override public int getMaxSpeedInKmH(){
        return 10;
    }
    @Override public double getMaxHealth(){
        return 1000D;
    }
    @Override public Component getVehicleTypeName(){
        return ModTexts.BATTERING_RAM;
    }

    // ─── STATE ENUM ───────────────────────────────────────────────────────────

    public enum RamState {
        IDLE(0),
        CHARGING(1),
        SWINGING(2),
        COOLDOWN(3);

        private final int index;
        RamState(int i){
            this.index = i;
        }
        public int getIndex(){
            return index;
        }

        public static RamState fromIndex(int x) {
            for (RamState s : values()) if (s.index == x) return s;
            return IDLE;
        }
    }
}
