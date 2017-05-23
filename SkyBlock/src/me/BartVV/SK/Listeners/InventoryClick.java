package me.BartVV.SK.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import me.BartVV.SK.GUI.MainGUI;
import me.BartVV.SK.Utils.SkyBlock;

public class InventoryClick implements Listener{
	
	public InventoryClick(Plugin p){
		p.getServer().getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void on(InventoryClickEvent e){
		if (e.getClickedInventory() != null){
			if(e.getClickedInventory().getTitle() == SkyBlock.prefix){
				Bukkit.broadcastMessage("1");
				MainGUI gui = new MainGUI();
				gui.manageListener(e);
			}
		}
	}

}
