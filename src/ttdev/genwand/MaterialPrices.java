package ttdev.genwand;

public class MaterialPrices {

    private static MaterialPrices singleton;

    private MaterialPrices() {

    }

    public static MaterialPrices getInstance() {
        if (singleton == null) {
            singleton = new MaterialPrices();
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

}