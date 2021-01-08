package paulevs.proceduralmc.utils;

import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.registry.Registry;
import paulevs.proceduralmc.InnerRegistry;

public class ModelHelper {
	public static final Map<Item, ModelIdentifier> MODELS = Maps.newHashMap();
	
	public static void clearModels() {
		MODELS.clear();
	}
	
	public static String makeCube(Identifier texture) {
		return String.format("{\"parent\": \"minecraft:block/cube_all\", \"textures\": {\"all\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}
	
	public static String makePillar(Identifier textureTop, Identifier textureSide) {
		return String.format("{\"parent\":\"minecraft:block/cube_column\",\"textures\":{\"end\":\"%s:%s\",\"side\":\"%s:%s\"}}", textureTop.getNamespace(), textureTop.getPath(), textureSide.getNamespace(), textureSide.getPath());
	}
	
	public static String makeFlatItem(Identifier texture) {
		return String.format("{\"parent\": \"minecraft:item/generated\",\"textures\":{\"layer0\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}
	
	public static void registerSimpleBlockModel(Block block, Identifier texture) {
		InnerRegistry.registerBlockModel(block, makeCube(texture));
		InnerRegistry.registerItemModel(block.asItem(), makeCube(texture));
	}
	
	public static void registerPillarBlock(Block block, Identifier textureTop, Identifier textureSide) {
		String json = makePillar(textureTop, textureSide);
		
		Identifier id = Registry.BLOCK.getId(block);
		Identifier model = new Identifier(id.getNamespace(), "block/" + id.getPath());
		InnerRegistry.registerModel(model, json);
		
		ModelVariant variant = new ModelVariant(model, AffineTransformation.identity(), false, 1);
		WeightedUnbakedModel side = new WeightedUnbakedModel(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.getDefaultState().with(PillarBlock.AXIS, Axis.Y), side);
		
		variant = new ModelVariant(model, new AffineTransformation(null, Vector3f.POSITIVE_Z.getDegreesQuaternion(90), null, null), false, 1);
		side = new WeightedUnbakedModel(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.getDefaultState().with(PillarBlock.AXIS, Axis.X), side);
		
		variant = new ModelVariant(model, new AffineTransformation(null, Vector3f.POSITIVE_X.getDegreesQuaternion(90), null, null), false, 1);
		side = new WeightedUnbakedModel(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.getDefaultState().with(PillarBlock.AXIS, Axis.Z), side);
		
		InnerRegistry.registerItemModel(block.asItem(), json);
	}
}
