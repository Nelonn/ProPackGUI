package me.nelonn.propack.bukkitgui;

import me.nelonn.commandlib.bukkit.BukkitCommands;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ProPackGUIPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        BukkitCommands.register(this, new ProPackGUICommand());
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof CUI cui) {
            cui.onClick(event);
        }
    }
}
