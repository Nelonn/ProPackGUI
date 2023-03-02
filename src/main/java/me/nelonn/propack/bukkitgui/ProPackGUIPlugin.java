package me.nelonn.propack.bukkitgui;

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
        new ProPackGUICommand().register(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GUI gui) {
            gui.onClick(event);
        }
    }
}
