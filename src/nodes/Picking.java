package nodes;


import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.GroundItem;

import static util.Constants.*;

public class Picking extends Node {

    @Override
    public boolean validate() {
        if(RED_SPIDERS.contains(Players.getLocal())){
            if(!Inventory.isFull()){
                return Skills.getBoostedLevel(Skill.PRAYER) > 0;

            }
        }
        return false;
    }

    @Override
    public void execute() {
        if(!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)){
            if(Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE)){
                Sleep.sleepUntil(() -> Prayers.isActive(Prayer.PROTECT_FROM_MELEE), 100,6);
            }
        }else{
            if(GroundItems.closest(EGG_ID) != null){
                setNodeStatus("Picking up spider egg");
                if(GroundItems.closest(EGG_ID).interact()){
                    Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), Calculations.random(500,750),Calculations.random(5,7));
                }
            }

        }
    }
}

