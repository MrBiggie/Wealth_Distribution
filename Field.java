/**
 * Haoyuan Tang 809040
 * Shuyuan Dang 840992
 */
import java.util.Random;

public class Field implements ITicker {

    private int width; //50
    private int height; //50
    private Patch[][] patches;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        Random random = new Random();
        patches = new Patch[width][height];
        double[][] patch_init_value = new double[width][height];

        //initialize patches with corresponding number of max grain and current grain
        for (int w = 0; w < this.width; w++) {
            for (int h = 0; h < this.height; h++) {
                if (random.nextDouble() < ((double) Parameters.PERCENT_BEST_LAND / 100)) {
                    patches[w][h] = new Patch(
                            Parameters.MAX_GRAIN_PATCH,
                            Parameters.MAX_GRAIN_PATCH,
                            new Location(w, h));
                    patch_init_value[w][h] = Parameters.MAX_GRAIN_PATCH;
                } else {
                    patches[w][h] = new Patch(
                            0,
                            0,
                            new Location(w, h));
                    patch_init_value[w][h] = 0;
                }
            }
        }
        //diffuse first five times
        for (int w = 0; w < this.width; w++) {
            for (int h = 0; h < this.height; h++) {
                if (patches[w][h].getMaxGrain() != 0) {
                    diffuse_five_times(patch_init_value, w, h);
                }
            }
        }
        //diffuse 10 times
        for (int w = 0; w < this.width; w++) {
            for (int h = 0; h < this.height; h++) {
                diffuse(patch_init_value, w, h);
            }
        }

        //initiate all values for patches
        for (int w = 0; w < this.width; w++) {
            for (int h = 0; h < this.height; h++) {
                patches[w][h].setCurrentGrain((int) patch_init_value[w][h]);
                patches[w][h].setMaxGrain((int) patch_init_value[w][h]);
            }
        }

        //round grains of patches to 50 if max grain exceed 50
        for (int w = 0; w < this.width; w++) {
            for (int h = 0; h < this.height; h++) {
                if(patches[w][h].getCurrentGrain()>50){
                    patches[w][h].setCurrentGrain(50);
                    patches[w][h].setMaxGrain(50);
                }
            }
        }

        //print the field
        for (int row = 0; row < height; ++row) {
            for (int column = 0; column < width; ++column) {
                System.out.print(patches[row][column].getCurrentGrain() + " ");
            }
            System.out.println("\n");
        }
    }

    public void diffuse_five_times(double[][] patch_init_value, int x, int y) {
        int up = ((y + 1) <= (this.height - 1)) ? (y + 1) : (y - (this.height - 1));
        int down = ((y - 1) >= 0) ? (y - 1) : (this.height - 1 + y);
        int left = ((x - 1) >= 0) ? (x - 1) : (this.width - 1 + x);
        int right = ((x + 1) <= (this.width - 1)) ? (x + 1) : (x - (this.width - 1));
        patch_init_value[x][up] += patch_init_value[x][y] * 1.25 / 8;
        patch_init_value[x][down] += patch_init_value[x][y] * 1.25 / 8;
        patch_init_value[left][y] += patch_init_value[x][y] * 1.25 / 8;
        patch_init_value[right][y] += patch_init_value[x][y] * 1.25 / 8;
        patch_init_value[left][up] += patch_init_value[x][y] * 1.25 / 8;
        patch_init_value[left][down] += patch_init_value[x][y] * 1.25 / 8;
        patch_init_value[right][up] += patch_init_value[x][y] * 1.25 / 8;
        patch_init_value[right][down] += patch_init_value[x][y] * 1.25 / 8;

    }

    public void diffuse(double[][] patch_init_value, int x, int y) {
        patch_init_value[x][y] = patch_init_value[x][y] * 0.75;
        int up = ((y + 1) <= (this.height - 1)) ? (y + 1) : (y - (this.height - 1));
        int down = ((y - 1) >= 0) ? (y - 1) : (this.height - 1 + y);
        int left = ((x - 1) >= 0) ? (x - 1) : (this.width - 1 + x);
        int right = ((x + 1) <= (this.width - 1)) ? (x + 1) : (x - (this.width - 1));
        patch_init_value[x][up] += patch_init_value[x][y] * 0.25 / 8;
        patch_init_value[x][down] += patch_init_value[x][y] * 0.25 / 8;
        patch_init_value[left][y] += patch_init_value[x][y] * 0.25 / 8;
        patch_init_value[right][y] += patch_init_value[x][y] * 0.25 / 8;
        patch_init_value[left][up] += patch_init_value[x][y] * 0.25 / 8;
        patch_init_value[left][down] += patch_init_value[x][y] * 0.25 / 8;
        patch_init_value[right][up] += patch_init_value[x][y] * 0.25 / 8;
        patch_init_value[right][down] += patch_init_value[x][y] * 0.25 / 8;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Patch getPatch(int x, int y) {
        return patches[x][y];
    }

    @Override
    public void tick() {
        for (int w = 0; w < this.width; w++) {
            for (int h = 0; h < this.height; h++) {
                patches[w][h].tick();
            }
        }
    }


}
