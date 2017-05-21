package me.BartVV.SK.SkyBlockManager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.BartVV.SK.Enums.Rank;

public class PlayerManager{
	
	private static HashMap<UUID, PlayerManager> playermanager = new HashMap<>();
	private SkyBlockManager sbm;
	private Rank rank = Rank.NO_SKYBLOCK;
	private Player p;
	
	@SuppressWarnings("deprecation")
	public PlayerManager(Player p){
		this.p = p;
		sbm = SkyBlockManager.getSkyBlock(p);
		if (sbm == null){
			rank = Rank.NO_SKYBLOCK;
		}else{
			rank = sbm.getRank(p);
		}
		playermanager.put(p.getUniqueId(), this);
	}
	
	public SkyBlockManager getIsland(){
		return sbm;
	}
	
	public Rank getRank(){
		return rank;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public static PlayerManager getPlayerManager(UUID uuid){
		PlayerManager pm = playermanager.get(uuid);
		if (pm == null){
			pm = new PlayerManager(Bukkit.getPlayer(uuid));
			
		}			
		return pm;
	}

	public void setIsland(SkyBlockManager skyBlockManager) {
		sbm = skyBlockManager;
		
	}
}
