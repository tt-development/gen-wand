package ttdev.genwand;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class FactionUtil {

    public static boolean isInOwnTerritory(Player player) {
        MPlayer mPlayer = MPlayer.get(player);
        System.out.println("Checking territory of " + mPlayer.getName());
        return mPlayer.isInOwnTerritory() && mPlayer.hasFaction();
    }

    public static boolean isInOwnTerritory(Location locationOne, Location locationTwo, Player player) {
        MPlayer mPlayer = MPlayer.get(player);
        Faction factionOne = BoardColl.get().getFactionAt(PS.valueOf(locationOne));
        Faction factionTwo = BoardColl.get().getFactionAt(PS.valueOf(locationTwo));
        boolean nonNull = Objects.nonNull(factionOne) && Objects.nonNull(factionTwo);
        boolean playerFaction = mPlayer.getFaction() == factionOne && mPlayer.getFaction() == factionTwo;
        return nonNull && playerFaction;
    }

    public static boolean isInOwnTerritory(Location location, Player player) {
        MPlayer mPlayer = MPlayer.get(player);
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(location));
        return faction != null && mPlayer.getFaction() == faction;
    }

}
