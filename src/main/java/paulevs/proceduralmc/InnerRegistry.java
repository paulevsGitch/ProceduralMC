package paulevs.proceduralmc;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import paulevs.proceduralmc.material.StoneMaterial;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.utils.ChangeableRegistry;
import paulevs.proceduralmc.utils.ModelHelper;

public class InnerRegistry {
	private static final Map<BlockState, UnbakedModel> BLOCK_MODELS = Maps.newHashMap();
	private static final Map<Item, JsonUnbakedModel> ITEM_MODELS = Maps.newHashMap();
	private static final Map<Identifier, JsonUnbakedModel> FREE_MODELS = Maps.newHashMap();
	private static final Map<Identifier, BufferTexture> TEXTURES = Maps.newHashMap();
	private static final Map<Identifier, Block> BLOCKS = Maps.newHashMap();
	private static final Map<Identifier, Item> ITEMS = Maps.newHashMap();
	private static final Set<Identifier> MODELED = Sets.newHashSet();
	
	public static void clearRegistries() {
		clearRegistry(Registry.BLOCK, BLOCKS.keySet());
		clearRegistry(Registry.ITEM, ITEMS.keySet());
		
		BLOCK_MODELS.clear();
		ITEM_MODELS.clear();
		TEXTURES.clear();
		BLOCKS.clear();
		MODELED.clear();
		
		ModelHelper.MODELS.clear();
		StoneMaterial.resetMaterials();
	}
	
	private static void clearRegistry(DefaultedRegistry<?> registry, Set<Identifier> ids) {
		ChangeableRegistry reg = (ChangeableRegistry) registry;
		ids.forEach((id) -> {
			reg.remove(id);
		});
		reg.recalculateLastID();
	}
	
	public static Block registerBlockAndItem(String name, Block block, ItemGroup group) {
		Identifier id = ProceduralMC.makeID(name);
		registerBlock(id, block);
		registerItem(id, new BlockItem(block, new Settings().group(group)));
		return block;
	}
	
	public static Block registerBlock(String name, Block block) {
		return registerBlock(ProceduralMC.makeID(name), block);
	}
	
	public static Block registerBlock(Identifier id, Block block) {
		Registry.register(Registry.BLOCK, id, block);
		BLOCKS.put(id, block);
		return block;
	}
	
	public static Item registerItem(Identifier id, Item item) {
		Registry.register(Registry.ITEM, id, item);
		ITEMS.put(id, item);
		return item;
	}
	
	public static Item registerItem(String name, Item block) {
		return registerItem(ProceduralMC.makeID(name), block);
	}
	
	public static BufferTexture registerTexture(Identifier id, BufferTexture image) {
		TEXTURES.put(id, image);
		return image;
	}
	
	public static void registerBlockModel(BlockState state, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		registerBlockModel(state, model);
	}
	
	public static void registerBlockModel(BlockState state, UnbakedModel model) {
		Identifier id = Registry.BLOCK.getId(state.getBlock());
		if (model instanceof JsonUnbakedModel) {
			((JsonUnbakedModel) model).id = BlockModels.getModelId(id, state).toString();
		}
		BLOCK_MODELS.put(state, model);
		MODELED.add(id);
	}
	
	public static void registerBlockModel(Block block, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		BlockState state = block.getDefaultState();
		registerBlockModel(state, model);
	}
	
	public static void registerModel(Identifier id, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		model.id = id.toString();
		FREE_MODELS.put(id, model);
	}
	
	public static void registerItemModel(Item item, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		Identifier id = Registry.ITEM.getId(item);
		model.id = id.getNamespace() + ":item/" + id.getPath();
		ITEM_MODELS.put(item, model);
		MODELED.add(id);
	}
	
	public static Collection<Identifier> getTextureIDs() {
		return TEXTURES.keySet();
	}
	
	public static BufferTexture getTexture(Identifier id) {
		return TEXTURES.get(id);
	}
	
	public static UnbakedModel getModel(BlockState state) {
		return BLOCK_MODELS.get(state);
	}
	
	public static JsonUnbakedModel getModel(Item item) {
		return ITEM_MODELS.get(item);
	}
	
	public static JsonUnbakedModel getModel(Identifier id) {
		return FREE_MODELS.get(id);
	}
	
	public static void iterateTextures(BiConsumer<? super Identifier, ? super BufferTexture> consumer) {
		TEXTURES.forEach(consumer);
	}
	
	public static boolean hasCustomModel(Identifier id) {
		return MODELED.contains(id);
	}

	public static Collection<Identifier> getItemIDs() {
		return ITEMS.keySet();
	}
}
