package logic;

import nodes.Node;
import nodes.Picking;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import script.Script;

import static util.Constants.*;
public class Traversing {


    public static void goToBank() {
        if (BankLocation.getNearest().canReach()) {
            Walking.walk(BankLocation.getNearest());
        } else {
            if(FEROX_ENCLAVE.contains(Players.getLocal())){
                Logger.warn("Failsafe running to ferox bank");
                Walking.walk(BankLocation.FEROX_ENCLAVE.getTile());
            }else if(LUMBRIDGE.contains(Players.getLocal())){
                Logger.warn("Failsafe running to lumbridge bank");
                Walking.walk(BankLocation.LUMBRIDGE.getTile());
            }
            else{
                Logger.error("Cannot reach bank from current destination");
                ScriptManager.getScriptManager().stop();
            }
        }
    }

    public static void teleportRingOfDueling() {
        if (Equipment.getItemInSlot(EquipmentSlot.RING) == null) {
            Logger.error("We need to teleport to ferox enclave but we have no ring");
            ScriptManager.getScriptManager().stop();
        }
        if (!Equipment.getItemInSlot(EquipmentSlot.RING).getName().contains("dueling")) {
            Logger.error("We need to teleport to ferox enclave but no ring");
            ScriptManager.getScriptManager().stop();
        }
        if (!Tab.EQUIPMENT.isOpen()) {
            Tabs.open(Tab.EQUIPMENT);
            Sleep.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), 500);
        }else{
            Equipment.getItemInSlot(EquipmentSlot.RING).interact("Ferox Enclave");
            Sleep.sleepUntil(() -> FEROX_ENCLAVE.contains(Players.getLocal()), Calculations.random(3400,4800));
            }
        }

    }
