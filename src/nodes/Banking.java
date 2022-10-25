package nodes;

import logic.BankingManager;
import logic.Traversing;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankQuantitySelection;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

import java.util.Arrays;

import static util.Constants.*;

public class Banking extends Node {

    private BankingManager bm;

    public Banking(BankingManager bm) {
        this.bm = bm;
    }

    @Override
    public boolean validate() {
        //is not at red spider eggs and inventory is not empty
        return (!Inventory.isEmpty() /*&& is not at red spiders*/) || bm.needRestock();
    }

    @Override
    public void execute() {
        if (!Bank.isOpen()) {
            //Bank is closed
            if(!FEROX_ENCLAVE.contains(Players.getLocal())){
                setNodeStatus("Teleporting to ferox enclave");
                Traversing.teleportRingOfDueling();
            }
            else{
                setNodeStatus("Running to bank");
                Logger.info("Running to bank.");
                Traversing.goToBank();
            }
            setNodeStatus("Opening bank.");
            if (Bank.open()) {
                Logger.info("Successfully opened the bank.");
            }
        } else {
            //Bank is opened
            if (!bm.hasFailsafesExecuted()) {
                bm.executeFailsafes();
            } else if (bm.needRestock()) {
                if (Inventory.isFull()) {
                    setNodeStatus("Depositing inventory.");
                    depositInventory();
                } else if (bm.needRingOfDueling()) {
                    if (bm.restockEquipable(e -> e.getName().contains("dueling("))) {
                        Logger.info("Succesfully equipped ring of dueling");
                    }
                } else if (bm.needMonkRobes()) {
                    if (bm.needMonkRobeT) {
                        if (bm.restockEquipable(e -> e.getID() == MONK_ROBE_TOP)) {
                            bm.needMonkRobeT = false;
                        }
                    } else if (bm.needMonkRobeB) {
                        if (bm.restockEquipable(e -> e.getID() == MONK_ROBE_BOTTOM)) {
                            bm.needMonkRobeB = false;
                        }
                    }
                }
            }
        }
    }

    private void depositInventory() {
        Logger.info("Depositing inventory");
        int spider_eggs = Inventory.count(EGG_ID);
        if (Bank.depositAllItems()) {
            Logger.info("Successfully deposited inventory");
            EGGS_COLLECTED += spider_eggs;
        }
    }
}

