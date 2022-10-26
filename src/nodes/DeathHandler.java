package nodes;

import logic.BankingManager;
import logic.Traversing;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;

import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

import java.util.List;
import java.util.stream.Collectors;

import static util.Constants.*;

public class DeathHandler extends Node{


    @Override
    public boolean validate() {
        return LUMBRIDGE.contains(new Tile(Players.getLocal().getX(), Players.getLocal().getY(),0)) && BankLocation.getNearest().walkingDistance(Players.getLocal().getTile()) > 10;
    }

    @Override
    public void execute() {
        if (Inventory.all().stream().anyMatch(e -> e != null &&  e.hasAction("Wear"))) {
            equipGear();
        } else {
            if (BankingManager.needRingOfDueling()) {
                if(BankLocation.getNearest().distance(Players.getLocal().getTile()) > 5){
                    setNodeStatus("banking");
                    Traversing.goToBank();
                }
            }
            else {
                setNodeStatus("teleporting to ferox enclave");
                Traversing.teleportRingOfDueling();
            }
        }
    }

    private void equipGear() {
        List<Item> equipables = Inventory.all().stream().filter(e -> e.hasAction("Wear")).collect(Collectors.toList());
        equipables.forEach(e -> {
            if(e.interact()){
                Sleep.sleep(Calculations.random(50,300));
            }
        });
    }
}
