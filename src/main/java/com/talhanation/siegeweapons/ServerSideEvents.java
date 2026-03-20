package com.talhanation.siegeweapons;

import com.talhanation.siegeweapons.entities.BallistaEntity;
import com.talhanation.siegeweapons.entities.SmallHorseCartEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerSideEvents {

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        // Sync passenger rotation when mounting Ballista
        if (event.getEntityBeingMounted() instanceof BallistaEntity ballista && event.getEntityMounting() instanceof LivingEntity living) {
            living.setYRot(ballista.getYRot());
            living.setXRot(ballista.getXRot());
            living.yHeadRot = ballista.getYRot();
        }

        // Prevent any entity from riding a horse that is attached to a SmallHorseCart
        if (event.isMounting()
                && event.getEntityBeingMounted() instanceof AbstractHorse horse
                && horse.isPassenger()
                && horse.getVehicle() instanceof SmallHorseCartEntity
                && event.getEntityMounting() instanceof Player) {
            event.setCanceled(true);
        }
    }
}
