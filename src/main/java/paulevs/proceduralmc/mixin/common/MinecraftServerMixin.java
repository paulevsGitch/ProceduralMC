package paulevs.proceduralmc.mixin.common;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import paulevs.proceduralmc.recipe.CustomRecipeManager;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	private ServerResourceManager serverResourceManager;
	
	@Final
	@Shadow
	private Map<RegistryKey<World>, ServerWorld> worlds;

	@Inject(method = "reloadResources", at = @At(value = "RETURN"), cancellable = true)
	private void procmcOnReloadResources(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		procmcInjectRecipes();
	}

	@Inject(method = "loadWorld", at = @At(value = "RETURN"), cancellable = true)
	private void procmcOnLoadWorld(CallbackInfo info) {
		procmcInjectRecipes();
	}

	private void procmcInjectRecipes() {
		if (FabricLoader.getInstance().isModLoaded("kubejs")) {
			RecipeManagerAccessor accessor = (RecipeManagerAccessor) serverResourceManager.getRecipeManager();
			accessor.setRecipes(CustomRecipeManager.getMap(accessor.getRecipes()));
		}
	}
}
