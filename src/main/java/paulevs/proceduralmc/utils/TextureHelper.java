package paulevs.proceduralmc.utils;

import java.io.IOException;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import paulevs.proceduralmc.ProceduralMC;
import paulevs.proceduralmc.texturing.CustomColor;

public class TextureHelper {
	private static final CustomColor COLOR = new CustomColor();
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
}
