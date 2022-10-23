package logic;

import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;

import static util.Constants.*;
public class Traversing {

    private static final String PREFIX = "[TRAVERSING] - ";

    public static void goToBank(){
        STATUS = PREFIX + "TO BANK";
        if(BankLocation.getNearest().canReach()){
            Walking.walk(BankLocation.getNearest());
        }else{
            Logger.error("Cannot reach bank from current destination");
            ScriptManager.getScriptManager().stop();
        }
    }
}
