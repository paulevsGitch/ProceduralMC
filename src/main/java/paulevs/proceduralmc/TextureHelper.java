package paulevs.proceduralmc;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

public class TextureHelper {
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
		return (255 << 24) | (b << 16) | (g << 8) | r;
	}
	
	public static Identifier makeBlockTextureID(String name) {
		return ProceduralMC.makeID("block/" + name);
	}
}
