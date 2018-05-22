/**
 * Haoyuan Tang 809040
 * Shuyuan Dang 840992
 */

import java.util.Comparator;

public class Patch_Comparator implements Comparator {
    @Override
    public int compare(Object object1, Object object2){
        Patch p1 = (Patch) object1;
        Patch p2 = (Patch) object2;
        if (p1.getCurrentGrain() < p2.getCurrentGrain())
            return 1;
        else if (p1.getCurrentGrain() > p2.getCurrentGrain())
            return -1;
        else
            return 0;
    }
}
