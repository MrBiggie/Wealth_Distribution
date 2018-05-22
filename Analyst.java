import java.io.*;
import java.util.ArrayList;

public class Analyst {

    /**
     * analyse every person's wealth level, and generate statistics data.
     * Four indices will be generated over this process: * [1]Class Plot [2]Class
     * Histogram [3]Lorenz Curve [4]Gini-Index
     */
    public static WealthLevel assessWealth(People people, int maxWealth) {
        if (people.getGrain() <= maxWealth / 3) {
            return WealthLevel.low;
        } else if (people.getGrain() > maxWealth * 2 / 3) {
            return WealthLevel.high;
        } else {
            return WealthLevel.mid;
        }
    }

    public static double[] Lorenz(ArrayList<People> ascendingPopulation) {
        double total_wealth = 0;
        double[] lorenz_para = new double[101];
        for (People people : ascendingPopulation) {
            total_wealth += people.getGrain();
        }

        for (int i = 0; i < 101; i++) {
            double wealth = 0;
            double wealth_percent = 0;
            for (int j = 0; j < (int) (0.01 * (i) * ascendingPopulation.size()); j++) {
                wealth += ascendingPopulation.get(j).getGrain();
            }
            wealth_percent = wealth / total_wealth;
            lorenz_para[i] = wealth_percent;
        }
        return lorenz_para;
    }

    public static void analyse(ArrayList<People> population) throws IOException {

        int lowCount = 0;
        int midCount = 0;
        int highCount = 0;
        int maxWealth = 0;

        //use predefined comparator to sort the arraylist
        Wealth_Comparator wc = new Wealth_Comparator();
        population.sort(wc);
        //set maxWealth
        maxWealth = population.get(population.size() - 1).getGrain();
        //
        for (People people : population) {
            WealthLevel wealthLevel = assessWealth(people, maxWealth);
            switch (wealthLevel) {
                case low:
                    ++lowCount;
                    // people.setWealthLevel(WealthLevel.low);
                    break;
                case mid:
                    ++midCount;
                    // people.setWealthLevel(WealthLevel.mid);
                    break;
                case high:
                    ++highCount;
                    // people.setWealthLevel(WealthLevel.high);
                    break;
            }
        }
        printClassData(lowCount, midCount, highCount);
        printLorenzData(population);
    }

    private static void printClassData(int lowCount, int midCount, int highCount)
            throws IOException {
        //open streams
        File file_class = new File("num_of_people_in_class.csv");
        try(FileOutputStream fos_class = new FileOutputStream(file_class)) {
            try(OutputStreamWriter osw_class = new OutputStreamWriter(fos_class)) {
                try(BufferedWriter bw_class = new BufferedWriter(osw_class)) {
                    //analyze dynamic data
                    bw_class.write(lowCount + "," + midCount + "," + highCount);
                    bw_class.newLine();
                }
            }
        }
    }

    private static void printLorenzData(ArrayList<People> population)
            throws IOException {
        //open streams
        File file_wealth = new File("wealth_of_people.csv");
        try(FileOutputStream fos_wealth = new FileOutputStream(file_wealth)) {
            try(OutputStreamWriter osw_wealth = new OutputStreamWriter(fos_wealth)) {
               try(BufferedWriter bw_wealth = new BufferedWriter(osw_wealth)) {
                   //print index line for lorenz curve
                   printIndexRow(bw_wealth);
                   //get lorenz data
                   double[] lorenz_data = Analyst.Lorenz(population);
                   //print y-axis data for lorenz curve
                   for (int k = 0; k < lorenz_data.length; k++) {
                       if (k != (lorenz_data.length - 1)) {
                           System.out.println(lorenz_data[k]);
                           bw_wealth.write(lorenz_data[k] + ",");
                       } else {
                           System.out.println(lorenz_data[k]);
                           bw_wealth.write(Double.toString(lorenz_data[k]));
                       }
                   }
                   bw_wealth.newLine();
               }
            }
        }
    }

    private static void printIndexRow(BufferedWriter bw_wealth) throws IOException {
        for (int j = 0; j <= 100; j++) {
            if (j != 100) {
                bw_wealth.write(j + ",");
            } else {
                bw_wealth.write(100);
            }
        }
        bw_wealth.newLine();
    }
}