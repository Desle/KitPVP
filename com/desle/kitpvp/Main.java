package com.desle.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	
	
	public static Main getMain() {
		return (Main) Bukkit.getPluginManager().getPlugin("KitPVP");
	}
	

}
