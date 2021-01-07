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
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import paulevs.proceduralmc.InnerRegistry;
import paulevs.proceduralmc.utils.ModelHelper;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Final
	@Shadow
	private Map<Identifier, UnbakedModel> unbakedModels;
	
	@Shadow
	private void putModel(Identifier id, UnbakedModel unbakedModel) {}

	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void procmcLoadModel(Identifier id, CallbackInfo info) throws Exception {
		if (id instanceof ModelIdentifier) {
			ModelIdentifier modelID = (ModelIdentifier) id;
			Identifier cleanID = new Identifier(id.getNamespace(), id.getPath());
			if (InnerRegistry.hasCustomModel(cleanID)) {
				if (modelID.getVariant().equals("inventory")) {
					Item item = Registry.ITEM.get(cleanID);
					JsonUnbakedModel model = InnerRegistry.getModel(item);
					if (model != null) {
						ModelHelper.MODELS.put(Registry.ITEM.get(cleanID), modelID);
						Identifier identifier2 = new Identifier(id.getNamespace(), "item/" + id.getPath());
						model.id = identifier2.toString();
						putModel(modelID, model);
						unbakedModels.put(identifier2, model);
						info.cancel();
					}
					else {
						System.out.println(String.format("Missing item model for %s", cleanID));
					}
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
							System.out.println(String.format("Missing block model for %s for state %s", cleanID, state));
						}
					});
					info.cancel();
				}
			}
		}
	}
}
