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

public class DeleteGUI{

	private static DeleteGUI DeleteGUI;
	
	public static DeleteGUI getInstance(){
		return DeleteGUI;
	}
	
	public static void setInstance(DeleteGUI dgui){
		DeleteGUI = dgui;
	}
	
	private Inventory inv;
	
	public void setup(){
		Inventory inv = Bukkit.createInventory(null, 9, "§7(§9Delete GUI§7)");
		List<String> lore = new ArrayList<>();
		lore.add("§cNOTE:");
		lore.add("§7If you click here,");
		lore.add("§7Your island will be §cdeleted!");
		lore.add("§7This §ccan not §7be rollbacked!");
		setItemStack(inv, Material.WOOL, 2, "§cDelete your island", 1, (byte)14, lore);
		lore.clear();
		lore.add("§7Clicking here will result in closing this GUi!");
		setItemStack(inv, Material.WOOL, 5, "§aDon't delete my island", 1, (byte)5, lore);
		this.inv = inv;
	}

	public void openDelete(Player p) {
		p.openInventory(inv);
	}

	public void manageListener(InventoryClickEvent e) {
		if(isClickedInventory("§7(§9Delete GUI§7)", e.getClickedInventory())){
			if(e.getWhoClicked() instanceof Player){
				Player p = (Player)e.getWhoClicked();
				SkyBlockManager sbm = PlayerManager.getPlayerManager(p.getUniqueId()).getIsland();
				if(sbm == null){
					p.sendMessage(SkyBlock.prefix + "You don't have an island!");
					p.closeInventory();
					return;
				}
				p.closeInventory();
				p.sendMessage(SkyBlock.prefix + "Deleting island...");
				sbm.deleteIsland();
				p.sendMessage(SkyBlock.prefix + "Island deleted!");	
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
