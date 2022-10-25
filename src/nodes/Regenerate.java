package nodes;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import util.Constants;

import static util.Constants.*;

public class Regenerate extends Node{
    @Override
    public boolean validate() {
        return FEROX_ENCLAVE.contains(Players.getLocal()) && Skills.getRealLevel(Skill.PRAYER) < Skills.getBoostedLevel(Skill.PRAYER);
    }

    @Override
    public void execute() {
        if(Prayers.isActive(Prayer.PROTECT_FROM_MELEE)){
            Prayers.toggle(false, Prayer.PROTECT_FROM_MELEE);
        }
        else{
            //REGENERATE IN POOL
        }

    }
}
