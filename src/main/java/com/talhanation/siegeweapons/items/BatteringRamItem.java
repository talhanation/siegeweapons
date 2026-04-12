package com.talhanation.siegeweapons.items;

import com.talhanation.siegeweapons.entities.BatteringRamEntity;
import com.talhanation.siegeweapons.init.ModEntityTypes;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class BatteringRamItem extends Item {

    public BatteringRamItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

        if (hitresult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(itemstack);

        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BatteringRamEntity entity = new BatteringRamEntity(ModEntityTypes.BATTERING_RAM.get(), level);
            entity.setYRot(player.getYRot() + 90F);
            entity.setPos(hitresult.getLocation().add(0, 1, 0));

            if (!level.isClientSide) {
                level.addFreshEntity(entity);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.getLocation());
                if (!player.getAbilities().instabuild) itemstack.shrink(1);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }

        return InteractionResultHolder.pass(itemstack);
    }
}
