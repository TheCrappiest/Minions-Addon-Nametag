package com.thecrappiest.minions.addons.nametag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.events.PickupMinionEvent;
import com.thecrappiest.minions.messages.Messages;
import com.thecrappiest.objects.Minion;
import com.thecrappiest.versionclasses.VersionMaterial;

public class PickupMinion implements Listener {

	public final NametagAddon nametagAddon;
	public PickupMinion(NametagAddon nametagAddon) {
		this.nametagAddon = nametagAddon;
		Bukkit.getPluginManager().registerEvents(this, nametagAddon);
	}
	
	Messages msgUtil = Messages.util();
	
	@EventHandler(ignoreCancelled=true)
	public void onPickup(PickupMinionEvent event) {
		Player player = event.getPlayer();
		Minion minion = event.getMinion();
		
		if(!nametagAddon.nametagged.containsKey(minion))
			return;
		
		ItemStack nametag = new ItemStack(VersionMaterial.NAME_TAG.getItemStack());
		ItemMeta nametagMeta = nametag.getItemMeta();
		
		String displayName = nametagAddon.nametagged.get(minion);
		nametagMeta.setDisplayName(displayName);
		if(nametagAddon.usesColor.contains(minion.getEntityID()))
			nametagMeta.setDisplayName(msgUtil.addColor(displayName));
		
		nametag.setItemMeta(nametagMeta);
		
		if(player.getInventory().firstEmpty() == -1)
			player.getWorld().dropItemNaturally(player.getLocation(), nametag);
		else
			player.getInventory().addItem(nametag);
		
		nametagAddon.nametagged.remove(minion);
		nametagAddon.usesColor.remove(minion.getEntityID());
	}
}
