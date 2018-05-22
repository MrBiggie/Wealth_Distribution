public class Parameters {
    public final static int TICK_INTERVAL = 0;

    public final static int MAX_GRAIN_PATCH = 50;
    public static final int FIELD_HEIGHT = 50;
    public static final int FIELD_WIDTH = 50;
    public static final int INITIAL_MAX_GRAIN_WEALTH_PER_PEOPLE = 50;
    public static final int NUM_OF_PEOPLE = 250;
    public static final int MAX_VISION = 3;
    public static final int LIFE_EXPECTANCY_MAX = 70;
    public static final int LIFE_EXPECTANCY_MIN = 1;
    public static final int METABOLISM_MAX = 15;
    public static final int NUM_OF_TICKS = 1000;
    public static final int PERCENT_BEST_LAND = 20;
    public static final int GRAIN_GROWTH_INTERVAL = 2;
    public static final int NUM_GRAIN_GROWN = 5;
    public static final int MAX_WEALTH = 70;

    //Patch decay
    public static final boolean PATCH_DECAY = false;
    public static final int HARVEST_REMAIN = 6;
    public static final double HARVEST_DECAY = 0.15;

    //Inheritance
    public static final boolean WEALTH_INHERITANCE = false;
    public static final double WEALTH_INHERITANCE_PROPORTION = 1;

    //Tax policy
    public static final boolean TAX = false;
    public static final double TAX_RATE_RICH = 0.4;
    public static final double TAX_RATE_POOR = 0.2;
    public static final double TAX_RATE_MIDDLE = 0;

}