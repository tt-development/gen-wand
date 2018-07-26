package ttdev.genwand;

import org.bukkit.ChatColor;

class ConfigUtil {

    static int getObsidianCost() {
        return GenWand.getInstance().getConfig().getInt("prices.obsidian");
    }

    static int getSandCost() {
        return GenWand.getInstance().getConfig().getInt("prices.sand");
    }

    static int getCobblestoneCost() {
        return GenWand.getInstance().getConfig().getInt("prices.cobblestone");
    }

    static int getMaxBlocks() {
        return GenWand.getInstance().getConfig().getInt("max-blocks");
    }

    static int getDelay() {
        return GenWand.getInstance().getConfig().getInt("delay");
    }

    static String getWandReceivedMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.wand-received");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getNotEnoughMoneyMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.not-enough-money");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getIncompleteSelectionMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.incomplete-selection");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getAttemptUnclaimedEditMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.attempt-unclaimed-edit");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getEditSuccessMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.edit-success");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getEditAvailableMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.edit-available");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getEditUnavailableMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.edit-unavailable");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getPositionOneSetMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.pos1-set");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    static String getPositionTwoSetMessage() {
        final String message = GenWand.getInstance().getConfig().getString("message.pos2-set");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
