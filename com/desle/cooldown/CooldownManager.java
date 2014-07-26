package com.desle.cooldown;

import java.util.HashMap;
import java.util.Map;
 
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
 
public class CooldownManager {
 
    private long currentTime;
    private int cooldownTime;
    private static Map<String, CooldownManager> cooldowns = new HashMap<String, CooldownManager>();
    private String name;
    private Plugin plugin;
 
    public CooldownManager(Plugin plugin){
        this.plugin = plugin;
    }
 
    public void start(final Player player, int cooldownTime, final String name){
        final long currentTime = System.currentTimeMillis();
        this.currentTime = currentTime;
        this.cooldownTime = cooldownTime;
        this.name = name;
        cooldowns.put(name+player.getUniqueId().toString(), this);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            public void run(){
                cooldowns.remove(name+player.getUniqueId());
            }
        }, cooldownTime*20L);
    }
 
    public static CooldownManager getCooldown(String name, UUID uuid){
        return cooldowns.get(name+uuid.toString());
    }
 
    public boolean isInCooldown(UUID uuid, String name){
        return cooldowns.containsKey(name+uuid.toString());
    }
 
    public String getName(){
        return name;
    }
 
    public int getTimeLeft(UUID uuid, String name){
        long actual = System.currentTimeMillis();       
        long c = getCooldown(name, uuid).currentTime;
        int d = getCooldown(name, uuid).cooldownTime;
        int r = (int) (actual - c) / 1000;
        int f = (r - d) * (-1);
 
        return f;
    }
 
}