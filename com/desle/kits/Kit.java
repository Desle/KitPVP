package com.desle.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.desle.abilities.Ability;

public class Kit {	
	
	
	public static List<Kit> list = new ArrayList<Kit>();
	
	private ItemStack displayitem;
	private String name;
	private String description;
	private int cost;
	
	private Ability ability1;
	private Ability ability2;
	
	private ItemStack item1;
	private ItemStack item2;
	
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	
	
	
//
	public Kit(
//
			ItemStack displayitem,
			String name,
			String description,
			int cost,
			
			Ability ability1,
			Ability ability2,
			
			ItemStack item1,
			ItemStack item2,
			
			ItemStack helmet,
			ItemStack chestplate,
			ItemStack leggings,
			ItemStack boots) {
		
		
		this.displayitem = displayitem;
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.ability1 = ability1;
		this.ability2 = ability2;
		this.item1 = item1;
		this.item2 = item2;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		
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
	
	public int getCost() {
		return this.cost;
	}
	
	public Ability getAbility1() {
		return this.ability1;
	}
	
	public Ability getAbility2() {
		return this.ability2;
	}
	
	public ItemStack getItem1() {
		return this.item1;
	}
	
	public ItemStack getItem2() {
		return this.item2;
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
