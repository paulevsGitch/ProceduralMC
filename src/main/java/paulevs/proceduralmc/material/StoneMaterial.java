package paulevs.proceduralmc.material;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import paulevs.proceduralmc.InnerRegistry;
import paulevs.proceduralmc.ProceduralMC;
import paulevs.proceduralmc.block.BaseBlock;
import paulevs.proceduralmc.block.BasePillarBlock;
import paulevs.proceduralmc.block.BaseSlabBlock;
import paulevs.proceduralmc.block.BaseStairsBlock;
import paulevs.proceduralmc.block.BaseStoneButtonBlock;
import paulevs.proceduralmc.block.BaseStonelateBlock;
import paulevs.proceduralmc.block.BaseWallBlock;
import paulevs.proceduralmc.namegen.NameGenerator;
import paulevs.proceduralmc.recipe.GridRecipe;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.texturing.CustomColor;
import paulevs.proceduralmc.utils.ModelHelper;
import paulevs.proceduralmc.utils.TagHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class StoneMaterial extends ComplexMaterial {
	public final Block stone;
	
	public final Block polished;
	public final Block tiles;
	public final Block pillar;
	public final Block stairs;
	public final Block slab;
	public final Block wall;
	public final Block button;
	public final Block pressure_plate;
	
	public final Block bricks;
	public final Block brick_stairs;
	public final Block brick_slab;
	public final Block brick_wall;
	
	public final String name;
	
	public StoneMaterial(String name, Random random) {
		super(random);
		this.name = name;
		name = name.toLowerCase();
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.STONE).materialColor(MaterialColor.GRAY);
		
		stone = InnerRegistry.registerBlockAndItem(name, new BaseBlock(material), ItemGroup.BUILDING_BLOCKS);
		polished = InnerRegistry.registerBlockAndItem(name + "_polished", new BaseBlock(material), ItemGroup.BUILDING_BLOCKS);
		tiles = InnerRegistry.registerBlockAndItem(name + "_tiles", new BaseBlock(material), ItemGroup.BUILDING_BLOCKS);
		pillar = InnerRegistry.registerBlockAndItem(name + "_pillar", new BasePillarBlock(material), ItemGroup.BUILDING_BLOCKS);
		stairs = InnerRegistry.registerBlockAndItem(name + "_stairs", new BaseStairsBlock(stone), ItemGroup.BUILDING_BLOCKS);
		slab = InnerRegistry.registerBlockAndItem(name + "_slab", new BaseSlabBlock(stone), ItemGroup.BUILDING_BLOCKS);
		wall = InnerRegistry.registerBlockAndItem(name + "_wall", new BaseWallBlock(stone), ItemGroup.BUILDING_BLOCKS);
		button = InnerRegistry.registerBlockAndItem(name + "_button", new BaseStoneButtonBlock(stone), ItemGroup.BUILDING_BLOCKS);
		pressure_plate = InnerRegistry.registerBlockAndItem(name + "_plate", new BaseStonelateBlock(stone), ItemGroup.BUILDING_BLOCKS);
		
		bricks = InnerRegistry.registerBlockAndItem(name + "_bricks", new BaseBlock(material), ItemGroup.BUILDING_BLOCKS);
		brick_stairs = InnerRegistry.registerBlockAndItem(name + "_bricks_stairs", new BaseStairsBlock(bricks), ItemGroup.BUILDING_BLOCKS);
		brick_slab = InnerRegistry.registerBlockAndItem(name + "_bricks_slab", new BaseSlabBlock(bricks), ItemGroup.BUILDING_BLOCKS);
		brick_wall = InnerRegistry.registerBlockAndItem(name + "_bricks_wall", new BaseWallBlock(bricks), ItemGroup.BUILDING_BLOCKS);
		
		// Recipes //
		GridRecipe.make(name + "_bricks", bricks).setOutputCount(4).setShape("##", "##").addMaterial('#', stone).setGroup("end_bricks").build();
		GridRecipe.make(name + "_polished", polished).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("end_tile").build();
		GridRecipe.make(name + "_tiles", tiles).setOutputCount(4).setShape("##", "##").addMaterial('#', polished).setGroup("end_small_tile").build();
		GridRecipe.make(name + "_pillar", pillar).setShape("#", "#").addMaterial('#', slab).setGroup("end_pillar").build();
		
		GridRecipe.make(name + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', stone).setGroup("end_stone_stairs").build();
		GridRecipe.make(name + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', stone).setGroup("end_stone_slabs").build();
		GridRecipe.make(name + "_bricks_stairs", brick_stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("end_stone_stairs").build();
		GridRecipe.make(name + "_bricks_slab", brick_slab).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("end_stone_slabs").build();
		
		GridRecipe.make(name + "_wall", wall).setOutputCount(6).setShape("###", "###").addMaterial('#', stone).setGroup("end_wall").build();
		GridRecipe.make(name + "_bricks_wall", brick_wall).setOutputCount(6).setShape("###", "###").addMaterial('#', bricks).setGroup("end_wall").build();
		
		GridRecipe.make(name + "_button", button).setList("#").addMaterial('#', stone).setGroup("end_stone_buttons").build();
		GridRecipe.make(name + "_pressure_plate", pressure_plate).setShape("##").addMaterial('#', stone).setGroup("end_stone_plates").build();
		
		// Item Tags //
		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone);
		
		// Block Tags //
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.WALLS, wall, brick_wall);
		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
		TagHelper.addTags(pressure_plate, BlockTags.PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);
	}
	
	@Override
	public void initClient(Random random) {
		NativeImage stoneTex = TextureHelper.loadImage("minecraft", "textures/block/stone.png");
		NativeImage cobblTex = TextureHelper.loadImage("minecraft", "textures/block/cobblestone.png");
		
		BufferTexture texture = new BufferTexture(16, 16);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(stoneTex, x, y);
				texture.setPixel(x, y, color);
			}
		}
		
		String textureBaseName = name.toLowerCase();
		String mainName = ProceduralMC.MOD_ID + "." + textureBaseName;
		Identifier stoneTextureID = TextureHelper.makeBlockTextureID(textureBaseName);
		InnerRegistry.registerTexture(stoneTextureID, texture);
		
		texture = new BufferTexture(16, 16);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(cobblTex, x, y);
				texture.setPixel(x, y, color);
			}
		}
		Identifier pillarTopTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_pillar_top");
		InnerRegistry.registerTexture(pillarTopTexID, texture);
		
		ModelHelper.registerSimpleBlockModel(stone, stoneTextureID);
		NameGenerator.addTranslation("block." + mainName, name);
		
		ModelHelper.registerSimpleBlockModel(polished, stoneTextureID);
		NameGenerator.addTranslation("block." + mainName + "_polished", name);
		
		ModelHelper.registerSimpleBlockModel(bricks, stoneTextureID);
		NameGenerator.addTranslation("block." + mainName + "_bricks", name);
		
		ModelHelper.registerSimpleBlockModel(tiles, stoneTextureID);
		NameGenerator.addTranslation("block." + mainName + "_tiles", name);
		
		ModelHelper.registerPillarBlock(pillar, pillarTopTexID, stoneTextureID);
		NameGenerator.addTranslation("block." + mainName + "_pillar", name);
	}
}
