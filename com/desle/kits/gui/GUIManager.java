package com.desle.kits.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
			
			int[] sizes = {9,18,27,36,45,54,63,72};
			
			for (int size : sizes) {
				if (Kit.list.size() <= size) {
					correctSize = size;
				}
			}
			
		}
		
		return correctSize;
	}
	
	
	public ItemStack getOwned(ItemStack item)  {
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
	    
	    return CraftItemStack.asCraftMirror(nmsStack);
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
		
		
		// OWNED
		for (Kit ownedkit : PlayerStorage.getInstance().getAllKits(player)) {
			inventory.addItem(getOwned(ownedkit.getDisplayItem()));
		}
		
		// NOT OWNED
		for (Kit kit : Kit.list) {
			ItemStack displayitem = kit.getDisplayItem();
			
			if (!PlayerStorage.getInstance().getAllKits(player).contains(kit))
				inventory.addItem(displayitem);
		}
	}
	
	
	
	
	
	public void openFor(Player player) {
		if (!players.containsKey(player.getUniqueId()))
			initializeGUI(player);
		
		
		player.openInventory(players.get(player.getUniqueId()));
		player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1, 1);
	}
}
