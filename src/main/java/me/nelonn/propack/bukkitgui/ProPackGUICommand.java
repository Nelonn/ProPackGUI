package me.nelonn.propack.bukkitgui;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.nelonn.commandlib.Command;
import me.nelonn.commandlib.CommandContext;
import me.nelonn.flint.path.Key;
import me.nelonn.flint.path.Path;
import me.nelonn.propack.ResourcePack;
import me.nelonn.propack.asset.ItemModel;
import me.nelonn.propack.bukkit.ProPack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProPackGUICommand extends Command<CommandSender> {
    public ProPackGUICommand() {
        super("propackgui", "ppgui");
        requires(s -> s.hasPermission("propackgui.use"));
    }

    @Override
    public boolean run(@NotNull CommandContext<CommandSender> commandContext) {
        CommandSender sender = commandContext.getSource();
        if (!(sender instanceof Player player)) return false;
        ResourcePack resourcePack = ProPack.getCore().getDispatcher().getAppliedResourcePack(player);
        if (resourcePack == null) {
            sender.sendMessage(ChatColor.RED + "Missing resource pack");
            return false;
        }
        player.openInventory(new ListGUI(resourcePack).getInventory());
        return true;
    }

    private static final class ListGUI implements CUI {
        private static final int ROWS = 6;
        private final ResourcePack resourcePack;
        private final Inventory inventory;
        private int page = 1;

        @SuppressWarnings("deprecation")
        public ListGUI(@NotNull ResourcePack resourcePack) {
            this.resourcePack = resourcePack;
            inventory = Bukkit.createInventory(this, ROWS * 9, "Models in " + resourcePack.getName());
            render();
        }

        public void render() {
            int pages = (int) Math.ceil(((double) resourcePack.resources().getItemModels().size()) / ((ROWS - 1) * 9));
            if (pages < 1) pages = 1;
            page = page < 1 ? 1 : Math.min(page, pages);

            inventory.clear();

            if (pages > 1) {
                if (page != 1) {
                    inventory.setItem((ROWS * 9) - 9, new ItemStackBuilder(Material.ARROW).setDisplayName("Previous").getItem());
                }
                if (page < pages) {
                    inventory.setItem((ROWS * 9) - 1, new ItemStackBuilder(Material.ARROW).setDisplayName("Next").getItem());
                }
            }

            int start = (page - 1) * ((ROWS - 1) * 9);
            int end = page == pages ? resourcePack.resources().getItemModels().size() : page * ((ROWS - 1) * 9);
            List<ItemModel> itemModels = resourcePack.resources().getItemModels().stream().toList().subList(start, end);

            for (int i = 0; i < itemModels.size(); i++) {
                ItemModel itemModel = itemModels.get(i);
                Material material = Material.matchMaterial(itemModel.getTargetItems().stream().findFirst().orElseThrow().toString());
                ItemStackBuilder itemStack = new ItemStackBuilder(material)
                        .setDisplayName(itemModel.friendlyPath().value())
                        .setLore("&7" + itemModel.friendlyPath().namespace());
                itemStack.getNBT().setString("CustomModel", itemModel.friendlyPath().toString());
                inventory.setItem(i, itemStack.getItem());
            }
        }

        @Override
        public void onClick(@NotNull InventoryClickEvent event) {
            if (!inventory.equals(event.getClickedInventory())) return;
            event.setCancelled(true);

            if (event.getSlot() == (ROWS * 9) - 9) {
                page--;
                render();
            } else if (event.getSlot() == (ROWS * 9) - 1) {
                page++;
                render();
            } else if (event.getCurrentItem() != null) {
                try {
                    NBTItem nbtItem = new NBTItem(event.getCurrentItem());
                    Path path = Path.of(nbtItem.getString("CustomModel"));
                    ItemModel itemModel = resourcePack.resources().itemModel(path);
                    event.getWhoClicked().openInventory(new GiveModelGUI(itemModel).getInventory());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public @NotNull Inventory getInventory() {
            return inventory;
        }
    }

    private static final class GiveModelGUI implements CUI {
        private static final int ROWS = 6;
        private final ItemModel itemModel;
        private final Inventory inventory;
        private int page = 1;

        @SuppressWarnings("deprecation")
        public GiveModelGUI(@NotNull ItemModel itemModel) {
            this.itemModel = itemModel;
            inventory = Bukkit.createInventory(this, ROWS * 9, "Give " + itemModel.friendlyPath());
            render();
        }

        public void render() {
            int pages = (int) Math.ceil(((double) itemModel.getTargetItems().size()) / ((ROWS - 1) * 9));
            if (pages < 1) pages = 1;
            page = page < 1 ? 1 : Math.min(page, pages);

            inventory.clear();

            if (pages > 1) {
                if (page != 1) {
                    inventory.setItem((ROWS * 9) - 9, new ItemStackBuilder(Material.ARROW).setDisplayName("Previous").getItem());
                }
                if (page < pages) {
                    inventory.setItem((ROWS * 9) - 1, new ItemStackBuilder(Material.ARROW).setDisplayName("Next").getItem());
                }
            }

            int start = (page - 1) * ((ROWS - 1) * 9);
            int end = page == pages ? itemModel.getTargetItems().size() : page * ((ROWS - 1) * 9);
            List<Key> targetItems = itemModel.getTargetItems().stream().toList().subList(start, end);

            for (int i = 0; i < targetItems.size(); i++) {
                Key targetItemId = targetItems.get(i);
                Material material = Material.matchMaterial(targetItemId.toString());
                ItemStackBuilder itemStack = new ItemStackBuilder(material)
                        .setLore("&7" + itemModel.friendlyPath(), "&fas: " + targetItemId);
                inventory.setItem(i, itemStack.getItem());
            }
        }

        @Override
        public void onClick(@NotNull InventoryClickEvent event) {
            if (!inventory.equals(event.getClickedInventory())) return;
            event.setCancelled(true);

            if (event.getSlot() == (ROWS * 9) - 9) {
                page--;
                render();
            } else if (event.getSlot() == (ROWS * 9) - 1) {
                page++;
                render();
            } else if (event.getCurrentItem() != null) {
                NBTItem nbtItem = new NBTItem(new ItemStack(event.getCurrentItem().getType()));
                nbtItem.setString("CustomModel", itemModel.friendlyPath().toString());
                event.getWhoClicked().getInventory().addItem(nbtItem.getItem());
            }
        }

        @Override
        public @NotNull Inventory getInventory() {
            return inventory;
        }
    }
}
