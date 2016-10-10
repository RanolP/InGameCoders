package me.ranol.ingamecoder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleOptionFile {
	HashMap<String, String> map = new HashMap<>();
	List<String> values = new ArrayList<>();
	File file;

	public SimpleOptionFile(File file) {
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		this.file = file;
		readToEnd(file);
	}

	public SimpleOptionFile(File folder, String str) {
		this(new File(folder, str));
	}

	public String getValue(String key) {
		if (map.containsKey(key))
			return map.get(key);
		return key;
	}

	public void setValue(String key, String value) {
		map.put(key, value);
	}

	public void save() {
		String data = "";
		HashMap<String, String> map = new HashMap<>(this.map);
		for (String s : values) {
			if (s.startsWith("#") || s.startsWith("//") || !s.contains("=")) {
				data += "\n" + s;
				continue;
			}
			String key = s.substring(0, s.indexOf("="));
			if (map.containsKey(key)) {
				data += "\n" + key + "=" + map.get(key);
				map.remove(key);
			} else
				data += "\n" + s;
		}
		for (String s : map.keySet()) {
			data += "\n" + s + "=" + map.get(s);
		}
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(data.substring(1));
		} catch (Exception e) {

		}
	}

	private void readToEnd(File file) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			String read = null;
			while ((read = br.readLine()) != null) {
				values.add(read);
				if (read.startsWith("#"))
					continue;
				if (read.startsWith("//"))
					continue;
				int index = read.indexOf("=");
				String v = read.substring(index);
				if (index != -1)
					map.put(read.substring(0, index),
							v.startsWith("=") ? v.substring(1) : v);
			}
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
}
