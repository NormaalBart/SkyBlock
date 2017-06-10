package me.BartVV.SK.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitServerInterface;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.BartVV.SK.Enums.Rank;
import me.BartVV.SK.Utils.SkyBlock;

@SuppressWarnings("deprecation")
public class SkyBlockManager {
	
	private static Random r = new Random();
	private static ArrayList<Location> freelocation = new ArrayList<>();
	private static HashMap<Integer, SkyBlockManager> SkyBlocks = new HashMap<>();
	private Boolean changed = false;
	private String ownername;
	private UUID owner;
	private List<UUID> members = new ArrayList<>();
	private ArrayList<UUID> visitors = new ArrayList<>();
	private Location loc;
	private Integer maxplayers;
	private ProtectedRegion region;
	private Integer islandid;

	public SkyBlockManager(Player owner, Integer maxplayers){
		this.ownername = owner.getName();
		this.owner = owner.getUniqueId();
		this.maxplayers = maxplayers;
		this.islandid = SkyBlocks.size()+1;
		PlayerManager pm = PlayerManager.getPlayerManager(owner.getUniqueId());
		if(pm == null){
			pm = new PlayerManager(owner);
		}
		pm.setIsland(this);
		pm.setRank(Rank.OWNER);
		setChanged(true);
		if(SkyBlocks.containsKey(SkyBlocks.size()+1)){
			for(int i = SkyBlocks.size(); SkyBlocks.containsKey(i); i++){
				if(!SkyBlocks.containsKey(i)){
					this.islandid = i;
					break;
				}
			}
		}
	}
	
	public SkyBlockManager(Player owner, Integer maxplayers, Location loc, ProtectedRegion region, 
			Integer id, List<UUID> members, ArrayList<UUID> visitors){
		this.owner = owner.getUniqueId();
		this.ownername = owner.getName();
		this.maxplayers = maxplayers;
		this.loc = loc;
		this.region = region;
		this.islandid = id;
		this.members = members;
		this.visitors = visitors;
		SkyBlocks.put(id, this);
	}
	
	public SkyBlockManager(OfflinePlayer owner, Integer maxplayers, Location loc, ProtectedRegion region, 
			Integer id, List<UUID> members, ArrayList<UUID> visitors){
		this.owner = owner.getUniqueId();
		this.ownername = owner.getName();
		this.maxplayers = maxplayers;
		this.loc = loc;
		this.region = region;
		this.islandid = id;
		this.members = members;
		this.visitors = visitors;
		SkyBlocks.put(id, this);	
	}
	
