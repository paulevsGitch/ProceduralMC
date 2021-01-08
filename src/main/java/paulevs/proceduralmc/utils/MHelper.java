package paulevs.proceduralmc.utils;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class MHelper {
	public static float wrap(float x, float y) {
	    return x - MathHelper.floor(x / y) * y;
	}

	public static float lengthSqr(float x, float y, float z) {
		return x * x + y * y + z * z;
	}
	
	public static float randRange(float min, float max, Random random) {
		return min + random.nextFloat() * (max - min);
	}
}
