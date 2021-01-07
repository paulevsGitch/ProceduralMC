package paulevs.proceduralmc;

import net.minecraft.util.Identifier;

public interface ChangeableRegistry {
	public void remove(Identifier key);
	
	public void recalculateLastID();
}
