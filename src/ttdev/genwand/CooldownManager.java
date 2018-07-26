package ttdev.genwand;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class CooldownManager {

    private static Set<UUID> coolingPlayers = new HashSet<>();

    static void add(Player player) {
        UUID uuid = player.getUniqueId();
        if (coolingPlayers.contains(uuid)) {
            return;
        }

        coolingPlayers.add(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                coolingPlayers.remove(player.getUniqueId());
                player.sendMessage(ConfigUtil.getEditAvailableMessage());
            }
        }.runTaskLater(GenWand.getInstance(), ConfigUtil.getDelay() * 20);

        return;
    }

    static boolean isCooling(UUID uuid) {
        return coolingPlayers.contains(uuid);
    }

}
