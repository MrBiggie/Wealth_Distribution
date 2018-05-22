/**
 * Haoyuan Tang 809040
 * Shuyuan Dang 840992
 */

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Simulation implements ITicker {
    static Simulation simulation;

    Field field;

    ArrayList<People> population;


    public static void main(String[] args) throws InterruptedException, IOException {
        simulation = new Simulation();
        simulation.simulate();
    }

    /**
     * Simulate the model
     * @throws InterruptedException
     * @throws IOException
     */
    public void simulate() throws InterruptedException, IOException {
        //fields
        field = new Field(Parameters.FIELD_WIDTH, Parameters.FIELD_HEIGHT);
        population = new ArrayList<>();

        initPopulation();

        String classCsv = "num_of_people_in_class.csv";
        String wealthCsv = "wealth_of_people.csv";
        Analyst.initFileStream(classCsv, wealthCsv);

        for (int i = 0; i< Parameters.NUM_OF_TICKS; ++i) {
            Thread.sleep(Parameters.TICK_INTERVAL);
            tick();
        }
        Analyst.clearFileStream();
    }

    /**
     * randomly distribute the init population
     */
    public void initPopulation() {
        Random random = new Random();
        //initialize people on the field
        for (int n = 0; n < Parameters.NUM_OF_PEOPLE; n++) {
            People people = new People(field,
                    (Parameters.LIFE_EXPECTANCY_MIN +
                            random.nextInt(Parameters.LIFE_EXPECTANCY_MAX -
                                    Parameters.LIFE_EXPECTANCY_MIN)),
                    random.nextInt(Parameters.METABOLISM_MAX),
                    1 + random.nextInt(Parameters.MAX_VISION),
                    0,
                    random.nextInt(Parameters.MAX_WEALTH));
            int x = random.nextInt(Parameters.FIELD_WIDTH);
            int y = random.nextInt(Parameters.FIELD_HEIGHT);
            people.setLocation(new Location(x, y));
            population.add(people);
        }
    }

    @Override
    public void tick() {
        field.tick();
        for (People people : population) {
            people.tick();
        }
        try {
            Analyst.analyse(population);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}