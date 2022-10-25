package script;

import logic.BankingManager;
import nodes.Banking;
import nodes.Node;
import nodes.Picking;
import nodes.Regenerate;
import org.dreambot.api.methods.Calculations;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import util.PaintHelper;
import static util.Constants.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@ScriptManifest(author = "Shock#5170", name = "sRedSpiderEggs", version = 1.0, description = "Private script for MrWalkway, no reselling is allowed", category = Category.MONEYMAKING)
public class Script extends AbstractScript {

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
        BankingManager bm = new BankingManager();
        bm.init();

        nodes.addAll(Arrays.asList(
                new Banking(bm),
                new Regenerate(),
                new Picking()
        ));

        failSafes();
    }

    private void failSafes() {

    }
}
