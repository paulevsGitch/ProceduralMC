package paulevs.proceduralmc.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import paulevs.proceduralmc.ChangeableRegistry;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> implements ChangeableRegistry {
	@Final
	@Shadow
	private ObjectList<T> rawIdToEntry;
	@Final
	@Shadow
	private Object2IntMap<T> entryToRawId;
	@Final
	@Shadow
	private BiMap<Identifier, T> idToEntry;
	@Final
	@Shadow
	private BiMap<RegistryKey<T>, T> keyToEntry;
	@Final
	@Shadow
	private Map<T, Lifecycle> entryToLifecycle;
	@Shadow
	private int nextId;

	@Override
	public void remove(Identifier key) {
		T entry = idToEntry.get(key);
		if (entry != null) {
			int rawID = entryToRawId.getInt(entry);
			rawIdToEntry.remove(rawID);
			entryToRawId.removeInt(rawID);
			idToEntry.remove(key);
			keyToEntry.inverse().remove(entry);
			entryToLifecycle.remove(entry);
		}
	}

	@Override
	public void recalculateLastID() {
		nextId = 0;
		for (int id: entryToRawId.values()) {
			nextId = id > nextId ? id : nextId;
		}
		nextId ++;
	}
}
