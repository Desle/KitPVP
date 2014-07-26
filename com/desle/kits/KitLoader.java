package com.desle.kits;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desle.kitpvp.Main;

public class KitLoader {
	
	
	private static KitLoader instance;
	public static KitLoader getInstance() {
		if (instance == null) 
			instance = new KitLoader();
		
		return instance;
	}

	
	
	private ItemStack loadItem(String path) {
		FileConfiguration f = getKitF();
		
		if (f.get(path) == null) {
			return new ItemStack(Material.AIR);
		}
		
		ItemStack item = new ItemStack(Material.matchMaterial(f.getString(path + ".material").toUpperCase()));
		
		
		/*
		 * 
		 * USUAL CRAP */
		if (f.get(path + ".amount") != null) {
			item.setAmount(f.getInt(path + ".amount"));
		}
		if (f.get(path + ".durability") != null) {
			item.setDurability((short) f.getInt(path + ".durability"));
		}
		
		
		/*
		 * 
		 * ITEM META */
		ItemMeta itemmeta = item.getItemMeta();
		
		if (f.getString(path + ".name") != null) {
			itemmeta.setDisplayName(f.getString(path + ".name"));
		}
		if (f.getStringList(path + ".lore") != null) {
			itemmeta.setLore(f.getStringList(path + ".lore"));
		}
		
		item.setItemMeta(itemmeta);
		
		
		/*
		 * 
		 * ENCHANTMENTS */
		if (f.getStringList(path + ".enchantments") != null) {
			for (String enchantstring : f.getStringList(path + ".enchantments")) {
			
				Enchantment enchantment = Enchantment.getByName(enchantstring.split("@")[0]);
				int level = Integer.parseInt(enchantstring.split("@")[1]);
			
				item.addUnsafeEnchantment(enchantment, level);
			}
		}
//
		return item;
//
	}
	
	
	
	
	
	/*
	 * 
	 * LOAD AND UNLOAD
	 * 
	 */
	
	
	public void loadKits() {
		
		FileConfiguration f = getKitF();
		for (String name : f.getConfigurationSection("kits").getKeys(false)) {
			
			String key = "kits." + name + ".";
		
			ItemStack displayitem = loadItem(key + "displayitem");
			
			String description = f.getString(key + "description");
			
			int cooldown = f.getInt(key + "cooldown");
			int cost = f.getInt(key + "cost");
			
			List<ItemStack> items = new ArrayList<ItemStack>();
			
			for (String k : f.getConfigurationSection(key + "items").getKeys(false)) {
				items.add(loadItem(key + "items" + k));
			}
			
			ItemStack helmet = loadItem(key + "helmet");
			ItemStack chestplate = loadItem(key + "chestplate");
			ItemStack leggings = loadItem(key + "leggings");
			ItemStack boots = loadItem(key + "boots");
			
			new Kit(displayitem, cooldown, name, description, cost, helmet, chestplate, leggings, boots, items);
		}
	}
	
	
	
	
	
	
	public List<Kit> getDefault() {
		
		List<String> defs = getKitF().getStringList("defaultkits");
		
		List<Kit> kits = new ArrayList<Kit>();
		
		for (Kit kit : Kit.list) {
			if (defs.contains(kit.getName())) {
				kits.add(kit);
			}
		}
		
		return kits;
	}
	
	
	public Kit getKitByDisplayItem(ItemStack item) {
		
		ItemMeta im = item.getItemMeta();
		
		if (!im.hasLore()) return null;
		if (!im.hasDisplayName()) return null;
		
		im.getLore().remove(im.getLore().size() - 1);
		im.getLore().remove(im.getLore().size() - 1);
		item.setItemMeta(im);
		
		for (Kit kit : Kit.list) {
			
			if (
					kit.getDisplayItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()) &&
					kit.getDisplayItem().getType() == item.getType())
			
				return kit;
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 *                KIT FILE
	 * 
	 * 
	 * 
	 * 
	 */
	
	  private FileConfiguration kits = null;
	  private File KitF = null;
	
	
	  public void reloadKitF()
	  {
	    if (this.KitF == null) {
	      this.KitF = new File(Main.getMain().getDataFolder(), "kits.yml");
	    }
	    this.kits = YamlConfiguration.loadConfiguration(this.KitF);

	    InputStream defConfigStream = Main.getMain().getResource("kits.yml");
	    if (defConfigStream != null) {
	      @SuppressWarnings("deprecation")
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	      this.kits.setDefaults(defConfig);
	    }
	  }
	  
	  

	  public FileConfiguration getKitF() {
	    if (this.kits == null) {
	      reloadKitF();
	    }
	    return this.kits;
	  }

	  
	  
	  public void saveKitF() {
	    if ((this.kits == null) || (this.KitF == null))
	      return;
	    try
	    {
	      getKitF().save(this.KitF);
	    } catch (IOException ex) {
	    	Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + this.KitF, ex);
	    }
	  }

	  
	  
	  public void saveDefaultLitF() {
	    if (this.KitF == null) {
	      this.KitF = new File(Main.getMain().getDataFolder(), "kits.yml");
	    }
	    if (!this.KitF.exists())
	    	Main.getMain().saveResource("kits.yml", false);
	  }
}
