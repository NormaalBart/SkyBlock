package me.BartVV.SK.Utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.BartVV.SK.Commands.CMDManager;
import me.BartVV.SK.Config.Config;
import me.BartVV.SK.Listeners.InventoryClick;
import me.BartVV.SK.Listeners.onJoin;
import me.BartVV.SK.Listeners.onLeave;

public class SkyBlock extends JavaPlugin{

	public static String prefix = "§8(§9SkyMines§8)§7 ";
	public static File schematica;
	public static FileConfiguration playerdata, skyblockdata;
	public static Plugin p;
	public static Location loc = new Location(Bukkit.getWorld("world"), 0, 100, 1000);
	
	public void onEnable(){
		p = this;
		CMDManager.setup();
		getCommand("SkyBlock").setExecutor(new CMDManager());
		register();
		registerConfig();
	}
	
	public void onDisable(){
		
		
		schematica = null;
	}
	
	private void register(){
		new onJoin(p);
		new onLeave(p);
		new InventoryClick(p);
	}
	
	private void registerConfig(){
		getDataFolder().mkdirs();
		File f = new File(getDataFolder() + "/Schematica");
		f.mkdirs();
		schematica = f;
		Config playerdata = new Config(this, "playerdata.yml");
		SkyBlock.playerdata = playerdata.getFileConfiguration();
		Config skyblockdata = new Config(this, "skyblockdata.yml");
		SkyBlock.skyblockdata = skyblockdata.getFileConfiguration();
	}
}
