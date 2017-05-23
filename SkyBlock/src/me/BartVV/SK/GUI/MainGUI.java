package me.BartVV.SK.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.BartVV.SK.Utils.SkyBlock;

public class MainGUI extends SkyBlockGUI {

	@Override
	public void openMain(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, SkyBlock.prefix);
		setItemStack(inv, Material.WOOL, 0, "§6Create island", 1, (byte) 5, null);
		p.openInventory(inv);
	}
	
	@Override
	public void manageListener(InventoryClickEvent e) {
		if(e.isShiftClick()){
			e.setCancelled(true);
			return;
		}
		Integer slot = e.getSlot();
		if(slot == 0){
			e.setCancelled(true);
			CreateGUI gui = new CreateGUI();
			gui.manageListener(e);
		}
	}
	
	public void openCreate(Player p) {
	}

	public void openOwner(Player p) {
	}


}
