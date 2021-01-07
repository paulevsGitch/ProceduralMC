package paulevs.proceduralmc.utils;

import net.minecraft.util.Identifier;

public class ModelHelper {
	public static String makeCube(Identifier texture) {
		return String.format("{\"parent\": \"minecraft:block/cube_all\", \"textures\": {\"all\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}
}
