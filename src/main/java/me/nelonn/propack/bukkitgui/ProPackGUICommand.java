package me.nelonn.propack.bukkitgui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class ProPackGUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender, args);
        return true;
    }

    private void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender.hasPermission("propackgui.open")) {
            sender.sendMessage(ChatColor.RED + "No permission");
            return;
        }
        Inventory inventory = Bukkit.createInventory(null, 6 * 9);
    }
}
