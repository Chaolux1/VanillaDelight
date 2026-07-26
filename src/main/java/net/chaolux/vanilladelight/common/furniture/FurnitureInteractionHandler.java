package net.chaolux.vanilladelight.common.furniture;

import net.chaolux.vanilladelight.Config;
import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.api.furniture.FurnitureSection;
import net.chaolux.vanilladelight.common.block.ModularFurnitureBlock;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class FurnitureInteractionHandler {
    private static final int WAX=3003;
    private static final int WAX_OFF=3004;
    private static final SectionResolver SECTION_RESOLVER=new DefaultSectionResolver();
    private FurnitureInteractionHandler() {

    }

    public static InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof FurnitureBlockEntity furnitureBlockEntity)) return InteractionResult.PASS;
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.HONEYCOMB) && !player.isShiftKeyDown()) return useHoneycomb(level, blockPos, player, itemStack, furnitureBlockEntity);
        if (player.isShiftKeyDown() && itemStack.canPerformAction(ToolActions.AXE_WAX_OFF)) return removeHoneycomb(level, blockPos, player, hand, itemStack, furnitureBlockEntity);
        if (player.isShiftKeyDown() && itemStack.isEmpty()) return cycleStyle(level, player, furnitureBlockEntity);
        if (itemStack.getItem() instanceof BlockItem blockItem) return useMaterial(blockState, level, blockPos, player, itemStack, blockItem, blockHitResult, furnitureBlockEntity);
        return InteractionResult.PASS;
    }

    private static InteractionResult useHoneycomb(Level level,BlockPos blockPos,Player player,ItemStack itemStack,FurnitureBlockEntity furnitureBlockEntity) {
        if(furnitureBlockEntity.isAppearanceLocked()) return InteractionResult.PASS;
        if(!level.isClientSide) {
            if (furnitureBlockEntity.lockAppearance()) {
                if (!player.getAbilities().instabuild) itemStack.shrink(1);
                level.levelEvent(null, WAX, blockPos, 0);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static InteractionResult removeHoneycomb(Level level,BlockPos blockPos,Player player,InteractionHand hand,ItemStack itemStack,FurnitureBlockEntity furnitureBlockEntity) {
        if(!furnitureBlockEntity.isAppearanceLocked()) return InteractionResult.PASS;
        if(!level.isClientSide) {
            if (furnitureBlockEntity.unlockAppearance()) {
                level.playSound(null, blockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.levelEvent(null, WAX_OFF, blockPos, 0);
                if (!player.getAbilities().instabuild)
                    itemStack.hurtAndBreak(1, player, target -> target.broadcastBreakEvent(hand));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static InteractionResult cycleStyle(Level level,Player player,FurnitureBlockEntity furnitureBlockEntity) {
        if(furnitureBlockEntity.isAppearanceLocked()) {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if(!level.isClientSide) {
            String string=furnitureBlockEntity.cycleStyle();
            player.displayClientMessage(Component.translatable("message.vanilladelight.furniture.style",Component.translatable(furnitureBlockEntity.getFurnitureAppearance().definition().styleTranslationKey(string))),true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static InteractionResult useMaterial(BlockState blockState,Level level,BlockPos blockPos,Player player,ItemStack itemStack,BlockItem blockItem,BlockHitResult blockHitResult,FurnitureBlockEntity furnitureBlockEntity) {
        FurnitureAppearance furnitureAppearance = furnitureBlockEntity.getFurnitureAppearance();
        FurnitureDefintion furnitureDefintion = furnitureAppearance.definition();
        Optional<FurnitureSection> resolveSection = resolveSection(blockState, blockPos, blockHitResult, furnitureAppearance, furnitureDefintion);
        if (resolveSection.isEmpty()) return InteractionResult.PASS;
        FurnitureSection furnitureSection = resolveSection.get();
        String string = furnitureSection.material();
        boolean alreadyInstall = furnitureAppearance.isInstallMaterial(string);
        if (furnitureBlockEntity.isAppearanceLocked()) {
            if (!player.isShiftKeyDown()) return InteractionResult.PASS;
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (alreadyInstall && !player.isShiftKeyDown()) return InteractionResult.PASS;
        BlockState state = materialState(blockItem, itemStack);
        MaterialState materialState = MaterialState.from(state);
        MaterialState oldMaterial = furnitureAppearance.material(string);

        if (materialState.equals(oldMaterial)) return InteractionResult.sidedSuccess(level.isClientSide);
        if (!level.isClientSide) {
            if (!isAllowMaterial(level, blockPos, state)) {
                player.displayClientMessage(Component.translatable("message.vanilladelight.furniture.invalid_material"), true);
                return InteractionResult.CONSUME;
            }
            if (furnitureBlockEntity.setMaterial(string, state)) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    if (alreadyInstall) {
                        ItemStack stack = oldMaterial.toItemStack();
                        if (!stack.isEmpty()) Containers.dropItemStack(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, stack);
                    }
                }
                player.displayClientMessage(Component.translatable("message.vanilladelight.furniture.material", Component.translatable(furnitureDefintion.sectionTranslationKey(resolveSection.get().id())), blockItem.getBlock().getName()), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static BlockState materialState(BlockItem blockItem,ItemStack itemStack) {
        BlockState blockState=blockItem.getBlock().defaultBlockState();
        CompoundTag compoundTag=itemStack.getTagElement("BlockStateTag");
        if(compoundTag == null) return blockState;
        Map<String,String> values=new LinkedHashMap<>();
        for(String string : compoundTag.getAllKeys()) {
            if(compoundTag.contains(string, Tag.TAG_STRING)) values.put(string,compoundTag.getString(string));
        }
        MaterialState materialState=new MaterialState(BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()),values);
        return materialState.state();
    }

    private static Optional<FurnitureSection> resolveSection(BlockState blockState,BlockPos blockPos,BlockHitResult blockHitResult,FurnitureAppearance furnitureAppearance,FurnitureDefintion furnitureDefintion) {
        Vec3 vec3=blockHitResult.getLocation().subtract(blockPos.getX(),blockPos.getY(),blockPos.getZ());
        return SECTION_RESOLVER.resolve(furnitureDefintion,furnitureAppearance.style(),vec3,blockState.getValue(ModularFurnitureBlock.FACING));
    }

    private static boolean isAllowMaterial(Level level,BlockPos blockPos,BlockState blockState) {
        if(blockState.isAir() || blockState.getRenderShape() == RenderShape.INVISIBLE) return false;
        if(Config.ALLOW_NON_FULL_BLOCK_MATERIAL.get()) return true;
        return Block.isShapeFullBlock(blockState.getCollisionShape(level,blockPos));
    }
}