	/*
	 * Creating a island for the owner...
	 */
	public void createIsland(){
		Player p = Bukkit.getPlayer(owner);
		if (p == null) return;
		if(getfreeLocations().isEmpty()){
			if(pasteSchematica(p, SkyBlock.loc)){
				p.sendMessage(SkyBlock.prefix + "Creating your island...");
				p.sendMessage(SkyBlock.prefix + "§aIsland created! §7Teleporting");
				this.loc = new Location(SkyBlock.loc.getWorld(), SkyBlock.loc.getX(), SkyBlock.loc.getY(), SkyBlock.loc.getZ(), 180, 0);	
				PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
				pm.setIsland(this);
			}else{
				return;
			}
		}else{
			Location loc = getfreeLocations().get(0);
			if(pasteSchematica(p, loc)){
				p.sendMessage(SkyBlock.prefix + "Creating your island...");
				p.sendMessage(SkyBlock.prefix + "§aIsland created! §7Teleporting");
				this.loc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), 180, 0);	
				PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
				pm.setIsland(this);
			}else{
				return;
			}
		}
		SkyBlock.loc = SkyBlock.loc.add(0.0, 0.0, 1000);
		SkyBlockManager.SkyBlocks.put(this.islandid, this);
		createRegion(p);
		setChanged(true);
	}
	
	public void deleteIsland(){
		Player p = Bukkit.getPlayer(owner);
		if(p == null) return;
		p.teleport(new Location(p.getWorld(), 0, 200, 0));
		removeskyblock(this.loc);
		freelocation.add(this.loc);
		Bukkit.broadcastMessage("SIZE: " + SkyBlocks.size());
		setChanged(true);
		for(OfflinePlayer of : getPlayers()){
			PlayerManager pm = PlayerManager.getPlayerManager(of.getUniqueId());
			pm.setIsland(null);
			if(!of.isOnline()) pm.save();
		}
		File file = new File(SkyBlock.p.getDataFolder(), "skyblockdata.yml");
		FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
		fc.set("skyblockdata." + this.getIslandID(), null);
		try {
			fc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SkyBlocks.remove(this.getIslandID());
	}
	
	/*
	 * Adding a player to the island
	 */
	public void addPlayer(Player p){
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		members.add(p.getUniqueId());
		PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
		pm.setIsland(this);
		TeleportToIsland(p, this);
		setChanged(true);
		return;
	}
	
	public Integer getMaxPlayers(){
		return this.maxplayers;
	}
	
	public Integer getIslandID(){
		return this.islandid;
	}
	
	/*
	 * Teleporting a player to the island
	 */
	
	public static void TeleportToIsland(Player p, SkyBlockManager sbm){
		Location loc = sbm.getSpawnLocation();
		if(loc == null){
			return;
		}else{
			p.teleport(loc);	
		}
	}
	
	public static List<SkyBlockManager> getIslandIDs(List<Integer> i){
		List<SkyBlockManager> lsbm = new ArrayList<>();
		for(Integer id : i){
			SkyBlockManager sbm = SkyBlocks.get(id);
			if(sbm != null){
				lsbm.add(sbm);
			}
		}
		return lsbm;
	}
	
	public static SkyBlockManager getIsland(Integer id){
		SkyBlockManager sbm = SkyBlocks.get(id);
		if(sbm != null){
			return sbm;
		}else{
			return null;
		}
	}
	
	
	/*
	 * Get the spawn point of the island
	 */
	public Location getSpawnLocation(){
		if(loc == null){
			return null;
		}else{
			Location loc = new Location(this.loc.getWorld(), 
					this.loc.getX() + 0.5, 
					this.loc.getY() + 0.2, 
					this.loc.getZ() + 0.5, 
					this.loc.getYaw(), 
					this.loc.getPitch());
			return loc;	
		}
	}
	
	public ArrayList<UUID> getVisitors(){
		return visitors;
	}
	
	public void addVisitor(UUID uuid){
		this.visitors.add(uuid);
	}
	
	public void removeVisitor(UUID uuid){
		this.visitors.remove(uuid);
	}
	
	public ProtectedRegion getRegion(){
		return region;
	}
	
	@Deprecated
	public Location getRawLocation(){
		return this.loc;
	}
	/*
	 * @Param Player getting the rank of that specifiek Player
	 * Deprecated because you can get it better from the player class.
	 * Getting the rank of the player
	 */
	@Deprecated
	public Rank getRank(Player p){
		if (owner == p.getUniqueId()){
			return Rank.OWNER;
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
		for (UUID p : members){
			players.add(Bukkit.getOfflinePlayer(p));
		}
		return players;
	}
	
	
	/*
	 * Get the All skyblocks.
	 */
	public static Collection<SkyBlockManager> getSkyBlocks(){
		return SkyBlocks.values();
	}
	
	
	/*
	 * Get the owner's name
	 */
	public String getOwnerName(){
		return ownername;
	}
	
	public UUID getOwnerUUID(){
		return this.owner;
	}
	
	public List<UUID> getMembers(){
		return this.members;
	}

	public Integer getIntOnlinePlayers(){
		Integer count = 0;
		for(OfflinePlayer of : getPlayers()){
			if(of.isOnline()){
				count = count + 1;
			}
		}
		return count;
	}
	
	/*
	 * @Param Player for getting the skyblock of that certain player
	 * Deprecated because you can get it better from the PlayerManager.
	 * 
	 * Get the SkyBlock of the player
	 */
	@Deprecated
	public static SkyBlockManager getSkyBlock(Player p){
		for (SkyBlockManager sbm : getSkyBlocks()){
			if (sbm.getPlayers().contains(p)){
				return sbm;
			}
		}
		return null;
	}
	
	@Deprecated
	public static SkyBlockManager getIsland(Player p){
		SkyBlockManager sbm = null;
		for(SkyBlockManager sb : SkyBlocks.values()){
			if(sb.getPlayers().contains(p)){
				sbm = sb;
			}
		}
		return sbm;
	}
	
	@Deprecated
	public static SkyBlockManager getIsland(OfflinePlayer p){
		SkyBlockManager sbm = null;
		for(SkyBlockManager sb : SkyBlocks.values()){
			if(sb.getPlayers().contains(p)){
				sbm = sb;
			}
		}
		return sbm;
	}
	
	private ArrayList<Location> getfreeLocations(){
		return freelocation;
	}
	
	
	private Boolean pasteSchematica(Player p, Location focus){
		File file;
		Integer files = SkyBlock.schematica.listFiles().length;
		Integer random = r.nextInt(files+1);
		if (random == 0 || random > files+1){
			random = 1;
		}
		file = new File(SkyBlock.schematica + "/SB" + random + ".schematic");
		
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
		        	if(WEworld.getName() == null);
		        	else if(WEworld.getName().equalsIgnoreCase(focus.getWorld().getName())) {
		                WEsessionEdit = new EditSession(WEworld, -1);
		                break;
		            }
		        }
		        Vector WEvector = new Vector(focus.getX(), focus.getY(), focus.getZ());
		        try {
		    		setChanged(true);
		            WEclipboard.paste(WEsessionEdit, WEvector, false);
		            return true;
		        } catch (MaxChangedBlocksException e) {
		        	e.printStackTrace();
		        	return false;
		        }
		     }
		 }else{
			p.sendMessage(SkyBlock.prefix + "An error occured!"); 
			p.sendMessage(SkyBlock.prefix + "We will soon check what happens");
			p.sendMessage(SkyBlock.prefix + "In the mean time, you may be mining to a higher prison rank?");
			if(p.getOpenInventory() != null){
				p.closeInventory();
			}
			for(Player all : Bukkit.getOnlinePlayers()){
				if(all.hasPermission("skymines.admin")){
					all.sendMessage(SkyBlock.prefix + "=====§aSkyMines§7=====");
					all.sendMessage(" ");
					all.sendMessage("§7An error occured while attempting to create §a" + p.getName() + "'s §7island!");
					all.sendMessage("§7Can someone please check if there are still schematica's in the file directory?");
					all.sendMessage("§7Directory: plugins/SkyBlock/Schematicas");
					all.sendMessage("§7If there isn't please create some!");
					all.sendMessage(" ");
					all.sendMessage(SkyBlock.prefix + "=====§aSkyMines§7=====");
				}
			}
			return false;
		 }
		p.sendMessage(SkyBlock.prefix + "An error occured!"); 
		p.sendMessage(SkyBlock.prefix + "We will soon check what happens");
		p.sendMessage(SkyBlock.prefix + "In the mean time, you may be mining to a higher prison rank?");
		if(p.getOpenInventory() != null){
			p.closeInventory();
		}
		for(Player all : Bukkit.getOnlinePlayers()){
			if(all.hasPermission("skymines.admin")){
				all.sendMessage(SkyBlock.prefix + "=====§aSkyMines§7=====");
				all.sendMessage(" ");
				all.sendMessage("§7An error occured while attempting to create §a" + p.getName() + "'s §7island!");
				all.sendMessage("§7Can someone please check if there are still schematica's in the file directory?");
				all.sendMessage("§7Directory: plugins/SkyBlock/Schematicas");
				all.sendMessage("§7If there isn't please create some!");
				all.sendMessage(" ");
				all.sendMessage(SkyBlock.prefix + "=====§aSkyMines§7=====");
			}
		}
		return false;
	}
	
	private void removeskyblock(Location focus){
        BukkitServerInterface WEinterface = new BukkitServerInterface((WorldEditPlugin)Bukkit
        		.getPluginManager().getPlugin("WorldEdit"), Bukkit.getServer());
        EditSession WEsessionEdit = null;
        for (LocalWorld WEworld : WEinterface.getWorlds()) {
            if (WEworld.getName().equalsIgnoreCase(focus.getWorld().getName())) {
                WEsessionEdit = new EditSession(WEworld, -1);
                break;
            }
        }
        BlockVector v = region.getMaximumPoint();
		BlockVector v2 = region.getMinimumPoint();
		CuboidSelection sel = new CuboidSelection(focus.getWorld(), v, v2);
		try {
			Region r = sel.getRegionSelector().getRegion();
			setChanged(true);
			WEsessionEdit.setBlocks(r, new BaseBlock(0));
		} catch (IncompleteRegionException | MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}
	
	private void createRegion(Player p){
		Location top = new Location(loc.getWorld(), loc.getX() + 200, 256, loc.getZ() + 200);
		Location down = new Location(loc.getWorld(), loc.getX() - 200, 0, loc.getZ() - 200);
		BlockVector btop = convertToBlockVector(top);
		BlockVector bdown = convertToBlockVector(down);
		ProtectedCuboidRegion reg = new ProtectedCuboidRegion(p.getName() + "_" + p.getUniqueId(), btop, bdown);
		DefaultDomain owner = new DefaultDomain();
		owner.addPlayer(SkyBlock.wg.wrapPlayer(p));
		reg.setOwners(owner);
		SkyBlock.wg.getRegionManager(loc.getWorld()).addRegion(reg);
		setChanged(true);
		this.region = SkyBlock.wg.getRegionManager(loc.getWorld()).getRegion(p.getName() + "_" + p.getUniqueId());
	}
	
	private com.sk89q.worldedit.BlockVector convertToBlockVector(Location location){
		return new com.sk89q.worldedit.BlockVector(location.getX(),location.getY(),location.getZ());
	}

	public Boolean getChanged() {
		return changed;
	}

	private void setChanged(Boolean changed) {
		this.changed = changed;
	}
	
	public static void saveSkyBlocks(){
		try{
			File file = new File(SkyBlock.p.getDataFolder(), "skyblockdata.yml");
			FileConfiguration sf = YamlConfiguration.loadConfiguration(file);
			Bukkit.broadcastMessage("Size:" + SkyBlocks.size());
			for(Integer ui : SkyBlocks.keySet()){
				Bukkit.broadcastMessage("" + ui);
				SkyBlockManager sbm = SkyBlocks.get(ui);
				if(sbm != null){
					String str = "skyblockdata." + sbm.getIslandID();
					sf.set(str + ".ownerUUID", sbm.getOwnerUUID().toString());
					sf.set(str + ".location.world", sbm.getRawLocation().getWorld().getName());
					sf.set(str + ".location.X", sbm.getRawLocation().getBlockX());
					sf.set(str + ".location.Y", sbm.getRawLocation().getBlockY());
					sf.set(str + ".location.Z", sbm.getRawLocation().getBlockZ());
					sf.set(str + ".location.Yaw", sbm.getRawLocation().getYaw());
					sf.set(str + ".location.Pitch", sbm.getRawLocation().getPitch());
					sf.set(str + ".region", sbm.getRegion().getId());
					for(UUID u : sbm.getMembers()){
						sf.set(str + ".members", u.toString());	
					}
					sf.set(str + ".maxplayers", sbm.maxplayers);	
					for(UUID u : sbm.getVisitors()){
						sf.set(str + ".visitors", u.toString());	
					}
				}else{
					java.util.logging.Logger l = SkyBlock.p.getLogger();
					l.warning("=============");
					l.warning(" ");
					l.warning("Failed to save the island of" + ui);
					l.warning(" ");
					l.warning("=============");
				}
				try {
					sf.save(file);
				} catch (IOException e) {
					java.util.logging.Logger l = SkyBlock.p.getLogger();
					l.warning("=============");
					l.warning(" ");
					l.warning("Failed to save the island of" + sbm.getOwnerName());
					l.warning("Message: " + e.getMessage());
					l.warning(" ");
					l.warning("=============");
				}
			}	
		}catch(NullPointerException npe){
			java.util.logging.Logger l = SkyBlock.p.getLogger();
			l.warning("=============");
			l.warning(" ");
			l.warning("Didn't save any islands!");
			l.warning("Message: " + npe.getMessage());
			l.warning(" ");
			l.warning("=============");
		}
	}
	
	public static void loadSkyBlocks(){
		try{
			File file = new File(SkyBlock.p.getDataFolder(), "skyblockdata.yml");
			FileConfiguration sf = YamlConfiguration.loadConfiguration(file);
			String str = "skyblockdata.";
			for(String s : sf.getConfigurationSection("skyblockdata").getKeys(false)){
				Bukkit.broadcastMessage(s);
				Integer i;
				try{
					i = Integer.parseInt(s);	
					str = "skyblockdata." + i + ".";
				}catch(NumberFormatException nfe){
					java.util.logging.Logger l = SkyBlock.p.getLogger();
					l.warning("=============");
					l.warning(" ");
					l.warning("There is a string instead of a integer in SkyBlock data!");
					l.warning("Aborting...");
					l.warning("Message: " + nfe.getMessage());
					l.warning("String: " + s);
					l.warning(" ");
					l.warning("=============");
					return;
				}
				OfflinePlayer of = Bukkit.getOfflinePlayer(UUID.fromString(sf.getString(str + "ownerUUID")));
				Integer mp = Integer.parseInt(sf.getString(str + "maxplayers"));
				String l = str + "location.";
				World w = Bukkit.getWorld(sf.getString(l + "world"));
				Integer x = Integer.parseInt(sf.getString(l + "X"));
				Integer y = Integer.parseInt(sf.getString(l + "Y"));
				Integer z = Integer.parseInt(sf.getString(l + "Z"));
				Float yaw = Float.parseFloat(sf.getString(l + "Yaw"));
				Float pitch = Float.parseFloat(sf.getString(l + "Pitch"));
				Location loc = new Location(w,x,y,z,yaw,pitch);
				ProtectedRegion pr = SkyBlock.wg.getRegionManager(w).getRegion(sf.getString(str + "region"));
				List<UUID> members = new ArrayList<>();
				ArrayList<UUID> visitors = new ArrayList<>();
				
				for(String u : sf.getStringList(str + "members")){
					members.add(UUID.fromString(u));
				}
				if(members.isEmpty()){
					try{
						members.add(UUID.fromString(sf.getString(str + "members")));
					}catch(NullPointerException npe){ } 
				}
				for(String u : sf.getStringList(str + "visitors")){
					Bukkit.broadcastMessage("Getting visitors");
					visitors.add(UUID.fromString(u));
				}
				if(visitors.isEmpty()){
					try{
						visitors.add(UUID.fromString(sf.getString(str + "visitors")));
					}catch(NullPointerException npe){ } 
				}
				new SkyBlockManager(of, mp, loc, pr, i, members, visitors);
			}	
		}catch(NullPointerException npe){
			java.util.logging.Logger l = SkyBlock.p.getLogger();
			l.warning("=============");
			l.warning(" ");
			l.warning("No islands to load!");
			l.warning(" ");
			l.warning("=============");
			return;
		}
	}
}
