package ttdev.genwand;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.money.Money;
import com.sk89q.worldedit.bukkit.selections.Selection;
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

        int[] costs=calculateCosts(size);

        obsidian.addLore("&fClick to fill selection with obsidian.");
        obsidian.addLore("&a$"+costs[0]);

        cobblestone.addLore("&fClick to fill selection with cobblestone.");
        cobblestone.addLore("&a$"+costs[1]);

        sand.addLore("&fClick to fill selection with sand.");
        sand.addLore("&a$"+costs[2]);

        inventory.setItem(obsidian, 2);
        inventory.setItem(cobblestone, 4);
        inventory.setItem(sand, 6);

        player.openInventory(inventory.getInventory());
    }

    private static int[] calculateCosts(int area){
        int[] costs=new int[3];
        costs[0]=ConfigUtil.getInstance().getObsidianCost()*area;
        costs[1]=ConfigUtil.getInstance().getCobblestoneCost()*area;
        costs[2]=ConfigUtil.getInstance().getSandCost()*area;
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

            /* If the player is in a faction then the money will be taken
            from the faction balance, otherwise it will be take from their
            balance.
             */
            double money;

            MPlayer mPlayer = MPlayer.get(player);
            money = (mPlayer.hasFaction()) ? Money.get(mPlayer.getFaction()) : GenWand.getEconomy().getBalance(player);

            if (money < cost) {
                player.sendMessage(ConfigUtil.getInstance().getNotEnoughMoneyMessage());
                return false;
            }

            if (mPlayer.hasFaction()) {
                Money.set(mPlayer.getFaction(), null, money - cost);
                player.sendMessage(ChatColor.RED + "$" + cost + " has been removed from the faction balance.");
            } else {
                GenWand.getEconomy().withdrawPlayer(player, cost);
                player.sendMessage(ChatColor.RED + "$" + cost + " has been removed from your account.");
            }

            player.sendMessage(ConfigUtil.getInstance().getEditSuccessMessage());
        }
        return true;
    }

    private enum MaterialType {
        OBSIDIAN, COBBLESTONE, SAND
    }

}
