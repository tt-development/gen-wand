package ttdev.genwand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditWand {

    private static final String name = ChatColor.YELLOW + "Gen Wand";
    private static ItemStack genTool = null;

    @SuppressWarnings("deprecation")
	public static boolean isEqual(ItemStack other) {
        if (genTool == null) {
            getEditWand();
        }
        return genTool.getTypeId() == other.getTypeId() && name.equals(other.getItemMeta().getDisplayName());
    }
    
	public static ItemStack getEditWand() {
        if (genTool != null) {
            return genTool;
        }
        String wandItem = GenWand.getInstance().getConfig().getString("edit-tool");
        
        switch (wandItem) {
	        case "stick":
	        	genTool = new ItemStack(Material.STICK);
	        	break;
	        case "redstone_torch":
	        	genTool = new ItemStack(Material.REDSTONE_TORCH_ON);
	        	break;
	        case "tripwire":
	        	genTool = new ItemStack(Material.TRIPWIRE);
	        	break;
        }
        
        ItemMeta meta = genTool.getItemMeta();
        meta.setDisplayName(name);
        genTool.setItemMeta(meta);
        return genTool;
    }

    public static void setNull() {
        genTool = null;
    }

}