package me.BartVV.SK.Config;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.BartVV.SK.Utils.SkyBlock;

public class Config {
	
	private File file;
	private FileConfiguration fileconfiguration;
	private SkyBlock skyblock;
	
	
	public Config(SkyBlock skyblock, String filename){
		this.skyblock = skyblock;
		this.file = new File(skyblock.getDataFolder(), filename + ".yml");
		reload();
	}

	public File getFile(){
		return this.file;
	}
	
	public void reload() {
		if(!skyblock.getDataFolder().exists()){
			skyblock.getDataFolder().mkdirs();
		}
		
		if(!file.exists()){
			skyblock.saveResource(file.getName(), true);
			skyblock.getLogger().info("Succesfully made " + file.getName() + "!");
		}
		fileconfiguration = YamlConfiguration.loadConfiguration(file);
	}

}
