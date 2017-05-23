package me.BartVV.SK.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class CreateGUI extends SkyBlockGUI{

	@Override
	public void openCreate(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, "§6§lSkyBlock");
		setItemStack(inv, Material.WOOL, 0, "§6Create Island", 1, (byte)5, null);
		p.openInventory(inv);
	}
	
	@Override
	public void manageListener(InventoryClickEvent e) {
	}

	public void openOwner(Player p) {
	}
	
	public void openMain(Player p) {
	}
}
