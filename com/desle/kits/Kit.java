package com.desle.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Kit {	
	
	
	public static List<Kit> list = new ArrayList<Kit>();
	
	private ItemStack displayitem;
	private int cooldown;
	private String name;
	private String description;
	private int cost;
	
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	
	private List<ItemStack> items;
	
//
	public Kit(
//
			ItemStack displayitem,
			int cooldown,
			String name,
			String description,
			int cost,
			
			ItemStack helmet,
			ItemStack chestplate,
			ItemStack leggings,
			ItemStack boots,
			
			List<ItemStack> items) {
		
		
		this.displayitem = displayitem;
		this.cooldown = cooldown;
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		
		this.items = items;
		
		list.add(this);
	}	
	
	
	/*
	 * 
	 *        GETTERS
	 * 
	 */
	
	
	public ItemStack getDisplayItem() {
		return this.displayitem;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getCooldown() {
		return this.cooldown;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public List<ItemStack>getItems() {
		return this.items;
	}
	
	public ItemStack getHelmet() {
		return this.helmet;
	}
	
	public ItemStack getChestplate() {
		return this.chestplate;
	}
	
	public ItemStack getLeggings() {
		return this.leggings;
	}
	
	public ItemStack getBoots() {
		return this.boots;
	}
}
