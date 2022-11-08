package nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.utilities.Sleep;
import util.Constants;

import java.util.List;

import static util.Constants.*;

public class WorldHopping extends Node {

    private List<World> hoppableWorlds = Worlds.all(e -> !e.isPVP() && !e.isPvpArena() && e.isMembers() && !e.isDeadmanMode() && !e.isLeagueWorld() && !e.isFreshStart() && !e.isTargetWorld() && !e.isPvpArena() && e.getMinimumLevel() == 0 && (e.getWorld() >= Worlds.getMyWorld().getWorld() - 50 && e.getWorld() <= Worlds.getMyWorld().getWorld() + 50) && e.getWorld() != Worlds.getMyWorld().getWorld());


    @Override
    public boolean validate() {
        return Constants.SHOULDWORLDHOP;
    }

    @Override
    public void execute() {
        if (!Players.getLocal().isHealthBarVisible() && !Players.getLocal().isInCombat()) {
            setNodeStatus("Hopping world");
            int current = Worlds.getMyWorld().getWorld();
            if (WorldHopper.hopWorld(hoppableWorlds.get(Calculations.random(0, hoppableWorlds.size() - 1)).getWorld(), true)) {
                setNodeStatus("Waiting till we arrived at new world");
                if (Sleep.sleepUntil(() -> current != Worlds.getMyWorld().getWorld() || Players.getLocal().isInCombat(), Calculations.random(1800, 2600) * 3)) {
                    if (current != Worlds.getCurrentWorld()) {
                        WORLDHOPPED++;
                        SHOULDWORLDHOP = false;
                    } else {
                        FAILEDHOPPED++;
                        SHOULDWORLDHOP = !Players.getLocal().isHealthBarVisible() && !Players.getLocal().isInCombat() && GroundItems.closest(EGG_ID) == null;
                    }
                } else {
                    FAILEDHOPPED++;
                    SHOULDWORLDHOP = !Players.getLocal().isHealthBarVisible() && !Players.getLocal().isInCombat() && GroundItems.closest(EGG_ID) == null;
                }
            }
        } else {
            SHOULDWORLDHOP = false;
        }
    }
}
