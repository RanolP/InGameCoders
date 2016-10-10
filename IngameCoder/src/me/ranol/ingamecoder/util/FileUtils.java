package me.ranol.ingamecoder.util;

import java.io.File;

public class FileUtils {
	public static boolean delete(File folder) {
		if (!folder.isDirectory())
			return folder.delete();
		File[] files = folder.listFiles();
		boolean b = true;
		for (File f : files) {
			b = delete(f) && b;
		}
		return b && folder.delete();
	}
}
