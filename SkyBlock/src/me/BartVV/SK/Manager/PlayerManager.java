package me.BartVV.SK.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.BartVV.SK.Enums.Rank;
import me.BartVV.SK.Utils.SkyBlock;

public class PlayerManager extends PermissionManager{
	
	private static HashMap<UUID, PlayerManager> playermanager = new HashMap<>();

	private SkyBlockManager sbm;
	private Rank rank = Rank.NO_SKYBLOCK;
	private Player p;
	private List<SkyBlockManager> visits = new ArrayList<>();
	
	public PlayerManager(Player p){
		super(p);
		File file = new File(SkyBlock.p.getDataFolder(), "playerdata.yml");
		FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
		String str = "playerdata." + p.getUniqueId().toString() + ".";
		if(SkyBlock.playerdata.getString("playerdata." + p.getUniqueId().toString()) != null){
			this.sbm = SkyBlockManager.getIsland(fc.getInt(str +  "islandID"));
			this.p = p;
			ArrayList<Integer> it = new ArrayList<>(fc.getIntegerList(str + "visits"));
			for(Integer i : it){
				SkyBlockManager sbm = SkyBlockManager.getIsland(i);
				if(sbm != null && !visits.contains(sbm)){
					this.visits.add(sbm);
				}
			}
			fc.set(str + "name", p.getName());
			if(!visits.isEmpty()){
				for(SkyBlockManager sbm : visits){
					fc.set(str + "visits", sbm.getIslandID());
				}	
			}
			if(this.sbm == null){
				fc.set(str + "options.canBreak", false);
				fc.set(str + "options.canEditPermissions", false);
				fc.set(str + "options.canInvite", false);
				fc.set(str + "options.canKick", false);
				fc.set(str + "options.canOpenChest", false);
				fc.set(str + "options.canEditOptions", false);
				fc.set(str + "options.canIslandInvite", false);
			}else{
				if(fc.contains(str + "options.canBreak")){
					canBreakBlocks(fc.getBoolean(str + "options.canBreak"));	
				}
				if(fc.contains(str + "options.canEditPermissions")){
					canEditPermissions(fc.getBoolean(str + "options.canEditPermissions"));
				}
				if(fc.contains(str + "options.canInvite")){
					canInvite(fc.getBoolean(str + "options.canInvite"));
				}
				if(fc.contains(str + "options.canKick")){
					canKick(fc.getBoolean(str + "options.canKick"));	
				}
				if(fc.contains(str + "options.canOpenChest")){
					canOpenChest(fc.getBoolean(str + "options.canOpenChest"));
				}	
				if(fc.contains(str + "options.canEditOptions")){
					CanEditOptions(fc.getBoolean(str + "options.canEditOptions"));
				}	
				if(fc.contains(str + "options.canIslandInvite")){
					CanIslandInvite(fc.getBoolean(str + "options.canIslandInvite"));
				}
			}
			
			try {
				fc.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			this.p = p;
			
			List<Integer> visits = new ArrayList<Integer>();
			fc.set(str + "name", p.getName());
			fc.set(str + "islandID", -1);
			fc.set(str + "visits", visits);
			fc.set(str + ".options.canBreak", false);
			fc.set(str + ".options.canEditPermissions", false);
			fc.set(str + ".options.canInvite", false);
			fc.set(str + ".options.canKick", false);
			fc.set(str + ".options.canOpenChest", false);
			fc.set(str + "options.canEditOptions", false);
			fc.set(str + ".options.canIslandInvite", false);
			try {
				fc.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(fc.contains(str + "options.canBreak")){
				canBreakBlocks(fc.getBoolean(str + "options.canBreak"));	
			}
			if(fc.contains(str + "options.canEditPermissions")){
				canEditPermissions(fc.getBoolean(str + "options.canEditPermissions"));
			}
			if(fc.contains(str + "options.canInvite")){
				canInvite(fc.getBoolean(str + "options.canInvite"));
			}
			if(fc.contains(str + "options.canKick")){
				canKick(fc.getBoolean(str + "options.canKick"));	
			}
			if(fc.contains(str + "options.canOpenChest")){
				canOpenChest(fc.getBoolean(str + "options.canOpenChest"));
			}	
			if(fc.contains(str + "options.canEditOptions")){
				CanEditOptions(fc.getBoolean(str + "options.canEditOptions"));
			}	
			if(fc.contains(str + "options.canIslandInvite"))
				CanIslandInvite(fc.getBoolean(str + "options.canIslandInvite"));
		}
		PlayerManager.playermanager.put(p.getUniqueId(), this);
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
		PlayerManager pm = playermanager.get(uuid);
		if (pm == null){
			pm = new PlayerManager(Bukkit.getPlayer(uuid));
			Bukkit.broadcastMessage("Created");
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

	public void setIsland(SkyBlockManager skyBlockManager) {
		this.sbm = skyBlockManager;
	}
	
	public void removePlayerManager(){
		playermanager.remove(this);
	}
	
	public static void removePlayerManager(PlayerManager pm){
		playermanager.remove(pm);
	}
	
	public static void clearPlayerManager(){
		playermanager.clear();
	}

	
	public List<SkyBlockManager> getVisits() {
		return visits;
	}

	public void addVisits(SkyBlockManager sbm) {
		visits.add(sbm);
	}

	public void removeVisits(SkyBlockManager sbm) {
		visits.remove(sbm);
	}
	
	public void save(){
		File file = new File(SkyBlock.p.getDataFolder(), "playerdata.yml");
		FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
		String str = "playerdata." + p.getUniqueId().toString() + ".";
		
		List<Integer> visits = new ArrayList<Integer>();
		for(SkyBlockManager sbm : this.visits){
			visits.add(sbm.getIslandID());
		}
		fc.set(str + "name", p.getName());
		if(this.sbm == null){
			fc.set(str + "islandID", -1);
		}else{
			fc.set(str + "islandID", sbm.getIslandID());
		}
		fc.set(str + "visits", visits);
		fc.set(str + "options.canBreak", canBreakBlocks());
		fc.set(str + "options.canEditPermissions", canEditPermissions());
		fc.set(str + "options.canInvite", canInvite());
		fc.set(str + "options.canKick", canKick());
		fc.set(str + "options.canOpenChest", canOpenChest());
		fc.set(str + "options.canEditOptions", CanEditOptions());
		fc.set(str + "options.canIslandInvite", CanIslandInvite());
		try {
			fc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		playermanager.remove(this.p.getUniqueId(), this);
	}
}
