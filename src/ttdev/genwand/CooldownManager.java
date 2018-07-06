package ttdev.genwand;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CooldownManager {

    private static Set<UUID> coolingPlayers = new HashSet<>();

    public static boolean add(Player player) {
        UUID uuid = player.getUniqueId();
        if (coolingPlayers.contains(uuid)) {
            return false;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                coolingPlayers.remove(player.getUniqueId());
                player.sendMessage(ConfigUtil.getInstance().getEditAvailableMessage());
            }
        }.runTaskLater(GenWand.getInstance(), ConfigUtil.getInstance().getDelay());

        return true;
    }

}
