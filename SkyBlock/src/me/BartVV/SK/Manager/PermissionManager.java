package me.BartVV.SK.Manager;

import org.bukkit.entity.Player;

public class PermissionManager{

	private Player p;
	private Boolean canOpenChest = false;
	private Boolean canBreakBlocks = false;
	private Boolean canDestroyBlocks = false;
	private Boolean canEditPermissions = false;
	private Boolean canEditOptions = false;
	private Boolean canInvite = false;
	private Boolean canKick = false;
	private Boolean canIslandInvite = false;
	
	public PermissionManager(Player p){
		this.p = p;
	}

	public Player getPlayer(){
		return p;
	}
	public Boolean canOpenChest() {
		return canOpenChest;
	}

	public void canOpenChest(Boolean canOpenChest) {
		this.canOpenChest = canOpenChest;
	}

	public Boolean canBreakBlocks() {
		return canBreakBlocks;
	}

	public void canBreakBlocks(Boolean canBreakBlocks) {
		this.canBreakBlocks = canBreakBlocks;
	}

	public Boolean canDestroyBlocks() {
		return canDestroyBlocks;
	}

	public void canDestroyBlocks(Boolean canDestroyBlocks) {
		this.canDestroyBlocks = canDestroyBlocks;
	}

	public Boolean canEditPermissions() {
		return canEditPermissions;
	}

	public void canEditPermissions(Boolean canEditPermissions) {
		this.canEditPermissions = canEditPermissions;
	}

	public Boolean canInvite() {
		return canInvite;
	}

	public void canInvite(Boolean canInvite) {
		this.canInvite = canInvite;
	}

	public Boolean canKick() {
		return canKick;
	}

	public void canKick(Boolean canKick) {
		this.canKick = canKick;
	}

	public Boolean CanEditOptions() {
		return canEditOptions;
	}

	public void CanEditOptions(Boolean canEditOptions) {
		this.canEditOptions = canEditOptions;
	}

	public Boolean CanIslandInvite() {
		return canIslandInvite;
	}

	public void CanIslandInvite(Boolean canIslandInvite) {
		this.canIslandInvite = canIslandInvite;
	}
}
