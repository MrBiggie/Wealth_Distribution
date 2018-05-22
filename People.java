import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Person is as turtle in the Wealth Distribution model
 */
public class People implements ITicker {
    private Field field;
    private int current_age;
    private int life_expectancy;
    private int metabolism;
    private int grain;
    private int vision;
    private Location location;
    private WealthLevel wealthLevel;

    public People(Field field, int life_expectancy, int metabolism, int vision, int current_age, int grain) {
        this.field = field;
        this.life_expectancy = life_expectancy;
        this.metabolism = metabolism;
        this.vision = vision;
        this.current_age = current_age;
        this.grain = grain;
    }

    public int getGrain() {
        return grain;
    }

    public Patch getNextPatch() {

        // get the location of current people
        int X = location.getX();
        int Y = location.getY();

        // get the limitation of field width and height
        int x_max = field.getWidth() - 1;
        int y_max = field.getHeight() - 1;
        ArrayList<Patch> Patches = new ArrayList<>();

        // make an arraylist to store all possible patches within the vision
        for (int i = 1; i <= vision; i++) {
            int up = ((Y + i) <= y_max) ? (Y + i) : (i - (y_max - Y) - 1);
            int down = ((Y - i) >= 0) ? (Y - i) : (y_max + Y + 1 - i);
            int left = ((X - i) >= 0) ? (X - i) : (x_max + X + 1 - i);
            int right = ((X + i) <= x_max) ? (X + i) : (i - (x_max - X) - 1);
            Patches.add(field.getPatch(X, up));
            Patches.add(field.getPatch(X, down));
            Patches.add(field.getPatch(left, Y));
            Patches.add(field.getPatch(right, Y));
        }

        // use predefined comparator to sort the arraylist
        Patch_Comparator pc = new Patch_Comparator();
        Collections.sort(Patches, pc);

        // Get best patch with the most grains inside
        Patch Best_Patch = Patches.get(0);
        if (Best_Patch.getLocation().getX() == location.getX()) {
            if (Best_Patch.getLocation().getY() > location.getY()) {
                return field.getPatch(location.getX(), location.getY() + 1);
            } else {
                return field.getPatch(location.getX(), location.getY() - 1);
            }
        } else {
            if (Best_Patch.getLocation().getX() > location.getX()) {
                return field.getPatch(location.getX() + 1, location.getY());
            } else {
                return field.getPatch(location.getX() - 1, location.getY());
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void move() {
        Location location = getNextPatch().getLocation();
        this.location = location;
    }

    // collect grain of certain patch
    public void harvest() {
        Patch patch = field.getPatch(location.getX(), location.getY());
        // if (this.wealthLevel == wealthLevel.low) {
        // System.out.println("low");
        // grain += (int) (patch.harvest() * Parameters.TAX_RATE_POOR);
        // } else if (this.wealthLevel == wealthLevel.mid) {
        // System.out.println("mid");
        // grain += (int) (patch.harvest() * Parameters.TAX_RATE_MIDDLE);
        // } else {
        // System.out.println("high");
        // grain += (int) (patch.harvest() * Parameters.TAX_RATE_RICH);
        // }
        grain += patch.harvest();
        // System.out.println("harvested grains: " + grain);
    }

    @Override
    public void tick() {

        // people move to next patch
        move();

        // collect grain
        harvest();

        // eat metabolism
        grain -= metabolism;

        // ages increases
        current_age += 1;

        // this people die if he is older than
        if (current_age > life_expectancy || grain <= 0) {
            reincarnate();
        }
    }

    public void reincarnate() {
        Random random = new Random();
        int metabolism = 1 + random.nextInt(Parameters.METABOLISM_MAX);
        int life_expectancy = Parameters.LIFE_EXPECTANCY_MIN
                + random.nextInt(Parameters.LIFE_EXPECTANCY_MAX - Parameters.LIFE_EXPECTANCY_MIN + 1);
        int grain;
        int vision = 1 + random.nextInt(Parameters.MAX_VISION);
        int age = 0;
        if (Parameters.WEALTH_INHERITANCE == true) {
            grain = (int) (this.grain * Parameters.WEALTH_INHERITANCE_PROPORTION);
        } else {
            grain = random.nextInt(Parameters.INITIAL_MAX_GRAIN_WEALTH_PER_PEOPLE);
        }

        this.metabolism = metabolism;
        this.life_expectancy = life_expectancy;
        this.grain = grain;
        this.vision = vision;
        this.current_age = age;
    }

    public void setWealthLevel(WealthLevel wealthLevel) {
        this.wealthLevel = wealthLevel;
    }
}