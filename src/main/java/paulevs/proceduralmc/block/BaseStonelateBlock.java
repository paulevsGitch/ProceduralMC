package paulevs.proceduralmc.block;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

public class BaseStonelateBlock extends PressurePlateBlock {
	public BaseStonelateBlock(Block source) {
		super(ActivationRule.MOBS, FabricBlockSettings.copyOf(source).nonOpaque());
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
}
