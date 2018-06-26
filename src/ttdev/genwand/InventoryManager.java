package ttdev.genwand;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import ttdev.api.inventory.AInventory;
import ttdev.api.inventory.events.inventoryclick.InventoryClick;
import ttdev.api.inventory.events.inventoryclick.InventoryListener;
import ttdev.api.items.Item;

public class InventoryManager implements InventoryListener {
	
	public static void openInventory(Player player) {
		
		Block pos1 = Genwand.pos1.get(player);
		Block pos2 = Genwand.pos2.get(player);
		
		int x = pos1.getX() - pos2.getX();
		int y = pos1.getY() - pos2.getY();
		int z = pos1.getZ() - pos2.getZ();
		
		if (x < 0) {
			x = x * -1;
		}
		if (y < 0) {
			y = y * -1;
		}
		if (z < 0) {
			z = z * -1;
		}
		
		int size = x * y * z;
		
		AInventory inventory = new AInventory("&cBlocks: " + size, 1);
		
		Item obsidian = new Item(Material.OBSIDIAN);
		Item cobblestone = new Item(Material.COBBLESTONE);
		Item sand = new Item(Material.SAND);
		
		inventory.setItem(obsidian, 2);
		inventory.setItem(cobblestone, 4);
		inventory.setItem(sand, 6);
		
		
	}
	
	

	@Override
	public void InventoryClickEvent(InventoryClick event) {
		if (event.getPlayerInventory().getName().contains("Blocks: ")) {
			
		}
	}
	
}
