package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity;
import net.chaolux.vanilladelight.common.block.entity.ModularStoveBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefintionProvider;
import net.chaolux.vanilladelight.common.furniture.FurnitureInteractionHandler;
import net.chaolux.vanilladelight.common.furniture.ModularStoveFireStyle;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.chaolux.vanilladelight.registry.particle.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class ModularStoveBlock extends CommonStoveBlock implements FurnitureDefintionProvider {
    private final ResourceLocation resourceLocation;

    public ModularStoveBlock(Properties properties,ResourceLocation resourceLocation) {
        super(properties);
        this.resourceLocation=resourceLocation;
    }

    @Override
    public ResourceLocation getFurnitureDefintionId() {
        return this.resourceLocation;
    }

    @Override
    protected BlockEntityType<? extends CommonStoveBlockEntity> getStoveBlockEntity() {
        return ModBlockEntityTypes.MODULAR_STOVE.get();
    }

    @Override
    public InteractionResult use(BlockState blockState,Level level,BlockPos blockPos,Player player,InteractionHand interactionHand,BlockHitResult blockHitResult) {
        InteractionResult interactionResult=FurnitureInteractionHandler.use(blockState,level,blockPos,player,interactionHand,blockHitResult);
        if(interactionResult.consumesAction()) return interactionResult;
        return super.use(blockState,level,blockPos,player,interactionHand,blockHitResult);
    }

    @Override
    public void animateTick(BlockState blockState,Level level,BlockPos blockPos,RandomSource randomSource) {
        if (!blockState.getValue(LIT)) return;
        double x = blockPos.getX() + 0.5;
        double y = blockPos.getY();
        double z = blockPos.getZ() + 0.5;
        if (randomSource.nextInt(10) == 0) level.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
        ModularStoveFireStyle modularStoveFireStyle = this.getFireStyle(level,blockPos);
        Direction direction=blockState.getValue(FACING);
        Direction facing=direction.getClockWise();
        if(randomSource.nextFloat() < 0.48f) {
            ArcParticle arcParticle=this.arcParticle(randomSource);
            double frontOffset=0.518;
            double flameX=x + direction.getStepX() * frontOffset + facing.getStepX() * arcParticle.horizontal();
            double flameY=y + arcParticle.vertical();
            double flameZ=z + direction.getStepZ() * frontOffset + facing.getStepZ() * arcParticle.horizontal();
            this.spawnFlame(level,flameX,flameY,flameZ,modularStoveFireStyle,level.getGameTime(),randomSource.nextInt());
            if(randomSource.nextFloat() < 0.16f) level.addParticle(ParticleTypes.SMOKE,flameX,flameY + 0.025,flameZ,0.0,0.0015,0.0);
        }
    }

    private ArcParticle arcParticle(RandomSource randomSource) {
        double verticalPixel=1.0 + randomSource.nextDouble() * 7.2;
        double maxHorizontalPixel;
        if(verticalPixel < 6.0) {
            maxHorizontalPixel=3.15;
        } else if (verticalPixel < 8.0) {
            maxHorizontalPixel=2.15;
        } else {
            maxHorizontalPixel=1.15;
        }
        double horizontalPixel=(randomSource.nextDouble() * 2.0 - 1.0) * maxHorizontalPixel;
        return new ArcParticle(horizontalPixel / 16.0,verticalPixel / 16.0);
    }

    @Override
    public void playerWillDestroy(Level level,BlockPos blockPos,BlockState blockState,Player player) {
        if(!level.isClientSide && !player.getAbilities().instabuild) {
            BlockEntity blockEntity=level.getBlockEntity(blockPos);
            if(blockEntity instanceof ModularStoveBlockEntity modularStoveBlockEntity) modularStoveBlockEntity.dropInstallMaterial();
        }
        super.playerWillDestroy(level,blockPos,blockState,player);
    }

    private void spawnFlame(Level level,double x,double y,double z,ModularStoveFireStyle modularStoveFireStyle,long tick,int seed) {
        int color;
        if(modularStoveFireStyle == ModularStoveFireStyle.RAINBOW) {
            color=modularStoveFireStyle.particleColor(tick,seed);
        } else {
            color=modularStoveFireStyle.firstColor();
        }
        double red=((color >> 16) & 255) / 255.0;
        double green=((color >> 8) & 255) / 255.0;
        double blue=(color & 255) / 255.0;
        level.addParticle(ModParticleTypes.MODULAR_FLAME.get(),x,y,z,red,green,blue);
    }

    private ModularStoveFireStyle getFireStyle(Level level,BlockPos blockPos) {
        BlockEntity blockEntity=level.getBlockEntity(blockPos);
        if(blockEntity instanceof ModularStoveBlockEntity modularStoveBlockEntity) return ModularStoveFireStyle.fromId(modularStoveBlockEntity.getFurnitureAppearance().style());
        return ModularStoveFireStyle.ORANGE;
    }

    private record ArcParticle(double horizontal,double vertical) {

    }
}
