package paulevs.proceduralmc.utils;

import net.minecraft.util.math.MathHelper;

public class MHelper {
	public static float wrap(float x, float y) {
	    return x - MathHelper.floor(x / y) * y;
	}
}
