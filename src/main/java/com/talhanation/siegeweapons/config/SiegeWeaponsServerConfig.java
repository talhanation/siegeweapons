package com.talhanation.siegeweapons.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class SiegeWeaponsServerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SERVER;

    public static ForgeConfigSpec.DoubleValue catapultHealth;
    public static ForgeConfigSpec.DoubleValue catapultCobbleClusterDamage;
    public static ForgeConfigSpec.DoubleValue catapultCobbleDamage;
    public static ForgeConfigSpec.DoubleValue catapultCobbleDestruction;
    public static ForgeConfigSpec.IntValue firePotRange;
    public static ForgeConfigSpec.DoubleValue explosionPotDestruction;
    public static ForgeConfigSpec.DoubleValue ballistaHealth;
    public static ForgeConfigSpec.DoubleValue ballistaProjectileDamage;

    static {
        BUILDER.comment("Siege Weapons Config:").push("Siege Weapons");

        catapultHealth = BUILDER.comment("""
                        
                        Health a catapult has.
                        \t(takes effect after restart)
                        \tdefault: 600""")
                .worldRestart()
                .defineInRange("catapultHealth", 600D, 0, 1453);

        catapultCobbleDamage = BUILDER.comment("""
                        
                        Damage a catapult cobblestone projectile makes.
                        \t(takes effect after restart)
                        \tdefault: 65""")
                .worldRestart()
                .defineInRange("catapultCobbleDamage", 65D, 0, 1453);

        catapultCobbleDestruction = BUILDER.comment("""
                        
                        Destruction a catapult cobblestone projectile makes. Reference: 4 = TNT-Block
                        \t(takes effect after restart)
                        \tdefault: 2.5""")
                .worldRestart()
                .defineInRange("catapultCobbleDestruction", 2.5D, 0, 1453);

        catapultCobbleClusterDamage = BUILDER.comment("""
                        
                        Damage a catapult cobblestone cluster projectile makes.
                        \t(takes effect after restart)
                        \tdefault: 25""")
                .worldRestart()
                .defineInRange("catapultCobbleClusterDamage", 25D, 0, 1453);


        firePotRange = BUILDER.comment("""
                        
                        Radius of blocks a fire pot ignites the area.
                        \t(takes effect after restart)
                        \tdefault: 2""")
                .worldRestart()
                .defineInRange("firePotRange", 2, 0, 1453);


        explosionPotDestruction = BUILDER.comment("""
                        
                        Destruction of blocks a fire pot explodes the area on impact. Reference: 4 = TNT-Block
                        \t(takes effect after restart)
                        \tdefault: 3.5""")
                .worldRestart()
                .defineInRange("explosionPotDestruction", 3.5D, 0, 1453);

        ballistaHealth = BUILDER.comment("""
                        
                        Health a Ballista has.
                        \t(takes effect after restart)
                        \tdefault: 200""")
                .worldRestart()
                .defineInRange("ballistaHealth", 200D, 0, 1453);

        ballistaProjectileDamage = BUILDER.comment("""
                        
                        Damage a ballista projectile makes.
                        \t(takes effect after restart)
                        \tdefault: 20""")
                .worldRestart()
                .defineInRange("ballistaProjectileDamage", 20D, 0, 1453);


        //Crafting Config
/*
        BUILDER.pop();
        BUILDER.comment("Siege Weapons Crafting Config:").push("Crafting");

        ballistaRecipe = BUILDER.comment("""

                        List of foods that recruits should not eat.
                        \t(takes effect after restart)
                        \tFood items in this list will not be eaten by recruits and also not be picked up from upkeep.""")
                .worldRestart()
                .define("FoodBlackList", FOOD_BLACKLIST);
*/
        SERVER = BUILDER.build();
    }


    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }
}

