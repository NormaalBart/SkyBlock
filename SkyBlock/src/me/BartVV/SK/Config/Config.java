package me.BartVV.SK.Config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.BartVV.SK.Utils.SkyBlock;

public class Config {
	
	private File file;
	private SkyBlock skyblock;
	
	
	public Config(SkyBlock skyblock, String filename){
		this.skyblock = skyblock;
		if(filename.endsWith(".yml")){
			this.file = new File(skyblock.getDataFolder(), filename);
		}else{
			this.file = new File(skyblock.getDataFolder(), filename + ".yml");	
		}
		reload();
	}

	public File getFile(){
		return this.file;
	}
	
	public FileConfiguration getFileConfiguration(){
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static void saveConfig(String filename){
		FileConfiguration config;
		if(filename.endsWith(".yml")){
			config = YamlConfiguration.loadConfiguration(new File(SkyBlock.p.getDataFolder(), filename));
		}else{
			filename = filename + ".yml";
			config = YamlConfiguration.loadConfiguration(new File(SkyBlock.p.getDataFolder(), filename));
		}
		try {
			config.save(new File(SkyBlock.p.getDataFolder(), filename));
			Bukkit.broadcastMessage("Saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reload() {
		if(!skyblock.getDataFolder().exists()){
			skyblock.getDataFolder().mkdirs();
		}
		
		if(!file.exists()){
			skyblock.saveResource(file.getName(), true);
			skyblock.getLogger().info("Succesfully made " + file.getName() + "!");
		}
	}

	public static void saveConfig(FileConfiguration config, String filename) {
		if(filename.endsWith(".yml")){
		}else{
			filename = filename + ".yml";
		}
		
		try {
			config.save(new File(SkyBlock.p.getDataFolder(), filename));
			Bukkit.broadcastMessage("Saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
