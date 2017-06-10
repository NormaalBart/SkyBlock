package me.BartVV.SK.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.BartVV.SK.GUI.CreateGUI;
import me.BartVV.SK.GUI.DeleteGUI;
import me.BartVV.SK.GUI.OptionsGUI;
import me.BartVV.SK.GUI.VisitGUI;
import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Manager.SkyBlockManager;
import me.BartVV.SK.Utils.SkyBlock;

public class ISCMD {

	private static HashMap<Player, OfflinePlayer> invite = new HashMap<>();
	private static CreateGUI CreateGUI = me.BartVV.SK.GUI.CreateGUI.getInstance();
	private static DeleteGUI DeleteGUI = me.BartVV.SK.GUI.DeleteGUI.getInstance();
	private static OptionsGUI OptionsGUI = me.BartVV.SK.GUI.OptionsGUI.getInstance();
	private static VisitGUI VisitGUI = me.BartVV.SK.GUI.VisitGUI.getInstance();
	
	public static void setISCMD(CommandSender cs, Command cmd, String[] args){
		if(cs instanceof Player){
			Player p = (Player)cs;
			PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
			if(args[0].equalsIgnoreCase("go")){
				SkyBlockManager sbm = pm.getIsland();
				if(sbm != null){
					SkyBlockManager.TeleportToIsland(p, sbm);
					p.sendMessage(SkyBlock.prefix + "Teleporting to your island!");
					return;
				}else{
					p.sendMessage(SkyBlock.prefix + "You don't have an island!");
					return;
				}
			}else if(args[0].equalsIgnoreCase("create")){
				CreateGUI.openCreate(p);
				return;
			}else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")){
				if(pm.getIsland() != null){
					if(pm.getIsland().getOwnerUUID() == p.getUniqueId()){
						DeleteGUI.openDelete(p);	
					}else{
						if(pm.getIsland() != null){
							p.sendMessage(SkyBlock.prefix + "Only owners can delete the island!"); 	
						}else{
							p.sendMessage(SkyBlock.prefix + "You have to have an island to do this!");
						}
					}	
				}else{
					p.sendMessage(SkyBlock.prefix + "You have to have an island to do this!");
					return;
				}
				return;
			}else if(args[0].equalsIgnoreCase("options") || args[0].equalsIgnoreCase("option")){
				if(pm.getIsland() != null){
					if (pm.getIsland().getOwnerUUID().equals(p.getUniqueId()) || pm.canEditPermissions()){
						OptionsGUI.openOptions(p);
						return;
					}else{
						p.sendMessage(SkyBlock.prefix + "You can't edit the permissions!"); 
						return;
					}	
				}else{
					p.sendMessage(SkyBlock.prefix + "You have to have an island to do this!");
					return;
				}
			}else if(args[0].equalsIgnoreCase("visit")){
				VisitGUI.openVisit(p);
			}else if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("inv")){
				if(pm.getIsland() != null){
					if(pm.CanIslandInvite() || pm.getIsland().getOwnerUUID() == p.getUniqueId()){
						if(args.length <= 1){
							p.sendMessage(SkyBlock.prefix + "Usage: /is invite (Player)");
							return;
						}
						Player target = Bukkit.getPlayer(args[1]);
						if(target == null){
							p.sendMessage(SkyBlock.prefix + "Player isn't found!");
							return;
						}
						PlayerManager targetpm = PlayerManager.getPlayerManager(target.getUniqueId());
						if(pm.getIsland() == null){
							p.sendMessage(SkyBlock.prefix + "You don't have an island!");
							return; 
						}
						if (targetpm.getIsland() != null){
							p.sendMessage(SkyBlock.prefix + "Player is already a member of an island!");
							return;
						}
						if(targetpm.getIsland() != null){
							if (targetpm.getIsland().getIslandID() == pm.getIsland().getIslandID()){
								p.sendMessage(SkyBlock.prefix + "He is already a member of your island!");
								return;
							}else{
								p.sendMessage(SkyBlock.prefix + "Player is already a member of an island!");
								return;
							}
						}
						else if (pm.getIsland().getPlayers().size() >= pm.getIsland().getMaxPlayers()){
							p.sendMessage(SkyBlock.prefix + "Max players reached! (" + pm.getIsland().getMaxPlayers() + ")");
							return;
						}
						invite.put(target, p);
						p.sendMessage(SkyBlock.prefix + "Invite send!");
						target.sendMessage(SkyBlock.prefix + "You got an invite to join the island of " + pm.getIsland().getOwnerName());
						target.sendMessage(SkyBlock.prefix + "Type §a/is accept §7to accept it!");
						new BukkitRunnable() {
							@Override
							public void run() {
								invite.remove(target, p);
							}
						}.runTaskLater(SkyBlock.p, (20*60*5));
					}
				}else{
					p.sendMessage(SkyBlock.prefix + "You have to have an island to do this!");
					return;
				}
			}else if (args[0].equalsIgnoreCase("accept")){
				if(invite.containsKey(p)){
					OfflinePlayer of = invite.get(p);
					PlayerManager targetpm = PlayerManager.getPlayerManager(of.getUniqueId());
					if(targetpm.getIsland() != null){
						if(targetpm.getIsland().getPlayers().size() >= targetpm.getIsland().getMaxPlayers()){
							p.sendMessage(SkyBlock.prefix + "Max players reached! (" + pm.getIsland().getMaxPlayers() + ")");
							for(OfflinePlayer all : targetpm.getIsland().getPlayers()){
								if(all.isOnline()){
									Player send = (Player)all;
									send.sendMessage(SkyBlock.prefix + p.getName() + " tried to join your island but the island is full!");
								}
							}
							return;
						}
						for(OfflinePlayer all : targetpm.getIsland().getPlayers()){
							if(all.isOnline()){
								Player send = (Player)all;
								send.sendMessage(SkyBlock.prefix + "§a" + p.getName() + " §7joined your island!");
							}
						}
						targetpm.getIsland().addPlayer(p);
						p.sendMessage(SkyBlock.prefix + "You're added to the island!");
					}else{
						p.sendMessage(SkyBlock.prefix + "Player deleted his island!");
						invite.remove(p, of);
						return;
					}
				}else{
					p.sendMessage(SkyBlock.prefix + "You don't have any invites!");
					return;
				}
			}
		}
	}

}
