package ttdev.genwand;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import ttdev.api.bukkit.Manager;
import ttdev.api.user.inventory.AInventory;
import ttdev.api.user.inventory.events.inventoryclick.InventoryClick;
import ttdev.api.user.inventory.events.inventoryclick.InventoryListener;
import ttdev.api.user.items.Item;

public class InventoryManager implements InventoryListener {
	
	static {
		new InventoryManager();
	}
	
	private InventoryManager() {
		Manager.registerEvent(this);
	}
	
	public static void openInventory(Player player) {

		if (!Genwand.pos1.containsKey(player)) {
			return;
		}
		if (!Genwand.pos2.containsKey(player)) {
			return;
		}
		
		//Calculate the amount of blocks
		Block pos1 = Genwand.pos1.get(player);
		Block pos2 = Genwand.pos2.get(player);
		
		int xPos = pos1.getX() - pos2.getX();
		int yPos = pos1.getY() - pos2.getY();
		int zPos = pos1.getZ() - pos2.getZ();
		
		if (xPos < 1) {
			xPos = xPos * -1;
		}
		if (yPos < 1) {
			yPos = yPos * -1;
		}
		if (zPos < 1) {
			zPos = zPos * -1;
		}
		int totalBlocks = (xPos + 1) * (yPos + 1) * (zPos + 1);
		
		//Charge the player the money.
		//TODO Vault
		
		//Create the inventory.
		AInventory inventory = new AInventory("&cBlocks: " + totalBlocks, 1);
		
		
		Item obsidian = new Item(Material.OBSIDIAN);
		Item cobblestone = new Item(Material.COBBLESTONE);
		Item sand = new Item(Material.SAND);
		
		//TODO Configuration
		obsidian.setName("&cObsidian");
		cobblestone.setName("&bCobblestone");
		sand.setName("&aSand");
	
		obsidian.addLore("Lore");
		cobblestone.addLore("Lore");
		sand.addLore("Lore");
		
		inventory.setItem(obsidian, 2);
		inventory.setItem(cobblestone, 4);
		inventory.setItem(sand, 6);
		
		player.openInventory(inventory.getInventory());
	}



	@Override
	public void InventoryClickEvent(InventoryClick event) {
		
		if (event.getInventory().getName().contains(ChatColor.RED + "Blocks: ")) {
			
			event.cancelAction();
			
			 if (event.getClickedItem().getName().equals(ChatColor.RED + "Obsidian")) {
				 Generator.startGenerating(event.getWhoClicked(), Material.OBSIDIAN);
				 event.getWhoClicked().closeInventory();
				 return;
			 }
			 if (event.getClickedItem().getName().equals(ChatColor.AQUA + "Cobblestone")) {
				 Generator.startGenerating(event.getWhoClicked(), Material.COBBLESTONE);
				 event.getWhoClicked().closeInventory();
				 return;
			 }
			 if (event.getClickedItem().getName().equals(ChatColor.GREEN + "Sand")) {
				 Generator.startGenerating(event.getWhoClicked(), Material.SAND);
				 event.getWhoClicked().closeInventory();
				 return;
			 }
		}
	}
	
}
