package paulevs.proceduralmc.material;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import paulevs.proceduralmc.CreativeTabs;
import paulevs.proceduralmc.InnerRegistry;
import paulevs.proceduralmc.block.BaseDropBlock;
import paulevs.proceduralmc.namegen.NameGenerator;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.utils.TextureHelper;

public abstract class OreMaterial extends ComplexMaterial {
	protected static BufferTexture stone;
	
	public final Block ore;
	protected Item drop;
	
	public final String name;
	
	public OreMaterial(Random random) {
		super(random);
		this.name = NameGenerator.makeOreName(random);
		String regName = this.name.toLowerCase() + "_ore";
		
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.STONE).materialColor(MaterialColor.GRAY);
		ore = InnerRegistry.registerBlockAndItem(regName, new BaseDropBlock(material, () -> { return drop; }), CreativeTabs.BLOCKS);
		drop = ore.asItem();
	}
	
	protected abstract void initClientCustom(Random random);
	
	@Override
	public void initClient(Random random) {
		loadStaticImages();
		initClientCustom(random);
	}
	
	private void loadStaticImages() {
		if (stone == null) {
			stone = TextureHelper.loadTexture("minecraft", "textures/block/stone.png");
		}
	}
}
