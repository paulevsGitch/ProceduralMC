package paulevs.proceduralmc.block;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

public class BaseSlabBlock extends SlabBlock {
	public BaseSlabBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
}
