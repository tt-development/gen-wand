package ttdev.genwand;

import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ttdev.api.bukkit.Manager;
import ttdev.api.user.inventory.AInventory;
import ttdev.api.user.inventory.events.inventoryclick.InventoryClick;
import ttdev.api.user.inventory.events.inventoryclick.InventoryListener;
import ttdev.api.user.items.Item;

/* Copyright (C) alexslime11 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tre Logan <tr3.logan@gmail.com>, July 2018
 */

public class InventoryManager implements InventoryListener {

    static {
        new InventoryManager();
    }

    private InventoryManager() {
        Manager.registerEvent(this);
    }

    static void openInventory(Player player) {

        int size = GenWand.selectionMap.get(player).getArea();

        AInventory inventory = new AInventory("&cBlocks: " + size, 1);

        Item obsidian = new Item(Material.OBSIDIAN);
        Item cobblestone = new Item(Material.COBBLESTONE);
        Item sand = new Item(Material.SAND);

        obsidian.setName("&cObsidian");
        cobblestone.setName("&bCobblestone");
        sand.setName("&aSand");

        int[] costs = calculateCosts(size);

        obsidian.addLore("&fClick to fill selection with obsidian.");
        obsidian.addLore("&a$" + costs[0]);

        cobblestone.addLore("&fClick to fill selection with cobblestone.");
        cobblestone.addLore("&a$" + costs[1]);

        sand.addLore("&fClick to fill selection with sand.");
        sand.addLore("&a$" + costs[2]);

        inventory.setItem(obsidian, 2);
        inventory.setItem(cobblestone, 4);
        inventory.setItem(sand, 6);

        player.openInventory(inventory.getInventory());
    }

    private static int[] calculateCosts(int area) {
        int[] costs = new int[3];
        costs[0] = ConfigUtil.getObsidianCost() * area;
        costs[1] = ConfigUtil.getCobblestoneCost() * area;
        costs[2] = ConfigUtil.getSandCost() * area;
        return costs;
    }

    @Override
    public void InventoryClickEvent(InventoryClick event) {

        Player player = event.getWhoClicked();

        if (event.getInventory().getName().contains(ChatColor.RED + "Blocks: ")) {

            event.cancelAction();

            /* Check if the player can bypass claims (is admin) */
            boolean admin = player.hasPermission(GenWand.ADMIN_PERMISSION);

            Selection selection = GenWand.selectionMap.get(player);
            if (!admin && !FactionUtil.isInOwnTerritory(selection.getMinimumPoint(), selection.getMaximumPoint(), player)) {
                player.sendMessage(ChatColor.RED + "You can only edit your claim.");
                return;
            }

            if (event.getClickedItem().getName().equals(ChatColor.RED + "Obsidian")) {
                PlacedEditOrder placedEditOrder = placeEditOrder(player, MaterialType.OBSIDIAN, selection.getArea());
                if (placedEditOrder != null) {
                    ConfirmationInventory.open(player, placedEditOrder);
                }
            }

            if (event.getClickedItem().getName().equals(ChatColor.AQUA + "Cobblestone")) {
                PlacedEditOrder placedEditOrder = placeEditOrder(player, MaterialType.COBBLESTONE, selection.getArea());
                if (placedEditOrder != null) {
                    ConfirmationInventory.open(player, placedEditOrder);
                }
            }
            if (event.getClickedItem().getName().equals(ChatColor.GREEN + "Sand")) {
                PlacedEditOrder placedEditOrder = placeEditOrder(player, MaterialType.SAND, selection.getArea());
                if (placedEditOrder != null) {
                    ConfirmationInventory.open(player, placedEditOrder);
                }
            }

        }
    }

    static PlacedEditOrder placeEditOrder(Player player, MaterialType materialType, int selection) {
        player.closeInventory();

        EditOrder editOrder = EditOrder.of(player);
        editOrder.addArea(selection);
        editOrder.addMaterial(materialType);

        return editOrder.place();
    }

}
