package ttdev.genwand;

import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ttdev.api.APair;
import ttdev.api.bukkit.Manager;
import ttdev.api.user.inventory.AInventory;
import ttdev.api.user.inventory.events.inventoryclick.InventoryClick;
import ttdev.api.user.inventory.events.inventoryclick.InventoryListener;
import ttdev.api.user.items.Item;

public class InventoryManager implements InventoryListener {

    static {
        new InventoryManager();
    }

    private InventoryManager() {
        Manager.registerEvent(this);
    }

    public static void openInventory(Player player) {

        int size = GenWand.selectionMap.get(player).getArea();

        AInventory inventory = new AInventory("&cBlocks: " + size, 1);

        Item obsidian = new Item(Material.OBSIDIAN);
        Item cobblestone = new Item(Material.COBBLESTONE);
        Item sand = new Item(Material.SAND);

        //TODO Configuration
        obsidian.setName("&cObsidian");
        cobblestone.setName("&bCobblestone");
        sand.setName("&aSand");

        obsidian.addLore("Lore");
        cobblestone.addLore("Lore");
        sand.addLore("Lore");

        inventory.setItem(obsidian, 2);
        inventory.setItem(cobblestone, 4);
        inventory.setItem(sand, 6);

        player.openInventory(inventory.getInventory());
    }


    @Override
    public void InventoryClickEvent(InventoryClick event) {

        Player player = event.getWhoClicked();

        if (event.getInventory().getName().contains(ChatColor.RED + "Blocks: ")) {

            event.cancelAction();

            Selection selection = GenWand.selectionMap.get(player);
            if (!FactionUtil.isInOwnTerritory(selection.getMinimumPoint(), selection.getMaximumPoint(), player)) {
                player.sendMessage(ChatColor.RED + "You can only edit your claim.");
                return;
            }

            // A pair representing the number of affected blocks and the area of the selection
            APair<Integer, Integer> affectedArea;

            if (event.getClickedItem().getName().equals(ChatColor.RED + "Obsidian")) {
                affectedArea = WorldEditUtil.setBlocks(player.getWorld(), GenWand.selectionMap.get(player), Material.OBSIDIAN);
                event.getWhoClicked().closeInventory();
                if (affectedArea.getKey().equals(affectedArea.getValue())) {
                    System.out.println("Key " + affectedArea.getKey() + ", Value: " + affectedArea.getValue());
                    player.sendMessage(ConfigUtil.getInstance().getEditSuccessMessage());
                }
                return;
            }

            if (event.getClickedItem().getName().equals(ChatColor.AQUA + "Cobblestone")) {
                affectedArea = WorldEditUtil.setBlocks(player.getWorld(), GenWand.selectionMap.get(player), Material.COBBLESTONE);
                event.getWhoClicked().closeInventory();
                if (affectedArea.getKey().equals(affectedArea.getValue())) {
                    player.sendMessage(ConfigUtil.getInstance().getEditSuccessMessage());
                }
                return;
            }
            if (event.getClickedItem().getName().equals(ChatColor.GREEN + "Sand")) {
                affectedArea = WorldEditUtil.setBlocks(player.getWorld(), GenWand.selectionMap.get(player), Material.SAND);
                event.getWhoClicked().closeInventory();
                if (affectedArea.getKey().equals(affectedArea.getValue())) {
                    player.sendMessage(ConfigUtil.getInstance().getEditSuccessMessage());
                }
                return;
            }

        }
    }

}
