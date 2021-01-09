package paulevs.proceduralmc;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import paulevs.proceduralmc.material.ComplexMaterial;
import paulevs.proceduralmc.material.MetalOreMaterial;
import paulevs.proceduralmc.material.OreMaterial;
import paulevs.proceduralmc.material.StoneMaterial;
import paulevs.proceduralmc.namegen.NameGenerator;
import paulevs.proceduralmc.utils.ModelHelper;
import paulevs.proceduralmc.utils.TagHelper;

public class ProceduralMC implements ModInitializer {
	public static final String MOD_ID = "proceduralmc";
	private static final Random RANDOM = new Random();
	private static boolean register = true;
	private static long seed = Long.MIN_VALUE;
	
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
		synchronized(world) {
			if (register && seed != world.getSeed()) {
				System.out.println("Start new generator!");
				
				register = false;
				seed = world.getSeed();
				
				InnerRegistry.clearRegistries();
				TagHelper.clearTags();
				StoneMaterial.resetMaterials();
				
				if (isClient()) {
					ModelHelper.clearModels();
				}
				
				RANDOM.setSeed(seed);
				List<ComplexMaterial> materials = Lists.newArrayList();
				
				for (int i = 0; i < 16; i++) {
					StoneMaterial material = new StoneMaterial(RANDOM);
					materials.add(material);
				}
				
				for (int i = 0; i < 32; i++) {
					OreMaterial material = new MetalOreMaterial(RANDOM);
					materials.add(material);
				}
				
				world.getServer().reloadResources(world.getServer().getDataPackManager().getEnabledNames());
				
				System.out.println("Make Client update!");
				RANDOM.setSeed(seed);
				if (isClient()) {
					materials.forEach((material) -> {
						material.initClient(RANDOM);
					});
					
					MinecraftClient.getInstance().reloadResources().thenRun(new Runnable() {
						@Override
						public void run() {
							MinecraftClient.getInstance().getItemRenderer().getModels().reloadModels();
						}
					});
				}
			}
		}
	}
	
	public static void onServerStop() {
		System.out.println("Stop!");
		register = true;
	}
}
