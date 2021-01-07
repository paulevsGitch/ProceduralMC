package paulevs.proceduralmc;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.texturing.ColorGragient;
import paulevs.proceduralmc.texturing.CustomColor;
import paulevs.proceduralmc.utils.ModelHelper;
import paulevs.proceduralmc.utils.SilentWorldReloader;
import paulevs.proceduralmc.utils.TextureHelper;

public class ProceduralMC implements ModInitializer {
	public static final String MOD_ID = "proceduralmc";
	private static boolean register = true;
	
	@Override
	public void onInitialize() {}
	
	public static Identifier makeID(String name) {
		return new Identifier(MOD_ID, name);
	}
	
	public static boolean isFromMod(Identifier id) {
		return id.getNamespace().equals(MOD_ID);
	}
	
	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
	
	public static void onServerStart() {
		if (register) {
			register = false;
			Random random = new Random(0);
			
			Set<Identifier> ids = Sets.newHashSet();
			for (int i = 0; i < 10; i++) {
				String name = Long.toString(random.nextLong());
				Identifier id = makeID(name);
				ids.add(id);
			}
			
			ids.forEach((id) -> {
				makeRandomOreServer(id);
			});
			
			if (isClient()) {
				NativeImage stone = TextureHelper.loadImage("minecraft", "textures/block/stone.png");
				NativeImage ore = TextureHelper.loadImage("textures/block/common_ore.png");
				
				ids.forEach((id) -> {
					makeRandomOreClient(id, random, stone, ore);
				});
				
				SilentWorldReloader.setSilent();
				MinecraftClient.getInstance().reloadResources();
			}
		}
	}
	
	public static void onServerStop() {
		InnerRegistry.clearRegistries();
		register = true;
	}
	
	private static void makeRandomOreServer(Identifier id) {
		Block testblock = new Block(FabricBlockSettings.copyOf(Blocks.STONE));
		Item blockItem = new BlockItem(testblock, new Settings().group(ItemGroup.BUILDING_BLOCKS));
		InnerRegistry.registerBlock(id, testblock);
		InnerRegistry.registerItem(id, blockItem);
	}
	
	private static void makeRandomOreClient(Identifier id, Random random, NativeImage stone, NativeImage ore) {
		CustomColor dark = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
		CustomColor bright = new CustomColor(dark.getR() + 0.25F, dark.getG() + 0.25F, dark.getB() + 0.25F);
		ColorGragient gradient = new ColorGragient(dark, bright);
		
		BufferTexture texture = new BufferTexture(16, 16);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(ore, x, y);
				float r = color.getR();
				float a = color.getA();
				color = TextureHelper.getFromTexture(stone, x, y);
				if (a > 0) {
					color = gradient.getColor(r);
				}
				texture.setPixel(x, y, color);
			}
		}
		
		Identifier textureID = TextureHelper.makeBlockTextureID(id.getPath());
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerBlockModel(Registry.BLOCK.get(id), ModelHelper.makeCube(textureID));
		InnerRegistry.registerItemModel(Registry.ITEM.get(id), ModelHelper.makeCube(textureID));
	}
}
