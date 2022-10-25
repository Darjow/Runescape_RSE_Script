package logic;

import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

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

    public static void teleportRingOfDueling() {
        STATUS = PREFIX + "TELEPORTING";
        if(Equipment.getItemInSlot(EquipmentSlot.RING) == null){
            Logger.error("We need to teleport to ferox enclave but we have no ring");
            ScriptManager.getScriptManager().stop();
        }
        if(!Equipment.getItemInSlot(EquipmentSlot.RING).getName().contains("dueling")){
            Logger.error("We need to teleport to ferox enclave but no rod");
            ScriptManager.getScriptManager().stop();
        }

        if(!Tab.EQUIPMENT.isOpen()){
            Tabs.open(Tab.EQUIPMENT);
            if(Sleep.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), 30,40)){
                Equipment.getItemInSlot(EquipmentSlot.RING).interact("Ferox Enclave");
                Sleep.sleepUntil(() -> FEROX_ENCLAVE.contains(Players.getLocal()), 30,100);
            }
        }

    }
}
