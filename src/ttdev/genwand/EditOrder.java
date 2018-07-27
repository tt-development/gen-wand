package ttdev.genwand;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.money.Money;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EditOrder {

    private Player player;

    private MaterialType materialType;
    private int area;
    private int cost;

    private EditOrder(Player player) {
        this.player = player;
    }

    static EditOrder of(Player player) {
        return new EditOrder(player);
    }

    void addArea(int area) {
        this.area = area;
    }

    void addMaterial(MaterialType materialType) {
        this.materialType = materialType;
    }

    PlacedEditOrder place() {

        Material material;

        switch (materialType) {
            case OBSIDIAN:
                cost = ConfigUtil.getObsidianCost() * area;
                material = Material.OBSIDIAN;
                break;
            case COBBLESTONE:
                cost = ConfigUtil.getCobblestoneCost() * area;
                material = Material.COBBLESTONE;
                break;
            case SAND:
                cost = ConfigUtil.getSandCost() * area;
                material = Material.SAND;
                break;
            default:
                cost = 0;
                material = null;
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
                player.sendMessage(ConfigUtil.getNotEnoughMoneyMessage());
                return null;
            }

            if (!player.hasPermission(GenWand.ADMIN_PERMISSION)) {
                if (CooldownManager.isCooling(player.getUniqueId())) {
                    player.sendMessage(ConfigUtil.getEditUnavailableMessage());
                    return null;
                } else {
                    CooldownManager.add(player);
                }
            }

        }

        return new PlacedEditOrder(player, material,cost);
    }

}
