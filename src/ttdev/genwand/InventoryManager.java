package ttdev.genwand;

import com.sk89q.worldedit.bukkit.selections.Selection;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

        obsidian.setName("&cObsidian");
        cobblestone.setName("&bCobblestone");
        sand.setName("&aSand");

        obsidian.addLore("Click to fill selection with obsidian.");
        cobblestone.addLore("Click to fill selection with cobblestone.");
        sand.addLore("Click to fill selection with sand.");

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

            /* Check if the player can bypass claims (is admin) */
            boolean admin = player.hasPermission(GenWand.ADMIN_PERMISSION);

            Selection selection = GenWand.selectionMap.get(player);
            if (!admin && !FactionUtil.isInOwnTerritory(selection.getMinimumPoint(), selection.getMaximumPoint(), player)) {
                player.sendMessage(ChatColor.RED + "You can only edit your claim.");
                return;
            }

            if (event.getClickedItem().getName().equals(ChatColor.RED + "Obsidian")) {
                boolean valid = validateEdit(player, MaterialType.OBSIDIAN, selection.getArea());
                if (valid) {
                    WorldEditUtil.setBlocks(player.getWorld(), GenWand.selectionMap.get(player), Material.OBSIDIAN);
                }
            }

            if (event.getClickedItem().getName().equals(ChatColor.AQUA + "Cobblestone")) {
                boolean valid = validateEdit(player, MaterialType.COBBLESTONE, selection.getArea());
                if (valid) {
                    WorldEditUtil.setBlocks(player.getWorld(), GenWand.selectionMap.get(player), Material.COBBLESTONE);
                }
            }
            if (event.getClickedItem().getName().equals(ChatColor.GREEN + "Sand")) {
                boolean valid = validateEdit(player, MaterialType.SAND, selection.getArea());
                if (valid) {
                    WorldEditUtil.setBlocks(player.getWorld(), GenWand.selectionMap.get(player), Material.SAND);
                }
                return;
            }

        }
    }

    private boolean validateEdit(Player player, MaterialType materialType, int selection) {
        player.closeInventory();
        int cost = 0;
        switch (materialType) {
            case OBSIDIAN:
                cost = ConfigUtil.getInstance().getObsidianCost() * selection;
                break;
            case COBBLESTONE:
                cost = ConfigUtil.getInstance().getCobblestoneCost() * selection;
                break;
            case SAND:
                cost = ConfigUtil.getInstance().getSandCost() * selection;
                break;
        }

        if (!player.hasPermission(GenWand.ADMIN_PERMISSION)) {
            CooldownManager.add(player);
        }
        if (!player.hasPermission(GenWand.NOPAY_PERMISSION)) {
            Economy economy = GenWand.getEconomy();
            double balance = economy.getBalance(player);
            if (balance < cost) {
                player.sendMessage(ConfigUtil.getInstance().getNotEnoughMoneyMessage());
                return false;
            }
            economy.withdrawPlayer(player, cost);
            player.sendMessage(ConfigUtil.getInstance().getEditSuccessMessage());
        }
        return true;
    }

    private enum MaterialType {
        OBSIDIAN, COBBLESTONE, SAND
    }

}
