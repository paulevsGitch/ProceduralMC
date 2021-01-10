package paulevs.proceduralmc.material;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import paulevs.proceduralmc.CreativeTabs;
import paulevs.proceduralmc.InnerRegistry;
import paulevs.proceduralmc.namegen.NameGenerator;
import paulevs.proceduralmc.recipe.FurnaceRecipe;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.texturing.ColorGradient;
import paulevs.proceduralmc.texturing.CustomColor;
import paulevs.proceduralmc.texturing.ProceduralTextures;
import paulevs.proceduralmc.utils.ModelHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class GemOreMaterial extends OreMaterial {
	private static BufferTexture[] oreVeins;
	private static BufferTexture[] gems;
	
	public final Item gem;
	
	public GemOreMaterial(Random random) {
		super(random);
		String regName = this.name.toLowerCase();
		gem = InnerRegistry.registerItem(regName + "_gem", new Item(new Settings().group(CreativeTabs.ITEMS)));
		drop = gem;
		
		FurnaceRecipe.make(regName, ore, gem).setXP(MathHelper.floor(random.nextFloat() * 10) / 10F).build();
	}

	@Override
	protected void initClientCustom(Random random) {
		loadStaticImages();
		String regName = this.name.toLowerCase();
		
		ColorGradient gradient = ProceduralTextures.makeGemPalette(random);
		
		Identifier textureID = TextureHelper.makeItemTextureID(regName + "_ore");
		BufferTexture texture = ProceduralTextures.randomColored(oreVeins, gradient, random);
		BufferTexture outline = TextureHelper.outline(texture, new CustomColor(104, 104, 104), new CustomColor(143, 143, 143), 0, 1);
		texture = TextureHelper.cover(stone, texture);
		texture = TextureHelper.cover(texture, outline);
		InnerRegistry.registerTexture(textureID, texture);
		
		InnerRegistry.registerItemModel(this.ore.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.ore, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(regName + "_ore"), this.name + " Ore");
		
		texture = ProceduralTextures.randomColored(gems, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_gem");
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.gem, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_gem"), this.name + " Gem");
	}
	
	private void loadStaticImages() {
		if (oreVeins == null) {
			oreVeins = new BufferTexture[10];
			for (int i = 0; i < oreVeins.length; i++) {
				oreVeins[i] = TextureHelper.loadTexture("textures/block/ore/gem/ore_gem_" + i + ".png");
				TextureHelper.normalize(oreVeins[i], 0.35F, 1F);
			}
			
			gems = new BufferTexture[13];
			for (int i = 0; i < gems.length; i++) {
				gems[i] = TextureHelper.loadTexture("textures/item/gems/gem_" + i + ".png");
				TextureHelper.normalize(gems[i]);
			}
		}
	}
}
