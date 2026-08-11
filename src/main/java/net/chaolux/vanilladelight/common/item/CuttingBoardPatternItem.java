package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.common.block.CuttingBoardPattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CuttingBoardPatternItem extends Item {
    private final CuttingBoardPattern cuttingBoardPattern;

    public CuttingBoardPatternItem(CuttingBoardPattern boardPattern,Properties properties) {
        super(properties);
        this.cuttingBoardPattern=boardPattern;
    }

    public CuttingBoardPattern cuttingBoardPattern() {
        return this.cuttingBoardPattern;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> componentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack,tooltipContext,componentList,tooltipFlag);
        componentList.add(Component.empty());
        componentList.add(Component.translatable("tooltip.vanilladelight.cutting_board_pattern.pattern").withStyle(ChatFormatting.GRAY));
        componentList.add(Component.translatable(this.cuttingBoardPattern.translationKey()).withStyle(ChatFormatting.BLUE));
        componentList.add(Component.empty());
        componentList.add(Component.translatable("tooltip.vanilladelight.cutting_board_pattern.apply").withStyle(ChatFormatting.GRAY));
        componentList.add(Component.translatable("block.vanilladelight.modular_cutting_board").withStyle(ChatFormatting.BLUE));
    }
}
