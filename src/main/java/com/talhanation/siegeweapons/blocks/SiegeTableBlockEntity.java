package com.talhanation.siegeweapons.blocks;

import com.talhanation.siegeweapons.SiegeWeapons;
import com.talhanation.siegeweapons.entities.BallistaEntity;
import com.talhanation.siegeweapons.entities.CatapultEntity;
import com.talhanation.siegeweapons.init.ModBlockEntityTypes;
import com.talhanation.siegeweapons.inventory.SiegeTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

//
public class SiegeTableBlockEntity extends BaseContainerBlockEntity {
    public int finishTime = 1; //20 * 60 * 3; // Example value in ticks
    private int progressTimer = 1;
    private boolean isCrafting = false;
    public SiegeWeapons selection;
    public Random random = new Random();
    public SiegeTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SIEGE_TABLE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("entity.siegeweapons.siegetable");
    }

    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("Selection")) {
            this.selection = SiegeWeapons.fromIndex(compoundTag.getInt("Selection"));
        }
        if (compoundTag.contains("IsCrafting")) {
            this.isCrafting = compoundTag.getBoolean("IsCrafting");
        }
        if (compoundTag.contains("Progress")) {
            this.progressTimer = compoundTag.getInt("Progress");
        }
        if (compoundTag.contains("FinishTime")) {
            this.finishTime = compoundTag.getInt("FinishTime");
        }
    }

    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (this.selection != null) {
            compoundTag.putInt("Selection", this.selection.getIndex());
        }
        compoundTag.putBoolean("IsCrafting", this.isCrafting);
        compoundTag.putInt("Progress", this.progressTimer);
        compoundTag.putInt("FinishTime", this.finishTime);

    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory) {
        return new SiegeTableMenu(i, this, inventory);
    }

    @Override
    public int getContainerSize() {
        return 10; // Example inventory size
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void startCrafting(int id) {
        if(level != null && this.level.isClientSide()) return;

        this.selection = SiegeWeapons.fromIndex(id);
        this.finishTime = selection.getRecipe().getCraftingTime();
        if (!isCrafting) {
            isCrafting = true;
        }
    }
    private void cancelCrafting(boolean returnItems) {
        setCrafting(false);
        progressTimer = 1;
        finishTime = 0;
    }
    private void finishCrafting() {
        cancelCrafting(false);
        // Spawn crafted entity near the table
        if (level != null && !level.isClientSide) {
            Entity entity = this.selection.getEntity(level);
            entity.setPos(this.getBlockPos().above(1).getCenter());

            if(entity instanceof ItemEntity itemEntity){
                itemEntity.setNoPickUpDelay();
            }
            else if (entity instanceof CatapultEntity catapult){
                catapult.setState(CatapultEntity.CatapultState.LOADED);
            }
            else if (entity instanceof BallistaEntity ballista){
                ballista.setState(BallistaEntity.BallistaState.LOADED);
            }

            level.addFreshEntity(entity);
            this.playSound(SoundEvents.ANVIL_PLACE, 3.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
        }
        this.selection = null;
    }

    public void playSound(SoundEvent soundEvent, float v, float v1) {
        this.getLevel().playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), soundEvent, this.getSoundSource(), v, v1);
    }

    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    public boolean getCrafting(){
        return isCrafting;
    }

    public void setCrafting(boolean x){
        isCrafting = x;
    }

    public int getProgressTime(){
        return progressTimer;
    }

    public void setFinishTime(int x){
       progressTimer = x;
    }

    public int getCraftingProgress() {
        if (finishTime == 0) return 0;
        return (int) ((progressTimer / (float) finishTime) * 100);
    }


    public void serverTick() {
        if (isCrafting) {
            progressTimer++;

            if (progressTimer >= finishTime) {
                finishCrafting();
                sync();
                return;
            }

            if (progressTimer % 10 == 0) {
                sync();
            }
        }
    }

    private void sync() {
        if (level != null && !level.isClientSide) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}