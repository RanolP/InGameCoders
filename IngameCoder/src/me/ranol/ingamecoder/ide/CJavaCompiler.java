package me.ranol.ingamecoder.ide;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.google.common.util.concurrent.UncheckedExecutionException;

public class CJavaCompiler {
	static JavaCompiler compiler;

	public static void compile(File file, String sources) {
		compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new UncheckedExecutionException(new Exception(
					"서버가 JRE로 실행되었습니다."));
		}
		StandardJavaFileManager sjfm = compiler.getStandardFileManager(null,
				null, null);
		try (PrintWriter writer = new PrintWriter(file)) {
			writer.println(sources);
		} catch (Exception e) {
		}
		Iterable<? extends JavaFileObject> fo = sjfm.getJavaFileObjects(file);
		if (!compiler.getTask(null, sjfm, null, null, null, fo).call()) {
			throw new UncheckedExecutionException(new Exception("컴파일에 실패했습니다."));
		}
		try {
			URL[] urls = new URL[] { new File("").toURI().toURL() };
			try (URLClassLoader loader = new URLClassLoader(urls)) {
				Object o = loader.loadClass("test").newInstance();
				o.getClass().getMethod("run").invoke(o);
			}
		} catch (Exception e) {
		}
		if (file.exists())
			file.delete();
	}
}
