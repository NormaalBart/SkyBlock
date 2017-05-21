package me.BartVV.SK.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.BartVV.SK.SkyBlockManager.PlayerManager;

public class onJoin implements Listener{
	
	@EventHandler
	public void on(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(PlayerManager.getPlayerManager(p.getUniqueId()) == null){
			PlayerManager pm = new PlayerManager(p);
			e.setJoinMessage(null);
		}
	}
}
