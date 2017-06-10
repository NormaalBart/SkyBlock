package me.BartVV.SK.Commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Manager.SkyBlockManager;
import me.BartVV.SK.Utils.SkyBlock;

public class InviteCMD {
	
	private static HashMap<OfflinePlayer, OfflinePlayer> invite = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public static void setISCMD(CommandSender cs, Command cmd, String[] args){
		Player p = (Player)cs;
		if(args.length == 0){
			p.sendMessage(SkyBlock.prefix + "Usage: §9/invite (player)");
			return;
		}else{
			if(args[0].equalsIgnoreCase("accept")){
				if(invite.containsKey(p)){
					PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
					OfflinePlayer target = invite.get(p);
					PlayerManager targetpm = PlayerManager.getPlayerManager(target.getUniqueId());
					if(targetpm.getIsland() == null){
						p.sendMessage(SkyBlock.prefix + "The target apperantly deleted his island!");
						invite.remove(p, target);
						return;
					}
					pm.addVisits(targetpm.getIsland());
					targetpm.getIsland().addVisitor(p.getUniqueId());
					p.sendMessage(SkyBlock.prefix + "You can now visit " + target.getName() + "'s island!");
					if(target.isOnline()){
						Player t = (Player)target;
						t.sendMessage(SkyBlock.prefix + p.getName() + " can now visit your island!");
					}
					invite.remove(p, target);
					if(!target.isOnline()){
						targetpm.save();
					}
					return;
				}else{
					p.sendMessage(SkyBlock.prefix + "You don't have any invites!");
					return;
				}
			}
			else if (args[0].equalsIgnoreCase("remove")){
				if(args.length == 1){
					p.sendMessage(SkyBlock.prefix + "Please define a player!");
					return;
				}else{
					OfflinePlayer of = Bukkit.getOfflinePlayer(args[1]);
					PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
					if(pm.getIsland().getVisitors().contains(of.getUniqueId())){
						pm.getIsland().removeVisitor(of.getUniqueId());
						if(of.isOnline()){
							Player target = (Player)of;
							PlayerManager targetpm = PlayerManager.getPlayerManager(target.getUniqueId());
							targetpm.removeVisits(pm.getIsland());
							target.sendMessage(SkyBlock.prefix + "You can't visit the island of " + p.getName() + " anymore!");
							p.updateInventory();
							p.sendMessage(SkyBlock.prefix + target.getName() + " can't visit your island anymore!");
							return;
						}else{
							File file = new File(SkyBlock.p.getDataFolder(), "playerdata.yml");
							if(file.exists()){
								FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
								String str = "playerdata." + of.getUniqueId().toString() + ".visits";
								List<Integer> visits = fc.getIntegerList(str);
								visits.remove(pm.getIsland().getIslandID());
								fc.set("playerdata." + of.getUniqueId().toString() + ".visits", visits);
								try {
									fc.save(file);
								} catch (IOException e) {
									p.sendMessage(SkyBlock.prefix + "Please try again later!");
									return;
								}
								p.sendMessage(SkyBlock.prefix + "Succesfully removed" + of.getName() + " from the visitors list!");
								return;
							}
						}
					}else{
						p.sendMessage(SkyBlock.prefix + args[1] + " isn't in the visitors list!");
						return;
					}
				}
			}else{
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null){
					p.sendMessage(SkyBlock.prefix + args[0] + " isn't online!");
					return;
				}
				SkyBlockManager sbm = PlayerManager.getPlayerManager(p.getUniqueId()).getIsland();
				if(sbm == null){
					p.sendMessage(SkyBlock.prefix + "You have to have an island to invite someon!");
					return;
				}
				else if(p.getName() == target.getName()){
					p.sendMessage(SkyBlock.prefix + "You can't invite yourself!");
					return;
				}
				else if(sbm.getVisitors().size() > 28){
					p.sendMessage(SkyBlock.prefix + "maximum of invites of your island is reached! (28)");
					return;
				}
				PlayerManager targetpm = PlayerManager.getPlayerManager(target.getUniqueId());
				if(targetpm.getVisits().contains(sbm)){
					p.sendMessage(SkyBlock.prefix + "He can already visit your island!");
					return;
				}
				invite.put(target, p);
				p.sendMessage(SkyBlock.prefix + args[0] + " is invited to your island!");
				target.sendMessage(SkyBlock.prefix + "Your invited by " + p.getName() + " to visit his island!");
				target.sendMessage(SkyBlock.prefix + "type /invite accept to visit his island!");
				return;
			}
		}
	}

}
