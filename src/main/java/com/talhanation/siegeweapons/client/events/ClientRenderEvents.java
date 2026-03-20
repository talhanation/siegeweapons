package com.talhanation.siegeweapons.client.events;

import com.talhanation.siegeweapons.ModTexts;
import com.talhanation.siegeweapons.client.TipManager;
import com.talhanation.siegeweapons.entities.AbstractVehicleEntity;
import com.talhanation.siegeweapons.entities.BallistaEntity;
import com.talhanation.siegeweapons.entities.CatapultEntity;
import com.talhanation.siegeweapons.entities.SmallHorseCartEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientRenderEvents {

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (Minecraft.getInstance().player.getVehicle() instanceof BallistaEntity ballista
                && ballista.getShowTrajectory()) {
            event.setCanceled(true);
        }
    }

    // ─── HORSE: Animation + Kopf-Rotation ─────────────────────────────────────
    //
    // RenderLivingEvent.Pre läuft direkt vor dem Pferd-Renderer → letzter Moment
    // vor renderToBuffer(). Alle vorherigen Tick-basierten Versuche scheiterten,
    // weil LivingEntity.updateWalkAnimation() danach mit onGround=false den Wert
    // auf 0 zurücksetzte. Hier sind wir definitiv nach allen Ticks.
    //
    // Drei Dinge werden gesetzt:
    //  1. walkAnimation  → Laufanimation der Pferde-Beine (Geschwindigkeit aus cart.getSpeed())
    //  2. yBodyRot       → Pferdekörper zeigt in Cart-Richtung
    //  3. yHeadRot       → Pferdekopf = Körper + steeringAngle (sichtbarer Lenkindikator)
    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderLivingPre(RenderLivingEvent.Pre event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;
        if (!(horse.getVehicle() instanceof SmallHorseCartEntity cart)) return;

        float cartSpeed    = Math.abs(cart.getSpeed());
        float steeringAngle = cart.getSteeringAngle();
        float cartYaw      = cart.getYRot();

        // 1. Laufanimation: normiert auf Maxspeed des Carts (≈ 0.174 Blöcke/Tick)
        float animSpeed = cartSpeed > 0.005F
                ? Math.min(cartSpeed * 5.75F, 1.0F)
                : 0F;
        horse.walkAnimation.update(1.0F, animSpeed);

        // 2. Körper: immer in Cart-Richtung
        horse.yBodyRot = cartYaw;

        // 3. Kopf: zeigt in Lenkrichtung – je stärker gelenkt wird, desto mehr
        //    dreht sich der Kopf (steeringAngle ist –30…+30, wird 1:1 als Grad-Offset genutzt)
        horse.yHeadRot = cartYaw + steeringAngle;
    }

    // ─── MOUNT TIPS ───────────────────────────────────────────────────────────

    private static final int LINE_SPACING = 2;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPlayerMountVehicle(EntityMountEvent event) {
        Entity vehicle      = event.getEntityBeingMounted();
        Entity passenger    = event.getEntityMounting();
        Entity clientPlayer = Minecraft.getInstance().player;

        if (!event.isMounting()) return;
        if (!(passenger instanceof Player player)) return;
        if (clientPlayer == null || !clientPlayer.getUUID().equals(player.getUUID())) return;

        List<Component> tips = null;
        Minecraft mc       = Minecraft.getInstance();
        String loadShoot   = mc.options.keyJump.getTranslatedKeyMessage().getString();
        String aim         = mc.options.keyUse.getTranslatedKeyMessage().getString();

        if (vehicle instanceof CatapultEntity) {
            tips = List.of(
                    Component.literal("[" + aim + " + Mouse Wheel] - ").append(ModTexts.CATAPULT_RANGE),
                    Component.literal("[" + loadShoot + "] - ").append(ModTexts.VEHICLE_LOAD_SHOOT),
                    Component.literal("[" + aim + "] - ").append(ModTexts.VEHICLE_AIM)
            );
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);

        } else if (vehicle instanceof BallistaEntity) {
            tips = List.of(
                    Component.literal("[" + aim + " + " + ModTexts.BALLISTA_BOLT.getString() + "] - ").append(ModTexts.BALLISTA_RELOAD),
                    Component.literal("[" + loadShoot + "] - ").append(ModTexts.VEHICLE_LOAD_SHOOT),
                    Component.literal("[" + aim + "] - ").append(ModTexts.VEHICLE_AIM)
            );
        }

        if (player.level().isClientSide() && tips != null) {
            TipManager.showTips(tips);
        }
    }

    // ─── TICK / GUI ───────────────────────────────────────────────────────────

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        TipManager.tick();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderGui(RenderGuiEvent.Post event) {
        List<Component> tips = TipManager.getCurrentTips();
        float alpha = TipManager.getFadeAlpha();
        if (alpha <= 0 || tips.isEmpty()) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Font font   = Minecraft.getInstance().font;
        int sw      = event.getWindow().getGuiScaledWidth();
        int sh      = event.getWindow().getGuiScaledHeight();
        int color   = (int) (alpha * 255) << 24 | 0xFFFFFF;
        int yStart  = (int) (sh / 1.25F);

        for (int i = 0; i < tips.size(); i++) {
            guiGraphics.drawString(font, tips.get(i),
                    sw / 2 - 90,
                    yStart - (i * (font.lineHeight + LINE_SPACING)),
                    color, false);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderGuiPre(RenderGuiOverlayEvent.Pre event) {
        if (Minecraft.getInstance().player.getVehicle() instanceof AbstractVehicleEntity) {
            Minecraft.getInstance().gui.setOverlayMessage(Component.empty(), false);
        }
    }
}
