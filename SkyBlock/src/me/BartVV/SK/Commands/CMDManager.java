package me.BartVV.SK.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.BartVV.SK.GUI.VisitGUI;
import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Manager.SkyBlockManager;
import me.BartVV.SK.Utils.SkyBlock;

public class CMDManager implements CommandExecutor, TabCompleter{
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String cmdLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("skyblock")){
			if(cs instanceof Player){
				Player p = (Player)cs;
				if(args.length == 0 || args[0].equalsIgnoreCase("help")){
					p.sendMessage("§7====== " + SkyBlock.prefix + " §7======");
					p.sendMessage("§9/is help§7: will display those messages");
					p.sendMessage("§9/is go§7: will go to your island!");
					p.sendMessage("§9/is create§7: will display a GUI to create a island!");
					p.sendMessage("§9/is delete§7: will display a GUI to delete your island!");
					p.sendMessage("§9/is invite§7: will invite someone to your island");
					p.sendMessage("§9/is visit§7: will display a GUI to visit other islands!");
					PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
					if(pm != null){
						SkyBlockManager sbm = pm.getIsland();
						if(sbm != null){
							if(sbm.getOwnerUUID().equals(p.getUniqueId()) || pm.canEditPermissions()){
								p.sendMessage("§9/is options§7: will display the options to edit your members permissions!");
							}		
							if(sbm.getOwnerUUID().equals(p.getUniqueId()) || pm.CanIslandInvite()){
								p.sendMessage("§9/is invite§7: invite someone to the island!");
							}
							if(sbm.getOwnerUUID().equals(p.getUniqueId()) || pm.canInvite()){
								p.sendMessage("§9/invite§7: to invite other players to come to your island!");
							}
						}
					}
					p.sendMessage("§7====== " + SkyBlock.prefix + " §7======");
					return true;
				}else{
					ISCMD.setISCMD(p, cmd, args);
				}
			}
		}else if(cmd.getName().equalsIgnoreCase("invite")){
			if(cs instanceof Player){
				InviteCMD.setISCMD(cs, cmd, args);
			}
		}else if(cmd.getName().equalsIgnoreCase("visit")){
			if(cs instanceof Player){
				Player p = (Player)cs;
				new VisitGUI().openVisit(p);	
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String cmdLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("invite")){
			List<String> matches = new ArrayList<String>();
			if(args.length > 1 || args[0].equalsIgnoreCase("accept")){
				return matches;
			}
			if(!(cs instanceof Player)){
				return matches;
			}
			Player t = (Player)cs;
			for(Player p : Bukkit.getOnlinePlayers()){
				if(t.getName().equalsIgnoreCase(p.getName())){
					
				}else{
					String name = p.getName().toLowerCase();
					if(name.startsWith(args[0].toLowerCase())){
						matches.add(p.getName());
					}	
				}
			}
			if(args.length == 0){
				matches.add("accept");
			}
			String arg = args[0].toLowerCase();
			if(arg.startsWith("accept")){
				matches.add("accept");
			}
			else if(arg.startsWith("accep")){
				matches.add("accept");
			}
			else if(arg.startsWith("acce")){
				matches.add("accept");
			}
			else if(arg.startsWith("acc")){
				matches.add("accept");
			}
			else if(arg.startsWith("ac")){
				matches.add("accept");
			}
			else if(arg.startsWith("a")){
				matches.add("accept");
			}
			return matches;
		}
		else if (cmd.getName().equalsIgnoreCase("island")){
			List<String> matches = new ArrayList<>();
			matches.add("//TODO");
			return matches;
		}
		return null;
	}
}
