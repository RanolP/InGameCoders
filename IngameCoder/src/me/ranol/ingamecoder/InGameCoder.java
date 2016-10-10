package me.ranol.ingamecoder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InGameCoder extends JavaPlugin {
	private static InGameCoder instance;
	public static Commands cmd;

	@Override
	public void onEnable() {
		String java_ver = System.getProperty("java_ver");
		Bukkit.getConsoleSender().sendMessage("§a현재 자바 버전: " + java_ver);
		instance = this;
		ProjectManager.loadProjects();
		getCommand("ingamecoder").setExecutor(cmd = new Commands());
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
	}

	public static final InGameCoder instance() {
		return instance;
	}
}
