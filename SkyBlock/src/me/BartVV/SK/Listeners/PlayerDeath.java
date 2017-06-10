package me.BartVV.SK.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import me.BartVV.SK.Manager.PlayerManager;

public class PlayerDeath implements Listener{
	
	public PlayerDeath(Plugin p){
		p.getServer().getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void on(PlayerDeathEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
			if(pm.getIsland() != null){
				p.spigot().respawn();
				p.teleport(pm.getIsland().getSpawnLocation());
			}else{
				p.teleport(new Location(p.getWorld(), 0, 100, 0, 180, 0));
			}
		}
	}

}
