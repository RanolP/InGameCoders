package me.ranol.ingamecoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ranol.ingamecoder.coding.CProject;
import me.ranol.ingamecoder.coding.ProjectFile;

public class ProjectManager {
	private static List<CProject> projects = new ArrayList<>();

	public static void loadProjects() {
		File data = InGameCoder.instance().getDataFolder();
		if (!data.exists())
			data.mkdirs();
		File[] folders = data.listFiles((file) -> file.isDirectory());
		for (File folder : folders) {
			File pdf = new File(folder, ".project");
			ProjectFile desc = new ProjectFile(pdf);
			CProject project = new CProject(folder);
			project.setDescriptionFile(desc);
			projects.add(project);
		}
	}

	public static List<CProject> getProjects() {
		return projects;
	}

	public static CProject getProject(String name) {
		CProject result = null;
		for (CProject project : projects) {
			if (project.getName().equalsIgnoreCase(name))
				result = project;
		}
		return result;
	}
}
