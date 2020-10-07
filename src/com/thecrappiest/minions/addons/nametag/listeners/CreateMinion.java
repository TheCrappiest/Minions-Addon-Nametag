package com.thecrappiest.minions.addons.nametag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.events.CreateMinionEntityEvent;
import com.thecrappiest.minions.maps.miniondata.MinionData;
import com.thecrappiest.minions.messages.Messages;
import com.thecrappiest.objects.Minion;

public class CreateMinion implements Listener {

	public final NametagAddon nametagAddon;
	public CreateMinion(NametagAddon nametagAddon) {
		this.nametagAddon = nametagAddon;
		Bukkit.getPluginManager().registerEvents(this, nametagAddon);
	}
	
	@EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
	public void onCreateMinionEntity(CreateMinionEntityEvent event) {
		Location location = event.getPlaceLocation();
		
		Minion minion = null;
		if(location != null) {
			minion = MinionData.getInstance().getMinionFromLocation(location);
		}else {
			if(event.getPath() != null) {
				String path = event.getPath().split("\\.")[1];
				String world = path.split("_")[0];
				double x = Double.valueOf(path.split("_")[1].replace("|", "."));
				double y = Double.valueOf(path.split("_")[2].replace("|", "."));
				double z = Double.valueOf(path.split("_")[3].replace("|", "."));
				double yaw = Double.valueOf(path.split("_")[4].replace("|", "."));
				location = new Location(Bukkit.getWorld(world),x,y,z,(float) yaw, 0f);
				minion = MinionData.getInstance().getMinionFromLocation(location);
			}
		}
		
		if(minion != null) {
			if(event.getData() == null) {return;}
			JSONObject jsonData = null;
			try {
				jsonData = (JSONObject) new JSONParser().parse(event.getData());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(jsonData == null) {return;}
			
			if(jsonData.containsKey("NameTag")) {
				ArmorStand as = (ArmorStand) minion.getEntity();
				boolean useColor = jsonData.containsKey("NameTag-Colored");
				
				String displayName = jsonData.get("NameTag").toString();
				nametagAddon.nametagged.put(minion, displayName);
				
				String nameWithPlaceholders = Messages.util().addPlaceHolders(displayName, minion.getPlaceHolders());
				as.setCustomName(Messages.util().removeColor(nameWithPlaceholders));
				
				if(useColor) {
					as.setCustomName(Messages.util().addColor(nameWithPlaceholders));
					nametagAddon.usesColor.add(minion.getEntityID());
				}
				as.setCustomNameVisible(true);
			}
		}
		
	}
}
