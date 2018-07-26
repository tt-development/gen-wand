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

/* Copyright (C) alexslime11 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tre Logan <tr3.logan@gmail.com>, July 2018
 */

class WorldEditUtil {

    static void setBlocks(World world, Selection selection, Material material) {
        EditSession session = new EditSession(BukkitUtil.getLocalWorld(world), ConfigUtil.getMaxBlocks());
        Location minP = selection.getMinimumPoint();
        Location maxP = selection.getMaximumPoint();
        Vector v1 = new Vector(minP.getBlockX(), minP.getBlockY(), minP.getBlockZ());
        Vector v2 = new Vector(maxP.getBlockX(), maxP.getBlockY(), maxP.getBlockZ());
        Region region = new CuboidRegion(v1, v2);
        try {
            session.setBlocks(region, new BaseBlock(material.getId()));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

}
