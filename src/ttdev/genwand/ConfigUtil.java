package ttdev.genwand;

import org.bukkit.ChatColor;

public class ConfigUtil {

    public static int getObsidianCost() {
        return GenWand.getInstance().getConfig().getInt("prices.obsidian");
    }

    public static int getSandCost() {
        return GenWand.getInstance().getConfig().getInt("prices.sand");
    }

    public static int getCobblestoneCost() {
        return GenWand.getInstance().getConfig().getInt("prices.cobblestone");
    }

    public static int getMaxBlocks() {
        return GenWand.getInstance().getConfig().getInt("max-blocks");
    }

    public static int getDelay() {
        return GenWand.getInstance().getConfig().getInt("delay");
    }

    public static String getWandReceivedMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.wand-received");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getNotEnoughMoneyMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.not-enough-money");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getIncompleteSelectionMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.incomplete-selection");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getAttemptUnclaimedEditMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.attempt-unclaimed-edit");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getEditSuccessMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.edit-success");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getEditAvailableMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.edit-available");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getEditUnavailableMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.edit-unavailable");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getPositionOneSetMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.pos1-set");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getPositionTwoSetMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.pos2-set");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
