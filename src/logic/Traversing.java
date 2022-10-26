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


    public static void goToBank(Node n) {
        n.setNodeStatus("running to bank");
        if (BankLocation.getNearest().canReach()) {
            Walking.walk(BankLocation.getNearest());
        } else {
            Logger.error("Cannot reach bank from current destination");
            ScriptManager.getScriptManager().stop();
        }
    }

    public static void teleportRingOfDueling(Node n) {
        n.setNodeStatus("teleporting to ferox enclave");
        if (Equipment.getItemInSlot(EquipmentSlot.RING) == null) {
            Logger.error("We need to teleport to ferox enclave but we have no ring");
            ScriptManager.getScriptManager().stop();
        }
        if (!Equipment.getItemInSlot(EquipmentSlot.RING).getName().contains("dueling")) {
            Logger.error("We need to teleport to ferox enclave but no rod");
            ScriptManager.getScriptManager().stop();
        }

        if (!Tab.EQUIPMENT.isOpen()) {
            Tabs.open(Tab.EQUIPMENT);
            if (Sleep.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), 30, 40)) {
                Equipment.getItemInSlot(EquipmentSlot.RING).interact("Ferox Enclave");
                Sleep.sleepUntil(() -> FEROX_ENCLAVE.contains(Players.getLocal()), 30, 100);
            }
        }

    }

    public static void goToRedSpiders(Node node) {
        Tile currentTile = Players.getLocal().getTile();
        if (FEROX_ENCLAVE.contains(Players.getLocal())) {
            if (GameObjects.closest(e -> e.getName().equals("Stairs") && e.hasAction("Walk-down")).exists()) {
                node.setNodeStatus("Handling stairs");
                Logger.log("Handling stairs");
                GameObjects.closest(e -> e.getName().equals("Stairs") && e.hasAction("Walk-down")).interact();
            } else {
                node.setNodeStatus("Running to staircase");
                Logger.log("Running to staircase");
                Walking.walk(new Tile(3150 + Calculations.random(0, 2), 3635 + Calculations.random(0, 4)));
            }
        } else if (currentTile.getY() > 10000) {
            if (GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter")).exists()) {
                node.setNodeStatus("Handling soul wars portal");
                Logger.log("Handling Soul wars portal");
                GameObjects.closest(e -> e.getName().equals("Soul Wars Portal") && e.hasAction("Enter")).interact();
            } else {
                node.setNodeStatus("Walking to soul wars portal");
                Logger.log("Walking to soul wars portal");
                Walking.walk(new Tile(3152 + Calculations.random(0, 2), 10020 + Calculations.random(0, 4)));
            }
        } else if (String.valueOf(currentTile.getX()).startsWith("220") && String.valueOf(currentTile.getY()).startsWith("285")) {
            node.setNodeStatus("Handling portal to edgeville");
            Logger.log("Handling portal to edgeville");
            if (GameObjects.closest(e -> e.getName().equals("Portal") && e.hasAction("Edgeville")).exists()) {
                GameObjects.closest(e -> e.getName().equals("Portal") && e.hasAction("Edgeville")).interact("Edgeville");
            }
        } else {
            Walking.walk(RED_SPIDERS.getRandomTile());
            if (Widgets.getWidget(475) != null) {
                Widgets.getWidget(475).getChild(11).interact();
            }

        }
    }
}