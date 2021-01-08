package paulevs.proceduralmc;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import paulevs.proceduralmc.namegen.NameGenerator;

public class CreativeTabs {
	public static final ItemGroup BLOCKS = makeGroup("Random Blocks", Blocks.STONE);
	public static final ItemGroup ITEMS = makeGroup("Random Items", Items.GOLD_INGOT);
	
	private static ItemGroup makeGroup(String name, ItemConvertible icon) {
		String idString = name.toLowerCase().replace(" ", "_");
		if (ProceduralMC.isClient()) {
			NameGenerator.addTranslation("itemGroup." + ProceduralMC.MOD_ID + "." + idString, name);
		}
		return FabricItemGroupBuilder.create(ProceduralMC.makeID(idString)).icon(() -> {
			return new ItemStack(icon);
		}).build();
	}
}
