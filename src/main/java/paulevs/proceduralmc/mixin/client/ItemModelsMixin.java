package paulevs.proceduralmc.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import paulevs.proceduralmc.utils.ModelHelper;

@Mixin(ItemModels.class)
public class ItemModelsMixin {
	@Final
	@Shadow
	public Int2ObjectMap<ModelIdentifier> modelIds;
	
	@Shadow
	public void putModel(Item item, ModelIdentifier modelId) {}
	
	@Inject(method = "reloadModels", at = @At("TAIL"))
	public void reloadModels(CallbackInfo info) {
		ModelHelper.MODELS.forEach((item, id) -> {
			putModel(item, id);
		});
	}
}
