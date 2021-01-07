package paulevs.proceduralmc;

import net.minecraft.client.texture.NativeImage;

public class BufferTexture {
	final int width;
	final int height;
	final int[] buffer;
	
	public BufferTexture(int width, int height) {
		this.width = width;
		this.height = height;
		buffer = new int[width * height];
	}
	
	public void setPixel(int x, int y, int r, int g, int b) {
		int color = TextureHelper.color(r, g, b);
		buffer[y * width + x] = color;
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
