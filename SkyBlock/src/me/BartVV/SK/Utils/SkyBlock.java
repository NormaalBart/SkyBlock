package me.BartVV.SK.Utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.BartVV.SK.Commands.CMDManager;
import me.BartVV.SK.Config.Config;
import me.BartVV.SK.GUI.CreateGUI;
import me.BartVV.SK.GUI.DeleteGUI;
import me.BartVV.SK.GUI.ManageInviteGUI;
import me.BartVV.SK.GUI.ManagePermissionsGUI;
import me.BartVV.SK.GUI.OptionsGUI;
import me.BartVV.SK.GUI.VisitGUI;
import me.BartVV.SK.Listeners.InventoryClick;
import me.BartVV.SK.Listeners.PlayerDeath;
import me.BartVV.SK.Listeners.onJoin;
import me.BartVV.SK.Listeners.onLeave;
import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Manager.SkyBlockManager;

public class SkyBlock extends JavaPlugin{

	public static WorldGuardPlugin wg;
	public static String prefix = "§8(§9SkyMines§8)§7 ";
	public static File schematica;
	public static FileConfiguration playerdata, skyblockdata;
	public static Config playerconfig, skyblockconfig;
	public static Plugin p;
	public static Location loc;
	
	public void onEnable(){
		p = this;
		getCommand("SkyBlock").setExecutor(new CMDManager());
		getCommand("invite").setExecutor(new CMDManager());
		getCommand("visit").setExecutor(new CMDManager());
		register();
		registerConfig();
		wg = getWorldGuard();
		
		SkyBlockManager.loadSkyBlocks();
		
		for(Player p : Bukkit.getOnlinePlayers()){
			new PlayerManager(p);
		}
		
		loc = new Location(Bukkit.getWorld("world"), 0, 100, 1000);
		
		CreateGUI.setInstance(new CreateGUI());
		DeleteGUI.setInstance(new DeleteGUI());
		ManageInviteGUI.setInstance(new ManageInviteGUI());
		ManagePermissionsGUI.setInstance(new ManagePermissionsGUI());
		OptionsGUI.setInstance(new OptionsGUI());
		VisitGUI.setInstance(new VisitGUI());
	}
	
	public void onDisable(){
		SkyBlockManager.saveSkyBlocks();
		for(Player p : Bukkit.getOnlinePlayers()){
			PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
			if(pm != null){
				pm.save();
			}
		}
	}
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	getLogger().warning("====================");
	    	getLogger().warning("");
	    	getLogger().warning("WorldGuard isn't found!");
	    	getLogger().warning("Disabling...");
	    	getLogger().warning("");
	    	getLogger().warning("====================");
	    	Bukkit.getServer().getPluginManager().disablePlugin(this);
	        return null;
	    }
	    return (WorldGuardPlugin) plugin;
	}
	
	private void register(){
		new onJoin(p);
		new onLeave(p);
		new InventoryClick(p);
		new PlayerDeath(p);
	}
	
	private void registerConfig(){
		getDataFolder().mkdirs();
		File f = new File(getDataFolder() + "/Schematica");
		f.mkdirs();
		schematica = f;
		SkyBlock.playerconfig = new Config(this, "playerdata.yml");
		SkyBlock.playerdata = playerconfig.getFileConfiguration();
		SkyBlock.skyblockconfig = new Config(this, "skyblockdata.yml");
		SkyBlock.skyblockdata = skyblockconfig.getFileConfiguration();
	}
}
