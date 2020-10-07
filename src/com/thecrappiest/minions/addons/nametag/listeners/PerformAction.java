package com.thecrappiest.minions.addons.nametag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.events.MinionPerformTaskEvent;
import com.thecrappiest.minions.messages.Messages;
import com.thecrappiest.objects.Minion;

public class PerformAction implements Listener {

	public final NametagAddon nametagAddon;
	public PerformAction(NametagAddon nametagAddon) {
		this.nametagAddon = nametagAddon;
		Bukkit.getPluginManager().registerEvents(this, nametagAddon);
	}
	
	@EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
	public void onPerformTask(MinionPerformTaskEvent event) {
		Minion minion = event.getMinion();
		ArmorStand as = (ArmorStand) minion.getEntity();
		
		if(!nametagAddon.nametagged.containsKey(minion)) {return;}
		
		String displayName = nametagAddon.nametagged.get(minion);
		boolean usesColor = nametagAddon.usesColor.contains(minion.getEntityID());
		
		String nameWithPlaceholders = Messages.util().addPlaceHolders(displayName, minion.getPlaceHolders());
		as.setCustomName(Messages.util().removeColor(nameWithPlaceholders));
		
		if(usesColor) {
			as.setCustomName(Messages.util().addColor(nameWithPlaceholders));
		}
		as.setCustomNameVisible(true);
	}
	
}
