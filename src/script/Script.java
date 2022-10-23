package script;

import nodes.Banking;
import nodes.Node;
import nodes.Picking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import util.PaintHelper;
import static util.Constants.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@ScriptManifest(author = "Shock#5170", name = "sRedSpiderEggs", version = 1.0, description = "Private script for MrWalkway, no reselling is allowed", category = Category.MONEYMAKING)
public class Script extends AbstractScript {

    public static long startTime;

    private ArrayList<Node> nodes;


    @Override
    public int onLoop() {
        return 0;
    }


    @Override
    public void onPaint(Graphics g) {
        PaintHelper.buildPaint(g);
    }

    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();

        nodes.addAll(Arrays.asList(
                new Banking(),
                new Picking()
        ));
    }
}
