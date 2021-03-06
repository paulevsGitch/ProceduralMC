package paulevs.proceduralmc.utils;

import java.util.List;
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
import net.minecraft.util.math.Quaternion;
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
	
	public static String makeCubeMirrored(Identifier texture) {
		return String.format("{\"parent\": \"minecraft:block/cube_mirrored_all\", \"textures\": {\"all\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
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
	
	public static void registerRotatedBlockModel(Block block, Identifier texture) {
		String model = makeCube(texture);
		Identifier modelID = Registry.BLOCK.getId(block);
		modelID = new Identifier(modelID.getNamespace(), "block/" + modelID.getPath());
		InnerRegistry.registerModel(modelID, model);
		InnerRegistry.registerItemModel(block.asItem(), model);
		
		List<ModelVariant> variants = Lists.newArrayList();
		for (int x = 0; x < 360; x += 90) {
			for (int y = 0; y < 360; y += 90) {
				for (int z = 0; z < 360; z += 90) {
					Quaternion rotation = new Quaternion(x, y, z, true);
					AffineTransformation transform = new AffineTransformation(null, rotation, null, null);
					variants.add(new ModelVariant(modelID, transform, false, 1));
				}
			}
		}
		WeightedUnbakedModel wModel = new WeightedUnbakedModel(variants);
		InnerRegistry.registerBlockModel(block.getDefaultState(), wModel);
	}
	
	public static void registerRandMirrorBlockModel(Block block, Identifier texture) {
		String model1 = makeCube(texture);
		String model2 = makeCubeMirrored(texture);
		
		Identifier id = Registry.BLOCK.getId(block);
		Identifier modelID1 = new Identifier(id.getNamespace(), "block/" + id.getPath());
		Identifier modelID2 = new Identifier(id.getNamespace(), "block/" + id.getPath() + "_mirrored");
		
		InnerRegistry.registerModel(modelID1, model1);
		InnerRegistry.registerModel(modelID2, model2);
		InnerRegistry.registerItemModel(block.asItem(), model1);
		
		List<ModelVariant> variants = Lists.newArrayList();
		variants.add(new ModelVariant(modelID1, AffineTransformation.identity(), false, 1));
		variants.add(new ModelVariant(modelID2, AffineTransformation.identity(), false, 1));
		WeightedUnbakedModel wModel = new WeightedUnbakedModel(variants);
		InnerRegistry.registerBlockModel(block.getDefaultState(), wModel);
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
