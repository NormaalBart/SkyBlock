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

public class VisitGUI{
	
	private static VisitGUI VisitGUI = new VisitGUI();
	
	public static VisitGUI getInstance(){
		return VisitGUI;
	}
	
	public static void setInstance(VisitGUI vgui){
		VisitGUI = vgui;
	}
	
	public void manageListener(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player){
			if(isClickedInventory("§7(§9Visit GUI§7)", e.getClickedInventory())){
				Player p = (Player)e.getWhoClicked();
				ItemStack is = e.getCurrentItem();
				String dis = "nothing";
				Integer slot = e.getSlot();
				if(is.hasItemMeta()){
					if(is.getItemMeta().hasDisplayName()){
						dis = is.getItemMeta().getDisplayName();
					}
				}else{
					return;
				}
				if(dis.equals("§aYour island")){
					PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
					SkyBlockManager.TeleportToIsland(p, pm.getIsland());
					return;
				}else if (dis.equals("§cNo invites")){
					return;
				}
				else{
					if(slot >= 19 && slot <= 41){
						PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
						switch(slot){
						
						case 19:
							teleportIsland(is, pm, slot);
							break;
						case 20:
							teleportIsland(is, pm, slot);
							break;
						case 21:
							teleportIsland(is, pm, slot);
							break;
						case 22:
							teleportIsland(is, pm, slot);
							break;
						case 23:
							teleportIsland(is, pm, slot);
							break;
						case 24:
							teleportIsland(is, pm, slot);
							break;
						case 25:
							teleportIsland(is, pm, slot);
							break;
						case 28:
							teleportIsland(is, pm, slot);
							break;
						case 29:
							teleportIsland(is, pm, slot);
							break;
							
							//New row
						case 30:
							teleportIsland(is, pm, slot);
							break;
						case 31:
							teleportIsland(is, pm, slot);
							break;
						case 32:
							teleportIsland(is, pm, slot);
							break;
						case 33:
							teleportIsland(is, pm, slot);
							break;
						case 34:
							teleportIsland(is, pm, slot);
							break;
							
							//New row
						case 37:
							teleportIsland(is, pm, slot);
							break;
						case 38:
							teleportIsland(is, pm, slot);
							break;
						case 39:
							teleportIsland(is, pm, slot);
							break;
						case 40:
							teleportIsland(is, pm, slot);
							break;
						case 41:
							teleportIsland(is, pm, slot);
							break;
						case 42:
							teleportIsland(is, pm, slot);
							break;
						case 43:
							teleportIsland(is, pm, slot);	
							break;
						default:
							return;
						}
					}
				}
			}
		}
	}
	
	public void openVisit(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "§7(§9Visit GUI§7)");
		List<String> lore = new ArrayList<>();
		PlayerManager pm = PlayerManager.getPlayerManager(p.getUniqueId());
		
		if(pm.getIsland() != null){
			lore.add("§7Click here to teleport");
			lore.add("§7to your island!");
			setItemStack(inv, Material.ENDER_PORTAL_FRAME, 4, "§aYour island", 1, null, lore);
			lore.clear();
			if(pm.getIsland().getOwnerUUID().equals(p.getUniqueId()) || pm.canInvite()){
				lore.add("§7Manage over here");
				lore.add("§7the people who can visit your island!");
				setItemStack(inv, Material.ANVIL, 8, "§9Manage your invites",1 , null, lore);
				lore.clear();
			}
		}else{
			lore.add("§7Create or be invited to");
			lore.add("§7teleport to your island!");
			setItemStack(inv, Material.ENDER_PORTAL_FRAME, 4, "§cNo island", 1, null, lore);
			lore.clear();
			
		}
		if(pm.getVisits().size() == 0){
			lore.add("§7You don't have any invites!");
			lore.add("§7Let people do §9/invite " + p.getName());
			lore.add("§7to be invited!");
			setItemStack(inv, Material.REDSTONE_BLOCK, 31, "§cNo invites", 1, null, lore);
			lore.clear();
		}else{
			setInvites(inv, pm);
		}
		p.openInventory(inv);
	}
	
	private void setInvites(Inventory inv, PlayerManager pm) {
		Integer i = 19;
		List<String> lore = new ArrayList<>();
		lore.add("§7Click here");
		lore.add("§7to visit the island of");
		for(SkyBlockManager sbm : pm.getVisits()){
			if(sbm != null){
				lore.set(1, "§a" + sbm.getOwnerName() + "§7 and " + (sbm.getIntOnlinePlayers()-1) + " other players!");
				setItemStack(inv, Material.EMERALD_BLOCK, i, "§a" + sbm.getOwnerName() + "'s island", 1, null, lore);
				i++;
				if(i == 25){
					i = 28;
				}else if (i == 34){
					i = 37;
				}else if(i == 44){
					break;
				}
			}else{
				return;
			}
		}
	}
	
	private void teleportIsland(ItemStack is, PlayerManager pm, Integer slot){
		if(is == null || pm == null || slot == null) return;
		slot = slot-19;
		SkyBlockManager sbm = pm.getVisits().get(slot);
		if(sbm == null){
			return;
		}
		SkyBlockManager.TeleportToIsland(pm.getPlayer(), sbm);
		pm.getPlayer().sendMessage(SkyBlock.prefix + "Teleporting to the island of " + sbm.getOwnerName() + "!");
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
