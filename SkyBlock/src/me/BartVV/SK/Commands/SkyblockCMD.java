package me.BartVV.SK.Commands;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.BartVV.SK.SkyBlockManager.SkyBlockManager;
import me.BartVV.SK.Utils.SkyBlock;

public class SkyblockCMD implements CommandExecutor{

	Random r = new Random();
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String cmdLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("SkyBlock")){
			if(!(cs instanceof Player)){
				cs.sendMessage(SkyBlock.prefix + "§cYou have to be a player to perform this command!");
				return true;
			}
			Player p = (Player)cs;
			if(getArg(args[0], "create")){
				SkyBlockManager sbm = new SkyBlockManager(p, 10);
				sbm.createIsland();
				p.sendMessage("Done!");
			}else if (getArg(args[0], "visit")){
				SkyBlockManager sbm = SkyBlockManager.getSkyBlock(p);
				SkyBlockManager.TeleportToIsland(p, sbm);
			}else if (getArg(args[0], "delete")){
				p.sendMessage("Todo, Delete");
			}else if (getArg(args[0], "option", "options")){
				p.sendMessage("Todo, Options");
			}
		}
		return false;
	}
	
	private boolean getArg(String arg, String... equals){
		for (String s : equals){
			if (arg.equalsIgnoreCase(s)){
				return true;
			}	
		}
		return false;
	}
}
