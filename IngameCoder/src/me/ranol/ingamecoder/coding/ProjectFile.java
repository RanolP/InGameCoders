package me.ranol.ingamecoder.coding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.ranol.ingamecoder.coding.interfaces.Saveable;
import me.ranol.ingamecoder.util.SimpleOptionFile;

public class ProjectFile implements Saveable {
	private String name;
	private String encoding;
	private List<String> libraries = new ArrayList<>();
	private List<String> packages = new ArrayList<>();
	SimpleOptionFile option;

	public ProjectFile(SimpleOptionFile file) {
		this.option = file;
		name = file.getValue("project-name");
		encoding = file.getValue("file-encoding");
		String l = file.getValue("libraries");
		if (l.contains(","))
			libraries.addAll(Arrays.asList(file.getValue("libraries")
					.split(",")));
		String p = file.getValue("packages");
		if (p.contains(","))
			addPackagesString(Arrays.asList(file.getValue("packages")
					.split(",")));
		else if (p.contains("."))
			addPackages(p);
	}

	public void save() {
		option.save();
	}

	public ProjectFile(File file) {
		this(new SimpleOptionFile(file));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		option.setValue("project-name", this.name);
		save();
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
		option.setValue("file-encoding", this.encoding);
		save();
	}

	public List<String> getLibraries() {
		return libraries;
	}

	public void addLibrary(String libraries) {
		this.libraries.add(libraries);
		option.setValue("libraries", l2s());
		save();
	}

	private String l2s() {
		String s = "";
		for (String s2 : libraries)
			s += "," + s2;
		return s.substring(2);
	}

	public void addLibraries(String... libraries) {
		this.libraries.addAll(Arrays.asList(libraries));
		option.setValue("libraries", l2s());
		save();
	}

	public void addLibraries(Collection<String> libraries) {
		this.libraries.addAll(libraries);
		option.setValue("libraries", l2s());
		save();
	}

	public void removeLibrary(String libraries) {
		this.libraries.remove(libraries);
		save();
	}

	public List<String> getPackages() {
		return packages;
	}

	public void addPackages(CPackage pack) {
		if (!packages.contains(pack.getDirectory()))
			packages.add(pack.getDirectory());
		option.setValue("packages", p2s());
		save();
	}

	public void addPackages(CPackage... packs) {
		for (CPackage pack : packs) {
			if (!packages.contains(pack.getDirectory()))
				packages.add(pack.getDirectory());
		}
		option.setValue("packages", p2s());
		save();
	}

	public void addPackages(List<CPackage> packs) {
		for (CPackage pack : packs) {
			if (!packages.contains(pack.getDirectory()))
				packages.add(pack.getDirectory());
		}
		option.setValue("packages", p2s());
		save();
	}

	public void addPackages(String pack) {
		if (!packages.contains(pack.trim()))
			packages.add(pack.trim());
		option.setValue("packages", p2s());
		save();
	}

	public void addPackages(String... packs) {
		for (String pack : packs) {
			if (!packages.contains(pack.trim()))
				packages.add(pack.trim());
		}
		option.setValue("packages", p2s());
		save();
	}

	public void addPackagesString(List<String> packs) {
		for (String pack : packs) {
			if (!packages.contains(pack.trim()))
				packages.add(pack.trim());
		}
		option.setValue("packages", p2s());
		save();
	}

	private String p2s() {
		String s = "";
		for (String s2 : packages)
			s += "," + s2;
		return s.substring(1);
	}
}
