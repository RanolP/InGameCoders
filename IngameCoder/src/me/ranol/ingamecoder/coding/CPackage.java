package me.ranol.ingamecoder.coding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ranol.ingamecoder.coding.interfaces.Jsonable;
import me.ranol.ingamecoder.coding.interfaces.TreeViewable;
import me.ranol.ingamecoder.util.Messager;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

public class CPackage extends TreeViewable implements CCompileable, Jsonable {
	File folder;
	List<TreeViewable> classes = new ArrayList<>();
	CProject superProj;

	public CPackage(CProject superProject, String dir, boolean add) {
		folder = new File(superProject.projectFolder, dir);
		if (!getFile().exists())
			getFile().mkdirs();
		setProject(superProject);
		if (add)
			superProject.addPackage(this);
		File[] files = getFile().listFiles();
		if (files != null) {
			for (File f : files) {
				if (f == null)
					continue;
				if (!f.isDirectory()) {
					CClass cls = new CClass(this, f.getName());
					addClass(cls);
				}
			}
		}
	}

	public File getFile() {
		return new File(folder.getAbsolutePath().replace(".", "\\"));
	}

	private void setProject(CProject superProject) {
		this.superProj = superProject;
		ride(superProject);
	}

	public String getDirectory() {
		String ab = folder.getAbsolutePath();
		ab = ab.substring(ab.lastIndexOf("\\") + 1).trim();
		return ab;
	}

	@Override
	public void treeview(Player p) {
		p.spigot().sendMessage(toJson());
		if (isOpened()) {
			classes.forEach(cls -> cls.treeview(p));
			p.spigot().sendMessage(createClass(this));
		}
	}

	private TextComponent createClass(CPackage cPackage) {
		TextComponent real = Messager.get(getSpaces()
				+ "&6[&a&l+&6] &7새 클래스...", "igc c c " + superProj.getName()
				+ " " + getDirectory(), "&a클래스를 새로 만듭니다.");
		return real;
	}

	@Override
	public void compile(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public TextComponent toJson() {
		TextComponent real = Messager.get(getSpaces() + "&b"
				+ (isOpened() ? "▼" : "▶") + " &e" + getDirectory(),
				"igc pa o " + superProj.getName() + " " + getDirectory(),
				isOpened() ? "&a트리 접기" : "&a트리 펼치기");
		return real;
	}

	public void addClass(CClass clazz) {
		classes.add(clazz);
	}

	public CClass getClass(String name) {
		for (TreeViewable clazz : classes) {
			if (clazz instanceof CClass)
				if (((CClass) clazz).getName().equalsIgnoreCase(name))
					return (CClass) clazz;
		}
		return null;
	}
}
