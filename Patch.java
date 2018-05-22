/**
 * Haoyuan Tang 809040
 * Shuyuan Dang 840992
 */

/**
 * Simulate the patch in Wealth Distribution model
 */
public class Patch implements ITicker {
    private int currentGrain;
    private int maxGrain;
    private int tick_counter = Parameters.GRAIN_GROWTH_INTERVAL;
    private Location patch_location;
    private int harvest_remain = Parameters.HARVEST_REMAIN;

    public Patch(int currentGrain, int maxGrain, Location patch_location) {
        this.currentGrain = currentGrain;
        this.maxGrain = maxGrain;
        this.patch_location = patch_location;
    }

    @Override
    public String toString() {
        return "Patch with" + currentGrain + "/" + maxGrain;
    }

    public Location getLocation() {
        return patch_location;
    }

    public void setLocation(Location patch_location) {
        this.patch_location = patch_location;
    }

    /**
     * harvest this patch and set current grain to 0.
     *
     * @return the current grain of this patch.
     */
    public int harvest() {
        int harvest = currentGrain;
        currentGrain = 0;
        if (Parameters.PATCH_DECAY == true) {
            if (harvest_remain == 0) {
                this.maxGrain *= (int) (1 - Parameters.HARVEST_DECAY);
                harvest_remain = Parameters.HARVEST_REMAIN;
            }
        }
        return harvest;
    }

    /**
     * grow up grain of this patch
     *
     * @return the current grain of this patch.
     */
    private void grow() {
        currentGrain += Parameters.NUM_GRAIN_GROWN;
        if (currentGrain > maxGrain) {
            currentGrain = maxGrain;
        }
        tick_counter = Parameters.GRAIN_GROWTH_INTERVAL;
    }

    @Override
    public void tick() {
        this.tick_counter -= 1;
        if (this.tick_counter == 0) {
            grow();
        }
    }

    /**
     * @return the currentGrain
     */
    public int getCurrentGrain() {
        return currentGrain;
    }

    /**
     * @param currentGrain the currentGrain to set
     */
    public void setCurrentGrain(int currentGrain) {
        this.currentGrain = currentGrain;
    }

    /**
     * @return the maxGrain
     */
    public int getMaxGrain() {
        return maxGrain;
    }

    /**
     * @param maxGrain the maxGrain to set
     */
    public void setMaxGrain(int maxGrain) {
        this.maxGrain = maxGrain;
    }

    public int getHarvest_remain() {
        return harvest_remain;
    }

    public void setHarvest_remain(int harvest_remain) {
        this.harvest_remain = harvest_remain;
    }
}