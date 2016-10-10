package me.ranol.ingamecoder.coding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ranol.ingamecoder.coding.interfaces.Jsonable;
import me.ranol.ingamecoder.coding.interfaces.TextEditable;
import me.ranol.ingamecoder.coding.interfaces.TreeViewable;
import me.ranol.ingamecoder.util.Messager;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

public class CClass extends TreeViewable implements CCompileable, Jsonable,
		TextEditable {
	File classFile;
	CPackage superPackage;
	List<CField> fields = new ArrayList<>();
	List<CMethod> methods = new ArrayList<>();

	public CClass(CPackage superPackage, String dir) {
		if (!dir.endsWith(".java"))
			dir = dir += ".java";
		if (dir.endsWith(".class"))
			dir = dir.substring(0, dir.length() - 6) + ".java";
		classFile = new File(superPackage.getFile(), dir);
		if (!classFile.exists()) {
			try {
				classFile.createNewFile();
			} catch (Exception e) {
			}
		}
		setSuper(superPackage);
	}

	public void setSuper(CPackage pack) {
		this.superPackage = pack;
		ride(pack);
	}

	@Override
	public void compile(Player p) {
		// TODO Auto-generated method stub

	}

	public String getName() {
		return classFile.getName();
	}

	@Override
	public TextComponent toJson() {
		TextComponent real = Messager.get(getSpaces() + "&b"
				+ (isOpened() ? "▼" : "▶") + " &e" + getName(),
				"igc c o " + superPackage.superProj.getName() + " "
						+ superPackage.getDirectory() + " " + getName(),
				isOpened() ? "&a트리 접기" : "&a트리 펼치기");
		real.addExtra(Messager.get("",
				"igc c e " + superPackage.superProj.getName() + " "
						+ superPackage.getDirectory() + " " + getName(),
				"&a소스를 수정합니다."));
		return real;
	}

	@Override
	public void treeview(Player p) {
		p.spigot().sendMessage(toJson());
	}

	@Override
	public void read(Player p) {
	}

}
