package com.thecrappiest.minions.addons.nametag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.thecrappiest.minions.addons.nametag.configurations.AddonConfiguration;
import com.thecrappiest.minions.addons.nametag.listeners.CreateMinion;
import com.thecrappiest.minions.addons.nametag.listeners.InteractWithMinion;
import com.thecrappiest.minions.addons.nametag.listeners.PerformAction;
import com.thecrappiest.minions.addons.nametag.listeners.PickupMinion;
import com.thecrappiest.minions.addons.nametag.listeners.SaveMinion;
import com.thecrappiest.minions.messages.ConsoleOutput;
import com.thecrappiest.objects.Minion;

public class NametagAddon extends JavaPlugin {

	public static NametagAddon instance;

	public void onEnable() {
		instance = this;
		
		ConsoleOutput.info("Configuration File Loader:");
		ConsoleOutput.info(" ");
		ConsoleOutput.info("Addon Configurations: NameTag");
		AddonConfiguration.getInstance().loadConfig();
		
		new InteractWithMinion(this);
		new CreateMinion(this);
		new PickupMinion(this);
		new SaveMinion(this);
		new PerformAction(this);
	}
	
	public void onDisable() {
		
		for(Entry<Minion, String> entry : nametagged.entrySet()) {
			Minion minion = entry.getKey();
			String nameTag = entry.getValue();
			minion.addSaveData("NameTag", nameTag);
			if(usesColor.contains(minion.getEntityID())) {
				minion.addSaveData("NameTag-Colored", true);
			}
		}
		
		nametagged.clear();
		usesColor.clear();

		instance = null;
	}
	
	public static NametagAddon getInstance() {
		return instance;
	}

	public Map<Minion, String> nametagged = new HashMap<>();
	public List<UUID> usesColor = new ArrayList<>();

}
