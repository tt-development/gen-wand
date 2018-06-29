package ttdev.genwand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import ttdev.api.API;

public class Generator {

	@SuppressWarnings("deprecation")
	public static void startGenerating(Player player, Material material) {
		
		if (!GenWand.pos1.containsKey(player)) {
			return;
		}
		if (!GenWand.pos2.containsKey(player)) {
			return;
		}
		
		Block pos1 = GenWand.pos1.get(player).getBlock();
		Block pos2 = GenWand.pos2.get(player).getBlock();
		
		//pos1 100 10 100
		//pos2 200 10 200
		
		int xPos = pos1.getX() - pos2.getX();
		int xSmallest;
		if (pos1.getX() < pos2.getX()) {
			xSmallest = pos1.getX();
		} else {
			xSmallest = pos2.getX();
		}
		if (xPos < 1) {
			xPos = xPos * -1;
		}
		
		int yPos = pos1.getY() - pos2.getY();
		int ySmallest;
		if (pos1.getY() < pos2.getY()) {
			ySmallest = pos1.getY();
		} else {
			ySmallest = pos2.getY();
		}
		if (yPos < 1) {
			yPos = yPos * -1;
		}
		
		int zPos = pos1.getZ() - pos2.getZ();
		int zSmallest;
		if (pos1.getZ() < pos2.getZ()) {
			zSmallest = pos1.getZ();
		} else {
			zSmallest = pos2.getZ();
		}
		if (zPos < 1) {
			zPos = zPos * -1;
		}
		
		int totalBlocks = (xPos + 1) * (yPos + 1) * (zPos + 1);
		
		//Charge the player the money.
		if (!API.getPluginManager().isPluginEnabled("Vault")) {
			player.sendMessage(ChatColor.RED + "Error: Vault not found.");
			return;
		}
		
		Economy economy = null;
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        
        int price = 50;
        if (material.equals(Material.OBSIDIAN)) {
        	price = MaterialPrices.getInstance().getObsidianCost();
        }
        if (material.equals(Material.COBBLESTONE)) {
        	price = MaterialPrices.getInstance().getCobblestoneCost();
        }
        if (material.equals(Material.SAND)) {
        	price = MaterialPrices.getInstance().getSandCost();
        }
        
        int totalCost = price * totalBlocks;
        
        int balance = (int) economy.getBalance(player.getName());
        
        if (balance >= totalCost) {
        	economy.withdrawPlayer(player.getName(), totalCost);
        } else {
        	player.sendMessage(ChatColor.RED + "You don't have enough money to place these blocks!");
        	return;
        }
		
		for (int x = xSmallest; x < (xSmallest + xPos + 1); x++) {
			for (int y = ySmallest; y < (ySmallest + yPos + 1); y++) {
				for (int z = zSmallest; z < (zSmallest + zPos + 1); z++) {
					Block tmpBlock = new Location(player.getWorld(), x, y, z).getBlock();
					tmpBlock.setType(material);
				}
			}
		}
	}
	
}
