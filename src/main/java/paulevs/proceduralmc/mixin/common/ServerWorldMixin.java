package paulevs.proceduralmc.mixin.common;

import java.util.List;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import paulevs.proceduralmc.InnerRegistry;
import paulevs.proceduralmc.ModelHelper;
import paulevs.proceduralmc.ProceduralMC;
import paulevs.proceduralmc.TextureHelper;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void init(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long l, List<Spawner> list, boolean bl, CallbackInfo info) {
		Identifier id = ProceduralMC.makeID("testblock");
		if (!Registry.BLOCK.containsId(id)) {
			Block testblock = new Block(FabricBlockSettings.copyOf(Blocks.STONE));
			Registry.register(Registry.BLOCK, id, testblock);
			Registry.register(Registry.ITEM, id, new BlockItem(testblock, new Settings().group(ItemGroup.BUILDING_BLOCKS)));
			
			if (ProceduralMC.isClient()) {
				InnerRegistry.registerBlock(id, testblock);
				
				NativeImage texture = TextureHelper.makeTexture();
				for (int x = 0; x < 16; x++) {
					int r = x << 4;
					for (int y = 0; y < 16; y++) {
						int g = y << 4;
						TextureHelper.setPixel(texture, x, y, r, g, 128);
					}
				}
				InnerRegistry.registerTexture(id, texture);
				InnerRegistry.registerBlockModel(testblock, ModelHelper.makeCube(id));
				
				MinecraftClient.getInstance().reloadResources();
			}
		}
	}
}
