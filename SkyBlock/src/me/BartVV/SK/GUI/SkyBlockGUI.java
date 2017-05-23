package me.BartVV.SK.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class SkyBlockGUI {

	public abstract void openCreate(Player p);
	public abstract void openOwner(Player p);
	public abstract void openMain(Player p);
	public abstract void manageListener(InventoryClickEvent e);
	
	public void setItemStack(Inventory inv, Material mat, Integer slot, String name, Integer amount, Byte b, List<String> lore){
		ItemStack is;
		if(b == null){
			is = new ItemStack(mat, amount);
		}else{
			is = new ItemStack(mat, amount, (byte)b);
		}
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		if(lore != null){
			List<String> lore2 = new ArrayList<>();
			lore2.addAll(lore);
			im.setLore(lore2);	
		}
		is.setItemMeta(im);
		inv.setItem(slot, is);
	}
}
