package nodes;

import logic.Traversing;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.items.Item;

import java.util.Arrays;

import static util.Constants.*;

public class Banking extends Node {


    private boolean needRing = false;

    @Override
    public boolean validate() {
        return FEROX_ENCLAVE.contains(Players.getLocal().getTile()) && (Inventory.contains(EGG_ID) || needRingOfDuelling());
    }

    @Override
    public void execute() {
        if (!Bank.isOpen()) {
            if (BankLocation.getNearest().walkingDistance(Players.getLocal().getTile()) > 10) {
                STATUS = setNodeStatus("Running to bank");
                Logger.info("Running to bank.");
                Traversing.goToBank();
            }
            STATUS = setNodeStatus("Opening bank.");
            if (Bank.open()) {
                Logger.info("Successfully opened the bank.");
            }
        } else {
            //Bank is opened
            if (needRingOfDuelling()) {
               if(restockEquipable(e -> e.getName().contains("duelling("))){
                   needRing = false;
                }
            }
            if (!Inventory.isEmpty()) {
                STATUS = setNodeStatus("Depositing items.");
                int spider_eggs = Inventory.count(EGG_ID);
                if (Bank.depositAllItems()) {
                    Logger.info("Successfully deposited inventory");
                    EGGS_COLLECTED += spider_eggs;
                }
            }
        }
    }

    private boolean needRingOfDuelling() {
        if (Bank.isOpen()) return needRing;

        if (Equipment.getItemInSlot(EquipmentSlot.RING).isValid()) {
            if (!Equipment.getItemInSlot(EquipmentSlot.RING).getName().contains("Ring of duelling")) {
                Logger.debug("Ring of duelling not equipped");
                needRing = true;
                return needRing;
            }
        }
        needRing = false;
        return needRing;
    }

    private boolean equip(Filter<Item> item) {
        Logger.info("Equipping: "+ item);
        if (Inventory.get(item).isValid()) {
                Logger.debug("Actions on item to equip: ");
                Arrays.stream(Inventory.get(item).getActions()).forEach(e -> Logger.debug(e));
                if (Inventory.get(item).interact(Inventory.get(item).getActions()[0])) {
                    Logger.info("Succesfully equiped item: " + item);
                    return true;
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

    private boolean restockEquipable(Filter<Item> item){
        if(withdraw(item)){
            if(equip(item)){
                return true;
            }
        }
        return false;
    }


}

