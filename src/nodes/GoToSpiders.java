package nodes;

import logic.BankingManager;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Sleep;

import static util.Constants.*;

public class GoToSpiders extends Node{
    @Override
    public boolean validate() {
        if(!BankingManager.needRestock() && Inventory.isEmpty()){
            if(!Regenerate.needRegenerate()){
                return !RED_SPIDERS.contains(Players.getLocal());
                }
            }
        return false;
    }

    @Override
    public void execute() {
        Tile currentTile = Players.getLocal().getTile();
        if (FEROX_ENCLAVE.contains(Players.getLocal())) {
            if (GameObjects.closest(e -> e.getName().equals("Stairs") && e.hasAction("Walk-down") && e.walkingDistance(Players.getLocal().getTile()) <10) != null) {
                setNodeStatus("Handling stairs");
                if(GameObjects.closest(e -> e.getName().equals("Stairs") && e.hasAction("Walk-down")).interact()){
                    Sleep.sleepUntil(() -> !FEROX_ENCLAVE.contains(Players.getLocal()), 100,40);
                }
            } else {
                setNodeStatus("Running to staircase");
                Walking.walk(new Tile(3150 + Calculations.random(0, 2), 3635 + Calculations.random(0, 4)));
            }
        } else if (currentTile.getY() > 10000) {
            if (GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter") && e.walkingDistance(Players.getLocal().getTile()) < 12) != null) {
                setNodeStatus("Handling soul wars portal");
                if(GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter")).interact()){
                    Sleep.sleepUntil(() -> !GameObjects.closest(e -> e.getName().equals("Soul Wars Portal")).exists(), 100,40);
                }
            } else {
                setNodeStatus("Walking to soul wars portal");
                Walking.clickTileOnMinimap(new Tile(3158 + Calculations.random(0, 2), 10025 + Calculations.random(0, 4)));
                Sleep.sleepUntil(() -> GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter") && e.walkingDistance(Players.getLocal().getTile()) < 12) != null, Calculations.random(200,500), Calculations.random(1,5));
            }
        } else if (String.valueOf(currentTile.getX()).startsWith("220") && String.valueOf(currentTile.getY()).startsWith("285")) {
            setNodeStatus("Handling portal to edgeville");
            if (GameObjects.closest(e -> e.getName().equals("Portal") && e.hasAction("Edgeville")).exists()) {
                if(GameObjects.closest(e -> e.getName().equals("Portal") && e.hasAction("Edgeville")).interact("Edgeville")){
                    Sleep.sleepUntil(() -> EDGEVILLE.contains(Players.getLocal()), Calculations.random(75,125),Calculations.random(15,25));
                }
            }
        } else {
            setNodeStatus("Walking to red spiders");
            Walking.walk(RED_SPIDERS.getRandomTile());
            if (Widgets.getWidget(475) != null) {
                Widgets.getWidget(475).getChild(11).interact();
            }
            Sleep.sleepUntil(() -> RED_SPIDERS.contains(Players.getLocal()), Calculations.random(200,500),Calculations.random(1,5));

        }
    }
}
