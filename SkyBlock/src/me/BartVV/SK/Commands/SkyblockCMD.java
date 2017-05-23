package me.BartVV.SK.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Manager.SkyBlockManager;
import me.BartVV.SK.Utils.SkyBlock;

public class SkyblockCMD implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String cmdLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("SkyBlock")){
			if(!(cs instanceof Player)){
				cs.sendMessage(SkyBlock.prefix + "§cYou have to be a player to perform this command!");
				return true;
			}
			Player p = (Player)cs;
			if(args.length == 0){
				//TODO: Adding help message.
				p.sendMessage(SkyBlock.prefix + "§cArgs length == 0!");
				return true;
			}
			if(getArg(args[0], "create")){
				if(SkyBlockManager.getIsland(p) != null){
					PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
					p.sendMessage(SkyBlock.prefix + "You are already a " + pm.getRank().getString() + "!");
					p.sendMessage(SkyBlock.prefix + "Please leave your island if you want to create a island!");
					return true;
				}
				SkyBlockManager sbm = new SkyBlockManager(p, 10);
				p.sendMessage(SkyBlock.prefix + "Creating your island...");
				sbm.createIsland();
				p.sendMessage(SkyBlock.prefix + "§aIsland created! §7Teleporting");
				SkyBlockManager.TeleportToIsland(p, sbm);
			}else if (getArg(args[0], "visit")){
				if(args.length == 2){
					if(Bukkit.getPlayer(args[1]) == null){
						OfflinePlayer island = Bukkit.getOfflinePlayer(args[1]);
						SkyBlockManager sbm = SkyBlockManager.getIsland(island);
						if (sbm == null){
							p.sendMessage(SkyBlock.prefix + "The player isn't part of a island!");
							return true;
						}
						p.sendMessage(SkyBlock.prefix + "Teleporting you to the island of " + args[1]);
						SkyBlockManager.TeleportToIsland(p, sbm);
						return true;
					}else if(Bukkit.getPlayer(args[1]).isOnline()){
						Player island = Bukkit.getPlayer(args[1]);
						SkyBlockManager sbm = SkyBlockManager.getIsland(island);
						if (sbm == null){
							p.sendMessage(SkyBlock.prefix + "The player isn't part of a island!");
							return true;
						}
						p.sendMessage(SkyBlock.prefix + "Teleporting you to the island of " + args[1]);
						SkyBlockManager.TeleportToIsland(p, sbm);
						return true;
					}else{
						p.sendMessage(SkyBlock.prefix + "§cAn error occured, please contact staff!");
						return true;
					}
				}else{
					p.sendMessage(SkyBlock.prefix + "Teleporting you to your island!");
					SkyBlockManager.TeleportToIsland(p, PlayerManager.getPlayerManager(p.getUniqueId())
							.getIsland());	
				}
			}else if (getArg(args[0], "delete")){
				SkyBlockManager sbm = PlayerManager.getPlayerManager(p.getUniqueId()).getIsland();
				sbm.deleteIsland();
				p.sendMessage(SkyBlock.prefix + "Island deleted!");
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
