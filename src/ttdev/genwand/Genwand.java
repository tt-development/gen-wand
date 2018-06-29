package ttdev.genwand;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

public class GenWand extends JavaPlugin implements Listener {

    public static HashMap<Player, Location> pos1 = new HashMap<>();
    public static HashMap<Player, Location> pos2 = new HashMap<>();

    private static GenWand singleton;

    private static final String usePermission = "genwand.use";
    private static final String adminPermission = "genwand.admin";

    private static final int REACH = 200;

    public static GenWand getInstance() {
        return singleton;
    }

    @Override
    public void onEnable() {
        singleton = this;
        
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().addPermission(new Permission(usePermission));
        Bukkit.getPluginManager().addPermission(new Permission(adminPermission));
        
        //Configuration
        this.getConfig().addDefault("items", "");
        this.getConfig().addDefault("edit-tool", "200");
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

                if (args[0].equalsIgnoreCase("reload") && player.hasPermission(adminPermission)) {
                    reloadConfig();
                    player.sendMessage(getName() + " reloaded.");
                    return true;
                }

                if (args[0].equalsIgnoreCase("wand")) {
                    player.getInventory().addItem(EditWand.getEditWand());
                    return true;
                }

                if (args[0].equalsIgnoreCase("pos1")) {
                    setPosition(player, true, Position.FIRST, null);
                    return true;
                }

                if (args[0].equalsIgnoreCase("pos2")) {
                    setPosition(player, true, Position.SECOND, null);
                    return true;
                }

                player.sendMessage(ChatColor.RED + "Incorrect syntax: /gen pos1 ; /gen pos2");

            } else {
                Location locationOne = pos1.get(player);
                Location locationTwo = pos2.get(player);
                if (locationOne == null || locationTwo == null) {
                    player.sendMessage(ChatColor.RED + "You must complete your selection first.");
                    return true;
                }

                InventoryManager.openInventory(player);
            }

        }

        return true;

    }

    private enum Position {
        FIRST("First"), SECOND("Second");

        private String friendlyName;

        Position(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        public String getFriendlyName() {
            return friendlyName;
        }
    }

    public void setPosition(Player player, boolean command, Position position, ItemStack itemInHand) {

        Location target;

        if (!command) {
            if (itemInHand == null || !EditWand.isEqual(itemInHand)) {
                return;
            }
        }
        
        target = getTargetBlock(player, REACH).getLocation();

        switch (position) {
            case FIRST:
                pos1.put(player, target);
                break;
            case SECOND:
                pos2.put(player, target);
                break;
        }

        player.sendMessage(position.getFriendlyName() + " position set.");

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        
        if (event.getItem() == null) {
        	return;
        }

        ItemStack itemStack = event.getItem();
        
        if (!itemStack.equals(EditWand.getEditWand())) {
        	return;
        }
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            setPosition(player, false, Position.FIRST, itemStack);
            event.setCancelled(true);
        }

        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            setPosition(player, false, Position.SECOND, itemStack);
            event.setCancelled(true);
        }
    }

    public final Block getTargetBlock(Player player, Integer range) {
        BlockIterator bi = new BlockIterator(player, range);
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