package ttdev.genwand;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

import net.md_5.bungee.api.ChatColor;

public class Genwand extends JavaPlugin {
	
	public static HashMap<Player, Block> pos1 = new HashMap<>();
	public static HashMap<Player, Block> pos2 = new HashMap<>();

	private static Genwand singleton;
	
	public static Genwand getInstance() {
		return singleton;
	}
	
	@Override
	public void onEnable() {
		singleton = this;
		
		//Configuration
		this.getConfig().addDefault("items", "");
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	@Override
	public void onDisable() {
		this.saveConfig();
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
    	if (!(sender instanceof Player)) {
    		return true;
    	}
    	
    	Player player = (Player) sender;
    	
    	if (label.equalsIgnoreCase("gen")) {
    		
    		if (args.length == 1) {
    			
    			if (args[0].equalsIgnoreCase("pos1")) {
    				player.sendMessage("Pos1 set");
    				pos1.put(player, getTargetBlock(player, 4));
    				return true;
    			}
    			
    			if (args[0].equalsIgnoreCase("pos2")) {
    				player.sendMessage("Pos2 set");
    				pos2.put(player, getTargetBlock(player, 4));
    				return true;
    			}
    			
    			player.sendMessage(ChatColor.RED + "Incorrect syntax: /gen pos1 ; /gen pos2");
    			return true;
    		} else {
    			InventoryManager.openInventory(player);
    			return true;
    		}
    		
    		
    	}
    	
    	return true;
    	
    }
    
	//Get target block.
    public final Block getTargetBlock(Player player, Integer range) {
        BlockIterator bi= new BlockIterator(player, range);
        Block lastBlock = bi.next();
        while (bi.hasNext()) {
            lastBlock = bi.next();
            if (lastBlock.getType() == Material.AIR)
                continue;
            break;
        }
        return lastBlock;
    }
	
}
