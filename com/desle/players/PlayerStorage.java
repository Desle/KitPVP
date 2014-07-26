package com.desle.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.desle.kits.Kit;

public class PlayerStorage {
	
	
	
	
	
	
	private Map<UUID, Kit> currentKit = new HashMap<UUID, Kit>();
	private Map<UUID, List<Kit>> allKit = new HashMap<UUID, List<Kit>>();
	
	
	
	
	
	
	/*
	 * 
	 * GETTERS
	 * 
	 */
	
	public Kit getCurrentKit(Player player) {
		return currentKit.get(player.getUniqueId());
	}
	public List<Kit> getAllKits(Player player) {
		return allKit.get(player.getUniqueId());
	}
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * SETTERS
	 * 
	 */
	
	public void setCurrentKit(Player player, Kit kit) {
		currentKit.put(player.getUniqueId(), kit);
	}
	
	public void addKit(Player player, Kit kit) {
		List<Kit> kits = new ArrayList<Kit>(allKit.get(player.getUniqueId()));
		
		if (kits.contains(kit)) return;
		
		kits.add(kit);
		allKit.put(player.getUniqueId(), kits);
	}
}
