package com.desle.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.desle.kits.KitLoader;
import com.desle.kits.gui.GUIManager;

public class Main extends JavaPlugin {
	
	
	
	public static Main getMain() {
		return (Main) Bukkit.getPluginManager().getPlugin("KitPVP");
	}

	
	
	
	@Override
	public void onEnable() {
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		KitLoader.getInstance().loadKits();
		
		GUIManager.getInstance().openFor(Bukkit.getOnlinePlayers()[0
		                                                           ]);
	}
}
