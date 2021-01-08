package paulevs.proceduralmc;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import paulevs.proceduralmc.material.ComplexMaterial;
import paulevs.proceduralmc.material.StoneMaterial;
import paulevs.proceduralmc.namegen.NameGenerator;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.texturing.ColorGragient;
import paulevs.proceduralmc.texturing.CustomColor;
import paulevs.proceduralmc.utils.ModelHelper;
import paulevs.proceduralmc.utils.SilentWorldReloader;
import paulevs.proceduralmc.utils.TagHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class ProceduralMC implements ModInitializer {
	public static final String MOD_ID = "proceduralmc";
	private static final Random RANDOM = new Random();
	private static boolean register = true;
	private static long seed = Long.MIN_VALUE;
	
	@Override
	public void onInitialize() {
		NameGenerator.init();
	}
	
	public static Identifier makeID(String name) {
		return new Identifier(MOD_ID, name);
	}
	
	public static boolean isFromMod(Identifier id) {
		return id.getNamespace().equals(MOD_ID);
	}
	
	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
	
	public static void onServerStart(ServerWorld world) {
		if (register && seed != world.getSeed()) {
			register = false;
			seed = world.getSeed();
			
			InnerRegistry.clearRegistries();
			TagHelper.clearTags();
			StoneMaterial.resetMaterials();
			
			RANDOM.setSeed(seed);
			List<ComplexMaterial> materials = Lists.newArrayList();
			
			for (int i = 0; i < 10; i++) {
				StoneMaterial material = new StoneMaterial(RANDOM);
				materials.add(material);
			}
			
			Item item = InnerRegistry.registerItem(makeID("test_hue1"), new Item(new Settings().group(CreativeTabs.ITEMS)));
			Item item2 = InnerRegistry.registerItem(makeID("test_hue2"), new Item(new Settings().group(CreativeTabs.ITEMS)));
			Identifier texture = TextureHelper.makeItemTextureID("test_hue1");
			Identifier texture2 = TextureHelper.makeItemTextureID("test_hue2");
			
			NativeImage stoneTex = TextureHelper.loadImage("textures/item/ingot.png");
			
			CustomColor start = new CustomColor(61, 37, 50).switchToHSV();
			CustomColor end = new CustomColor(246, 161, 40).switchToHSV();
			CustomColor start2 = new CustomColor(61, 37, 50);
			CustomColor end2 = new CustomColor(246, 161, 40);
			
			ColorGragient gradient = new ColorGragient(start, end);
			ColorGragient gradient2 = new ColorGragient(start2, end2);
			
			BufferTexture image = new BufferTexture(16, 16);
			BufferTexture image2 = new BufferTexture(16, 16);
			
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					CustomColor color = TextureHelper.getFromTexture(stoneTex, x, y);
					image.setPixel(x, y, gradient.getColor(color.getRed()).setAlpha(color.getAlpha()));
					image2.setPixel(x, y, gradient2.getColor(color.getRed()).setAlpha(color.getAlpha()));
				}
			}
			
			InnerRegistry.registerTexture(texture, image);
			InnerRegistry.registerTexture(texture2, image2);
			InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(texture));
			InnerRegistry.registerItemModel(item2, ModelHelper.makeFlatItem(texture2));
			
			world.getServer().reloadResources(world.getServer().getDataPackManager().getEnabledNames());
			
			RANDOM.setSeed(seed);
			if (isClient()) {
				ModelHelper.clearModels();
				
				materials.forEach((material) -> {
					material.initClient(RANDOM);
				});
				
				//SilentWorldReloader.setSilent();
				MinecraftClient.getInstance().reloadResources();
			}
		}
	}
	
	public static void onServerStop() {
		register = true;
	}
}
