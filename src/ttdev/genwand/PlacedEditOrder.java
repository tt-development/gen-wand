package ttdev.genwand;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlacedEditOrder {

    private final Player player;

    private final Material material;
    private final int cost;

    PlacedEditOrder(Player player, Material material, int cost) {
        this.player = player;
        this.material = material;
        this.cost = cost;
    }

    Player getPlayer() {
        return player;
    }

    Material getMaterial() {
        return material;
    }

    int getCost() {
        return cost;
    }

}
