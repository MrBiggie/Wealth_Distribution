import java.io.*;
import java.util.ArrayList;

public class Analyst {

    static FileOutputStream fos_class;
    static OutputStreamWriter osw_class;
    static BufferedWriter bw_class;

    static FileOutputStream fos_wealth;
    static OutputStreamWriter osw_wealth;
    static BufferedWriter bw_wealth;

    public static void initFileStream(String classCsv, String wealthCsv) throws IOException {
        File file_class = new File(classCsv);
        fos_class = new FileOutputStream(file_class);
        osw_class = new OutputStreamWriter(fos_class);
        bw_class = new BufferedWriter(osw_class);

        File file_wealth = new File(wealthCsv);
        fos_wealth = new FileOutputStream(file_wealth);
        osw_wealth = new OutputStreamWriter(fos_wealth);
        bw_wealth = new BufferedWriter(osw_wealth);
        //print index line for lorenz curve
        printIndexRow(bw_wealth);
    }

    public static void clearFileStream() throws IOException {
        bw_class.close();
        osw_class.close();
        fos_class.close();

        bw_wealth.close();
        osw_wealth.close();
        bw_wealth.close();
    }

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

    public static double gini(double[] lorenz_data) {
        double gini_number = 0;
        for (double i : lorenz_data) {
            gini_number += i;
        }
        return ((lorenz_data.length / 2) - gini_number) / (lorenz_data.length / 2);
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
        //calculate class population
        for (People people : population) {
            WealthLevel wealthLevel = assessWealth(people, maxWealth);
            switch (wealthLevel) {
                case low:
                    ++lowCount;
                    people.setWealthLevel(WealthLevel.low);
                    break;
                case mid:
                    ++midCount;
                    people.setWealthLevel(WealthLevel.mid);
                    break;
                case high:
                    ++highCount;
                    people.setWealthLevel(WealthLevel.high);
                    break;
            }
        }
        printClassData(lowCount, midCount, highCount);
        // printLorenzData(population);
        printGiniData(Lorenz(population));
    }

    private static void printClassData(int lowCount, int midCount, int highCount)
            throws IOException {
        //analyze dynamic data
        bw_class.write(lowCount + "," + midCount + "," + highCount);
        bw_class.newLine();
    }

    private static void printLorenzData(ArrayList<People> population)
            throws IOException {
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

    private static void printGiniData(double[] lorenz_data) throws IOException {
        bw_wealth.write(Double.toString(gini(lorenz_data)));
        bw_wealth.write(",");
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