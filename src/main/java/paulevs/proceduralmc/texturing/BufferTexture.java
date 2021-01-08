package paulevs.proceduralmc.texturing;

import net.minecraft.client.texture.NativeImage;
import paulevs.proceduralmc.utils.TextureHelper;

public class BufferTexture {
	final int width;
	final int height;
	final int[] buffer;
	
	public BufferTexture(int width, int height) {
		this.width = width;
		this.height = height;
		buffer = new int[width * height];
	}
	
	public BufferTexture(NativeImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		buffer = new int[width * height];
		for (int i = 0; i < buffer.length; i++) {
			int x = i % width;
			int y = i / width;
			buffer[i] = image.getPixelColor(x, y);
		}
	}
	
	public void setPixel(int x, int y, int r, int g, int b) {
		int color = TextureHelper.color(r, g, b);
		buffer[y * width + x] = color;
	}
	
	public void setPixel(int x, int y, CustomColor color) {
		buffer[y * width + x] = color.getAsInt();
	}
	
	public int getPixel(int x, int y) {
		return buffer[y * width + x];
	}
	
	public NativeImage makeImage() {
		NativeImage img = TextureHelper.makeTexture(width, height);
		for (int i = 0; i < buffer.length; i++) {
			int x = i % width;
			int y = i / width;
			img.setPixelColor(x, y, buffer[i]);
		}
		return img;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
