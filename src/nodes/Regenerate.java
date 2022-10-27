package nodes;

import logic.BankingManager;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

import static util.Constants.*;

public class Regenerate extends Node{
    @Override
    public boolean validate() {
        return FEROX_ENCLAVE.contains(Players.getLocal()) &&
                needRegenerate() &&
                !BankingManager.needRestock() &&
                Inventory.isEmpty();
    }

    @Override
    public void execute() {
        if(Prayers.isActive(Prayer.PROTECT_FROM_MELEE)){
            Logger.log("Prayer still active, disabling it");
            Prayers.toggle(false, Prayer.PROTECT_FROM_MELEE);
            Sleep.sleepUntil(() -> !Prayers.isActive(Prayer.PROTECT_FROM_MELEE), 50,40);
        }
        else{
            if(!Walking.isRunEnabled()){
                if(Walking.toggleRun()){
                    Sleep.sleepUntil(() -> Walking.isRunEnabled(), Calculations.random(100,300)*2);
                }
            }
            if(needRegenerate()){
                Logger.log("We need to regenerate");
                setNodeStatus("Regenerating health and prayer");
                regenerate();
            }
        }

    }

    private void regenerate() {
        if(GameObjects.closest(e -> e.getName().equals("Pool of Refreshment") && e.walkingDistance(Players.getLocal().getTile()) < 10) != null){
            if(GameObjects.closest("Pool of Refreshment").interact()){
                Sleep.sleepUntil(() -> !needRegenerate(), Calculations.random(3800,6800));
            }
        }else{
            Walking.walk(new Tile(3129, 3636,0));
            Sleep.sleep(Calculations.random(100,500)*5);
        }

    }

    public static boolean needRegenerate() {
        return Skills.getRealLevel(Skill.PRAYER) > Skills.getBoostedLevel(Skill.PRAYER);
    }
}
