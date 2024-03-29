package com.thecrappiest.minions.addons.nametag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.addons.nametag.configurations.AddonConfiguration;
import com.thecrappiest.minions.events.InteractWithMinionEvent;
import com.thecrappiest.minions.messages.Messages;
import com.thecrappiest.objects.Minion;
import com.thecrappiest.versionclasses.VersionMaterial;

public class InteractWithMinion implements Listener {

	public final NametagAddon nametagAddon;
	public InteractWithMinion(NametagAddon nametagAddon) {
		this.nametagAddon = nametagAddon;
		Bukkit.getPluginManager().registerEvents(this, nametagAddon);
	}
	
	Messages msgUtil = Messages.util();
	
	@EventHandler(ignoreCancelled=true, priority=EventPriority.LOW)
	public void onInteractWithMinion(InteractWithMinionEvent event) {
		Player player = event.getPlayer();
		Minion minion = event.getMinion();
		ItemStack interactionItem = event.getInteractionItem();
		
		if(!minion.getOwner().equals(player.getUniqueId())) {return;}
		if(interactionItem == null || (interactionItem != null && interactionItem.getType() == VersionMaterial.AIR.getMaterial())) {return;}
		if(interactionItem.getType() != VersionMaterial.NAME_TAG.getMaterial()) {return;}
		if(interactionItem.hasItemMeta() && !interactionItem.getItemMeta().hasDisplayName()) {return;}
		if(interactionItem.getItemMeta().getDisplayName().equals(VersionMaterial.NAME_TAG.getItemStack().getItemMeta().getDisplayName())) {return;}
		
		String displayName = interactionItem.getItemMeta().getDisplayName();
		Entity entity = minion.getEntity();
		
		if(nametagAddon.nametagged.containsKey(minion)) {
			String currentName = nametagAddon.nametagged.get(minion);
			if(nametagAddon.usesColor.contains(minion.getEntityID())) {
				currentName = msgUtil.addColor(nametagAddon.nametagged.get(minion));
			}
			
			String currName = currentName;
			if(msgUtil.removeColor(nametagAddon.nametagged.get(minion)).equals(msgUtil.removeColor(displayName))) {
				Bukkit.getScheduler().runTaskLater(nametagAddon, () -> entity.setCustomName(currName), 3);
				return;
			}
		}
		
		YamlConfiguration yaml = AddonConfiguration.getInstance().getYaml();
		
		if(!player.hasPermission(yaml.getString("Use_Permission"))) {return;}
		boolean useColor = player.hasPermission(yaml.getString("Use_ColorCodes"));
		
		nametagAddon.nametagged.put(minion, msgUtil.removeColor(displayName));
		
		minion.getPlaceHolders();
		
		String nameWithPlaceholders = msgUtil.addPlaceHolders(displayName, minion.getPlaceHolders());
		String withoutColor = msgUtil.removeColor(nameWithPlaceholders);
		String withColor = msgUtil.addColor(nameWithPlaceholders);
		
		event.setCancelled(true);
		
		new BukkitRunnable() {
			public void run() {
				if(useColor) {
					entity.setCustomName(withColor);
					nametagAddon.usesColor.add(minion.getEntityID());
				}else {
					entity.setCustomName(withoutColor);
				}
				entity.setCustomNameVisible(true);
			}
		}.runTaskLater(nametagAddon, 1);
		
		if(interactionItem.getAmount() > 1) {
			ItemStack cloneItem = interactionItem.clone();
			cloneItem.setAmount(1);
			player.getInventory().removeItem(cloneItem);
		}else {
		    player.getInventory().removeItem(interactionItem);
	    }
		
		event.setCancelled(true);
	}
	
}
