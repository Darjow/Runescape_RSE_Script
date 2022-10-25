package logic;

import nodes.Node;
import nodes.Picking;
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


    public static void goToBank(Node n){
        n.setNodeStatus("running to bank");
        if(BankLocation.getNearest().canReach()){
            Walking.walk(BankLocation.getNearest());
        }else{
            Logger.error("Cannot reach bank from current destination");
            ScriptManager.getScriptManager().stop();
        }
    }

    public static void teleportRingOfDueling(Node n) {
        n.setNodeStatus("teleporting to ferox enclave");
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

    public static void goToRedSpiders(Node node) {
        node.setNodeStatus("Running to red spiders location");
        //if at ferox, and z = 0 -> take portal
        //if at -1 -> take portal to castle wars
        //if at castle wars -> take portal to edgeville
        //if at edgeville -> go to red spiders
    }
}
