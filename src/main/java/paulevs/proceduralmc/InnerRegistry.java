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
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InnerRegistry {
	private static final Map<BlockState, JsonUnbakedModel> MODELS = Maps.newHashMap();
	private static final Map<Identifier, NativeImage> TEXTURES = Maps.newHashMap();
	private static final Map<Identifier, Block> BLOCKS = Maps.newHashMap();
	private static final Set<Identifier> MODELED = Sets.newHashSet();
	
	public static void registerBlock(Identifier id, Block block) {
		BLOCKS.put(id, block);
	}
	
	public static void registerTexture(Identifier id, NativeImage image) {
		TEXTURES.put(id, image);
	}
	
	public static void registerBlockModel(BlockState state, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		Identifier id = Registry.BLOCK.getId(state.getBlock());
		model.id = BlockModels.getModelId(id, state).toString();
		MODELS.put(state, model);
		MODELED.add(id);
	}
	
	public static void registerBlockModel(Block block, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		Identifier id = Registry.BLOCK.getId(block);
		model.id = id.toString();
		MODELS.put(block.getDefaultState(), model);
		MODELED.add(id);
	}
	
	public static Collection<Identifier> getTextureIDs() {
		return TEXTURES.keySet();
	}
	
	public static NativeImage getTexture(Identifier id) {
		return TEXTURES.get(id);
	}
	
	public static JsonUnbakedModel getModel(BlockState state) {
		return MODELS.get(state);
	}
	
	public static void iterateTextures(BiConsumer<? super Identifier, ? super NativeImage> consumer) {
		TEXTURES.forEach(consumer);
	}
	
	public static boolean hasCustomModel(Identifier id) {
		return MODELED.contains(id);
	}
}
