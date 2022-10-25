package nodes;

import logic.BankingManager;
import logic.Traversing;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;

import static util.Constants.*;

public class Picking extends Node {

    @Override
    public boolean validate() {
        return Inventory.isEmpty() && !BankingManager.needRestock();
    }

    @Override
    public void execute() {
        if(!RED_SPIDERS.contains(Players.getLocal())){
            Traversing.goToRedSpiders(this);
        }
    }
}
