package ttdev.genwand;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import ttdev.api.user.inventory.AInventory;
import ttdev.api.user.inventory.events.inventoryclick.InventoryClick;
import ttdev.api.user.inventory.events.inventoryclick.InventoryListener;
import ttdev.api.user.items.Item;

public class InventoryManager implements InventoryListener {
	
	public static void openInventory(Player player) {
		
		int size = GenWand.selectionMap.get(player).getArea();

		AInventory inventory = new AInventory("&cBlocks: " + size, 1);
		
		Item obsidian = new Item(Material.OBSIDIAN);
		Item cobblestone = new Item(Material.COBBLESTONE);
		Item sand = new Item(Material.SAND);
		
		inventory.setItem(obsidian, 2);
		inventory.setItem(cobblestone, 4);
		inventory.setItem(sand, 6);

		inventory.openInventory(player);
		
	}
	
	

	@Override
	public void InventoryClickEvent(InventoryClick event) {
		if (event.getPlayerInventory().getName().contains("Blocks: ")) {
			
		}
	}
	
}
