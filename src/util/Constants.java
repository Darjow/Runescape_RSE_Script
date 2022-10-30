package util;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public class Constants {

    //EGGS
    public static int EGG_ID = 223;
    public static int EGGS_COLLECTED = 0;

    //GEAR
    public static int MONK_ROBE_TOP = 544;
    public static int MONK_ROBE_BOTTOM = 542;

    //LOCATIONS
    public static Area FEROX_ENCLAVE = new Area( new Tile[] {
            new Tile(3124, 3640, 0),
            new Tile(3137, 3640, 0),
            new Tile(3137, 3646, 0),
            new Tile(3156, 3647, 0),
            new Tile(3156, 3627, 0),
            new Tile(3139, 3608, 0),
            new Tile(3123, 3608, 0),
            new Tile(3128, 3615, 0),
            new Tile(3122, 3615, 0),
            new Tile(3122, 3639, 0)
    });

    public static Area RED_SPIDERS = new Area(3113, 9962, 3132, 9948);

    public static Area EDGEVILLE = new Area(new Tile[]{
            new Tile(3074,3513),
            new Tile(3108,3513),
            new Tile(3101,3465),
            new Tile(3077,3464),

    });

    public static Area LUMBRIDGE = new Area(3200, 3237, 3254, 3189);



    //UTIL
    public static String STATUS = "Initialising";
    public static int DIED = 0;
    public static int WORLDHOPPED = 0;
}
