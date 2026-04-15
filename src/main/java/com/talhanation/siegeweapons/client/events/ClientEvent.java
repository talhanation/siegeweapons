package com.talhanation.siegeweapons.client.events;


import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.client.render.*;
import com.talhanation.siegeweapons.entities.BallistaEntity;
import com.talhanation.siegeweapons.init.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD , value = Dist.CLIENT)
public class ClientEvent {
    public static EntityRendererProvider.Context context;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void entityRenderersEvent(EntityRenderersEvent.RegisterRenderers event){
        EntityRenderers.register(ModEntityTypes.CATAPULT.get(),                (ctx) -> { context = ctx; return new CatapultRenderer(ctx); });
        EntityRenderers.register(ModEntityTypes.CATAPULT_PROJECTILE.get(),     (ctx) -> { context = ctx; return new CatapultProjectileRenderer(ctx); });
        EntityRenderers.register(ModEntityTypes.COBBLE_CLUSTER_PROJECTILE.get(),(ctx) -> { context = ctx; return new CobbleClusterProjectileRenderer(ctx, false); });
        EntityRenderers.register(ModEntityTypes.EXPLOSION_POT_PROJECTILE.get(),(ctx) -> { context = ctx; return new ExplosionPotProjectileRenderer(ctx, false); });
        EntityRenderers.register(ModEntityTypes.FIRE_POT_PROJECTILE.get(),     (ctx) -> { context = ctx; return new FirePotProjectileRenderer(ctx, false); });
        EntityRenderers.register(ModEntityTypes.BALLISTA.get(),                (ctx) -> { context = ctx; return new BallistaRenderer(ctx); });
        EntityRenderers.register(ModEntityTypes.BALLISTA_PROJECTILE.get(),     (ctx) -> { context = ctx; return new BallistaProjectileRenderer(ctx); });
        EntityRenderers.register(ModEntityTypes.TRANSPORT_CART.get(),          (ctx) -> { context = ctx; return new TransportCartRenderer(ctx); });
        EntityRenderers.register(ModEntityTypes.SMALL_HORSE_CART.get(),        (ctx) -> { context = ctx; return new SmallHorseCartRenderer(ctx); });
        EntityRenderers.register(ModEntityTypes.BATTERING_RAM.get(),           (ctx) -> { context = ctx; return new BatteringRamRenderer(ctx); });
        EntityRenderers.register(ModEntityTypes.SIEGE_TOWER.get(),             (ctx) -> { context = ctx; return new SiegeTowerRenderer(ctx); });
    }

    @Nullable
    public static Entity getEntityByLooking() {
        HitResult hit = Minecraft.getInstance().hitResult;
        if (hit instanceof EntityHitResult entityHitResult) return entityHitResult.getEntity();
        return null;
    }
}
