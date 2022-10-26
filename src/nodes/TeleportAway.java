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
        return RED_SPIDERS.contains(Players.getLocal()) && (Inventory.isFull() || Skills.getBoostedLevel(Skill.PRAYER) == 0);
    }

    @Override
    public void execute() {
        Traversing.teleportRingOfDueling(this);

    }
}
