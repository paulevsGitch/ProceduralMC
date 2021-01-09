package paulevs.proceduralmc.material;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.Identifier;
import paulevs.proceduralmc.CreativeTabs;
import paulevs.proceduralmc.InnerRegistry;
import paulevs.proceduralmc.ProceduralMC;
import paulevs.proceduralmc.block.BaseBlock;
import paulevs.proceduralmc.namegen.NameGenerator;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.texturing.ColorGradient;
import paulevs.proceduralmc.texturing.CustomColor;
import paulevs.proceduralmc.texturing.ProceduralTextures;
import paulevs.proceduralmc.utils.ModelHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class StoneMaterial extends ComplexMaterial {
	private static BufferTexture stoneFrame;
	private static BufferTexture stoneBricks;
	private static BufferTexture stoneTiles;
	
	public final Block stone;
	
	public final Block polished;
	public final Block bricks;
	public final Block tiles;
	
	/*public final Block pillar;
	public final Block stairs;
	public final Block slab;
	public final Block wall;
	public final Block button;
	public final Block pressure_plate;
	
	public final Block bricks;
	public final Block brick_stairs;
	public final Block brick_slab;
	public final Block brick_wall;*/
	
	public final String name;
	
	public StoneMaterial(Random random) {
		super(random);
		this.name = NameGenerator.makeRockName(random);
		String regName = this.name.toLowerCase();
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.STONE).materialColor(MaterialColor.GRAY);
		
		stone = InnerRegistry.registerBlockAndItem(regName, new BaseBlock(material), CreativeTabs.BLOCKS);
		polished = InnerRegistry.registerBlockAndItem(regName + "_polished", new BaseBlock(material), CreativeTabs.BLOCKS);
		tiles = InnerRegistry.registerBlockAndItem(regName + "_tiles", new BaseBlock(material), CreativeTabs.BLOCKS);
		bricks = InnerRegistry.registerBlockAndItem(regName + "_bricks", new BaseBlock(material), CreativeTabs.BLOCKS);
		
		/*pillar = InnerRegistry.registerBlockAndItem(regName + "_pillar", new BasePillarBlock(material), CreativeTabs.BLOCKS);
		stairs = InnerRegistry.registerBlockAndItem(regName + "_stairs", new BaseStairsBlock(stone), CreativeTabs.BLOCKS);
		slab = InnerRegistry.registerBlockAndItem(regName + "_slab", new BaseSlabBlock(stone), CreativeTabs.BLOCKS);
		wall = InnerRegistry.registerBlockAndItem(regName + "_wall", new BaseWallBlock(stone), CreativeTabs.BLOCKS);
		button = InnerRegistry.registerBlockAndItem(regName + "_button", new BaseStoneButtonBlock(stone), CreativeTabs.BLOCKS);
		pressure_plate = InnerRegistry.registerBlockAndItem(regName + "_plate", new BaseStonelateBlock(stone), CreativeTabs.BLOCKS);
		
		
		brick_stairs = InnerRegistry.registerBlockAndItem(regName + "_bricks_stairs", new BaseStairsBlock(bricks), CreativeTabs.BLOCKS);
		brick_slab = InnerRegistry.registerBlockAndItem(regName + "_bricks_slab", new BaseSlabBlock(bricks), CreativeTabs.BLOCKS);
		brick_wall = InnerRegistry.registerBlockAndItem(regName + "_bricks_wall", new BaseWallBlock(bricks), CreativeTabs.BLOCKS);*/
		
		// Recipes //
		/*GridRecipe.make(regName + "_bricks", bricks).setOutputCount(4).setShape("##", "##").addMaterial('#', stone).setGroup("end_bricks").build();
		GridRecipe.make(regName + "_polished", polished).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("end_tile").build();
		GridRecipe.make(regName + "_tiles", tiles).setOutputCount(4).setShape("##", "##").addMaterial('#', polished).setGroup("end_small_tile").build();
		GridRecipe.make(regName + "_pillar", pillar).setShape("#", "#").addMaterial('#', slab).setGroup("end_pillar").build();
		
		GridRecipe.make(regName + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', stone).setGroup("end_stone_stairs").build();
		GridRecipe.make(regName + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', stone).setGroup("end_stone_slabs").build();
		GridRecipe.make(regName + "_bricks_stairs", brick_stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("end_stone_stairs").build();
		GridRecipe.make(regName + "_bricks_slab", brick_slab).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("end_stone_slabs").build();
		
		GridRecipe.make(regName + "_wall", wall).setOutputCount(6).setShape("###", "###").addMaterial('#', stone).setGroup("end_wall").build();
		GridRecipe.make(regName + "_bricks_wall", brick_wall).setOutputCount(6).setShape("###", "###").addMaterial('#', bricks).setGroup("end_wall").build();
		
		GridRecipe.make(regName + "_button", button).setList("#").addMaterial('#', stone).setGroup("end_stone_buttons").build();
		GridRecipe.make(regName + "_pressure_plate", pressure_plate).setShape("##").addMaterial('#', stone).setGroup("end_stone_plates").build();
		
		// Item Tags //
		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone);
		
		// Block Tags //
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.WALLS, wall, brick_wall);
		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
		TagHelper.addTags(pressure_plate, BlockTags.PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);*/
	}
	
	@Override
	public void initClient(Random random) {
		loadStaticImages();
		
		String textureBaseName = name.toLowerCase();
		String mainName = ProceduralMC.MOD_ID + "." + textureBaseName;
		
		// Texture Genearation
		CustomColor mainColor = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
		ColorGradient palette = ProceduralTextures.makeStonePalette(mainColor, random);
		
		Identifier stoneTexID = TextureHelper.makeBlockTextureID(textureBaseName);
		BufferTexture texture = ProceduralTextures.makeStoneTexture(palette, random);
		InnerRegistry.registerTexture(stoneTexID, texture);
		
		texture = ProceduralTextures.makeBluredTexture(texture);
		
		BufferTexture variant = ProceduralTextures.coverWithOverlay(texture, stoneFrame, palette);
		Identifier frameTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_frame");
		InnerRegistry.registerTexture(frameTexID, variant);
		
		variant = ProceduralTextures.coverWithOverlay(texture, stoneBricks, palette);
		Identifier bricksTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_bricks");
		InnerRegistry.registerTexture(bricksTexID, variant);
		
		variant = ProceduralTextures.coverWithOverlay(texture, stoneTiles, palette);
		Identifier tilesTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_tiles");
		InnerRegistry.registerTexture(tilesTexID, variant);
		
		// Registering models
		ModelHelper.registerRandMirrorBlockModel(stone, stoneTexID);
		NameGenerator.addTranslation("block." + mainName, name);
		
		ModelHelper.registerSimpleBlockModel(polished, frameTexID);
		NameGenerator.addTranslation("block." + mainName + "_polished", name);
		
		ModelHelper.registerSimpleBlockModel(bricks, bricksTexID);
		NameGenerator.addTranslation("block." + mainName + "_bricks", name);
		
		ModelHelper.registerSimpleBlockModel(tiles, tilesTexID);
		NameGenerator.addTranslation("block." + mainName + "_tiles", name);
		
		/*ModelHelper.registerPillarBlock(pillar, pillarTopTexID, stoneTextureID);
		NameGenerator.addTranslation("block." + mainName + "_pillar", name);*/
	}
	
	private void loadStaticImages() {
		if (stoneFrame == null) {
			stoneFrame = TextureHelper.loadTexture("textures/block/stone_frame_01.png");
			stoneBricks = TextureHelper.loadTexture("textures/block/stone_bricks_01.png");
			stoneTiles = TextureHelper.loadTexture("textures/block/stone_tiles_01.png");
		}
	}
}
