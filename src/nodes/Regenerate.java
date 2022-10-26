package nodes;

import logic.BankingManager;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

import static util.Constants.*;

public class Regenerate extends Node{
    @Override
    public boolean validate() {
        return FEROX_ENCLAVE.contains(Players.getLocal()) &&
                needRegenerate() &&
                !BankingManager.needRestock();
    }

    @Override
    public void execute() {
        if(Prayers.isActive(Prayer.PROTECT_FROM_MELEE)){
            Logger.log("Prayer still active, disabling it");
            Prayers.toggle(false, Prayer.PROTECT_FROM_MELEE);
            Sleep.sleepUntil(() -> !Prayers.isActive(Prayer.PROTECT_FROM_MELEE), 50,40);
        }
        else{
            if(needRegenerate()){
                Logger.log("We need to regenerate");
                setNodeStatus("Regenerating health and prayer");
                regenerate();
            }
        }

    }

    private void regenerate() {
        if(GameObjects.closest("Pool of Refreshment").exists()){
            if(GameObjects.closest("Pool of Refreshment").interact()){
                Logger.log("Succesfully clicked pool of refreshment");
            }
        }

    }

    public static boolean needRegenerate(){
        return Skills.getRealLevel(Skill.PRAYER) > Skills.getBoostedLevel(Skill.PRAYER) || Skills.getRealLevel(Skill.HITPOINTS) > Skills.getBoostedLevel(Skill.HITPOINTS);
    }
}
