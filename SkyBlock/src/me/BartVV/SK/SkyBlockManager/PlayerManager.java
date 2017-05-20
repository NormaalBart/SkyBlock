package me.BartVV.SK.SkyBlockManager;

import org.bukkit.entity.Player;

import me.BartVV.SK.Enums.Rank;

public class PlayerManager{
	
	private SkyBlockManager sbm;
	private Rank rank = Rank.NO_SKYBLOCK;
	private Player p;
	
	public PlayerManager(Player p){
		this.p = p;
		sbm = SkyBlockManager.getSkyBlock(p);
		if (sbm == null){
			rank = Rank.NO_SKYBLOCK;
		}else{
			rank = sbm.getRank(p);
		}
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
}
