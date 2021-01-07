package paulevs.proceduralmc.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
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

	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void loadModel(Identifier id, CallbackInfo info) throws Exception {

		if (id instanceof ModelIdentifier) {
			ModelIdentifier modelID = (ModelIdentifier) id;
			Identifier cleanID = new Identifier(id.getNamespace(), id.getPath());
			if (InnerRegistry.hasCustomModel(cleanID)) {
				if (modelID.getVariant().equals("inventory")) {
					String json = String.format("{\"parent\": \"%s:block/%s\"}", id.getNamespace(), id.getPath());
					Identifier itemID = new Identifier(id.getNamespace(), "item/" + id.getPath());
					JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
					model.id = itemID.toString();
					putModel(modelID, model);
					this.unbakedModels.put(itemID, model);
					info.cancel();
				}
				else {
					Block block = Registry.BLOCK.get(cleanID);
					block.getStateManager().getStates().forEach((state) -> {
						JsonUnbakedModel model = InnerRegistry.getModel(state);
						if (model != null) {
							ModelIdentifier stateID = BlockModels.getModelId(cleanID, state);
							putModel(stateID, model);
						}
						else {
							System.out.println(String.format("Missing model for %s for state %s", cleanID, state));
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
