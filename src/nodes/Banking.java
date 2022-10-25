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


    public Banking() {

    }

    @Override
    public boolean validate() {
        return (!Inventory.isEmpty() && !RED_SPIDERS.contains(Players.getLocal())) || BankingManager.needRestock();
    }

    @Override
    public void execute() {
        if (!Bank.isOpen()) {
            //Bank is closed
            if(!FEROX_ENCLAVE.contains(Players.getLocal())){
                setNodeStatus("Teleporting to ferox enclave");
                Traversing.teleportRingOfDueling(this);
            }
            else{
                setNodeStatus("Running to bank");
                Logger.info("Running to bank.");
                Traversing.goToBank(this);
            }
            setNodeStatus("Opening bank.");
            if (Bank.open()) {
                Logger.info("Successfully opened the bank.");
            }
        } else {
            //Bank is opened
            if (!BankingManager.hasFailsafesExecuted()) {
                BankingManager.executeFailsafes();
            } else if (BankingManager.needRestock()) {
                if (Inventory.isFull()) {
                    setNodeStatus("Depositing inventory.");
                    depositInventory();
                } else if (BankingManager.needRingOfDueling()) {
                    if (BankingManager.restockEquipable(e -> e.getName().contains("dueling("))) {
                        Logger.info("Succesfully equipped ring of dueling");
                    }
                } else if (BankingManager.needMonkRobes()) {
                    if (BankingManager.needMonkRobeT) {
                        if (BankingManager.restockEquipable(e -> e.getID() == MONK_ROBE_TOP)) {
                            BankingManager.needMonkRobeT = false;
                        }
                    } else if (BankingManager.needMonkRobeB) {
                        if (BankingManager.restockEquipable(e -> e.getID() == MONK_ROBE_BOTTOM)) {
                            BankingManager.needMonkRobeB = false;
                        }
                    }
                }else if (!Inventory.isEmpty()){
                    depositInventory();
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

