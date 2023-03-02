package me.nelonn.propack.bukkitgui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface GUI {
    void onClick(@NotNull InventoryClickEvent event);
}
