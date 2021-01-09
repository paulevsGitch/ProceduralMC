package paulevs.proceduralmc.texturing;

import java.util.Random;

import paulevs.proceduralmc.utils.MHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class ProceduralTextures {
	public static ColorGradient makeStonePalette(CustomColor color, Random random) {
		CustomColor copy = color.clone().switchToHSV().setSaturation(MHelper.randRange(0, 0.2F, random)).setBrightness(MHelper.randRange(0.2F, 0.6F, random));
		return TextureHelper.makeDistortedPalette(copy, MHelper.randRange(0, 0.2F, random), MHelper.randRange(-0.1F, 0.1F, random), MHelper.randRange(0.1F, 0.25F, random));
	}
	
	public static BufferTexture makeStoneTexture(ColorGradient gradient, Random random) {
		BufferTexture texture = TextureHelper.makeNoiseTexture(random, MHelper.randRange(0.4F, 0.6F, random));
		BufferTexture distort = TextureHelper.makeNoiseTexture(random, MHelper.randRange(0.4F, 0.6F, random));
		BufferTexture additions = TextureHelper.makeNoiseTexture(random, MHelper.randRange(0.5F, 0.8F, random));
		BufferTexture result = TextureHelper.distort(texture, distort, MHelper.randRange(0F, 5F, random));
		BufferTexture pass = TextureHelper.hightPass(result, -1, -1);
		BufferTexture dark = TextureHelper.hightPass(result, 1, 1);
		dark = TextureHelper.invert(dark);
		
		pass = TextureHelper.normalize(pass);
		result = TextureHelper.clamp(result, 5);
		result = TextureHelper.normalize(result);
		//result = TextureHelper.blend(result, pass, 0.3F);
		result = TextureHelper.sub(result, pass);
		result = TextureHelper.add(result, pass);
		result = TextureHelper.blend(result, additions, 0.4F);
		
		result = TextureHelper.normalize(result);
		result = TextureHelper.clamp(result, 7);
		
		BufferTexture offseted1 = TextureHelper.offset(texture, -1, 0);
		BufferTexture offseted2 = TextureHelper.offset(texture, 0, -1);
		result = TextureHelper.blend(result, offseted1, 0.2F);
		result = TextureHelper.blend(result, offseted2, 0.2F);
		result = TextureHelper.normalize(result);
		
		return TextureHelper.applyGradient(result, gradient);
	}
}
