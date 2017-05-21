package me.BartVV.SK.Utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.BartVV.SK.Commands.SkyblockCMD;
import me.BartVV.SK.Listeners.onJoin;

public class SkyBlock extends JavaPlugin{

	public static String prefix = "§8(§9SkyMines§8)§7 ";
	public static File schematica;
	public static Plugin p;
	public static Location loc = new Location(Bukkit.getWorld("world"), 0, 100, 0);
	
	public void onEnable(){
		p = this;
		getDataFolder().mkdirs();
		File f = new File(getDataFolder() + "/Schematica");
		f.mkdirs();
		schematica = f;
		Bukkit.getPluginManager().registerEvents(new onJoin(), this);
		getCommand("SkyBlock").setExecutor(new SkyblockCMD());
	}
	
	public void onDisable(){
		
		
		schematica = null;
	}
}
