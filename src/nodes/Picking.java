package nodes;


import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

import static util.Constants.*;

public class Picking extends Node {

    @Override
    public boolean validate() {
        if(RED_SPIDERS.contains(Players.getLocal())){
            if(!Inventory.isFull()){
                return Skills.getBoostedLevel(Skill.PRAYER) > 0 || Skills.getBoostedLevel(Skill.HITPOINTS) > Calculations.random(6,9);

            }
        }
        return false;
    }

    @Override
    public void execute() {
        if(!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)){
            if(Skills.getBoostedLevel(Skill.PRAYER) > 0) {
                if (Players.getLocal().isHealthBarVisible() || Players.getLocal().isInCombat()) {
                    if (Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE)) {
                        Sleep.sleepUntil(() -> Prayers.isActive(Prayer.PROTECT_FROM_MELEE), 250);
                    }
                }
            }
    }
        if(GroundItems.closest(EGG_ID) != null){
            setNodeStatus("Picking up spider egg");
            int iCheck = Inventory.all(e -> e.isValid()).size();
            if(GroundItems.closest(EGG_ID).interact()){
                Sleep.sleepUntil(() -> Inventory.all(e -> e.isValid()).size() > iCheck, (Calculations.random(651,791)* Calculations.random(6,8)));
            }
        }

        }
    }


