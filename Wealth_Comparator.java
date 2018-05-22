/**
 * Haoyuan Tang 809040
 * Shuyuan Dang 840992
 */

import java.util.Comparator;

public class Wealth_Comparator implements Comparator {

    @Override
    public int compare(Object object1, Object object2) {
        People p1 = (People) object1;
        People p2 = (People) object2;
        if (p1.getGrain() < p2.getGrain())
            return -1;
        else if (p1.getGrain() > p2.getGrain())
            return 1;
        else
            return 0;
    }
}

