package me.BartVV.SK.Commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.BartVV.SK.GUI.GUI;
import me.BartVV.SK.GUI.MainGUI;
import me.BartVV.SK.GUI.SkyBlockGUI;
import me.BartVV.SK.Utils.SkyBlock;

public class CMDManager implements CommandExecutor{

	private static HashMap<GUI, SkyBlockGUI> gui = new HashMap<>();
	
	public static void setup(){
		gui.put(GUI.MAIN, new MainGUI());
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String cmdLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("skyblock")){
			if(cs instanceof Player){
				Player p = (Player)cs;
				SkyBlockGUI gui = CMDManager.gui.get(GUI.MAIN);
				if(gui == null){
					p.sendMessage(SkyBlock.prefix + "Something went wrong! Please try again");
					setup();
					return true;
				}
				gui.openMain(p);
				
			}
		}
		return false;
	}
}
