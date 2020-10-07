package com.thecrappiest.minions.addons.nametag.configurations;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;
import com.thecrappiest.minions.Core;
import com.thecrappiest.minions.addons.nametag.NametagAddon;
import com.thecrappiest.minions.messages.ConsoleOutput;

public class AddonConfiguration {

	private static AddonConfiguration instance;
	public static AddonConfiguration getInstance() {
		if(instance == null) {
			instance = new AddonConfiguration();
		}
		return instance;
	}
	
	String sChar = File.separator;
	private File nametag_file = new File(Core.instance.getDataFolder()+sChar+"Addons"+sChar+"Nametag"+sChar+"config.yml");
	YamlConfiguration nametag_yaml = null;

	public void loadDefault() {
		NametagAddon.getInstance().saveResource("config.yml", true);
		File loadedConfig = new File(NametagAddon.getInstance().getDataFolder()+sChar+"config.yml");
		if(loadedConfig.exists()) {
			try {
				Core.getInstance().createFile(nametag_file);
				Files.copy(loadedConfig, nametag_file);
			} catch (IOException e) {
				ConsoleOutput.debug("NameTag: config.yml has failed to copy to save location. Please message the author with any stack traces logged.");
				e.printStackTrace();
			}
			loadedConfig.delete();
		}
		if(NametagAddon.getInstance().getDataFolder().exists()) {
		    NametagAddon.getInstance().getDataFolder().delete();
		}
	}
	
	public YamlConfiguration getYaml() {
		if(nametag_yaml != null) {
			return nametag_yaml;
		}
		
		if(!nametag_file.exists()) {
			loadDefault();
		}
		nametag_yaml = YamlConfiguration.loadConfiguration(nametag_file);
		
		return nametag_yaml;
	}
	
	public void saveYaml(boolean reload) {
		YamlConfiguration yaml = getYaml();
		
		if(yaml != null) {
			try{
				if(reload) {
					yaml.load(nametag_file);
				}else {
					yaml.save(nametag_file);
				}
			}catch (IOException | InvalidConfigurationException exc) {
				ConsoleOutput.warn("NameTag: An error has occured while saving or loading the config file.");
				exc.printStackTrace();
			}
			
			nametag_yaml = yaml;
		}
	}
	
	public void loadConfig() {
		YamlConfiguration yaml = getYaml();
		if(yaml != null) {
			if(!yaml.getKeys(false).isEmpty()) {
			    ConsoleOutput.info("NameTag: config.yml loaded");
			}else {
				ConsoleOutput.info("NameTag: config.yml was loaded but is empty. Attempting to load default values...");
				loadDefault();
				yaml = getYaml();
				if(yaml != null && yaml.getKeys(false).isEmpty()) {
					ConsoleOutput.warn("NameTag: Attempting to load config.yml defaults has failed. Please message the author with any stack traces logged.");
				}else {
					ConsoleOutput.info("NameTag: The attempt to load config.yml defaults were succesful.");
				}
			}
		}
	}
	
	public static void clear(boolean save) {
		if(save) {
			getInstance().saveYaml(false);
		}
		
		getInstance().nametag_yaml = null;
	}
	
}
