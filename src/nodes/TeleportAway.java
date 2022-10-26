package nodes;

import logic.Traversing;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import static util.Constants.*;

public class TeleportAway extends Node{

    @Override
    public boolean validate() {
        if(RED_SPIDERS.contains(Players.getLocal())) {
            if(Inventory.isFull()) {
                return true;
            }
            return Skills.getBoostedLevel(Skill.PRAYER) == 0;
        }

        return false;
    }

    @Override
    public void execute() {
        Traversing.teleportRingOfDueling();

    }
}
