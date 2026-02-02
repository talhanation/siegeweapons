package com.talhanation.siegeweapons.entities;

import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.ModTexts;
import com.talhanation.siegeweapons.config.SiegeWeaponsServerConfig;
import com.talhanation.siegeweapons.entities.projectile.*;
import com.talhanation.siegeweapons.init.ModItems;
import com.talhanation.siegeweapons.init.ModSounds;
import com.talhanation.siegeweapons.inventory.VehicleInventoryMenu;
import com.talhanation.siegeweapons.network.MessageLoadAndShootWeapon;
import com.talhanation.siegeweapons.network.MessageOpenGUI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class CatapultEntity extends AbstractInventoryVehicleEntity implements IShootingWeapon {

    private static final EntityDataAccessor<Float> RANGE = SynchedEntityData.defineId(CatapultEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE = SynchedEntityData.defineId(CatapultEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(CatapultEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PROJECTILE = SynchedEntityData.defineId(CatapultEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> TRIGGERING = SynchedEntityData.defineId(CatapultEntity.class, EntityDataSerializers.BOOLEAN);
    private float loaderRotation;
    private LivingEntity driver;
    private boolean showTrajectory;
    private int loadingTime;
    private int shootCoolDown;
    public CatapultEntity(EntityType<? extends AbstractInventoryVehicleEntity> entityType, Level world) {
        super(entityType, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RANGE, 100F);
        this.entityData.define(ANGLE, -0.65F);
        this.entityData.define(STATE, 4);
        this.entityData.define(PROJECTILE, 0);
        this.entityData.define(TRIGGERING, false);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putFloat("range", this.getRange());
        compoundTag.putInt("state", this.getState().getIndex());
        compoundTag.putFloat("angle", this.getAngle());
        compoundTag.putInt("projectile", this.getProjectile().getIndex()                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     );
        compoundTag.putInt("loadingTime", loadingTime);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setRange(compoundTag.getFloat("range"));
        this.setState(CatapultState.fromIndex(compoundTag.getInt("state")));
        this.setAngle(compoundTag.getFloat("angle"));
        this.setProjectile(CatapultProjectiles.fromIndex(compoundTag.getInt("state")));
        if(compoundTag.contains("loadingTime")){
            this.loadingTime = compoundTag.getInt("loadingTime");
        }
        else{
            this.loadingTime = -1;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(--shootCoolDown > 0) return;
        updateLoaderRotation();
        updateAngleRotation();

        float angle = getAngle();
        CatapultState state = getState();
        boolean trigger = this.isTriggering();



        if(trigger && (state == CatapultState.LOADING || state == CatapultState.SHOT)){
            if(state == CatapultState.SHOT){
                loadingTime++;
                this.playLoadingSound();
            }

            if(angle == -0.65F){
                setState(CatapultState.LOADED);
                return;
            }
        }

        if(trigger && state == CatapultState.PROJECTILE_LOADED){
            setState(CatapultState.SHOOTING);
            this.playShootSound();

            return;
        }

        if(state == CatapultState.SHOOTING && angle == 0.65F){
            setState(CatapultState.SHOT);
            shootWeapon();
            loadingTime = -1;
        }
    }

    protected boolean tryRiding(Entity entity) {
        if(super.tryRiding(entity) && entity instanceof LivingEntity livingEntity){
            this.driver = livingEntity;
            return true;
        }
        return false;
    }

    public void updateLoaderRotation() {
        loaderRotation += getLoaderRotationAmount();
    }

    public float getLoaderRotation(float partialTicks) {
        return loaderRotation + getLoaderRotationAmount() * partialTicks;
    }

    public float getLoaderRotationAmount() {
        return this.getState() == CatapultState.LOADING || this.getState() == CatapultState.SHOT && this.isTriggering() ? 0.13F : 0F;
    }

    public void updateAngleRotation() {
        setAngle((float) Mth.clamp(getAngle() + getAngleRotationAmount(), -0.65, 0.65));
    }

    public float getAngleRotation(float partialTicks) {
        return (float) Mth.clamp(getAngle() + getAngleRotationAmount() * partialTicks, -0.65, 0.65);
    }
    public float getAngleRotationAmount() {
        if(this.isTriggering() && this.getState() == CatapultState.SHOT || this.getState() == CatapultState.LOADING) return -0.005F; // return -0.104F; <-debug
        else if (getState() == CatapultState.SHOOTING) return 0.3F + 0.01F * getRange();
        else return 0;
    }

    public void setRange(float x) {
        if(x > 100) x = 100;
        if(x < 0) x = 0;

        entityData.set(RANGE, x);
    }

    public float getRange(){
        return entityData.get(RANGE);
    }

    public void setAngle(float x) {
        entityData.set(ANGLE, x);
    }

    public float getAngle(){
        return entityData.get(ANGLE);
    }

    public void setState(CatapultState catapultState) {
        if(getControllingPassenger() != null) {
            this.driver = getControllingPassenger();
        }
        this.entityData.set(STATE, catapultState.getIndex());
    }

    public CatapultState getState(){
        return CatapultState.fromIndex(entityData.get(STATE));
    }
    public void setProjectile(CatapultProjectiles projectile) {
        this.entityData.set(PROJECTILE, projectile.getIndex());
    }
    public CatapultProjectiles getProjectile(){
        return CatapultProjectiles.fromIndex(entityData.get(PROJECTILE));
    }

    @Override
    public boolean itemInteraction(Player player, InteractionHand interactionHand) {
        if(super.itemInteraction(player, interactionHand)) {
            return true;
        }
        else{
            ItemStack itemStack = player.getMainHandItem();

            if(getState() == CatapultState.LOADED){
                if(itemStack.is(Items.COBBLESTONE)){
                    setProjectile(CatapultProjectiles.COBBLE_SHOT);
                }
                else if(itemStack.is(ModItems.COBBLE_CLUSTER_ITEM.get())){
                    setProjectile(CatapultProjectiles.BUNDLE_SHOT);
                }
                else if(itemStack.is(ModItems.FIRE_POT_ITEM.get())){
                    setProjectile(CatapultProjectiles.FIRE_SHOT);
                }
                else if(itemStack.is(ModItems.EXPLOSION_POT_ITEM.get())){
                    setProjectile(CatapultProjectiles.EXPLOSION_SHOT);
                }
                else {
                    return false;
                }

                setState(CatapultState.PROJECTILE_LOADED);
                playLoadedSound();
                itemStack.shrink(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public Vec3 getDriverPosition() {
        double f = -2.5F;
        double d = -1.0F;
        return (new Vec3(f, 0.0D, 0.0D + d)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
    }

    @Override
    public int getMaxPassengerSize() {
        return 1;
    }

    @Override
    public int getMaxSpeedInKmH() {
        return 10;//TODO: CONFIG
    }

    @Override
    public double getMaxHealth() {
        return SiegeWeaponsServerConfig.catapultHealth.get();//TODO: CONFIG
    }

    public Component getVehicleTypeName(){
        return ModTexts.CATAPULT;
    }

    @Override
    public void openGUI(Player player) {
        if (player instanceof ServerPlayer) {
            NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return CatapultEntity.this.getName();
                }

                @Override
                public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                    return new VehicleInventoryMenu(i, CatapultEntity.this, playerInventory);
                }
            }, packetBuffer -> {packetBuffer.writeUUID(CatapultEntity.this.getUUID());
            });
        } else {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageOpenGUI(CatapultEntity.this));
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.CATAPULT_ITEM.get());
    }

    protected void positionRider(Entity rider, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(rider)) {
            Vec3 vec = getDriverPosition();

            moveFunction.accept(rider, this.getX() + vec.x, this.getY(), this.getZ() + vec.z);
        }
    }


    @Override
    public void shootWeapon() {
        Vec3 forward = this.getForward();
        float speed = getCalcRange();

        double yShootVec = forward.y() + 35F/40F;

        this.shoot(forward, yShootVec, this.getControllingPassenger(), speed);
        shootCoolDown = 30;
    }

    /*
     * Method Important for reflection
     */
    public void shoot(Vec3 shootVec, double yShootVec, LivingEntity driverEntity, float speed){
        AbstractCatapultProjectile projectile = null;
        CatapultProjectiles projectileCase = getProjectile();
        if(driverEntity == null){
            if(driver == null) return;
            driverEntity = driver;
        }

        switch (projectileCase){
            case EMPTY -> {
                this.setProjectile(this.getProjectile().getNext());
                return;
            }

            case COBBLE_SHOT -> {
                projectile = new CatapultCobbleProjectile(this.getCommandSenderWorld(), driverEntity, this.getX(), this.getY() + 3.75, this.getZ());

                projectile.setOwner(this);
            }

            case FIRE_SHOT -> {
                projectile = new FirePotProjectile(this.getCommandSenderWorld(), driverEntity, this.getX(), this.getY() + 3.75, this.getZ());
            }

            case EXPLOSION_SHOT -> {
                projectile = new ExplosionPotProjectile(this.getCommandSenderWorld(), driverEntity, this.getX(), this.getY() + 3.75, this.getZ());
            }

            case BUNDLE_SHOT -> {
                int amount = 12;
                for(int i = 0; i< amount; i++) {
                    projectile = new CatapultCobbleClusterProjectile(this.getCommandSenderWorld(), driverEntity, this.getX(), this.getY() + 3.75, this.getZ());
                    projectile.shoot(shootVec.x(), yShootVec, shootVec.z(), speed, projectile.getAccuracy());
                    this.getCommandSenderWorld().addFreshEntity(projectile);

                }
                this.setProjectile(CatapultProjectiles.EMPTY);
                return;
            }
        }

        if(projectile != null){
            projectile.shoot(shootVec.x(), yShootVec, shootVec.z(), speed, projectile.getAccuracy());
            this.getCommandSenderWorld().addFreshEntity(projectile);
            this.setProjectile(this.getProjectile().getNext());
        }
    }

    @Override
    public void playShootSound() {
        this.playSound(ModSounds.CATAPULT_SHOT.get(), 5F, 0.8F + 0.4F * this.random.nextFloat());
    }

    private int lastPlayedPhase = -1;
    public void playLoadedSound() {
        this.playSound(ModSounds.CATAPULT_LOADED.get(), 5F, 0.8F + 0.4F * this.random.nextFloat());
    }
    public void playLoadingSound() {
        int phase = loadingTime / 20;

        if (phase != lastPlayedPhase) {
            SoundEvent loadingSound = switch (phase) {
                case 0 -> ModSounds.CATAPULT_DRAW_0.get();
                case 1 -> ModSounds.CATAPULT_DRAW_1.get();
                case 2 -> ModSounds.CATAPULT_DRAW_2.get();
                case 3 -> ModSounds.CATAPULT_DRAW_3.get();
                case 4, 5, 6 -> ModSounds.CATAPULT_DRAW_4.get();
                case 7, 8, 9 -> ModSounds.CATAPULT_DRAW_5.get();
                case 10 -> ModSounds.CATAPULT_DRAW_6.get();
                case 11 -> ModSounds.CATAPULT_DRAW_7.get();
                case 12 -> ModSounds.CATAPULT_DRAW_8.get();
                case 13 -> ModSounds.CATAPULT_DRAW_9.get();
                case 14 -> ModSounds.CATAPULT_DRAW_10.get();
                case 15 -> ModSounds.CATAPULT_DRAW_11.get();
                default -> null;
            };

            if (loadingSound != null) {
                this.getCommandSenderWorld().playSound(null, this.getX(), this.getY() + 4, this.getZ(),
                        loadingSound, this.getSoundSource(), 5.3F, 0.8F + 0.4F * this.random.nextFloat());
                lastPlayedPhase = phase;
            }
        }
    }

    @Override
    public void updateTrigger(boolean trigger, LivingEntity livingEntity) {
        boolean needsUpdate = false;

        if (this.isTriggering() != trigger) {
            this.setTriggering(trigger);
            needsUpdate = true;
        }

        if (this.getCommandSenderWorld().isClientSide && needsUpdate && livingEntity instanceof Player) {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageLoadAndShootWeapon(trigger, livingEntity.getUUID()));
        }
    }

    @Override
    public void setShowTrajectory(boolean rightClickKey) {
        this.showTrajectory = rightClickKey;
    }

    @Override
    public boolean getShowTrajectory() {
        return showTrajectory;
    }

    public boolean isTriggering() {
        return this.entityData.get(TRIGGERING);
    }

    public void setTriggering(boolean trigger) {
        this.entityData.set(TRIGGERING, trigger);
    }

    public float getCalcRange() {
        float range = getRange();
        return 1.5F + 2.0F * range * 0.01F;
    }

    public enum CatapultState {
        LOADING(0),
        LOADED(1),
        PROJECTILE_LOADED(2),
        SHOOTING(3),
        SHOT(4);

        private final int index;


        CatapultState(int index){
            this.index = index;

        }

        public int getIndex() {
            return index;
        }

        public static CatapultState fromIndex(int x){
            for (CatapultState state : CatapultState.values()) {
                if (state.getIndex() == x) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Invalid Status index: " + x);
        }
    }


    public enum CatapultProjectiles {
        EMPTY(0),
        COBBLE_SHOT(1),
        FIRE_SHOT(2),
        EXPLOSION_SHOT(3),
        BUNDLE_SHOT(4);

        private final int index;

        CatapultProjectiles(int index){
            this.index = index;

        }
        public int getIndex() {
            return index;
        }
        private static final CatapultProjectiles[] VALUES = values();
        public CatapultProjectiles getNext() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        public static CatapultProjectiles fromIndex(int x){
            for (CatapultProjectiles state : CatapultProjectiles.values()) {
                if (state.getIndex() == x) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Invalid Status index: " + x);
        }
    }
}

