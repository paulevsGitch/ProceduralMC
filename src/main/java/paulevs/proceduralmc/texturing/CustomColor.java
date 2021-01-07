package paulevs.proceduralmc.texturing;

import net.minecraft.util.math.MathHelper;
import paulevs.proceduralmc.utils.TextureHelper;

public class CustomColor {
	private float r;
	private float g;
	private float b;
	private float a;
	
	public CustomColor() {
		this(0F, 0F, 0F, 0F);
	}
	
	public CustomColor(float r, float g, float b) {
		this(r, g, b, 1F);
	}
	
	public CustomColor(float r, float g, float b, float a) {
		set(r, g, b, a);
	}
	
	public CustomColor(int r, int g, int b, int a) {
		set(r, g, b, a);
	}
	
	public CustomColor(int value) {
		set(value);
	}
	
	public int getAsInt() {
		int cr = (int) MathHelper.clamp(r * 255, 0, 255);
		int cg = (int) MathHelper.clamp(g * 255, 0, 255);
		int cb = (int) MathHelper.clamp(b * 255, 0, 255);
		return TextureHelper.color(cr, cg, cb);
	}
	
	public CustomColor set(int r, int g, int b, int a) {
		this.r = r / 255F;
		this.g = g / 255F;
		this.b = b / 255F;
		this.a = a / 255F;
		return this;
	}
	
	public CustomColor set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}
	
	public CustomColor set(CustomColor color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		return this;
	}
	
	public CustomColor set(int value) {
		r = (value & 255) / 255F;
		g = ((value >> 8) & 255) / 255F;
		b = ((value >> 16) & 255) / 255F;
		a = ((value >> 24) & 255) / 255F;
		return this;
	}
	
	public CustomColor mixWith(CustomColor color, float blend) {
		this.r = MathHelper.lerp(blend, this.r, color.r);
		this.g = MathHelper.lerp(blend, this.g, color.g);
		this.b = MathHelper.lerp(blend, this.b, color.b);
		return this;
	}

	public float getR() {
		return r;
	}

	public CustomColor setR(float r) {
		this.r = r;
		return this;
	}

	public float getG() {
		return g;
	}

	public CustomColor setG(float g) {
		this.g = g;
		return this;
	}

	public float getB() {
		return b;
	}

	public CustomColor setB(float b) {
		this.b = b;
		return this;
	}

	public float getA() {
		return a;
	}

	public CustomColor setA(float a) {
		this.a = a;
		return this;
	}
}
