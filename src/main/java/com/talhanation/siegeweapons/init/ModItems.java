package com.talhanation.siegeweapons.init;

import com.talhanation.siegeweapons.Main;
import com.talhanation.siegeweapons.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
    public static final RegistryObject<Item> SIEGE_TABLE_BLOCK = ITEMS.register("siege_table_block", () -> new BlockItem(ModBlocks.SIEGE_TABLE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<FirePotItem> FIRE_POT_ITEM = ITEMS.register("fire_pot_item",() -> new FirePotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ExplosionPotItem> EXPLOSION_POT_ITEM = ITEMS.register("explosion_pot_item",() -> new ExplosionPotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<CobbleClusterItem> COBBLE_CLUSTER_ITEM = ITEMS.register("cobble_cluster_item", () -> new CobbleClusterItem(new Item.Properties()));
    public static final RegistryObject<BallistaProjectileItem> BALLISTA_PROJECTILE_ITEM = ITEMS.register("ballista_projectile_item", () -> new BallistaProjectileItem(new Item.Properties()));

    public static final RegistryObject<CatapultItem> CATAPULT_ITEM = ITEMS.register("catapult_item", () -> new CatapultItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BallistaItem> BALLISTA_ITEM = ITEMS.register("ballista_item", () -> new BallistaItem(new Item.Properties().stacksTo(1)));
    //public static final RegistryObject<SmallHorseCartItem> SMALL_HORSE_CART_ITEM = ITEMS.register("small_horse_cart_item", () -> new SmallHorseCartItem(new Item.Properties().stacksTo(1)));
}
