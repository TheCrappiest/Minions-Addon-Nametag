package com.thecrappiest.minions.addons.nametag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.events.MinionEntityRespawnEvent;
import com.thecrappiest.objects.Minion;

public class MinionRespawn implements Listener {

	public final NametagAddon nametagAddon;
	public MinionRespawn(NametagAddon nametagAddon) {
		this.nametagAddon = nametagAddon;
		Bukkit.getPluginManager().registerEvents(this, nametagAddon);
	}
	
	@EventHandler
	public void onRespawn(MinionEntityRespawnEvent event) {
		Minion minion = event.getMinion();
		Entity initial = event.getInitialEntity();
		ArmorStand as = (ArmorStand) minion.getEntity();
		
		if(!nametagAddon.nametagged.containsKey(minion)) {return;}
		
		if(nametagAddon.usesColor.contains(initial.getUniqueId())) {
			nametagAddon.usesColor.remove(initial.getUniqueId());
			nametagAddon.usesColor.add(as.getUniqueId());
		}
	}
	
}
