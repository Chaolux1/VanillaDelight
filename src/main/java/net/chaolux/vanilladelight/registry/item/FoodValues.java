package net.chaolux.vanilladelight.registry.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FoodValues {
    public static final FoodProperties APPLE_SALAD = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.6F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 700, 2, false, false), 1.0F).build();
    public static final FoodProperties BERRIES_SALAD = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.5F).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0, false, false), 1.0F).build();
    public static final FoodProperties CARAMELIZED_CHICKEN = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.7F).meat().effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 0, false, false), 1.0F).build();
    public static final FoodProperties CARROT_SALAD = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.4F).effect(() -> new MobEffectInstance(MobEffects.JUMP, 400, 0, false, false), 1.0F).build();
    public static final FoodProperties COD_STEW = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.6F).meat().build();
    public static final FoodProperties GLOW_TROPICAL_FISH_STEW = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.7F).meat().build();
    public static final FoodProperties GRILLED_BEETROOT = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.4F).build();
    public static final FoodProperties CARAMEL_BOTTLE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final FoodProperties GLOW_BERRIES_JAM = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.8F).build();
    public static final FoodProperties GOLDEN_APPLE_CIDER = (new FoodProperties.Builder()).nutrition(5).saturationMod(1.2F).effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1400, 1, false, false), 1.0F).build();
    public static final FoodProperties SWEET_BERRIES_JAM = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.8F).build();
    public static final FoodProperties SWEET_BERRY_CUSTARD = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.7F).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1000, 0, false, false), 1.0F).build();
    public static final FoodProperties APPLE_SLICE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).fast().build();
    public static final FoodProperties BUTTER = (new FoodProperties.Builder()).nutrition(2).saturationMod(1.0F).fast().build();
    public static final FoodProperties CARAMEL_CANDY = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0, false, false), 1.0F).build();
    public static final FoodProperties COOKED_APPLE = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.7F).build();
    public static final FoodProperties COOKED_CARROT = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.7F).build();
    public static final FoodProperties GLOW_BERRIES_CANDY = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false), 1.0F).build();
    public static final FoodProperties GOLDEN_APPLE_SLICE = (new FoodProperties.Builder()).nutrition(2).saturationMod(1.2F).fast().effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 100, 0, false, false), 1.0F).build();
    public static final FoodProperties HONEY_CANDY = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0, false, false), 1.0F).build();
    public static final FoodProperties HONEY_PUDDING = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.9F).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1000, 1, false, false), 1.0F).build();
    public static final FoodProperties CHORUS_PIE_SLICE = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 1, false, false), 1.0F).build();
    public static final FoodProperties MELON_PIE_SLICE = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.JUMP, 600, 1, false, false), 1.0F).build();
    public static final FoodProperties CARROT_CAKE_SLICE = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.JUMP, 400, 1, false, false), 1.0F).build();
    public static final FoodProperties HONEY_CAKE_SLICE = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1, false, false), 1.0F).build();
    public static final FoodProperties PUFFERFISH_SLICE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.7F).fast().effect(() -> new MobEffectInstance(MobEffects.POISON, 100, 1, false, false), 1.0F).build();
    public static final FoodProperties PUMPKIN_PIE_SLICE = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.8F).fast().effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, false, false), 1.0F).build();
    public static final FoodProperties MILKY_PUMPKIN = (new FoodProperties.Builder()).nutrition(8).saturationMod(1.4F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 1200, 1, false, false), 1.0F).build();
    public static final FoodProperties PUFFERFISH_STEW = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.1F).build();
    public static final FoodProperties SWEET_FISH_SOUP = (new FoodProperties.Builder()).nutrition(14).saturationMod(0.9F).build();
    public static final FoodProperties CHARRED_PUMPKIN_SLICE = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.5F).build();
    public static final FoodProperties COOKED_BROWN_MUSHROOM = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.6F).build();
    public static final FoodProperties COOKED_RED_MUSHROOM = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.6F).build();
    public static final FoodProperties COOKED_BROWN_MUSHROOM_COLONY = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.4F).build();
    public static final FoodProperties COOKED_RED_MUSHROOM_COLONY = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.4F).build();
    public static final FoodProperties COOKED_TROPICAL_FISH = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.9F).build();
    public static final FoodProperties ROASTED_BEETS = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.7F).build();
    public static final FoodProperties ENCHANTED_GOLDEN_APPLE_SLICE = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).fast().build();
    public static final FoodProperties ENCHANTED_GOLDEN_CARROT = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.8F).build();
}
