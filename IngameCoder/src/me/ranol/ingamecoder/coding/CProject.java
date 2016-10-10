package me.ranol.ingamecoder.coding;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.ranol.ingamecoder.Commands;
import me.ranol.ingamecoder.InGameCoder;
import me.ranol.ingamecoder.coding.interfaces.Jsonable;
import me.ranol.ingamecoder.coding.interfaces.Saveable;
import me.ranol.ingamecoder.coding.interfaces.TreeViewable;
import me.ranol.ingamecoder.util.FileUtils;
import me.ranol.ingamecoder.util.Messager;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

public class CProject extends TreeViewable implements CCompileable, Jsonable,
		Saveable {
	File projectFolder;
	ProjectFile file;
	List<CPackage> packages = new ArrayList<>();

	public CProject(File projectFolder) {
		if (!projectFolder.getAbsolutePath().contains("\\plugins\\")) {
			projectFolder = new File(InGameCoder.instance().getDataFolder(),
					projectFolder.getAbsolutePath());
		}
		if (!projectFolder.exists())
			projectFolder.mkdirs();
		this.projectFolder = projectFolder;
	}

	public CProject(String s) {
		this(new File(InGameCoder.instance().getDataFolder(), s));
	}

	public void setDescriptionFile(ProjectFile file) {
		this.file = file;
		packages.clear();
		Iterator<String> it = new ArrayList<String>(file.getPackages())
				.iterator();
		while (it.hasNext()) {
			String s = it.next();
			if (getPackage(s.trim()) == null) {
				CPackage temp = new CPackage(this, s.trim(), false);
				packages.add(temp);
			}
		}
	}

	public String getName() {
		return file.getName();
	}

	public File getSaveFolder() {
		return projectFolder;
	}

	public boolean delete() {
		return FileUtils.delete(getSaveFolder());
	}

	public List<CPackage> getPackages() {
		return new ArrayList<>(packages);
	}

	public CPackage getPackage(String name) {
		for (CPackage pack : getPackages()) {
			if (pack.getDirectory().equalsIgnoreCase(name))
				return pack;
		}
		return null;
	}

	public void addPackage(CPackage pack) {
		file.addPackages(pack);
		packages.add(pack);
	}

	@Override
	public void treeview(Player p) {
		Commands.clean(p);
		p.spigot().sendMessage(toJson());
		if (isOpened()) {
			packages.forEach(pack -> pack.treeview(p));
			p.spigot().sendMessage(createPackage(this));
		}
		p.spigot().sendMessage(
				Messager.get("&c◀ 뒤로 돌아가기", "igc r", "&a뒤로 돌아가기"));
		Commands.lastWork(p);
	}

	private static TextComponent createPackage(CProject project) {
		TextComponent real = Messager.get("&6[&a&l+&6] &7새 패키지...", "igc pa c "
				+ project.getName(), "&a패키지를 새로 만듭니다.");
		return real;
	}

	@Override
	public TextComponent toJson() {
		TextComponent real = Messager.get("&b" + (isOpened() ? "▼" : "▶")
				+ " &e" + getName(), "igc p o " + getName(),
				isOpened() ? "&a트리 접기" : "&a트리 펼치기");
		return real;
	}

	@Override
	public void compile(Player p) {

	}

	@Override
	public void save() {
		file.save();
	}
}
