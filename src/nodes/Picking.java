package nodes;

import logic.BankingManager;
import logic.Traversing;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.List;

import static util.Constants.*;

public class Picking extends Node {

    @Override
    public boolean validate() {
        return RED_SPIDERS.contains(Players.getLocal()) && !Inventory.isFull() && Skills.getBoostedLevel(Skill.PRAYER) > 0;
    }

    @Override
    public void execute() {
        if(!RED_SPIDERS.contains(Players.getLocal())){
            Traversing.goToRedSpiders(this);
        }else{
            if(!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)){
                if(Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE)){
                    Sleep.sleepUntil(() -> Prayers.isActive(Prayer.PROTECT_FROM_MELEE), 100,6);
                }
            }else{
                GroundItem spiderEgg = GroundItems.closest(EGG_ID);
                if(spiderEgg != null){
                    spiderEgg.interact();
                }

            }
        }
    }
}
