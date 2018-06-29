package ttdev.genwand;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Generator {

	public static void startGenerating(Player player, Material material) {
		
		if (!Genwand.pos1.containsKey(player)) {
			return;
		}
		if (!Genwand.pos2.containsKey(player)) {
			return;
		}
		
		Block pos1 = Genwand.pos1.get(player);
		Block pos2 = Genwand.pos2.get(player);
		
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
		
		for (int x = xSmallest; x < (xSmallest + xPos + 1); x++) {
			for (int y = ySmallest; y < (ySmallest + yPos + 1); y++) {
				for (int z = zSmallest; z < (zSmallest + zPos + 1); z++) {
					Block tmpBlock = new Location(player.getWorld(), x, y, z).getBlock();
					tmpBlock.setType(material);
				}
			}
		}
		
		int totalBlocks = (xPos + 1) * (yPos + 1) * (zPos + 1);
		player.sendMessage("Blocks: " + totalBlocks);
	}
	
}
