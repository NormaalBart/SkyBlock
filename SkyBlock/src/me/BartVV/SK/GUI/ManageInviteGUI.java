package me.BartVV.SK.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.BartVV.SK.Manager.PlayerManager;
import me.BartVV.SK.Utils.SkyBlock;

public class ManageInviteGUI{

	private static ManageInviteGUI ManageInviteGUI;
	
	public static ManageInviteGUI getInstance(){
		return ManageInviteGUI;
	}
	
	public static void setInstance(ManageInviteGUI migui){
		ManageInviteGUI = migui;
	}
	
	public void openManageInviteGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, 54, "§7(§9Invite Management§7)");
		List<String> lore = new ArrayList<>();
		lore.add("§cRight-Click to delete this user!");
		PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
		Integer slot = 10;
		for(UUID u : pm.getIsland().getVisitors()){
			OfflinePlayer of = Bukkit.getOfflinePlayer(u);
			setHead(inv, slot, "§7" + of.getName(), of.getName(), lore);
			slot++;
			if(slot == 17 || slot == 26 || slot == 35){
				slot = slot + 2;
			}
			if(slot == 44){
				break;
			}
		}
		lore.clear();
		lore.add("§7Go back to the options menu");
		setItemStack(inv, Material.IRON_BLOCK, 45, "§7Go Back", 1, null, lore);
		p.openInventory(inv);
	}
	
	public void manageListener(InventoryClickEvent e){
		Bukkit.broadcastMessage("2");
		if(isClickedInventory("§7(§9Invite Management§7)", e.getClickedInventory())){
			e.setCancelled(true);
			Integer slot = e.getSlot();
			Bukkit.broadcastMessage("3");
			if(slot == 45){
				Bukkit.broadcastMessage("4");
				OptionsGUI.getInstance().openOptions((Player)e.getWhoClicked());
				return;
			}else{
				if(e.getClick() == ClickType.RIGHT){
					switch(slot){
					case 10:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 11:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 12:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 13:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 14:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 15:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 16:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
						
					case 19:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 20:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 21:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 22:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 23:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 24:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 25:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
						
					case 28:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
					case 29:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 30:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 31:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 32:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 33:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 34:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
						
					case 37:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 38:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 39:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 40:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 41:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 42:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					case 43:
						deleteFriend(e.getClickedInventory(), slot, (Player)e.getWhoClicked());
						
					}
				}	
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void deleteFriend(Inventory inv, Integer slot, Player whoClicked){
		ItemStack is = inv.getItem(slot);
		if(is != null){
			if(is.hasItemMeta()){
				if(is.getItemMeta().hasDisplayName()){
					String name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
					OfflinePlayer of = Bukkit.getOfflinePlayer(name);
					PlayerManager target = PlayerManager.getPlayerManager(of.getUniqueId());
					PlayerManager sender = PlayerManager.getPlayerManager(whoClicked.getUniqueId());
					target.removeVisits(sender.getIsland());
					sender.getIsland().removeVisitor(of.getUniqueId());
					if(of.isOnline()){
						Player t = (Player)of;
						OfflinePlayer owner = Bukkit.getOfflinePlayer(sender.getIsland().getOwnerUUID());
						t.sendMessage(SkyBlock.prefix + "You can't visit the island of " + owner.getName() + " from now on!");
					}else{
						target.save();
					}
					whoClicked.sendMessage(SkyBlock.prefix + "Removed " + of.getName() + " from the visitors list!");
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
