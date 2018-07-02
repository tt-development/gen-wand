package ttdev.genwand;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class WorldEditUtil {

    public static void setBlocks(World world, Selection selection, Material material) {
        EditSession session = new EditSession(BukkitUtil.getLocalWorld(world), ConfigUtil.getInstance().getMaxBlocks());
        Location minP = selection.getMinimumPoint();
        Location maxP = selection.getMaximumPoint();
        Vector v1 = new Vector(minP.getBlockX(), minP.getBlockY(), minP.getBlockZ());
        Vector v2 = new Vector(maxP.getBlockX(), maxP.getBlockY(), maxP.getBlockZ());
        Region region = new CuboidRegion(v1, v2);
        try {
            int area = region.getArea();
            int affected = session.setBlocks(region, new BaseBlock(material.getId()));
            System.out.println("Area: " + area + ", Affected: " + affected);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

}
