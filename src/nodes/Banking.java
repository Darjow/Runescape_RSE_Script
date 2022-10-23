package nodes;

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
import java.util.concurrent.locks.Condition;
import java.util.stream.Collectors;

import static util.Constants.*;

public class Banking extends Node {


    private boolean needMonkRobeT = false;
    private boolean needMonkRobeB = false;

    private boolean failSafesExecuted = false;

    @Override
    public boolean validate() {
        return FEROX_ENCLAVE.contains(Players.getLocal().getTile()) && (Inventory.contains(EGG_ID) || needRestock());
    }

    @Override
    public void execute() {
        if (!Bank.isOpen()) {
            //Bank is closed
            if (BankLocation.getNearest().walkingDistance(Players.getLocal().getTile()) > 10) {
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
            if(!failSafesExecuted){
                executeFailsafes();
            }

            //Inventory is full depositing to make space
            if(Inventory.isFull()){
                setNodeStatus("Inventory is full depositing items");
                Bank.depositAllItems();
            }


            if(!Inventory.isFull()){
                if (needRingOfDuelling()) {
                   if(restockEquipable(e -> e.getName().contains("duelling("))){
                       Logger.info("Succesfully equipped ring of duelling");
                    }
                }
                if(needMonkRobes()) {
                    if (needMonkRobeT) {
                        if (restockEquipable(e -> e.getID() == MONK_ROBE_TOP)) {
                            needMonkRobeT = false;
                        }
                    }
                    if (needMonkRobeB) {
                        if (restockEquipable(e -> e.getID() == MONK_ROBE_BOTTOM)) {
                            needMonkRobeB = false;
                        }
                    }
                }
            }

            if (!Inventory.isEmpty()) {
                setNodeStatus("Depositing items.");
                int spider_eggs = Inventory.count(EGG_ID);
                if (Bank.depositAllItems()) {
                    Logger.info("Successfully deposited inventory");
                    EGGS_COLLECTED += spider_eggs;
                }
            }
        }
    }

    private void executeFailsafes() {
        setNodeStatus("Failsafes");

        //set default withdraw to 1
        while (Bank.getDefaultQuantity() != BankQuantitySelection.ONE) {
            Bank.setDefaultQuantity(BankQuantitySelection.ONE);
            Sleep.sleepUntil(() -> Bank.getDefaultQuantity() == BankQuantitySelection.ONE, 100, 20);
        }
        //deposit gear
        if (needRestock()) {
            while (!Equipment.all(e -> e.isValid()).isEmpty()) {
                Bank.depositAllEquipment();
                Sleep.sleepUntil(() -> Equipment.all(e -> e.isValid()).isEmpty(), 100, 20);
            }
        }
    }

    private boolean needRestock(){
        return needRingOfDuelling() || needMonkRobes();
    }

    private boolean needMonkRobes() {
        if(Equipment.getItemInSlot(EquipmentSlot.CHEST).isValid()){
            if(Equipment.getItemInSlot(EquipmentSlot.CHEST).getID() == MONK_ROBE_TOP){
                needMonkRobeT = false;
            }else{
                needMonkRobeT = true;
            }
        }else{
            needMonkRobeT = true;
        }
        if(Equipment.getItemInSlot(EquipmentSlot.LEGS).isValid()){
            if(Equipment.getItemInSlot(EquipmentSlot.LEGS).getID() == MONK_ROBE_BOTTOM){
                needMonkRobeB = false;
            }else{
                needMonkRobeB = true;
            }
        }else{
            needMonkRobeB = true;
        }

        return needMonkRobeB || needMonkRobeT;
    }

    private boolean needRingOfDuelling() {
        if (Equipment.getItemInSlot(EquipmentSlot.RING).isValid()) {
            if (!Equipment.getItemInSlot(EquipmentSlot.RING).getName().contains("Ring of duelling")) {
                Logger.debug("Ring of duelling not equipped");
                return true;
            }
        }
        return false;
    }

    private boolean equip(Filter<Item> item) {
        Logger.info("Equipping: "+ item);
        if (Inventory.get(item).isValid()) {
                Logger.debug("Actions on item to equip: ");
                Arrays.stream(Inventory.get(item).getActions()).forEach(e -> Logger.debug(e));
                if (Inventory.get(item).interact(Inventory.get(item).getActions()[0])) {
                    if(Equipment.contains(item)){
                        Logger.info("Succesfully equiped item: " + item);
                        return true;
                    }
                }
            }
            Logger.warn("Failed to equip: " + item);
            return false;
    }

    private boolean withdraw(Filter<Item> item){
            Logger.info("Withdrawing: "+ item);
            if(Bank.contains(item)){
                if(Bank.withdraw(item, 1)) {
                    Logger.info("Succesfully withdrawn item: " + item);
                    return true;
                }
            }else{
                Logger.error("We don't have any items to withdraw: " + item);
                ScriptManager.getScriptManager().stop();
            }
            Logger.warn("Failed to withdraw item: " + item);
            return false;
    }

    private boolean restockEquipable(Filter<Item> item) {
        if (!Inventory.contains(item)) {
            withdraw(item);
        }
        if (Inventory.contains(item)) {
            if (equip(item)) {
                return true;
            }
        }
        return false;
    }


}

