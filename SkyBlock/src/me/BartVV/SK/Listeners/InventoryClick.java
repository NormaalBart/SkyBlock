package me.BartVV.SK.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import me.BartVV.SK.GUI.CreateGUI;
import me.BartVV.SK.GUI.DeleteGUI;
import me.BartVV.SK.GUI.ManageInviteGUI;
import me.BartVV.SK.GUI.ManagePermissionsGUI;
import me.BartVV.SK.GUI.OptionsGUI;
import me.BartVV.SK.GUI.VisitGUI;

public class InventoryClick implements Listener{
	
	public InventoryClick(Plugin p){
		p.getServer().getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void on(InventoryClickEvent e){
		Bukkit.broadcastMessage("!");
		if (e.getClickedInventory() != null && e.getWhoClicked() instanceof Player){;
			String name = e.getClickedInventory().getTitle();
			Bukkit.broadcastMessage("0 " + name);
			if(name.equalsIgnoreCase("§7(§9Create GUI§7)")){
				CreateGUI.getInstance().manageListener(e);
			}else if (name.equalsIgnoreCase("§7(§9Delete GUI§7)")){
				DeleteGUI.getInstance().manageListener(e);
			}else if (name.equalsIgnoreCase("§7(§9Options Menu§7)")){
				OptionsGUI.getInstance().manageListener(e);
			}else if (name.equalsIgnoreCase("§7(§9Visit GUI§7)")){
				VisitGUI.getInstance().manageListener(e);
			}else if (name.equalsIgnoreCase("§7(§9Invite Management§7)")){
				Bukkit.broadcastMessage("1");
				ManageInviteGUI.getInstance().manageListener(e);
			}else if(name.equalsIgnoreCase("§7(§9Permission Management§7)")){
				ManagePermissionsGUI.getInstance().manageListener(e);
			}
		}
	}

}
