package me.BartVV.SK.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.BartVV.SK.Manager.PlayerManager;

public class onJoin implements Listener{
	
	public onJoin(Plugin p){
			p.getServer().getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(PlayerManager.getPlayerManager(p.getUniqueId()) == null){
			new PlayerManager(p);
		}
	}
}
