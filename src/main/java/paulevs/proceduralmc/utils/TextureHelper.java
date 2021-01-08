package paulevs.proceduralmc.utils;

import java.io.IOException;
import java.util.Random;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import paulevs.proceduralmc.ProceduralMC;
import paulevs.proceduralmc.noise.OpenSimplexNoise;
import paulevs.proceduralmc.texturing.BufferTexture;
import paulevs.proceduralmc.texturing.ColorGradient;
import paulevs.proceduralmc.texturing.CustomColor;

public class TextureHelper {
	private static final CustomColor COLOR = new CustomColor();
	private static final CustomColor COLOR2 = new CustomColor();
	private static final int ALPHA = 255 << 24;
	
	public static NativeImage makeTexture(int width, int height) {
		return new NativeImage(width, height, false);
	}
	
	public static NativeImage makeTexture(int side) {
		return makeTexture(side, side);
	}
	
	public static NativeImage makeTexture() {
		return makeTexture(16);
	}
	
	public static void setPixel(NativeImage img, int x, int y, int r, int g, int b) {
		img.setPixelColor(x, y, color(r, g, b));
	}
	
	public static int color(int r, int g, int b) {
		return ALPHA | (b << 16) | (g << 8) | r;
	}
	
	public static int color(int r, int g, int b, int a) {
		return (a << 24) | (b << 16) | (g << 8) | r;
	}
	
	public static Identifier makeBlockTextureID(String name) {
		return ProceduralMC.makeID("block/" + name);
	}
	
	public static Identifier makeItemTextureID(String name) {
		return ProceduralMC.makeID("item/" + name);
	}
	
	public static CustomColor getFromTexture(NativeImage img, int x, int y) {
		return COLOR.set(img.getPixelColor(x, y));
	}
	
	public static NativeImage loadImage(String name) {
		try {
			Identifier id = ProceduralMC.makeID(name);
			Resource input = MinecraftClient.getInstance().getResourceManager().getResource(id);
			return NativeImage.read(input.getInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static NativeImage loadImage(String namespace, String name) {
		try {
			Identifier id = new Identifier(namespace, name);
			Resource input = MinecraftClient.getInstance().getResourceManager().getResource(id);
			return NativeImage.read(input.getInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferTexture makeNoiseTexture(Random random) {
		BufferTexture texture = new BufferTexture(16, 16);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		COLOR.setAlpha(1F);
		float scale = MHelper.randRange(0.4F, 0.7F, random);
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				float nx = (float) x / texture.getWidth();
				float ny = (float) y / texture.getWidth();
				
				float px1 = x * scale;
				float py1 = y * scale;
				float px2 = (x - texture.getWidth()) * scale;
				float py2 = (y - texture.getHeight()) * scale;
				
				float v1 = (float) noise.eval(px1, py1) * 0.5F + 0.5F;
				float v2 = (float) noise.eval(px2, py1) * 0.5F + 0.5F;
				float v3 = (float) noise.eval(px1, py2) * 0.5F + 0.5F;
				float v4 = (float) noise.eval(px2, py2) * 0.5F + 0.5F;
				
				v1 = MathHelper.lerp(nx, v1, v2);
				v2 = MathHelper.lerp(nx, v3, v4);
				
				v1 = MathHelper.lerp(ny, v1, v2);
				COLOR.set(v1, v1, v1);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}
	
	public static BufferTexture makeNoiseTexture(Random random, float scale) {
		BufferTexture texture = new BufferTexture(16, 16);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		COLOR.setAlpha(1F);
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				float nx = (float) x / texture.getWidth();
				float ny = (float) y / texture.getWidth();
				
				float px1 = x * scale;
				float py1 = y * scale;
				float px2 = (x - texture.getWidth()) * scale;
				float py2 = (y - texture.getHeight()) * scale;
				
				float v1 = (float) noise.eval(px1, py1) * 0.5F + 0.5F;
				float v2 = (float) noise.eval(px2, py1) * 0.5F + 0.5F;
				float v3 = (float) noise.eval(px1, py2) * 0.5F + 0.5F;
				float v4 = (float) noise.eval(px2, py2) * 0.5F + 0.5F;
				
				v1 = MathHelper.lerp(nx, v1, v2);
				v2 = MathHelper.lerp(nx, v3, v4);
				
				v1 = MathHelper.lerp(ny, v1, v2);
				COLOR.set(v1, v1, v1);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}
	
	public static BufferTexture blend(BufferTexture a, BufferTexture b, float mix) {
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				COLOR.set(a.getPixel(x, y)).mixWith(COLOR2.set(b.getPixel(x, y)), mix);
				a.setPixel(x, y, COLOR);
			}
		}
		return a;
	}
	
	public static BufferTexture clamp(BufferTexture texture, int levels) {
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				float r = (float) MathHelper.floor(COLOR.getRed() * levels) / levels;
				float g = (float) MathHelper.floor(COLOR.getGreen() * levels) / levels;
				float b = (float) MathHelper.floor(COLOR.getBlue() * levels) / levels;
				COLOR.set(r, g, b);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}
	
	public static BufferTexture applyGragient(BufferTexture texture, ColorGradient gradient) {
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				texture.setPixel(x, y, gradient.getColor(COLOR.getRed()).setAlpha(COLOR.getAlpha()));
			}
		}
		return texture;
	}

	public static ColorGradient makeSoftPalette(CustomColor color) {
		CustomColor colorStart = new CustomColor().set(color).switchToHSV();
		
		colorStart
		.setHue(colorStart.getHue() - 0.05F)
		.setSaturation(colorStart.getSaturation() * 0.5F)
		.setBrightness(colorStart.getBrightness() - 0.3F);
		
		CustomColor colorEnd = new CustomColor().set(color).switchToHSV();
		colorEnd
		.setHue(colorEnd.getHue() + 0.05F)
		.setSaturation(colorEnd.getSaturation() * 0.5F)
		.setBrightness(colorEnd.getBrightness() + 0.3F);
		
		return new ColorGradient(colorStart, colorEnd);
	}
}
