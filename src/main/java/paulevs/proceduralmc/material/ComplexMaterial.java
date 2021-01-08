package paulevs.proceduralmc.material;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public abstract class ComplexMaterial {
	private static final List<ComplexMaterial> MATERIALS = Lists.newArrayList();
	
	public ComplexMaterial(Random random) {}
	
	public abstract void initClient(Random random);
	
	public static void resetMaterials() {
		MATERIALS.clear();
	}
	
	public static void initMaterialsClient(Random random) {
		MATERIALS.forEach((material) -> {
			material.initClient(random);
		});
	}
}
