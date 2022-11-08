package script;

import logic.BankingManager;
import logic.Traversing;
import nodes.*;
import org.dreambot.api.methods.Calculations;

import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.widgets.message.Message;
import org.dreambot.launcher.proxy.Proxy;
import util.PaintHelper;
import static util.Constants.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@ScriptManifest(author = "Shock#5170", name = "sRedSpiderEggs", version = 1.2, description = "Private script for MrWalkway, no reselling is allowed", category = Category.MONEYMAKING)
public class Script extends AbstractScript implements ChatListener {

    public static long startTime;

    private ArrayList<Node> nodes = new ArrayList<>();


    @Override
    public int onLoop() {
        for (Node n: nodes){
            if(n.validate()){
                n.execute();
            }
        }

        return Calculations.random(250,750);
    }


    @Override
    public void onPaint(Graphics g) {
        PaintHelper.buildPaint(g);
    }

    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
        BankingManager.init();



        nodes.addAll(Arrays.asList(
                new Banking(),
                new Regenerate(),
                new GoToSpiders(),
                new Picking(),
                new TeleportAway(),
                new DeathHandler(),
                new WorldHopping()
        ));
    }

    @Override
    public void onGameMessage(Message message){
        if(message.getMessage().contains("you are dead")){
            Logger.warn("We have died!");
            DIED++;
        }else if(message.getMessage().contains("cannot switch worlds")){
            Logger.warn("Still in combat when trying to worldhop");
            SHOULDWORLDHOP = true;
        }
    }


}
