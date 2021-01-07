package paulevs.proceduralmc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ProceduralMC implements ModInitializer {
	public static final String MOD_ID = "proceduralmc";
	
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
		Identifier id = makeID("testblock");
		
		if (!Registry.BLOCK.containsId(id)) {
			Block testblock = new Block(FabricBlockSettings.copyOf(Blocks.STONE));
			Item blockItem = new BlockItem(testblock, new Settings().group(ItemGroup.BUILDING_BLOCKS));
			Registry.register(Registry.BLOCK, id, testblock);
			Registry.register(Registry.ITEM, id, blockItem);
			
			if (isClient()) {
				InnerRegistry.registerBlock(id, testblock);
				
				BufferTexture texture = new BufferTexture(16, 16);
				for (int x = 0; x < 16; x++) {
					int r = x << 4;
					for (int y = 0; y < 16; y++) {
						int g = y << 4;
						texture.setPixel(x, y, r, g, 128);
					}
				}
				
				Identifier textureID = TextureHelper.makeBlockTextureID("testblock");
				InnerRegistry.registerTexture(textureID, texture);
				InnerRegistry.registerBlockModel(testblock, ModelHelper.makeCube(textureID));
				InnerRegistry.registerItemModel(blockItem, ModelHelper.makeCube(textureID));
				
				SilentWorldReloader.setSilent();
				MinecraftClient.getInstance().reloadResources();
			}
		}
	}
	
	public static void onServerStop() {
		Identifier id = makeID("testblock");
		((ChangeableRegistry) Registry.BLOCK).remove(id);
		((ChangeableRegistry) Registry.BLOCK).recalculateLastID();
		((ChangeableRegistry) Registry.ITEM).remove(id);
		((ChangeableRegistry) Registry.ITEM).recalculateLastID();
		InnerRegistry.clearRegistries();
	}
}
