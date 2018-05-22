/**
 * Haoyuan Tang 809040
 * Shuyuan Dang 840992
 */

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
        this.wealthLevel = WealthLevel.none;
    }

    public int getGrain() {
        return grain;
    }

    /**
     * value every patch in vision, and decide the next patch the people should move towards
     * @return
     */
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

        // get best patch with the most grains inside
        Patch Best_Patch = Patches.get(0);
        // determine next move direction
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

    // harvest grains of the patch the people are currently on
    public void harvest() {
        Patch patch = field.getPatch(location.getX(), location.getY());
        //pay tax
        if (Parameters.TAX) {
            switch (this.wealthLevel) {
                case low:
                    grain += (int) (patch.harvest() * (1 - Parameters.TAX_RATE_POOR));
                    break;
                case mid:
                    grain += (int) (patch.harvest() * (1 - Parameters.TAX_RATE_MIDDLE));
                    break;
                case high:
                    grain += (int) (patch.harvest() * (1 - Parameters.TAX_RATE_RICH));
                    break;
                default:
                    grain += patch.harvest();
                    break;
            }
        } else {
            grain += patch.harvest();
            if (Parameters.PATCH_DECAY == true) {
                patch.setHarvest_remain(patch.getHarvest_remain() - 1);
            }
        }
        // System.out.println("harvested grains: " + grain);
    }

    @Override
    public void tick() {

        // people move to next patch
        move();

        // collect grain
        harvest();

        // eat metabolism
        consume();

        // ages increases
        current_age += 1;

        // this people die if he is older than
        if (current_age > life_expectancy || grain == 0) {
            reincarnate();
        }
    }

    private void consume() {
        grain -= metabolism;
        grain = grain < 0 ? 0 : grain;
    }

    /**
     * a people will reincarnate when he/she reaches lifespan limit or runs out of grains.
     */
    public void reincarnate() {
        Random random = new Random();
        int metabolism = 1 + random.nextInt(Parameters.METABOLISM_MAX);
        int life_expectancy = Parameters.LIFE_EXPECTANCY_MIN
                + random.nextInt(Parameters.LIFE_EXPECTANCY_MAX - Parameters.LIFE_EXPECTANCY_MIN + 1);
        int grain;
        int vision = 1 + random.nextInt(Parameters.MAX_VISION);
        int age = 0;
        if (Parameters.WEALTH_INHERITANCE && this.grain > this.metabolism) {
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