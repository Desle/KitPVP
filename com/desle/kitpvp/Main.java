package com.desle.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.desle.kits.KitLoader;
import com.desle.kits.gui.GUIListener;
import com.desle.kits.gui.GUIManager;
import com.desle.players.PlayerStorage;

public class Main extends JavaPlugin {
	
	
	
	public static Main getMain() {
		return (Main) Bukkit.getPluginManager().getPlugin("KitPVP");
	}

	
	
	
	@Override
	public void onEnable() {
		
		
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		KitLoader.getInstance().loadKits();
		
	}
	
	
	@Override
	public void onDisable() {
		
		PlayerStorage.getInstance().saveAll();
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (label.equalsIgnoreCase("kits")) {
			if (!(sender instanceof Player)) return false;
			
			GUIManager.getInstance().openFor(Bukkit.getPlayer(sender.getName()));
		}
		return false;
	}
}
