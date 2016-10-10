package me.ranol.ingamecoder.coding.interfaces;

import org.bukkit.entity.Player;

public abstract class TreeViewable {
	private static final int SPACE_SIZE = 2;
	int index = 0;
	private boolean opened = false;

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean b) {
		this.opened = b;
	}

	public String getSpaces() {
		StringBuilder sb = new StringBuilder();
		for (int j = SPACE_SIZE * index; j > 0; j--, sb.append(" "))
			;
		return sb.toString();
	}

	public void ride(TreeViewable view) {
		this.index = view.index + 1;
	}

	public abstract void treeview(Player p);
}
