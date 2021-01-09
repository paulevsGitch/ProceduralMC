package paulevs.proceduralmc.texturing;

import java.util.Random;

import paulevs.proceduralmc.utils.MHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class ProceduralTextures {
	public static ColorGradient makeStonePalette(CustomColor color, Random random) {
		float sat = MHelper.randRange(0, 0.2F, random);
		float val = MHelper.randRange(0.3F, 0.7F, random);
		color.switchToHSV().setSaturation(sat).setBrightness(val);
		float hue = MHelper.randRange(0, 0.3F, random);
		sat = random.nextFloat() * 0.3F;
		val = MHelper.randRange(0.3F, 0.5F, random);
		return TextureHelper.makeDistortedPalette(color, hue, sat, val);
	}
	
	public static BufferTexture makeStoneTexture(ColorGradient gradient, Random random) {
		BufferTexture texture = TextureHelper.makeNoiseTexture(random, 64, MHelper.randRange(0.4F, 0.6F, random) / 4F);
		BufferTexture distort = TextureHelper.makeNoiseTexture(random, 64, MHelper.randRange(0.4F, 0.6F, random) / 4F);
		BufferTexture additions = TextureHelper.makeNoiseTexture(random, 64, MHelper.randRange(0.5F, 0.8F, random) / 4F);
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
		
		result = TextureHelper.applyGradient(result, gradient);
		result = TextureHelper.downScale(result, 4);
		
		return result;
	}
	
	public static BufferTexture makeBluredTexture(BufferTexture texture) {
		BufferTexture result = texture.clone();
		BufferTexture b1 = TextureHelper.offset(texture,  1,  0);
		BufferTexture b2 = TextureHelper.offset(texture, -1,  0);
		BufferTexture b3 = TextureHelper.offset(texture,  0,  1);
		BufferTexture b4 = TextureHelper.offset(texture,  0, -1);
		result = TextureHelper.blend(result, b1, 0.1F);
		result = TextureHelper.blend(result, b2, 0.1F);
		result = TextureHelper.blend(result, b3, 0.1F);
		result = TextureHelper.blend(result, b4, 0.1F);
		return result;
	}
	
	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay.clone(), gradient);
		return TextureHelper.blend(texture, over, 0.5F);
	}
}
