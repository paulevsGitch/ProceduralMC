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
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InnerRegistry {
	private static final Map<BlockState, JsonUnbakedModel> BLOCK_MODELS = Maps.newHashMap();
	private static final Map<Item, JsonUnbakedModel> ITEM_MODELS = Maps.newHashMap();
	private static final Map<Identifier, BufferTexture> TEXTURES = Maps.newHashMap();
	private static final Map<Identifier, Block> BLOCKS = Maps.newHashMap();
	private static final Set<Identifier> MODELED = Sets.newHashSet();
	
	public static void registerBlock(Identifier id, Block block) {
		BLOCKS.put(id, block);
	}
	
	public static void registerTexture(Identifier id, BufferTexture image) {
		TEXTURES.put(id, image);
	}
	
	public static void registerBlockModel(BlockState state, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		Identifier id = Registry.BLOCK.getId(state.getBlock());
		model.id = BlockModels.getModelId(id, state).toString();
		BLOCK_MODELS.put(state, model);
		MODELED.add(id);
	}
	
	public static void registerBlockModel(Block block, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		Identifier id = Registry.BLOCK.getId(block);
		model.id = id.toString();
		BLOCK_MODELS.put(block.getDefaultState(), model);
		MODELED.add(id);
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
	
	public static JsonUnbakedModel getModel(BlockState state) {
		return BLOCK_MODELS.get(state);
	}
	
	public static JsonUnbakedModel getModel(Item item) {
		return ITEM_MODELS.get(item);
	}
	
	public static void iterateTextures(BiConsumer<? super Identifier, ? super BufferTexture> consumer) {
		TEXTURES.forEach(consumer);
	}
	
	public static boolean hasCustomModel(Identifier id) {
		return MODELED.contains(id);
	}
}
