package me.ranol.ingamecoder.ide;

import java.util.HashMap;

public class CTheme {
	private HashMap<String, String> patterns = new HashMap<>();

	public void registerPattern(String color, String... pattern) {
		for (String s : pattern) {
			patterns.put(s, color);
		}
	}
}
