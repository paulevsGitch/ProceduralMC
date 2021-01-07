package paulevs.proceduralmc.namegen;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;

import paulevs.proceduralmc.ProceduralMC;

public class MarkovNameGen {
	private static final MarkovChain ORE_GEN = makeChain("ores");
	private static final Map<String, String> NAMES = Maps.newHashMap();
	
	public static void init() {}
	
	private static MarkovChain makeChain(String name) {
		InputStream stream = MarkovNameGen.class.getResourceAsStream("/assets/" + ProceduralMC.MOD_ID + "/namegen/" + name + ".txt");
		return new MarkovChain(stream);
	}
	
	private static String makeName(MarkovChain chain, Random random, int min, int max, Set<String> preNames) {
		String result = chain.makeWord(min, max, random);
		result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		for (int i = 0; i < 100 && preNames.contains(result); i++) {
			result = chain.makeWord(min, max, random);
			result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		}
		return preNames.contains(result) ? result + "_" + Integer.toHexString(random.nextInt()) : result;
	}
	
	public static String makeOreName(Random random, Set<String> preNames) {
		return makeName(ORE_GEN, random, 6, 12, preNames);
	}
	
	public static void addTranslation(String raw, String translated) {
		NAMES.put(raw, translated);
	}
	
	public static boolean hasTranslation(String raw) {
		return NAMES.containsKey(raw);
	}
	
	public static String getTranslation(String raw) {
		return NAMES.get(raw);
	}
}
