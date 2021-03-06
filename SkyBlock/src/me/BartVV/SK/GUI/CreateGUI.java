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

public class CreateGUI{
	
	private static CreateGUI CreateGUI;
	
	public static CreateGUI getInstance(){
		return CreateGUI;
	}
	
	public static void setInstance(CreateGUI cgui){
		CreateGUI = cgui;
		setup();
	}
	
	private Inventory inv;
	
	private static void setup(){
		Inventory inv = Bukkit.createInventory(null, 9, "�7(�9Create GUI�7)");
		List<String> lore = new ArrayList<>();
		lore.add("�7Clicking here will result of creating an island");
		setItemStack(inv, Material.WOOL, 2, "�aCreate your island", 1, (byte)5, lore);
		lore.clear();
		lore.add("�7Clicking here will result in closing this GUi!");
		setItemStack(inv, Material.WOOL, 5, "�cDon't create my island", 1, (byte)14, lore);
		CreateGUI.inv = inv;
	}
	public void openCreate(Player p) {
		p.openInventory(inv);
	}
	
	public void manageListener(InventoryClickEvent e) {
		if(isClickedInventory("�7(�9Create GUI�7)", e.getClickedInventory())){
			if(e.getWhoClicked() instanceof Player){
				Player p = (Player)e.getWhoClicked();
				PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
				if(e.getSlot() == 2){
					if(pm.getIsland() != null){
						p.sendMessage(SkyBlock.prefix + "Please leave your island if you want to create a island!");
						p.closeInventory();
						return;
					}
					p.closeInventory();
					SkyBlockManager sbm = new SkyBlockManager(p, 10);
					sbm.createIsland();
					SkyBlockManager.TeleportToIsland(p, sbm);
					return;
				}else if (e.getSlot() == 5){
					p.closeInventory();
					p.sendMessage(SkyBlock.prefix + "Creating island cancelled!");
					return;
				}
			}	
		}
	}
	
	private Boolean isClickedInventory(String displayname, Inventory inv){
		if(inv.getTitle().equalsIgnoreCase(displayname)){
			return true;
		}else{
			return false;
		}
	}
	
	private static void setItemStack(Inventory inv, Material mat, Integer slot, 
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
