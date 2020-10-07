package com.thecrappiest.minions.addons.nametag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.events.SaveMinionEvent;
import com.thecrappiest.objects.Minion;

public class SaveMinion implements Listener {

	public final NametagAddon nametagAddon;
	public SaveMinion(NametagAddon nametagAddon) {
		this.nametagAddon = nametagAddon;
		Bukkit.getPluginManager().registerEvents(this, nametagAddon);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onSave(SaveMinionEvent event) {
		Minion minion = event.getMinion();
		
		if(!nametagAddon.nametagged.containsKey(minion)) {return;}
		
		minion.addSaveData("NameTag", nametagAddon.nametagged.get(minion));
		if(nametagAddon.usesColor.contains(minion.getEntityID())) {
			minion.addSaveData("NameTag-Colored", true);
		}
	}
}
