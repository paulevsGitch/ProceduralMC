package paulevs.proceduralmc.block;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

public class BaseDropBlock extends BaseBlock {
	private final Supplier<Item> drop;
	
	public BaseDropBlock(Settings settings, Supplier<Item> drop) {
		super(settings);
		this.drop = drop;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop.get()));
	}
}
