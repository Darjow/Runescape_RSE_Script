package nodes;

import logic.Traversing;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import java.util.logging.Logger;

import static util.Constants.*;

public class TeleportAway extends Node{

    @Override
    public boolean validate() {
        if(RED_SPIDERS.contains(Players.getLocal())) {
            if(Inventory.isFull()) {
                return true;
            }
            return Skills.getBoostedLevel(Skill.PRAYER) == 0 && Skills.getBoostedLevel(Skill.HITPOINTS) < Calculations.random(6,9);
        }else{
            if(!Combat.isInWild()){
                if(Skills.getRealLevel(Skill.PRAYER) > Skills.getBoostedLevel(Skill.PRAYER)){
                    return !FEROX_ENCLAVE.contains(Players.getLocal());
                }
            }
        }

        return false;
    }

    @Override
    public void execute() {
        Traversing.teleportRingOfDueling();

    }
}
