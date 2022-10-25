package com.thecrappiest.minions.addons.nametag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.json.simple.JSONObject;

import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.events.CreateMinionEntityEvent;
import com.thecrappiest.minions.events.LoadSavedMinionDataEvent;
import com.thecrappiest.minions.maps.miniondata.MinionData;
import com.thecrappiest.minions.messages.Messages;
import com.thecrappiest.minions.methods.ConversionMethods;
import com.thecrappiest.objects.Minion;

public class LoadMinionData implements Listener {

	public final NametagAddon nametagAddon;
	public LoadMinionData(NametagAddon nametagAddon) {
		this.nametagAddon = nametagAddon;
		Bukkit.getPluginManager().registerEvents(this, nametagAddon);
	}
	
	Messages msgUtil = Messages.util();
	
	@EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
	public void onLoadSavedData(LoadSavedMinionDataEvent event) {
		Minion minion = event.getMinion();
		ArmorStand as = (ArmorStand) minion.getEntity();
		
		String applyPlaceholders = msgUtil.addPlaceHolders(as.getCustomName(), minion.getPlaceHolders());
		as.setCustomName(applyPlaceholders);
		
		if(event.getData() == null) 
			return;
		
		JSONObject jsonData = ConversionMethods.parseString(event.getData());
		if(jsonData == null) 
			return;
		
		if(jsonData.containsKey("NameTag")) {
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
	
	@EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
	public void onCreateMinionEntity(CreateMinionEntityEvent event) {
        Location location = event.getPlaceLocation();
		
		Minion minion = null;
		if(location != null) {
			minion = MinionData.getInstance().getMinionFromLocation(location);
		}else if(event.getPath() != null) {
			String path = event.getPath().split("\\.")[1];
			String[] locData = path.replace(",", ".").split("\\|");
			String world = locData[0];
			double x = Double.valueOf(locData[1]);
			double y = Double.valueOf(locData[2]);
			double z = Double.valueOf(locData[3]);
			double yaw = Double.valueOf(locData[4]);
			location = new Location(Bukkit.getWorld(world),x,y,z,(float) yaw, 0f);
			minion = MinionData.getInstance().getMinionFromLocation(location);
		}
		
		if(minion == null)
			return;
		
		ArmorStand as = (ArmorStand) minion.getEntity();
		
		String applyPlaceholders = msgUtil.addPlaceHolders(as.getCustomName(), minion.getPlaceHolders());
		as.setCustomName(applyPlaceholders);
	}
}
