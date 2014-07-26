package com.desle.kits.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desle.kits.Kit;
import com.desle.players.PlayerStorage;

public class GUIManager {
	
	
	
	
	
	
	private static GUIManager instance;
	public static GUIManager getInstance() {
		if (instance == null) 
			instance = new GUIManager();
		
		return instance;
	}
	
	
	
	
	
	
	
	int correctSize;
	Map<UUID, Inventory> players = new HashMap<UUID, Inventory>();
	
	
	
	
	
	
	/*
	 * 
	 *       GETTERS
	 * 
	 */
	
	public int getCorrectSize() {
		if (correctSize == 0) {
			
			int[] sizes = {18,27,36,45,54,63,72};
			
			for (int size : sizes) {
				if (Kit.list.size() + 4 <= size) {
					correctSize = size;
				}
			}
			
		}
		
		return correctSize;
	}
	
	
	
	
	public ItemStack getOwned(ItemStack item)  {
		if (item == null) return item;
		
		net.minecraft.server.v1_7_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag;
		
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		} else {
			tag = nmsStack.getTag();
		}
		
		NBTTagList ench = new NBTTagList();
	    tag.set("ench", ench);
	    nmsStack.setTag(tag);
	    
	    ItemStack owneditem = CraftItemStack.asCraftMirror(nmsStack);
	    
	    ItemMeta im = owneditem.getItemMeta();
	    List<String> lore = new ArrayList<String>(im.getLore());
	    lore.add("");
	    lore.add(ChatColor.GREEN + "Click to select");
	    im.setLore(lore);
	    
	    owneditem.setItemMeta(im);
	    return owneditem;
	}
	
	public ItemStack getUnowned(ItemStack item, int price) {
		if (item == null) return item;
		
		ItemStack unowneditem = item.clone();
		
	    ItemMeta im = unowneditem.getItemMeta();
	    List<String> lore = new ArrayList<String>(im.getLore());
	    lore.add("");
	    lore.add(ChatColor.GRAY + "purchase for " + ChatColor.YELLOW + "" + ChatColor.BOLD + price + " COIN(s)");
	    im.setLore(lore);
	    
	    unowneditem.setItemMeta(im);
		return unowneditem;
	}
	
	public ItemStack getSelected(ItemStack item) {
		if (item == null) return item;
		
		ItemStack selecteditem = item.clone();
		
	    ItemMeta im = selecteditem.getItemMeta();
	    List<String> lore = new ArrayList<String>(im.getLore());
	    lore.add("");
	    lore.add(ChatColor.GRAY + "Currently selected");
	    im.setLore(lore);
	    
	    selecteditem.setItemMeta(im);
	    
	    return selecteditem;
	}
	
	
	
	
	
	
	
	/*
	 * 
	 *          SETTERS
	 * 
	 */
	
	public void initializeGUI(Player player) {
		if (!players.containsKey(player.getUniqueId()))
			players.put(player.getUniqueId(), Bukkit.createInventory(null, getCorrectSize(), "Kit Selector"));
		
		Inventory inventory = players.get(player.getUniqueId());
		
		// SELECTED
		ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta im = border.getItemMeta();
		im.setDisplayName("Currently selected");
		border.setItemMeta(im);
		
		
		inventory.setItem(0, getSelected(PlayerStorage.getInstance().getCurrentKit(player).getDisplayItem()));
		inventory.setItem(1, border);
		inventory.setItem(9, border);
		inventory.setItem(10, border);
		
		// OWNED
		for (Kit ownedkit : PlayerStorage.getInstance().getAllKits(player)) {
			if (!PlayerStorage.getInstance().getCurrentKit(player).equals(ownedkit)) {
				inventory.addItem(getOwned(ownedkit.getDisplayItem()));
			}
		}
		
		// NOT OWNED
		for (Kit kit : Kit.list) {
			ItemStack displayitem = kit.getDisplayItem();
			
			if (!PlayerStorage.getInstance().getAllKits(player).contains(kit))
				inventory.addItem(getUnowned(displayitem, kit.getCost()));
		}
	}
	
	
	
	
	
	public void openFor(Player player) {
		
		if (!players.containsKey(player.getUniqueId()))
			initializeGUI(player);
		
		
		player.openInventory(players.get(player.getUniqueId()));
		player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1, 1);
	}
}
