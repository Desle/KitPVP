package com.desle.players;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.desle.kitpvp.Main;
import com.desle.kits.Kit;
import com.desle.kits.KitLoader;

public class PlayerStorage {
	
	
	private static PlayerStorage instance;
	public static PlayerStorage getInstance() {
		if (instance == null) 
			instance = new PlayerStorage();
		
		return instance;
	}
	
	
	
	private Map<UUID, Kit> currentKit = new HashMap<UUID, Kit>();
	private Map<UUID, List<Kit>> allKit = new HashMap<UUID, List<Kit>>();
	private Map<UUID, Integer> balance = new HashMap<UUID, Integer>();
	
	
	
	
	
	
	/*
	 * 
	 * GETTERS
	 * 
	 */
	
	public Kit getCurrentKit(Player player) {
		if (currentKit.get(player.getUniqueId()) == null)
			loadPlayer(player);
			
		return currentKit.get(player.getUniqueId());
	}
	
	public List<Kit> getAllKits(Player player) {
		if (allKit.get(player.getUniqueId()) == null)
			loadPlayer(player);
			
		return allKit.get(player.getUniqueId());
	}
	
	
	public int getBalance(Player player) {
		if (!balance.containsKey(player.getUniqueId()))
			loadPlayer(player);
			
		return balance.get(player.getUniqueId());
	}
	
	
	
	
	
	
	
	
	/*
	 * 
	 * SETTERS
	 * 
	 */
	
	public void changeBalance(Player player, int amount) {
		balance.put(player.getUniqueId(), balance.get(player.getUniqueId()) + amount);
	}
	
	
	public void setCurrentKit(Player player, Kit kit) {		
		currentKit.put(player.getUniqueId(), kit);
	}
	
	
	public void addKit(Player player, Kit kit) {
		if (!allKit.containsKey(player.getUniqueId()))
			loadPlayer(player);
		
		List<Kit> kits = new ArrayList<Kit>(getAllKits(player));
		
		if (kits.contains(kit)) return;
		
		kits.add(kit);
		allKit.put(player.getUniqueId(), kits);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * LOADER
	 * 
	 * 
	 */
	
	
	
	
	
	public void loadPlayer(Player player) {
		
		FileConfiguration f = getPlayerF();
		String path = "players." + player.getUniqueId().toString() + ".";
		
		allKit.put(player.getUniqueId(), new ArrayList<Kit>());
		
		
		
		// OWNED
		
		if (f.getStringList(path + "owned") == null || f.getStringList(path + "owned").isEmpty()) {
			
			addKit(player, KitLoader.getInstance().getDefault());
			
		} else {
			for (String kitname : f.getStringList(path + "owned")) {
			
				for (Kit kit : Kit.list) {
					if (kitname.equals(kit.getName())) {
						addKit(player, kit);
						break;
					}
				}
			}
		}
		
		
		
		
		
		// SELECTED
		
		if (f.getString(path + "selected") == null) {
			
			setCurrentKit(player, KitLoader.getInstance().getDefault());
			
		} else {
			for (Kit kit : Kit.list) {
			
				if (f.getString(path + "selected").equals(kit.getName())) {
					setCurrentKit(player, kit);
					break;
				}
			}
		}
		
		
		
		// BALANCE
		
		balance.put(player.getUniqueId(), f.getInt(path + ".coins"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 *                PLAYER FILE
	 * 
	 * 
	 * 
	 * 
	 */
	
	  private FileConfiguration kits = null;
	  private File PlayerF = null;
	
	
	  public void reloadPlayerF()
	  {
	    if (this.PlayerF == null) {
	      this.PlayerF = new File(Main.getMain().getDataFolder(), "players.yml");
	    }
	    this.kits = YamlConfiguration.loadConfiguration(this.PlayerF);

	    InputStream defConfigStream = Main.getMain().getResource("players.yml");
	    if (defConfigStream != null) {
	      @SuppressWarnings("deprecation")
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	      this.kits.setDefaults(defConfig);
	    }
	  }
	  
	  

	  public FileConfiguration getPlayerF() {
	    if (this.kits == null) {
	      reloadPlayerF();
	    }
	    return this.kits;
	  }

	  
	  
	  public void savePlayerF() {
	    if ((this.kits == null) || (this.PlayerF == null))
	      return;
	    try
	    {
	      getPlayerF().save(this.PlayerF);
	    } catch (IOException ex) {
	    	Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + this.PlayerF, ex);
	    }
	  }

	  
	  
	  public void saveDefaultLitF() {
	    if (this.PlayerF == null) {
	      this.PlayerF = new File(Main.getMain().getDataFolder(), "players.yml");
	    }
	    if (!this.PlayerF.exists())
	    	Main.getMain().saveResource("players.yml", false);
	  }
}
