package util;

import org.dreambot.api.methods.grandexchange.LivePrices;

import java.awt.*;
import java.text.DecimalFormat;
import static script.Script.*;
import static util.Constants.*;

public class PaintHelper {


    public static void buildPaint(Graphics g){
        g.setFont(new Font("Sathu", Font.BOLD, 12));

        g.drawString("Time Running: " + formatTime(), 40, 90);
        g.drawString(String.format("Profit: %d", EGGS_COLLECTED * LivePrices.get(EGG_ID)), 40, 110);
        g.drawString(String.format("Profit per hour: %s", parseProfitPerhour()), 40, 130);
        g.drawString(status, 40, 170);

        g.drawString("DEBUG", 500, 90);
    }

    private static String formatTime() {
        long millis = System.currentTimeMillis() - startTime;
        int sec = (int) (millis / 1000) % 60;
        int min = (int) ((millis / (1000 * 60)) % 60);
        int hr = (int) ((millis / (1000 * 60 * 60)) % 24);
        return String.format("%s:%s:%s", hr < 10 ? "0" + hr : hr, min < 10 ? "0" + min : min, sec < 10 ? "0" + sec : sec);
    }

    private static String parseProfitPerhour() {
        DecimalFormat df = new DecimalFormat("0.00");

        double pph = EGGS_COLLECTED * LivePrices.get(EGG_ID) / ((System.currentTimeMillis() - startTime) / 3600000.0);

        if (pph > 100000) {
            return Math.round(pph / 1000) + "k";
        }
        return df.format(pph) + "";
    }


}
