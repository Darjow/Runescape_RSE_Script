package logic;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankQuantitySelection;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.items.Item;

import java.util.Arrays;

import static util.Constants.*;

public class BankingManager {

    private static boolean failSafesExecuted = false;

    public static boolean needMonkRobeT = false;
    public static boolean needMonkRobeB = false;

    public static boolean needRestock(){
        return needRingOfDueling() || needMonkRobes();
    }
    public static void init(){
        needRestock();
    }
    public static boolean needMonkRobes() {
        if(Equipment.getItemInSlot(EquipmentSlot.CHEST) != null){
            if(Equipment.getItemInSlot(EquipmentSlot.CHEST).getID() == MONK_ROBE_TOP){
                needMonkRobeT = false;
            }else{
                needMonkRobeT = true;
            }
        }else{
            needMonkRobeT = true;
        }
        if(Equipment.getItemInSlot(EquipmentSlot.LEGS) != null){
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

    public static boolean needRingOfDueling() {
        if (Equipment.getItemInSlot(EquipmentSlot.RING) != null) {
            if (!Equipment.getItemInSlot(EquipmentSlot.RING).getName().contains("Ring of dueling")) {
                Logger.debug("Ring of dueling not equipped");
                return true;
            }
            return false;
        }
        return true;
    }

    private static boolean equip(Filter<Item> item) {
        Logger.info("Equipping: "+ item);
        if (Inventory.get(item).isValid()) {
            Logger.debug("Actions on item to equip: ");
            Arrays.stream(Inventory.get(item).getActions()).forEach(e -> Logger.debug(e));
            if (Inventory.get(item).interact("Wear")) {
                Sleep.sleepUntil(() -> !Inventory.contains(item), 100,15);
                if(Inventory.contains(item)){
                    Logger.info("Succesfully equipped item");
                    return true;
                }
            }
        }
        Logger.warn("Failed to equip: " + item);
        return false;
    }

    public static boolean hasFailsafesExecuted(){
        return failSafesExecuted;
    }
    private static boolean withdraw(Filter<Item> item){
        Logger.info("Withdrawing item");
        if(Bank.contains(item)){
            if(Bank.withdraw(item, 1)) {
                Sleep.sleepUntil(() -> Inventory.contains(item), 300,5);
                return Inventory.contains(item);
            }
        }else{
            Logger.error("We don't have any items to withdraw");
            ScriptManager.getScriptManager().stop();
        }
        Logger.warn("Failed to withdraw item");
        return false;
    }

    public static boolean restockEquipable(Filter<Item> item) {
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

    public static void executeFailsafes() {
        STATUS = "[BANKINGMANAGER] - Executing failsafes";

        //set default withdraw to 1
        while (Bank.getDefaultQuantity() != BankQuantitySelection.ONE) {
            Bank.setDefaultQuantity(BankQuantitySelection.ONE);
            Sleep.sleepUntil(() -> Bank.getDefaultQuantity() == BankQuantitySelection.ONE, 100, 20);
        }
        //deposit gear
        if (needRestock()) {
            if (!Equipment.all(e -> e.isValid()).isEmpty()) {
                Logger.log("We need to remove all equipment");
                Bank.depositAllEquipment();
                Sleep.sleepUntil(() -> Equipment.all(e -> e.isValid()).isEmpty(), 100, 20);
            }
        }
        failSafesExecuted = true;
    }

}
