package paulevs.proceduralmc.block;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

public class BasePillarBlock extends PillarBlock {
	public BasePillarBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
}
