package ttdev.genwand;

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

}
