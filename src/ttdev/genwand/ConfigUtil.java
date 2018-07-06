package ttdev.genwand;

import org.bukkit.ChatColor;

public class ConfigUtil {

    private static ConfigUtil singleton;

    private ConfigUtil() {

    }

    public static ConfigUtil getInstance() {
        if (singleton == null) {
            singleton = new ConfigUtil();
        }
        return singleton;
    }

    public int getObsidianCost() {
        return GenWand.getInstance().getConfig().getInt("prices.obsidian");
    }

    public int getSandCost() {
        return GenWand.getInstance().getConfig().getInt("prices.sand");
    }

    public int getCobblestoneCost() {
        return GenWand.getInstance().getConfig().getInt("prices.cobblestone");
    }

    public int getMaxBlocks() {
        return GenWand.getInstance().getConfig().getInt("max-blocks");
    }

    public String getIncompleteSelectionMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.incomplete-selection");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getAttemptUnclaimedEditMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.attempt-unclaimed-edit");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getEditSuccessMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.edit-success");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getPositionOneSetMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.pos1-set");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getPositionTwoSetMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.pos2-set");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
