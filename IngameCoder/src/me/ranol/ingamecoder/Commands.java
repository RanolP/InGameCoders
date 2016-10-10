package me.ranol.ingamecoder;

import java.util.HashMap;
import java.util.UUID;

import me.ranol.ingamecoder.ChatListener.ModifyTarget;
import me.ranol.ingamecoder.ChatListener.ModifyType;
import me.ranol.ingamecoder.coding.CClass;
import me.ranol.ingamecoder.coding.CPackage;
import me.ranol.ingamecoder.coding.CProject;
import me.ranol.ingamecoder.ide.UTFMarks;
import me.ranol.ingamecoder.util.Messager;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	public static HashMap<UUID, String> lastWork = new HashMap<>();
	TextComponent addProject = new TextComponent("&6[&a&l+&6] &7새 프로젝트...");

	{
		addProject = Messager.get("&6[&a&l+&6] &7새 프로젝트...", "igc p c",
				"&a프로젝트를 새로 만듭니다.");
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (!(s instanceof Player)) {
			s.sendMessage("&c콘솔은 사용할 수 없습니다!");
			return true;
		}
		Player p = (Player) s;
		if (a.length == 0) {
			ProjectManager.getProjects()
					.forEach(
							(project) -> p.spigot().sendMessage(
									formatProject(project)));
			p.spigot().sendMessage(addProject);
			lastWork(p);
		}
		if (argCheck(a, "p", 0)) {
			if (argCheck(a, "c", 1)) {
				Messager.sendCenterMessage(p, "&a&l※ &7프로젝트를 새로 만듭니다.");
				Messager.sendCenterMessage(p, "&6이름을 채팅으로 적어주세요.");
				Messager.sendCenterMessage(p, "&f'&a&l?cancel&f' &b로 취소합니다.");
				ChatListener
						.setType(p, ModifyTarget.PROJECT, ModifyType.CREATE);
			}
			if (argCheck(a, "o", 1)) {
				CProject proj = ProjectManager.getProject(a[2]);
				proj.setOpened(!proj.isOpened());
				clean(p);
				proj.treeview(p);
			}
			if (argCheck(a, "dc", 1)) {
				CProject proj = ProjectManager.getProject(a[a.length - 1]);
				clean(p);
				p.spigot().sendMessage(delete(proj));
			}
			if (argCheck(a, "d", 1)) {
				CProject proj = ProjectManager.getProject(a[a.length - 1]);
				if (proj.delete()) {
					Commands.setLastWork(p, "프로젝트 " + proj.getName()
							+ "&r을(를) 삭제했습니다!");
					ProjectManager.getProjects().remove(proj);
				} else {
					Commands.setLastWork(p, "프로젝트 " + proj.getName()
							+ "&r을(를) 삭제할 수 없습니다.");
				}
				refresh(p);
			}
			if (argCheck(a, "pv", 1)) {
				CProject proj = ProjectManager.getProject(a[a.length - 1]);
				proj.treeview(p);
			}
		} else if (argCheck(a, "cc", 0)) {
			for (int i = 100; i > 0; i--) {
				s.sendMessage("");
			}
		} else if (argCheck(a, "r", 0)) {
			refresh(p);
		} else if (argCheck(a, "pa", 0)) {
			if (argCheck(a, "c", 1)) {
				Messager.sendCenterMessage(p, "&a&l※ &7패키지를 새로 만듭니다.");
				Messager.sendCenterMessage(p, "&6이름을 채팅으로 적어주세요.");
				Messager.sendCenterMessage(p, "&6대상 프로젝트: " + a[2]);
				Messager.sendCenterMessage(p, "&f'&a&l?cancel&f' &b로 취소합니다.");
				ChatListener.setType(p, ModifyTarget.PACKAGE,
						ModifyType.CREATE, ProjectManager.getProject(a[2]));
			}
			if (argCheck(a, "o", 1)) {
				CProject proj = ProjectManager.getProject(a[2]);
				CPackage pack = proj.getPackage(a[3]);
				pack.setOpened(!pack.isOpened());
				proj.treeview(p);
			}
		} else if (argCheck(a, "c", 0)) {
			if (argCheck(a, "c", 1)) {
				Messager.sendCenterMessage(p, "&a&l※ &7클래스를 새로 만듭니다.");
				Messager.sendCenterMessage(p, "&6이름을 채팅으로 적어주세요.");
				Messager.sendCenterMessage(p, "&6대상 프로젝트: " + a[2]);
				Messager.sendCenterMessage(p, "&6대상 패키지: " + a[3]);
				Messager.sendCenterMessage(p, "&f'&a&l?cancel&f' &b로 취소합니다.");
				CProject proj = ProjectManager.getProject(a[2]);
				CPackage pack = proj.getPackage(a[3]);
				ChatListener.setType(p, ModifyTarget.CLASS, ModifyType.CREATE,
						proj, pack);
			}
			if (argCheck(a, "e", 1)) {
				ProjectManager.getProject(a[2]).getPackage(a[3]).getClass(a[3])
						.read(p);
			}
			if (argCheck(a, "o", 1)) {
				CProject proj = ProjectManager.getProject(a[2]);
				CClass cls = proj.getPackage(a[3]).getClass(a[4]);
				cls.setOpened(!cls.isOpened());
				proj.treeview(p);
			}
		}
		return true;
	}

	public static void lastWork(Player p) {
		if (lastWork.containsKey(p.getUniqueId()))
			Messager.sendCenterMessage(
					p,
					"&6마지막 작업&f: &e"
							+ lastWork.get(p.getUniqueId()).replace("&r", "&e"));
	}

	private boolean argCheck(String[] args, String arg, int index) {
		if (args.length - 1 >= index)
			return args[index].equalsIgnoreCase(arg);
		return false;
	}

	private TextComponent formatProject(CProject project) {
		TextComponent real = Messager.get("&e" + project.getName(), null);
		TextComponent del = Messager.get("&c&l[" + UTFMarks.X_MARK + "]",
				"igc p dc " + project.getName(), "&c" + UTFMarks.CHECK
						+ " 프로젝트를 삭제합니다.");
		TextComponent name = Messager.get("&a&l[" + UTFMarks.PENCIL + "]",
				"igc p rn " + project.getName(), "&a" + UTFMarks.CHECK
						+ " 프로젝트의 이름을 바꿉니다.");
		TextComponent open = Messager.get("&b&l[?]",
				"igc p pv " + project.getName(), "&a" + UTFMarks.CHECK
						+ " 프로젝트르 트리 구조를 봅니다.");
		real.addExtra(del);
		real.addExtra(name);
		real.addExtra(open);
		return real;
	}

	private TextComponent delete(CProject project) {
		TextComponent real = Messager
				.get("&e" + project.getName() + "을 ", null);
		TextComponent del = Messager.get("&c&l[삭제합니다.]",
				"igc p d " + project.getName(), "&c되돌릴 수 없습니다!");
		TextComponent cancel = Messager.get("&a&l[삭제하지 않습니다]", "igc r",
				"&a프로젝트를 놔둡니다.");
		real.addExtra(del);
		real.addExtra(Messager.get("    ", null));
		real.addExtra(cancel);
		return real;
	}

	public static void setLastWork(Player p, String string) {
		lastWork.put(p.getUniqueId(), string);
	}

	public static void clean(Player p) {
		Bukkit.dispatchCommand(p, "igc cc");
	}

	public static void refresh(Player p) {
		clean(p);
		Bukkit.dispatchCommand(p, "igc");
	}
}
