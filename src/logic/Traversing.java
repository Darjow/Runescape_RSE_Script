package logic;

import nodes.Node;
import nodes.Picking;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

import static util.Constants.*;
public class Traversing {


    public static void goToBank() {
        if (BankLocation.getNearest().canReach()) {
            Walking.walk(BankLocation.getNearest());
        } else {
            Logger.error("Cannot reach bank from current destination");
            //ScriptManager.getScriptManager().stop();
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
        Logger.log("Teleporting away with ring of dueling");
        if (!Tab.EQUIPMENT.isOpen()) {
            Tabs.open(Tab.EQUIPMENT);
            if (Sleep.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), 500)) {
                Equipment.getItemInSlot(EquipmentSlot.RING).interact("Ferox Enclave");
                Sleep.sleepUntil(() -> FEROX_ENCLAVE.contains(Players.getLocal()), Calculations.random(3400,4800));
            }
        }

    }
}