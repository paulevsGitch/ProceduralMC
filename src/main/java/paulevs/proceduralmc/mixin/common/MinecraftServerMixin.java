package paulevs.proceduralmc.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import paulevs.proceduralmc.ProceduralMC;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "exit", at = @At("TAIL"))
	public void procmcOnServerStop(CallbackInfo info) {
		ProceduralMC.onServerStop();
	}
}
