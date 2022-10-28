package nodes;

import logic.BankingManager;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;


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
                    Sleep.sleepUntil(() -> !FEROX_ENCLAVE.contains(Players.getLocal()), 4000);
                }
            } else {
                setNodeStatus("Running to staircase");
                Walking.walk(new Tile(3150 + Calculations.random(0, 2), 3635 + Calculations.random(0, 4)));
            }
        } else if (currentTile.getY() > 10000) {
            if (GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter") && e.walkingDistance(Players.getLocal().getTile()) < 12) != null) {
                setNodeStatus("Handling soul wars portal");
                if(GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter")).interact()){
                    Sleep.sleepUntil(() -> GameObjects.closest(e -> e.getName().equals("Soul Wars Portal")) != null, 4000);
                }
            } else {
                setNodeStatus("Walking to soul wars portal");
                Walking.clickTileOnMinimap(new Tile(3158 + Calculations.random(0, 2), 10026 + Calculations.random(0, 4)));
                Sleep.sleepUntil(() -> GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter") && e.walkingDistance(Players.getLocal().getTile()) < 12) != null, Calculations.random(200,500) * Calculations.random(1,5));
            }
        } else if (String.valueOf(currentTile.getX()).startsWith("22") && String.valueOf(currentTile.getY()).startsWith("28")) {
            setNodeStatus("Handling portal to edgeville");
            if (GameObjects.closest(e -> e.getName().equals("Portal") && e.hasAction("Edgeville")) != null) {
                if(GameObjects.closest(e -> e.getName().equals("Portal") && e.hasAction("Edgeville")).interact("Edgeville")){
                    Sleep.sleepUntil(() -> EDGEVILLE.contains(Players.getLocal()), Calculations.random(75,125)*Calculations.random(15,25));
                }
            }
        } else if(EDGEVILLE.contains(Players.getLocal())) {
            if (GameObjects.closest(e -> e.getName().equals("Trapdoor") && String.valueOf(e.getX()).startsWith("309") && e.distance(Players.getLocal().getTile()) < 10) == null) {
                setNodeStatus("Running to trapdoor");
                Walking.clickTileOnMinimap(new Tile(3095 - Calculations.random(0, 3), 3468 + Calculations.random(0, 3)));
                Sleep.sleepUntil(() -> GameObjects.closest(e -> e.getName().equals("Trapdoor") && e.walkingDistance(Players.getLocal().getTile()) < 8) != null || !Players.getLocal().isMoving(), Calculations.random(6000, 8000));

            }else if(GameObjects.closest(e -> e.getName().equals("Trapdoor") && String.valueOf(e.getX()).startsWith("309") && e.distance(Players.getLocal().getTile()) < 10) != null) {
                setNodeStatus("Handling to trapdoor");
                GameObject trapdoor = GameObjects.closest(e -> e.getName().equals("Trapdoor"));
                boolean check = trapdoor.hasAction("Open");
                if (GameObjects.closest(e -> e.getName().equals("Trapdoor") && String.valueOf(e.getX()).startsWith("309")).interact(check ? "Open" : "Climb-down")) {
                    try{
                        Sleep.sleepUntil(() -> GameObjects.closest("Trapdoor") == null || GameObjects.closest("Trapdoor").hasAction("Open") != check, Calculations.random(2500, 4000));
                    }catch(Exception e){
                        Logger.warn("Didn't find trapdoor");
                    }
                }
            }
        }else{
            setNodeStatus("Walking to red spiders");
            Walking.walkExact(RED_SPIDERS.getRandomTile());
            if (GameObjects.closest(e -> e.getName().equals("Trapdoor") && String.valueOf(e.getX()).startsWith("309") && e.distance(Players.getLocal()) < 10) != null) {
                if (GameObjects.closest(e -> e.getName().equals("Trapdoor") && String.valueOf(e.getX()).startsWith("309")).isOnScreen()) {
                    if (GameObjects.closest(e -> e.getName().equals("Trapdoor") && String.valueOf(e.getX()).startsWith("309")).interact()) {
                        Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), Calculations.random(550, 800) * 8);
                    }
                }
            }
            if (Widgets.getWidget(475) != null) {
                Widgets.getWidget(475).getChild(11).interact();
            }
            Sleep.sleep(500, 800);

        }
    }
}
