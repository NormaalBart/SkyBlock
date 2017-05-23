package me.BartVV.SK.Manager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
	
	public void setRank(Rank rank){
		this.rank = rank;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public static PlayerManager getPlayerManager(UUID uuid){
		PlayerManager pm = null;
		if (pm == null){
			pm = new PlayerManager(Bukkit.getPlayer(uuid));
		}			
		return pm;
	}
	
	@Deprecated
	public static PlayerManager getPlayerManager(Player p){
		PlayerManager pm = playermanager.get(p.getUniqueId());
		if (pm == null){
			pm = new PlayerManager(Bukkit.getPlayer(p.getUniqueId()));
			
		}			
		return pm;	
	}

	@Deprecated
	public static PlayerManager getPlayerManager(OfflinePlayer p){
		PlayerManager pm = playermanager.get(p.getUniqueId());
		if (pm == null){
			pm = new PlayerManager(Bukkit.getPlayer(p.getUniqueId()));
			
		}			
		return pm;	
	}
	
	@Deprecated
	public static PlayerManager getPlayerManager(String player){
		PlayerManager pm;
		if(Bukkit.getOfflinePlayer(player) != null){
			OfflinePlayer p = Bukkit.getOfflinePlayer(player);
			pm = playermanager.get(p.getUniqueId());
			if (pm == null){
				pm = new PlayerManager(Bukkit.getPlayer(p.getUniqueId()));
				
			}		
		}else if(Bukkit.getPlayer(player) != null){
			Player p = Bukkit.getPlayer(player);
			pm = getPlayerManager(p);
		}else{
			pm = null;
		}
		return pm;	
	}
	
	public void savePlayer(){
		
	}
	public void setIsland(SkyBlockManager skyBlockManager) {
		sbm = skyBlockManager;
		
	}
}
