package me.BartVV.SK.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.BartVV.SK.Manager.PlayerManager;

public class onLeave implements Listener{
	
	public onLeave(Plugin p){
		p.getServer().getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e){
		Player p = e.getPlayer();
		PlayerManager.getPlayerManager(p.getUniqueId()).save();
	}
}
