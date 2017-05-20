package me.BartVV.SK.SkyBlockManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitServerInterface;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

import me.BartVV.SK.Enums.Rank;
import me.BartVV.SK.Utils.SkyBlock;

@SuppressWarnings("deprecation")
public class SkyBlockManager {
	
	private static Random r = new Random();
	private static ArrayList<SkyBlockManager> SkyBlocks = new ArrayList<>();
	private String ownername;
	private UUID owner;
	private List<UUID> co_owners = new ArrayList<>();
	private List<UUID> members = new ArrayList<>();
	private Location loc;
	private Integer maxplayers;

	public SkyBlockManager(Player owner, Integer maxplayers){
		this.ownername = owner.getName();
		this.owner = owner.getUniqueId();
		this.maxplayers = maxplayers;
	}
	
	
	/*
	 * Creating a island for the owner...
	 */
	public void createIsland(){
		Player p = Bukkit.getPlayer(owner);
		if (p == null) return;
		
		pasteSchematica(p, SkyBlock.loc);
		this.loc = SkyBlock.loc;
		p.sendMessage("Island created! Teleporting...");
		p.teleport(this.loc);
		SkyBlock.loc = SkyBlock.loc.add(0.0, 0.0, 100);
		SkyBlocks.add(this);
	}
	
	/*
	 * Adding a player to the island
	 */
	public boolean addPlayer(Player p){
		if (getPlayers().size() >= maxplayers){
			return false;
		}else{
			members.add(p.getUniqueId());
			TeleportToIsland(p, this);
			return true;
		}
	}
	
	/*
	 * Teleporting a player to the island
	 * Can throw a NullPointerException when the SkyBlockManager is null, of the Spawn Location is null...
	 */
	public static void TeleportToIsland(Player p, SkyBlockManager sbm){
		Location loc = sbm.getSpawnLocation();
		if(loc == null){
			throw new NullPointerException();
		}else{
			p.teleport(loc);	
		}
	}
	
	/*
	 * Get the spawn point of the island
	 */
	public Location getSpawnLocation(){
		if(loc == null){
			throw new NullPointerException();
		}else{
			return loc;	
		}
	}
	/*
	 * Getting the rank of the player
	 */
	public Rank getRank(Player p){
		if (owner == p.getUniqueId()){
			return Rank.OWNER;
		}
		else if (co_owners.contains(p.getUniqueId())){
			return Rank.CO_OWNER;
		}
		else if (members.contains(p.getUniqueId())){
			return Rank.MEMBER;
		}else{
			return Rank.NO_SKYBLOCK;	
		}
	}
	
	
	/*
	 * Get all the players of the skyblock.
	 */
	public List<OfflinePlayer> getPlayers(){
		List<OfflinePlayer> players= new ArrayList<>();
		players.add(Bukkit.getOfflinePlayer(owner));
		for (UUID p : co_owners){
			players.add(Bukkit.getOfflinePlayer(p));
		}
		for (UUID p : members){
			players.add(Bukkit.getOfflinePlayer(p));
		}
		return players;
	}
	
	
	/*
	 * Get the All skyblocks.
	 */
	public static ArrayList<SkyBlockManager> getSkyBlocks(){
		return SkyBlocks;
	}
	
	
	/*
	 * Get the owner's name
	 */
	public String getOwnerName(){
		return ownername;
	}

	
	/*
	 * Get the SkyBlock of the player
	 * Throws NullPointerException when the skyblock of the players isn't found
	 * Or when the player isn't in a skyblock
	 */
	public static SkyBlockManager getSkyBlock(Player p){
		for (SkyBlockManager sbm : getSkyBlocks()){
			if (sbm.getPlayers().contains(p)){
				return sbm;
			}
		}
		throw new NullPointerException();
	}
	
	private void pasteSchematica(Player p, Location focus){
		File file;
		Integer files = SkyBlock.schematica.listFiles().length;
		Bukkit.broadcastMessage("Files: " + files);
		Integer random = r.nextInt(files+1);
		if (random == 0 || random > files+1){
			random = 1;
		}
		file = new File(SkyBlock.schematica + "/SB" + random + ".schematic");
		if (!file.exists() || file == null){
			file = new File(SkyBlock.schematica + "/SB1.schematic");
			if (!file.exists() || file == null){
				Logger log = SkyBlock.p.getLogger();
				p.sendMessage(SkyBlock.prefix + "§cFailed to create your island!");
				log.info("===================");
				log.info("   ");
				log.info("Failed to creating the island of " + p.getName());
				log.info("Disabling....");
				log.info("   ");
				log.info("===================");
				Bukkit.getPluginManager().disablePlugin(SkyBlock.p);
			}
		}
		if (file.exists() && !file.isDirectory()) {
		    /* Paste schematic */
			SchematicFormat format = SchematicFormat.getFormat(file);
			CuboidClipboard WEclipboard = null;
	        try {
	        	WEclipboard = format.load(file);
			}catch (DataException | IOException e1) {
				e1.printStackTrace();
			}
		    if (WEclipboard != null) {
		        BukkitServerInterface WEinterface = new BukkitServerInterface((WorldEditPlugin)Bukkit
		        		.getPluginManager().getPlugin("WorldEdit"), Bukkit.getServer());
		        EditSession WEsessionEdit = null;
		        for (LocalWorld WEworld : WEinterface.getWorlds()) {
		            if (WEworld.getName().equalsIgnoreCase(focus.getWorld().getName())) {
		                WEsessionEdit = new EditSession(WEworld, -1);
		                break;
		            }
		        }
		        Vector WEvector = new Vector(focus.getX(), focus.getY(), focus.getZ());
		        try {
		            WEclipboard.paste(WEsessionEdit, WEvector, false);
		        } catch (MaxChangedBlocksException e) {
		        	e.printStackTrace();
		        }
		     }
		 }	
	}
}
