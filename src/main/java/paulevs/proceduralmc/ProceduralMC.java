package paulevs.proceduralmc;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import paulevs.proceduralmc.material.ComplexMaterial;
import paulevs.proceduralmc.material.StoneMaterial;
import paulevs.proceduralmc.namegen.NameGenerator;
import paulevs.proceduralmc.utils.SilentWorldReloader;

public class ProceduralMC implements ModInitializer {
	public static final String MOD_ID = "proceduralmc";
	private static final Random RANDOM = new Random();
	private static boolean register = true;
	
	@Override
	public void onInitialize() {
		NameGenerator.init();
	}
	
	public static Identifier makeID(String name) {
		return new Identifier(MOD_ID, name);
	}
	
	public static boolean isFromMod(Identifier id) {
		return id.getNamespace().equals(MOD_ID);
	}
	
	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
	
	public static void onServerStart(ServerWorld world) {
		if (register) {
			register = false;
			
			InnerRegistry.clearRegistries();
			
			RANDOM.setSeed(world.getSeed());
			List<ComplexMaterial> materials = Lists.newArrayList();
			
			Set<String> names = Sets.newHashSet();
			for (int i = 0; i < 1; i++) {
				String name = NameGenerator.makeOreName(RANDOM, names);
				StoneMaterial material = new StoneMaterial(name, RANDOM);
				materials.add(material);
			}
			
			RANDOM.setSeed(world.getSeed());
			if (isClient()) {
				materials.forEach((material) -> {
					material.initClient(RANDOM);
				});
				
				SilentWorldReloader.setSilent();
				MinecraftClient.getInstance().reloadResources();
			}
		}
	}
	
	public static void onServerStop() {
		register = true;
	}
}
