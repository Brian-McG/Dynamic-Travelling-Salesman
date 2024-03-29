import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TSP {

    private static final int cityShiftAmount = 60; //DO NOT CHANGE THIS.

    /**
     * How many cities to use.
     */
    protected static int cityCount;

    /**
     * How many chromosomes to use.
     */
    protected static int populationSize = 100; //DO NOT CHANGE THIS.

    /**
     * The part of the population eligable for mating.
     */
    protected static int matingPopulationSize;

    /**
     * The part of the population selected for mating.
     */
    protected static int selectedParents = 1;

    /**
     * The current generation
     */
    protected static int generation;

    /**
     * The list of cities (with current movement applied).
     */
    protected static City[] cities;

    /**
     * The list of cities that will be used to determine movement.
     */
    private static City[] originalCities;

    /**
     * The list of chromosomes.
     */
    protected static Chromosome[] chromosomes;

    /**
     * Frame to display cities and paths
     */
    private static JFrame frame;

    /**
     * Integers used for statistical data
     */
    private static double min;
    private static double avg;
    private static double max;
    private static double sum;
    private static double genMin;

    /**
     * Width and Height of City Map, DO NOT CHANGE THESE VALUES!
     */
    private static int width = 600;
    private static int height = 600;


    private static Panel statsArea;
    private static TextArea statsText;

    /**
     * Writing to an output file with the costs.
     */
    private static void writeLog(String content) {
        String filename = "results.out";
        FileWriter out;

        try {
            out = new FileWriter(filename, true);
            out.write(content + "\n");
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Deals with printing same content to System.out and GUI
     */
    private static void print(boolean guiEnabled, String content) {
        if (guiEnabled) {
            statsText.append(content + "\n");
        }

        System.out.println(content);
    }

    /**
     * Executes the evolutionary algorithm
     */
    public static void evolve() {
        // Sort Chromosomes based on cost (costs are updated after cities are moved)
        Chromosome.sortChromosomes(chromosomes, chromosomes.length);

        /* Generate up to 100 mutations while keeping population <= 100 and mutant chromosomes evaluated <= 100.
           Note: This process relies on two assumptions, both of which have been approved by Dr. Nitschke via email (which can be made available on request):
             1. We can swap in mutants for parents when they are generated.
             2. We can mutate a mutant that was generated in the current evolutionary process in this generation. */
        for (int i = 0; i < populationSize; ++i) {
            /* We kill off the worst chromosome (making the assumption it will be bad for every landscape)
               and use its index to hold the mutant chromosome (this ensures population <= 100). */
            chromosomes[populationSize - 1] = null;

            // Generate a mutated city ordering of the chromosome that is best suited for this landscape
            int[] mutatedCityOrdering = chromosomes[0].mutate();
            chromosomes[populationSize - 1] = new Chromosome(cities, mutatedCityOrdering);

            /* Evaluate the mutant chromosome.
               If we get a mutant that is better than the best chromosome for this current landscape, we swap it in.
               The reasoning behind this is that we know the landscape is dynamic, we therefore need one very good local optima for each landscape.
               The other, worse, chromosomes may become good in the future generations so we don't want to modify them too much.
               However, we again make the assumption that the last few chromosomes are poor in general, so we replace them with mutants that could be good
               for a similar landscape to this (this provides an additional reduction in average cost of between 50-100). */
            if (chromosomes[populationSize - 1].getCost() < chromosomes[0].getCost()) {
                chromosomes[0] = chromosomes[populationSize - 1];
            } else if (i != populationSize - 1) {
                for (int z = populationSize - 2; z >= populationSize - 3; --z) {
                    if (chromosomes[populationSize - 1].getCost() < chromosomes[z].getCost()) {
                        chromosomes[z] = chromosomes[populationSize - 1];
                        break;
                    }
                }
            }
        }
    }

    /**
     * Update the display
     */
    public static void updateGUI() {
        Image img = frame.createImage(width, height);
        Graphics g = img.getGraphics();
        FontMetrics fm = g.getFontMetrics();

        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        if (true && (cities != null)) {
            for (int i = 0; i < cityCount; i++) {
                int xpos = cities[i].getx();
                int ypos = cities[i].gety();
                g.setColor(Color.green);
                g.fillOval(xpos - 5, ypos - 5, 10, 10);

                //// SHOW Outline of movement boundary
                // xpos = originalCities[i].getx();
                // ypos = originalCities[i].gety();
                // g.setColor(Color.darkGray);
                // g.drawLine(xpos + cityShiftAmount, ypos, xpos, ypos + cityShiftAmount);
                // g.drawLine(xpos, ypos + cityShiftAmount, xpos - cityShiftAmount, ypos);
                // g.drawLine(xpos - cityShiftAmount, ypos, xpos, ypos - cityShiftAmount);
                // g.drawLine(xpos, ypos - cityShiftAmount, xpos + cityShiftAmount, ypos);
            }

            g.setColor(Color.gray);
            for (int i = 0; i < cityCount; i++) {
                int icity = chromosomes[0].getCity(i);
                if (i != 0) {
                    int last = chromosomes[0].getCity(i - 1);
                    g.drawLine(
                            cities[icity].getx(),
                            cities[icity].gety(),
                            cities[last].getx(),
                            cities[last].gety());
                }
            }

            int homeCity = chromosomes[0].getCity(0);
            int lastCity = chromosomes[0].getCity(cityCount - 1);

            //Drawing line returning home
            g.drawLine(
                    cities[homeCity].getx(),
                    cities[homeCity].gety(),
                    cities[lastCity].getx(),
                    cities[lastCity].gety());
        }
        frame.getGraphics().drawImage(img, 0, 0, frame);
    }

    private static City[] LoadCitiesFromFile(String filename, City[] citiesArray) {
        ArrayList<City> cities = new ArrayList<City>();
        try {
            FileReader inputFile = new FileReader(filename);
            BufferedReader bufferReader = new BufferedReader(inputFile);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                String[] coordinates = line.split(", ");
                cities.add(new City(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
            }

            bufferReader.close();

        } catch (Exception e) {
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }

        citiesArray = new City[cities.size()];
        return cities.toArray(citiesArray);
    }

    private static City[] MoveCities(City[] cities) {
        City[] newPositions = new City[cities.length];
        Random randomGenerator = new Random();

        for (int i = 0; i < cities.length; i++) {
            int x = cities[i].getx();
            int y = cities[i].gety();

            int position = randomGenerator.nextInt(5);

            if (position == 1) {
                y += cityShiftAmount;
            } else if (position == 2) {
                x += cityShiftAmount;
            } else if (position == 3) {
                y -= cityShiftAmount;
            } else if (position == 4) {
                x -= cityShiftAmount;
            }

            newPositions[i] = new City(x, y);
        }

        return newPositions;
    }

    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentTime = df.format(today);

        int runs;
        boolean display = false;
        String formatMessage = "Usage: java TSP 1 [gui] \n java TSP [Runs] [gui]";

        if (args.length < 1) {
            System.out.println("Please enter the arguments");
            System.out.println(formatMessage);
            display = false;
        } else {

            if (args.length > 1) {
                display = true;
            }

            try {
                cityCount = 50;
                populationSize = 100;
                runs = Integer.parseInt(args[0]);

                if (display) {
                    frame = new JFrame("Traveling Salesman");
                    statsArea = new Panel();

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setSize(width + 300, height);
                    frame.setResizable(false);
                    frame.setLayout(new BorderLayout());

                    statsText = new TextArea(35, 35);
                    statsText.setEditable(false);

                    statsArea.add(statsText);
                    frame.add(statsArea, BorderLayout.EAST);

                    frame.setVisible(true);
                }


                min = 0;
                avg = 0;
                max = 0;
                sum = 0;

                originalCities = cities = LoadCitiesFromFile("CityList.txt", cities);

                writeLog("Run Stats for experiment at: " + currentTime);
                for (int y = 1; y <= runs; y++) {
                    genMin = 0;
                    print(display, "Run " + y + "\n");

                    // create the initial population of chromosomes
                    chromosomes = new Chromosome[populationSize];
                    for (int x = 0; x < populationSize; x++) {
                        chromosomes[x] = new Chromosome(cities);
                    }

                    generation = 0;
                    double thisCost = 0.0;

                    while (generation < 100) {
                        evolve();
                        if (generation % 5 == 0) {
                            cities = MoveCities(originalCities); //Move from original cities, so they only move by a maximum of one unit.
                            /* Get an up-to-date cost of each chromosome
                               This should not be done in evolve function as it will give invalid costs,
                               as MoveCities is called after evolve thus the costs used at evolve are no longer valid */
                            for (Chromosome chromosome : chromosomes) {
                                chromosome.calculateCost(cities);
                            }
                        }
                        generation++;

                        Chromosome.sortChromosomes(chromosomes, populationSize);
                        double cost = chromosomes[0].getCost();
                        thisCost = cost;

                        if (thisCost < genMin || genMin == 0) {
                            genMin = thisCost;
                        }

                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setMinimumFractionDigits(2);
                        nf.setMinimumFractionDigits(2);

                        print(display, "Gen: " + generation + " Cost: " + (int) thisCost);

                        if (display) {
                            updateGUI();
                        }
                    }

                    writeLog(genMin + "");

                    if (genMin > max) {
                        max = genMin;
                    }

                    if (genMin < min || min == 0) {
                        min = genMin;
                    }

                    sum += genMin;

                    print(display, "");
                }

                avg = sum / runs;
                print(display, "Statistics after " + runs + " runs");
                print(display, "Solution found after " + generation + " generations." + "\n");
                print(display, "Statistics of minimum cost from each run \n");
                print(display, "Lowest: " + min + "\nAverage: " + avg + "\nHighest: " + max + "\n");

            } catch (NumberFormatException e) {
                System.out.println("Please ensure you enter integers for cities and population size");
                System.out.println(formatMessage);
            }
        }
    }
}