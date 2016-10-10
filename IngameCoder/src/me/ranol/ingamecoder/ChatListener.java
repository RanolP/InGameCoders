package me.ranol.ingamecoder;

import java.util.HashMap;
import java.util.UUID;

import me.ranol.ingamecoder.coding.CClass;
import me.ranol.ingamecoder.coding.CPackage;
import me.ranol.ingamecoder.coding.CProject;
import me.ranol.ingamecoder.coding.ProjectFile;
import me.ranol.ingamecoder.util.Messager;
import me.ranol.ingamecoder.util.SimpleOptionFile;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	private static HashMap<UUID, ModifyType> type = new HashMap<>();
	private static HashMap<UUID, Object[]> args = new HashMap<>();
	private static HashMap<UUID, ModifyTarget> target = new HashMap<>();

	public enum ModifyTarget {
		PROJECT, CLASS, METHOD, FIELD, YAML, PACKAGE
	}

	public enum ModifyType {
		CREATE, REMOVE, EDIT
	}

	public static void setType(Player p, ModifyTarget target, ModifyType type,
			Object... args) {
		ChatListener.target.put(p.getUniqueId(), target);
		ChatListener.type.put(p.getUniqueId(), type);
		ChatListener.args.put(p.getUniqueId(), args);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		UUID uid = e.getPlayer().getUniqueId();
		if (!target.containsKey(uid))
			return;
		if (!type.containsKey(uid))
			return;
		Player p = e.getPlayer();
		boolean cancel = e.getMessage().equalsIgnoreCase("?cancel");
		a: switch (type.get(uid)) {
		case CREATE:
			switch (target.get(uid)) {
			case PROJECT:
				if (cancel) {
					Commands.setLastWork(p, "생성을 취소합니다.");
					Commands.refresh(p);
					break a;
				}
				if (ProjectManager.getProject(e.getMessage()) != null) {
					Messager.warning(p, "프로젝트 " + e.getMessage()
							+ "&r은(는) 이미 존재합니다!");
					break;
				}
				CProject p_proj = new CProject(e.getMessage());
				SimpleOptionFile sof = new SimpleOptionFile(
						p_proj.getSaveFolder(), ".project");
				sof.setValue("project-name", e.getMessage());
				sof.save();
				p_proj.setDescriptionFile(new ProjectFile(sof));
				Commands.setLastWork(p, "프로젝트 " + e.getMessage()
						+ "&r을(를) 만들었습니다.");
				ProjectManager.getProjects().add(p_proj);
				Commands.refresh(p);
				break a;
			case PACKAGE:
				CProject pa_proj = (CProject) args.get(uid)[0];
				if (cancel) {
					Commands.setLastWork(p, "생성을 취소합니다.");
					pa_proj.treeview(p);
					break a;
				}
				CPackage pa_pack = new CPackage(pa_proj, e.getMessage(), true);
				Commands.setLastWork(p, "프로젝트 " + pa_proj.getName() + "에 패키지 "
						+ pa_pack.getDirectory() + "을(를) 만들었습니다.");
				Commands.clean(p);
				pa_proj.treeview(p);
				break a;
			case CLASS:
				CProject c_proj = (CProject) args.get(uid)[0];
				if (cancel) {
					Commands.setLastWork(p, "생성을 취소합니다.");
					c_proj.treeview(p);
					break a;
				}
				CPackage c_pack = (CPackage) args.get(uid)[1];
				CClass c_cls = new CClass(c_pack, e.getMessage());
				c_pack.addClass(c_cls);
				Commands.setLastWork(p, "프로젝트 " + c_proj.getName() + "에 패키지 "
						+ c_pack.getDirectory() + " 안에 " + c_cls.getName()
						+ "을(를) 만들었습니다.");
				Commands.clean(p);
				c_proj.treeview(p);
				break a;
			default:
				break;
			}
		default:
			if (!cancel)
				Commands.setLastWork(p, "아무 일도 하지 않았습니다.");
			Commands.refresh(p);
			break;
		}
		type.remove(uid);
		args.remove(uid);
		e.setCancelled(true);
	}
}
