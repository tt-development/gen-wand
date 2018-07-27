package ttdev.genwand;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.money.Money;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ttdev.api.user.inventory.AInventory;
import ttdev.api.user.items.Item;

public class ConfirmationInventory implements Listener {

    private static final String inventoryTitle = "Are you sure?";

    private static PlacedEditOrder tempPlacedEditOrder;
    private static final String yesText = ChatColor.GREEN + "Yes";
    private static final String noText = ChatColor.RED + "No";

    private static ConfirmationInventory singleton = null;

    private ConfirmationInventory() {
        singleton = this;
        GenWand.getInstance().registerEvent(this);
    }

    public static void open(Player player, PlacedEditOrder placedEditOrder) {
        if (singleton == null) {
            new ConfirmationInventory();
        }

        tempPlacedEditOrder = placedEditOrder;

        AInventory inventory = new AInventory(inventoryTitle + "      Cost: " + ChatColor.RED + placedEditOrder.getCost(), 1);

        Item yesItem = new Item(Material.STAINED_GLASS);
        Item noItem = new Item(Material.STAINED_GLASS);

        yesItem.setName(yesText);
        noItem.setName(noText);

        yesItem.setDurability(13);
        noItem.setDurability(14);

        inventory.setItem(yesItem, 3);
        inventory.setItem(noItem, 5);

        inventory.openInventory(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getInventory().getName().equals(inventoryTitle + "      Cost: " + ChatColor.RED + tempPlacedEditOrder.getCost())) {

            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
            if (itemName.equals(yesText)) {

                WorldEditUtil.setBlocks(tempPlacedEditOrder.getPlayer().getWorld(), GenWand.selectionMap.get(player), tempPlacedEditOrder.getMaterial());
                MPlayer mPlayer = MPlayer.get(player);
                double money = (mPlayer.hasFaction()) ? Money.get(mPlayer.getFaction()) : GenWand.getEconomy().getBalance(player);
                int cost = tempPlacedEditOrder.getCost();
                if (mPlayer.hasFaction()) {
                    Money.set(mPlayer.getFaction(), null, money - cost);
                    player.sendMessage(ChatColor.RED + "$" + cost + " has been removed from the faction balance.");
                } else {
                    GenWand.getEconomy().withdrawPlayer(player, cost);
                    player.sendMessage(ChatColor.RED + "$" + cost + " has been removed from your account.");
                }

                player.sendMessage(ConfigUtil.getEditSuccessMessage());

            } else if (itemName.equals(noText)) {
                player.sendMessage(ConfigUtil.getEditCancelledMessage());
            } else {

            }

            player.closeInventory();
        }
    }
}
