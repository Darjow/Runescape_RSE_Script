package nodes;


import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.utilities.Sleep;

import java.util.List;

import static util.Constants.*;

public class Picking extends Node {

    private List<World> hoppableWorlds = Worlds.all(e -> !e.isPvpArena() && e.isMembers() && !e.isDeadmanMode() && !e.isLeagueWorld() && !e.isFreshStart() && !e.isTargetWorld() && !e.isPvpArena() && e.getMinimumLevel() == 0 && (e.getWorld() >= Worlds.getMyWorld().getWorld() - 50 && e.getWorld() <= Worlds.getMyWorld().getWorld() + 50) && e.getWorld() != Worlds.getMyWorld().getWorld());

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
        }else{
            if(!Players.getLocal().isHealthBarVisible() && !Players.getLocal().isInCombat()){
                setNodeStatus("WorldHopping");
                int current = Worlds.getMyWorld().getWorld();
                if(WorldHopper.hopWorld(hoppableWorlds.get(Calculations.random(0, hoppableWorlds.size()-1)).getWorld(), true)){
                    setNodeStatus("Waiting till we arrived at new world");
                    if(Sleep.sleepUntil(() ->  current != Worlds.getMyWorld().getWorld() || Players.getLocal().isInCombat(), Calculations.random(1800,2600)*3)){
                        WORLDHOPPED++;
                    }
                }
            }
        }

        }
    }


