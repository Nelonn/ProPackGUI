package me.nelonn.propack.bukkitgui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface GUI extends InventoryHolder {
    void onClick(@NotNull InventoryClickEvent event);
}
