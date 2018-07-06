package ttdev.genwand;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.md_5.bungee.api.ChatColor;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class GenWand extends JavaPlugin implements Listener {

    public static HashMap<Player, Location> pos1 = new HashMap<>();
    public static HashMap<Player, Location> pos2 = new HashMap<>();

    public static Map<Player, Selection> selectionMap = new HashMap<>();

    private static GenWand singleton;
    private static WorldEditPlugin worldEdit;

    private static final String usePermission = "genwand.use";
    private static final String adminPermission = "genwand.admin";

    private static final int REACH = 200;

    public static GenWand getInstance() {
        return singleton;
    }

    @Override
    public void onEnable() {
        singleton = this;

        PluginManager pluginManager = getServer().getPluginManager();
        worldEdit = (WorldEditPlugin) pluginManager.getPlugin("WorldEdit");
        if (worldEdit == null) {
            getLogger().log(Level.WARNING, "Couldn't find plugin \"WorldEdit\"");
        }

        pluginManager.registerEvents(this, this);

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

            if (args.length == 1 && player.hasPermission(usePermission)) {

                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.YELLOW + "GenWand Help");
                    player.sendMessage("/gen reload - Reload configuration");
                    player.sendMessage("/gen wand - Give yourself the edit wand");
                    player.sendMessage("/gen pos1 - Set first cuboid point");
                    player.sendMessage("/gen pos2 - Set second cuboid point");
                }

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
                    player.sendMessage(ConfigUtil.getInstance().getIncompleteSelectionMessage());
                    return true;
                }

                CuboidSelection selection = new CuboidSelection(player.getWorld(), locationOne, locationTwo);
                selectionMap.put(player, selection);

                InventoryManager.openInventory(player);
            }

        }

        return true;

    }

    public static WorldEditPlugin getWorldEdit() {
        return worldEdit;
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

    public boolean setPosition(Player player, boolean command, Position position, ItemStack itemInHand) {

        Location target = player.getTargetBlock((Set<Material>) null, REACH).getLocation();

        if (!command) {
            if (itemInHand == null || !EditWand.isEqual(itemInHand)) {
                return false;
            }
            if (!FactionUtil.isInOwnTerritory(target, player)) {
                player.sendMessage(ConfigUtil.getInstance().getAttemptUnclaimedEditMessage());
                return true;
            }
        }

        switch (position) {
            case FIRST:
                pos1.put(player, target);
                player.sendMessage(ConfigUtil.getInstance().getPositionOneSetMessage());
                break;
            case SECOND:
                pos2.put(player, target);
                player.sendMessage(ConfigUtil.getInstance().getPositionTwoSetMessage());
                break;
        }

        return true;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        ItemStack itemStack = event.getItem();

        if (!player.hasPermission(usePermission)) {
            return;
        }

        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            if (setPosition(player, false, Position.FIRST, itemStack)) {
                event.setCancelled(true);
            }
        }

        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            if (setPosition(player, false, Position.SECOND, itemStack)) {
                event.setCancelled(true);
            }
        }
    }

    //Get target block.
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
