package paulevs.proceduralmc;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import paulevs.proceduralmc.namegen.NameGenerator;
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
		if (register) {
			register = false;
			
			InnerRegistry.clearRegistries();
			Random random = new Random(world.getSeed());
			
			Set<Identifier> ids = Sets.newHashSet();
			Set<String> names = Sets.newHashSet();
			for (int i = 0; i < 10; i++) {
				String name = NameGenerator.makeOreName(random, names);
				names.add(name);
				Identifier id = makeID(name.toLowerCase());
				String orename = id.getNamespace() + "." + id.getPath();
				NameGenerator.addTranslation("block." + orename, name);
				NameGenerator.addTranslation("item." + orename + "_ingot", name + " Ingot");
				ids.add(id);
			}
			
			ids.forEach((id) -> {
				makeRandomOreServer(id);
			});
			
			if (isClient()) {
				NativeImage stone = TextureHelper.loadImage("minecraft", "textures/block/stone.png");
				NativeImage oreCommon = TextureHelper.loadImage("textures/block/common_ore.png");
				NativeImage oreRubirald = TextureHelper.loadImage("textures/block/rubirald_ore.png");
				NativeImage ingot = TextureHelper.loadImage("textures/item/ingot.png");
				
				ids.forEach((id) -> {
					makeRandomOreClient(id, random, ingot, stone, oreCommon, oreRubirald);
				});
				
				SilentWorldReloader.setSilent();
				MinecraftClient.getInstance().reloadResources();
			}
		}
	}
	
	public static void onServerStop() {
		register = true;
	}
	
	private static void makeRandomOreServer(Identifier id) {
		Block testblock = new Block(FabricBlockSettings.copyOf(Blocks.STONE)) {
			@Override
			public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
				ItemStack tool = builder.get(LootContextParameters.TOOL);
				if (tool != null && tool.isEffectiveOn(state)) {
					return Collections.singletonList(new ItemStack(this));
				}
				return Collections.emptyList();
			}
		};
		Item blockItem = new BlockItem(testblock, new Settings().group(ItemGroup.BUILDING_BLOCKS));
		InnerRegistry.registerBlock(id, testblock);
		InnerRegistry.registerItem(id, blockItem);
		
		Identifier ingot = makeID(id.getPath() + "_ingot");
		Item item = new Item(new Settings().group(ItemGroup.MATERIALS));
		InnerRegistry.registerItem(ingot, item);
	}
	
	private static void makeRandomOreClient(Identifier id, Random random, NativeImage ingot, NativeImage stone, NativeImage... ores) {
		float red = random.nextFloat();
		float green = random.nextFloat();
		float blue = random.nextFloat();
		CustomColor dark = new CustomColor(red - 0.2F, green - 0.2F, blue - 0.2F);
		CustomColor bright = new CustomColor(red + 0.2F, green + 0.2F, blue + 0.2F);
		ColorGragient gradient = new ColorGragient(dark, bright);
		
		BufferTexture texture = new BufferTexture(16, 16);
		NativeImage ore = ores[random.nextInt(ores.length)];
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
		
		texture = new BufferTexture(16, 16);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(ingot, x, y);
				if (color.getA() > 0) {
					color = gradient.getColor(color.getR());
				}
				texture.setPixel(x, y, color);
			}
		}
		
		Identifier ingotID = makeID(id.getPath() + "_ingot");
		textureID = TextureHelper.makeItemTextureID(ingotID.getPath());
		InnerRegistry.registerItemModel(Registry.ITEM.get(ingotID), ModelHelper.makeFlatItem(textureID));
		InnerRegistry.registerTexture(textureID, texture);
	}
}
