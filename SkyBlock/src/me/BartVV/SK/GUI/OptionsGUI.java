package me.BartVV.SK.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Manager.SkyBlockManager;
import me.BartVV.SK.Utils.SkyBlock;

public class OptionsGUI{
	
	private static OptionsGUI OptionsGUI;
	
	public static OptionsGUI getInstance(){
		return OptionsGUI;
	}
	
	public static void setInstance(OptionsGUI ogui){
		OptionsGUI = ogui;
	}
	
	public void openOptions(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, "§7(§9Options Menu§7)");
		PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
		SkyBlockManager sbm = pm.getIsland();
		if(pm != null && sbm != null){
			List<String> lore = new ArrayList<>();
			lore.add("§7Edit the people");
			lore.add("§7who can visit this island!");
			lore.add(" ");
			if(pm.canInvite() || pm.getIsland().getOwnerUUID() == p.getPlayer().getUniqueId()){
				lore.add("§7Status: §aEditable!");
			}else{
				lore.add("§7Status: §cNot Editable!");
			}
			setItemStack(inv, Material.WOOL, 0, "§9Visiting", 1, (byte)3, lore);
			lore.clear();
			
			lore.add("§7Edit the permissions of ");
			lore.add("§7the members of your island!");
			if(pm.canEditPermissions()){
				lore.add("§7Status: §aEditable!");
			}else{
				lore.add("§7Status: §cNot Editable!");
			}
			setItemStack(inv, Material.WOOL, 1, "§9Edit Permissions", 1, (byte)5, lore);
			p.openInventory(inv);
			return;
		}else{
			p.sendMessage(SkyBlock.prefix + "Something went terribly wrong!");
			p.sendMessage(SkyBlock.prefix + "Error Code: OptionsGUI:23#openOptions(Player p) @ me.BartVV.SK.GUI.OptionsGUI");
			p.sendMessage(SkyBlock.prefix + "Please share this with staff!");
			p.closeInventory();
		}
	}

	public void manageListener(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player){
			if(isClickedInventory("§7(§9Options Menu§7)", e.getClickedInventory())){
				Player p = (Player)e.getWhoClicked();
				if(e.getSlot() == 0){
					
					ManageInviteGUI.getInstance().openManageInviteGUI(p);
				}else if(e.getSlot() == 1){
					ManagePermissionsGUI.getInstance().openManagePermissions(p);
				}
			}	
		}
		Bukkit.broadcastMessage("5");
	}
	
	private Boolean isClickedInventory(String displayname, Inventory inv){
		if(inv.getTitle().equalsIgnoreCase(displayname)){
			return true;
		}else{
			return false;
		}
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


}
