package paulevs.proceduralmc.mixin.client;

import java.io.IOException;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import paulevs.proceduralmc.InnerRegistry;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Final
	@Shadow
	private Map<Identifier, UnbakedModel> unbakedModels;
	
	@Shadow
	private void putModel(Identifier id, UnbakedModel unbakedModel) {}
	
	@Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
	private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info) throws IOException {
		String path = id.getPath();
		if (path.startsWith("item/")) {
			Item item = Registry.ITEM.get(new Identifier(id.getNamespace(), path.substring(path.lastIndexOf('/') + 1)));
			JsonUnbakedModel model = InnerRegistry.getModel(item);
			if (model != null) {
				model.id = id.toString();
				info.setReturnValue(model);
				info.cancel();
			}
		}
	}

	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void loadModel(Identifier id, CallbackInfo info) throws Exception {
		if (id instanceof ModelIdentifier) {
			ModelIdentifier modelID = (ModelIdentifier) id;
			Identifier cleanID = new Identifier(id.getNamespace(), id.getPath());
			if (InnerRegistry.hasCustomModel(cleanID)) {
				if (!modelID.getVariant().equals("inventory")) {
					/*String json = String.format("{\"parent\": \"%s:block/%s\"}", id.getNamespace(), id.getPath());
					Identifier itemID = new Identifier(id.getNamespace(), "item/" + id.getPath());
					JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
					model.id = itemID.toString();
					putModel(modelID, model);
					this.unbakedModels.put(itemID, model);
					info.cancel();*/
					
					/*Block block = Registry.BLOCK.get(modelID);
					JsonUnbakedModel model = InnerRegistry.getModel(block.getDefaultState());
					Identifier itemID = new Identifier(id.getNamespace(), "item/" + id.getPath());
					model.id = itemID.toString();
					putModel(modelID, model);
					this.unbakedModels.put(itemID, model);
					info.cancel();*/
					
					/*Item item = Registry.ITEM.get(cleanID);
					JsonUnbakedModel model = InnerRegistry.getModel(item);
					if (model != null) {
						model.id = cleanID.toString();
						putModel(cleanID, model);
						this.unbakedModels.put(cleanID, model);
						info.cancel();
					}
					else {
						System.out.println(String.format("Missing item model for %s", cleanID));
					}*/
				//}
				//else {
					Block block = Registry.BLOCK.get(cleanID);
					block.getStateManager().getStates().forEach((state) -> {
						JsonUnbakedModel model = InnerRegistry.getModel(state);
						if (model != null) {
							ModelIdentifier stateID = BlockModels.getModelId(cleanID, state);
							putModel(stateID, model);
						}
						else {
							System.out.println(String.format("Missing block model for %s for state %s", cleanID, state));
						}
					});
					info.cancel();
				}
			}
		}
		
		/*if (ProceduralMC.isFromMod(id) && id instanceof ModelIdentifier) {
			ModelIdentifier modelID = (ModelIdentifier) id;
			if (modelID.getVariant().equals("inventory")) {
				String json = String.format("{\"parent\": \"%s:block/%s\"}", id.getNamespace(), id.getPath());
				//String json = String.format("{\"parent\": \"%s:block/%s\"}", "minecraft", "stone");
				Identifier itemID = new Identifier(id.getNamespace(), "item/" + id.getPath());
				JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
				model.id = itemID.toString();
				putModel(modelID, model);
				this.unbakedModels.put(itemID, model);
				info.cancel();
			}
			else {
				Identifier cleanID = new Identifier(id.getNamespace(), id.getPath());
				//JsonUnbakedModel model = JsonUnbakedModel.deserialize(BLOCK);
				//model.id = cleanID.toString();
				Block block = Registry.BLOCK.get(id);
				block.getStateManager().getStates().forEach((state) -> {
					//ModelIdentifier stateID = BlockModels.getModelId(cleanID, state);
					//putModel(stateID, model);
					JsonUnbakedModel model = InnerRegistry.getModel(state);
					if (model != null) {
						ModelIdentifier stateID = BlockModels.getModelId(cleanID, state);
						putModel(stateID, model);
					}
				});
				info.cancel();
			}
		}*/
	}
}
