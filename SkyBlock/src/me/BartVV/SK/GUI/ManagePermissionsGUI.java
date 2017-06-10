package me.BartVV.SK.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Manager.SkyBlockManager;

public class ManagePermissionsGUI {
	
	private static ManagePermissionsGUI ManagePermissionsGUI;
	
	public static ManagePermissionsGUI getInstance(){
		return ManagePermissionsGUI;
	}
	
	public static void setInstance(ManagePermissionsGUI mpgui){
		ManagePermissionsGUI = mpgui;
	}
	
	@SuppressWarnings("deprecation")
	public void openManagePermissions(Player p){
		Inventory inv = Bukkit.createInventory(null, 54, "§7(§9Permission Management§7)");
		PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
		SkyBlockManager sbm = pm.getIsland();
		List<String> lore = new ArrayList<>();
		lore.add("§7Go back to the options menu");
		setItemStack(inv, Material.IRON_BLOCK, 45, "§7Go Back", 1, null, lore);
		lore.clear();
		lore.add("§cClick here to edit the permissions of");
		lore.add("");
		Integer size = sbm.getPlayers().size();
		
		if(size == 0){
			lore.set(0, "§cThere aren't any members");
			lore.set(1, "§cto edit the permissions from!");
			OfflinePlayer of = Bukkit.getOfflinePlayer("MHF_Question");
			setHead(inv, 22, "§7No players", of.getName(), lore);
			lore.clear();
		}else if (size == 1){
			lore.set(0, "§cThere aren't any members");
			lore.set(1, "§cto edit the permissions from!");
			OfflinePlayer of = Bukkit.getOfflinePlayer("MHF_Question");
			setHead(inv, 22, "§7No players", of.getName(), lore);
			lore.clear();
		}else if (size == 2){
			Integer slot = 21;
			for (OfflinePlayer of : sbm.getPlayers()){
				lore.set(1, "§a" + of.getName());
				setHead(inv, slot, "§7" + of.getName() + "'s permissions", of.getName(), lore);
				slot = slot + 2;
			}
		}else if (size == 3){
			Integer slot = 21;
			for (OfflinePlayer of : sbm.getPlayers()){
				lore.set(1, "§a" + of.getName());
				setHead(inv, slot, "§7" + of.getName() + "'s permissions", of.getName(), lore);
				slot++;
			}
		}else if (size == 4){
			Integer slot = 21;
			for(OfflinePlayer of : sbm.getPlayers()){
				if(slot == 23) slot = 31;
				lore.set(1, "§a" + of.getName());
				setHead(inv, slot, "§7" + of.getName() + "'s permissions", of.getName(), lore);
				slot++;
			}
		}else if (size == 5){
			Integer slot = 20;
			for(OfflinePlayer of : sbm.getPlayers()){
				lore.set(1, "§a" + of.getName());
				setHead(inv, slot, "§7" + of.getName() + "'s permissions", of.getName(), lore);
				slot++;
			}
		}else if(size == 6){
			Integer slot = 20;
			for(OfflinePlayer of : sbm.getPlayers()){
				if(slot == 24){
					slot = 31;
				}
				lore.set(1, "§a" + of.getName());
				setHead(inv, slot, "§7" + of.getName() + "'s permissions", of.getName(), lore);
				slot++;
			}
		}else if (size == 7){
			Integer slot = 20;
			for(OfflinePlayer of : sbm.getPlayers()){
				if(slot == 24){
					slot = 30;
				}else if (slot == 30){
					slot = 32;
				}
				lore.set(1, "§a" + of.getName());
				setHead(inv, slot, "§7" + of.getName() + "'s permissions", of.getName(), lore);
				slot++;
			}
		}else if (size == 8){
			Integer slot = 20;
			for(OfflinePlayer of : sbm.getPlayers()){
				if(slot == 24){
					slot = 30;
				}
				lore.set(1, "§a" + of.getName());
				setHead(inv, slot, "§7" + of.getName() + "'s permissions", of.getName(), lore);
				slot++;
			}
		}else if (size == 9){
			
		}else if (size == 10){
			
		}
		p.openInventory(inv);
	}
	
	private void setItemStack(Inventory inv, Material mat, Integer slot, 
			String displayname, Integer amount, Byte b, List<String> lore){
		if(inv == null || mat == null || slot == null || amount == null) return;
		ItemStack is;
		if(b == null){
			is = new ItemStack(mat, amount);	
		}else{
			is = new ItemStack(mat, amount, b);
		}
		ItemMeta im = is.getItemMeta();
		if(displayname != null){
			im.setDisplayName(displayname);
		}
		if(lore != null){
			im.setLore(lore);
		}
		is.setItemMeta(im);
		inv.setItem(slot, is);
	}

	public void manageListener(InventoryClickEvent e) {
		Integer slot = e.getSlot();
		Player p = (Player)e.getWhoClicked();
		if(slot == 45){
			OptionsGUI.getInstance().openOptions(p);
		}
	}
	
	private void setHead(Inventory inv, Integer slot, String displayname, String of, List<String> lore){
		if(inv == null || slot == null || displayname == null || of == null) return;
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
        skullMeta.setOwner(of);
        skullMeta.setDisplayName(displayname);
        if(lore != null) skullMeta.setLore(lore);
        skull.setItemMeta(skullMeta);
        inv.setItem(slot, skull);
	}
}
