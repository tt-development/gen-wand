package ttdev.genwand;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditWand {

    private static final String name = ChatColor.YELLOW + "Gen Wand";
    private static ItemStack genTool = null;

    public static boolean isEqual(ItemStack other) {
        if (genTool == null) {
            getEditWand();
        }
        return genTool.getTypeId() == other.getTypeId() && name.equals(other.getItemMeta().getDisplayName());
    }

    public static ItemStack getEditWand() {
        return getEditWand(1);
    }

    public static ItemStack getEditWand(int amount){
        int editToolId = Integer.parseInt(GenWand.getInstance().getConfig().getString("edit-tool"));
        genTool = new ItemStack(editToolId, amount, (short) 0);
        ItemMeta meta = genTool.getItemMeta();
        meta.setDisplayName(name);
        genTool.setItemMeta(meta);
        return genTool;
    }

}
