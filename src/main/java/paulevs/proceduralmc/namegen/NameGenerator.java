package paulevs.proceduralmc.namegen;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import paulevs.proceduralmc.ProceduralMC;

public class NameGenerator {
	private static final MarkovChain ORE_GEN = makeChain("ores");
	private static final MarkovChain ROCK_GEN = makeChain("rocks");
	private static final Map<String, String> NAMES = Maps.newHashMap();
	private static final Set<String> GENERATED = Sets.newHashSet();
	
	public static void init() {}
	
	private static MarkovChain makeChain(String name) {
		InputStream stream = NameGenerator.class.getResourceAsStream("/assets/" + ProceduralMC.MOD_ID + "/namegen/" + name + ".txt");
		return new MarkovChain(stream);
	}
	
	private static String makeName(MarkovChain chain, Random random, int min, int max) {
		String result = chain.makeWord(min, max, random);
		result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		for (int i = 0; i < 100 && GENERATED.contains(result); i++) {
			result = chain.makeWord(min, max, random);
			result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		}
		result = GENERATED.contains(result) ? result + "_" + Integer.toHexString(random.nextInt()) : result;
		GENERATED.add(result);
		return result;
	}
	
	public static void clearNames() {
		GENERATED.clear();
	}
	
	public static String makeOreName(Random random) {
		return makeName(ORE_GEN, random, 6, 12);
	}
	
	public static String makeRockName(Random random) {
		return makeName(ROCK_GEN, random, 6, 12);
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
