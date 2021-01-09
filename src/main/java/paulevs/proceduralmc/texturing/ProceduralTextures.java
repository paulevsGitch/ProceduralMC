package paulevs.proceduralmc.texturing;

import java.util.Random;

import paulevs.proceduralmc.utils.MHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class ProceduralTextures {
	public static BufferTexture makeStoneTexture(Random random) {
		BufferTexture texture = TextureHelper.makeNoiseTexture(random, MHelper.randRange(0.4F, 0.6F, random));
		BufferTexture distort = TextureHelper.makeNoiseTexture(random, MHelper.randRange(0.4F, 0.6F, random));
		BufferTexture additions = TextureHelper.makeNoiseTexture(random, MHelper.randRange(0.5F, 0.8F, random));
		BufferTexture result = TextureHelper.distort(texture, distort, MHelper.randRange(0F, 5F, random));
		BufferTexture pass = TextureHelper.hightPass(result, -1, -1);
		pass = TextureHelper.normalize(pass);
		result = TextureHelper.clamp(result, 5);
		result = TextureHelper.normalize(result);
		result = TextureHelper.blend(result, pass, 0.3F);
		result = TextureHelper.add(result, pass);
		result = TextureHelper.blend(result, additions, 0.3F);
		result = TextureHelper.normalize(result);
		result = TextureHelper.clamp(result, 7);
		
		BufferTexture offseted1 = TextureHelper.offset(texture, -1, 0);
		BufferTexture offseted2 = TextureHelper.offset(texture, 0, -1);
		result = TextureHelper.blend(result, offseted1, 0.2F);
		result = TextureHelper.blend(result, offseted2, 0.2F);
		
		CustomColor color = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
		color.switchToHSV().setSaturation(0.3F).setBrightness(0.5F);
		ColorGradient gradient = TextureHelper.makeDistortedPalette(color, 0.2F, -0.1F, 0.3F);
		TextureHelper.applyGradient(result, gradient);
		
		return result;
	}
}
