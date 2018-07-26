package ttdev.genwand;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class GenWand extends JavaPlugin implements Listener {

    private static HashMap<Player, Location> pos1 = new HashMap<>();
    private static HashMap<Player, Location> pos2 = new HashMap<>();

    public static Map<Player, Selection> selectionMap = new HashMap<>();

    private static GenWand singleton;
    private static WorldEditPlugin worldEdit;
    private static Economy economy;

    public static final String USE_PERMISSION = "genwand.use";
    public static final String ADMIN_PERMISSION = "genwand.admin";
    public static final String NOPAY_PERMISSION = "genwand.nopay";

    private static final int REACH = 200;

    public static GenWand getInstance() {
        return singleton;
    }

    @Override
    public void onEnable() {
        singleton = this;

        if(!setupEconomy()){
            getLogger().log(Level.WARNING,"Couldn't enable Economy.");
        }

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

        if (label.equalsIgnoreCase("gw")) {

            if (player.hasPermission(USE_PERMISSION) && ((args.length > 0 && args[0].equalsIgnoreCase("help"))) || args.length == 0) {
                player.sendMessage(ChatColor.YELLOW + "GenWand Help Menu:");
                player.sendMessage("/gw reload - Reload configuration");
                player.sendMessage("/gw wand [amount] - Give yourself the edit wand");
                player.sendMessage("/gw give <player> [amount] - Give somebody else the edit wand");
                player.sendMessage("/gw pos1 - Set first cuboid point");
                player.sendMessage("/gw pos2 - Set second cuboid point");
            }
            if (args.length > 0 && player.hasPermission(USE_PERMISSION)) {

                if (args[0].equalsIgnoreCase("reload") && player.hasPermission(ADMIN_PERMISSION)) {
                    reloadConfig();
                    player.sendMessage(getName() + " reloaded.");
                    return true;
                }

                if (args[0].equalsIgnoreCase("give") && args.length >= 2) {
                    // Give without amount
                    if (args.length == 2) {
                        Player givePlayer = Bukkit.getPlayer(args[1]);
                        if (givePlayer == null) {
                            player.sendMessage(ChatColor.RED + "Player not found.");
                            return true;
                        }
                        givePlayer.getInventory().addItem(EditWand.getEditWand());
                        givePlayer.sendMessage(ConfigUtil.getInstance().getWandReceivedMessage());
                    }
                    // Give with amount
                    else if (args.length == 3) {
                        Player givePlayer = Bukkit.getPlayer(args[1]);
                        if (givePlayer == null) {
                            player.sendMessage(ChatColor.RED + "Player not found.");
                            return true;
                        }
                        int amount = Integer.parseInt(args[2]);
                        givePlayer.getInventory().addItem(EditWand.getEditWand(amount));
                        givePlayer.sendMessage(ConfigUtil.getInstance().getWandReceivedMessage());
                    } else {
                        player.sendMessage(ChatColor.RED + "Incorrect syntax.");
                        return false;
                    }
                }

                if (args[0].equalsIgnoreCase("gen")) {
                    Location locationOne = pos1.get(player);
                    Location locationTwo = pos2.get(player);
                    if (locationOne == null || locationTwo == null) {
                        player.sendMessage(ConfigUtil.getInstance().getIncompleteSelectionMessage());
                        return true;
                    }

                    CuboidSelection selection = new CuboidSelection(player.getWorld(), locationOne, locationTwo);
                    selectionMap.put(player, selection);

                    InventoryManager.openInventory(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("wand")) {
                    if (args.length == 1) {
                        player.getInventory().addItem(EditWand.getEditWand());
                        player.sendMessage(ConfigUtil.getInstance().getWandReceivedMessage());
                    } else if (args.length == 2) {
                        int amount = Integer.parseInt(args[1]);
                        player.getInventory().addItem(EditWand.getEditWand(amount));
                        player.sendMessage(ConfigUtil.getInstance().getWandReceivedMessage());
                    }
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

            } else if (!player.hasPermission(USE_PERMISSION)) {
                player.sendMessage(ChatColor.RED + "No permission.");
            }

        }

        return true;

    }

    public static WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    private enum Position {
        FIRST,SECOND
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

        ItemStack itemStack=event.getItem();

        if (!player.hasPermission(USE_PERMISSION)&&EditWand.isEqual(itemStack)) {
            player.sendMessage(ChatColor.RED+"No permission.");
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
