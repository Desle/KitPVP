package com.desle.kits.gui;

import java.util.List;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.desle.cooldown.CooldownManager;
import com.desle.kitpvp.Main;
import com.desle.kits.Kit;
import com.desle.kits.KitLoader;
import com.desle.players.PlayerStorage;

public class GUIListener implements Listener {	
	
	
	
	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		
		if (!e.getInventory().equals(GUIManager.getInstance().getInventory((Player) e.getWhoClicked()))) return;
		
		
		final Player player = (Player) e.getWhoClicked();
		final ItemStack item = e.getCurrentItem();
		
		if (item == null || item.getType() == Material.AIR) return;
		
		KitLoader kl = KitLoader.getInstance();
		
		if (e.getRawSlot() == 1 || e.getRawSlot() == 9 || e.getRawSlot() == 10) e.setCancelled(true);
		
		if (kl.getKitByDisplayItem(item) == null) return;
		
		e.setCancelled(true);
		final Kit kit = kl.getKitByDisplayItem(item);
		
		PlayerStorage ps = PlayerStorage.getInstance();
		if (ps.getAllKits(player).contains(kit)) {
			
			if (ps.getCurrentKit(player) == kit) {
				/*
				 * 
				 *     SELECTED 
				 * 
				 */
				
				//
				
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
				GUIManager.getInstance().initializeGUI(player);
				
				return;
			} else {
				
				/*
				 * 
				 *   ON COOLDOWN
				 * 
				 * 
				 */
				
				final CooldownManager cm = new CooldownManager(Main.getMain());
				
				if (cm.isInCooldown(player.getUniqueId(), kit.getName())) {
					
					
					ItemMeta im = item.getItemMeta();
					List<String> lore = im.getLore();
					
					if (im.getLore().size() != GUIManager.getInstance().getOwned(kit.getDisplayItem()).getItemMeta().getLore().size()) return;
					
					lore.add(ChatColor.RED + "" + ChatColor.BOLD + cm.getTimeLeft(player.getUniqueId(), kit.getName()) + ChatColor.DARK_RED + "" + ChatColor.BOLD + " COOLDOWN");
					im.setLore(lore);
					item.setItemMeta(im);					
						
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
				    
			        NBTTagList am = new NBTTagList();
			        tag.set("AttributeModifiers", am);
			        nmsStack.setTag(tag);
					
				    ItemStack olditem = CraftItemStack.asCraftMirror(nmsStack);
				    
				    e.getInventory().setItem(e.getSlot(), olditem);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new BukkitRunnable() {
						
						@Override
						public void run() {
							
							try {
								
								ItemMeta im = item.getItemMeta();
								List<String> lore = im.getLore();
								lore.remove(lore.size() - 1);
								im.setLore(lore);
								item.setItemMeta(im);
								
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
							    
						        NBTTagList am = new NBTTagList();
						        tag.set("AttributeModifiers", am);
						        nmsStack.setTag(tag);
								
							    final ItemStack olditem = CraftItemStack.asCraftMirror(nmsStack);
								
							    e.getInventory().setItem(e.getSlot(), olditem);
								
								
							} catch (NullPointerException NPE) {
								GUIManager.getInstance().initializeGUI(player);
							}
							
						}
					}, 40L);
					
					return;
				}
				
				
				
				/*
				 * 
				 *     TO SELECT 
				 * 
				 */
				
				ps.setCurrentKit(player, kit);//
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
				GUIManager.getInstance().initializeGUI(player);
				
				return;
			}
		} else {			
			/*
			 * 
			 *     TO BUY
			 * 
			 */
			
			if (ps.getBalance(player) >= kit.getCost()) {
				
				ps.changeBalance(player, -kit.getCost());
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				
				ps.addKit(player, kit);
				ps.setCurrentKit(player, kit);
				
				GUIManager.getInstance().initializeGUI(player);
				
			} else {
				
				ItemMeta im = item.getItemMeta();
				List<String> lore = im.getLore();
				
				if (im.getLore().size() != GUIManager.getInstance().getOwned(kit.getDisplayItem()).getItemMeta().getLore().size()) return;
				
				lore.add(ChatColor.RED + "" + ChatColor.BOLD + (kit.getCost() - ps.getBalance(player)) + " COIN(s)" + ChatColor.DARK_RED + "" + ChatColor.BOLD + " SHORT");
				im.setLore(lore);
				item.setItemMeta(im);					
					
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
			    
		        NBTTagList am = new NBTTagList();
		        tag.set("AttributeModifiers", am);
		        nmsStack.setTag(tag);
				
			    ItemStack olditem = CraftItemStack.asCraftMirror(nmsStack);
			    
			    e.getInventory().setItem(e.getSlot(), olditem);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), new BukkitRunnable() {
					
					@Override
					public void run() {
						
						try {
							
							ItemMeta im = item.getItemMeta();
							List<String> lore = im.getLore();
							lore.remove(lore.size() - 1);
							im.setLore(lore);
							item.setItemMeta(im);
							
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
						    
					        NBTTagList am = new NBTTagList();
					        tag.set("AttributeModifiers", am);
					        nmsStack.setTag(tag);
							
						    final ItemStack olditem = CraftItemStack.asCraftMirror(nmsStack);
							
						    e.getInventory().setItem(e.getSlot(), olditem);
							
							
						} catch (NullPointerException NPE) {
							GUIManager.getInstance().initializeGUI(player);
						}
						
					}
				}, 40L);
				
			}
			
			//
			
			return;
			
		}
	}
}
