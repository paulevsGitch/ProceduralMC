package paulevs.proceduralmc.utils;

import net.minecraft.util.Identifier;

public interface ChangeableRegistry {
	public void remove(Identifier key);
	
	public void recalculateLastID();
}
