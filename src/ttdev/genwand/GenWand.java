package ttdev.genwand;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import net.milkbowl.vault.Vault;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ttdev.api.player.IPlayer;

import java.util.logging.Level;

public class GenWand extends JavaPlugin implements Listener {

    WorldEditPlugin worldEditPlugin;
    Vault vaultPlugin;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        PluginManager pluginManager = getServer().getPluginManager();

        worldEditPlugin = (WorldEditPlugin) pluginManager.getPlugin("WorldEdit");
        if (worldEditPlugin == null) {
            getLogger().log(Level.WARNING, "Couldn't find plugin 'WorldEdit'.");
        }

        vaultPlugin = (Vault) pluginManager.getPlugin("Vault");
        if (vaultPlugin == null) {
            getLogger().log(Level.WARNING, "Couldn't find plugin 'Vault'.");
        }

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info(getName() + " enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info(getName() + " disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to perform this command.");
            return true;
        }

        Player player = (Player) sender;
        IPlayer iPlayer = IPlayer.createInstance(player);

        if (label.equalsIgnoreCase("gen")) {

            if (args.length == 0) {
                //TODO Open GUI
                Selection selection = worldEditPlugin.getSelection(player);
                if (selection == null) {
                    iPlayer.sendColoredMessage("&cYou must make a selection first.");
                    return true;
                }

                Region selectedRegion;
                try {
                    selectedRegion = worldEditPlugin.getSelection(player).getRegionSelector().getRegion();
                } catch (IncompleteRegionException e) {
                    e.printStackTrace();
                }

                EditSession session = worldEditPlugin.createEditSession(player);
                session.fillXZ()


            }

            switch(args[0]) {
                case "pos1":
                    iPlayer.sendColoredMessage("&ePosition one set.");
                    break;
                case "pos2":
                    iPlayer.sendColoredMessage("&ePosition one set.");
                    break;

            }
        }

        return true;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Action action = event.getAction();

        if (action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        Location location = block.getLocation();

        Player player = event.getPlayer();

    }

}
