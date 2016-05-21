import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Chromosome {

    /**
     * The list of cities, which are the genes of this chromosome.
     */
    protected int[] cityList;

    /**
     * The cost of following the cityList order of this chromosome.
     */
    protected double cost = -1;

    /**
     * An instantiation of
     */
    protected Random random;

    /**
     * @param cities The order that this chromosome would visit the cities.
     */
    Chromosome(City[] cities) {
        random = new Random();
        cityList = new int[cities.length];
        //cities are visited based on the order of an integer representation [o,n] of each of the n cities.
        for (int x = 0; x < cities.length; x++) {
            cityList[x] = x;
        }

        //shuffle the order so we have a random initial order
        for (int y = 0; y < cityList.length; y++) {
            int temp = cityList[y];
            int randomNum = random.nextInt(cityList.length);
            cityList[y] = cityList[randomNum];
            cityList[randomNum] = temp;
        }

        calculateCost(cities);
    }

    Chromosome(City[] cities, int[] ordering) {
        random = new Random();

        cityList = new int[cities.length];
        for (int x = 0; x < cities.length; x++) {
            cityList[x] = ordering[x];
        }

        calculateCost(cities);
    }


    /**
     * Mutates the Chromosome using inversion
     *
     * Randomly generates two different indices and then inverts the ordering of visiting those cities
     */
    public int[] mutate() {
        if(cityList.length <= 1) {
            int[] copy = new int[cityList.length];
            System.arraycopy( cityList.clone(), 0, copy, 0, cityList.length );
            return copy;
        }

        int x1 = random.nextInt(cityList.length);
        int x2;
        do {
            x2 = random.nextInt(cityList.length);
        } while(x2 == x1);

        if (x1 > x2) {
            int tmp = x2;
            x2 = x1;
            x1 = tmp;
        }

        int[] childChromosome = new int[cityList.length];
        System.arraycopy( cityList, 0, childChromosome, 0, cityList.length );
        int g = x1;
        for (int i = x2; i >= x1; --i, ++g) {
            childChromosome[g] = cityList[i];
        }
        return childChromosome;
    }

    /**
     * Calculate the cost of the specified list of cities.
     *
     * @param cities A list of cities.
     */
    void calculateCost(City[] cities) {
        cost = 0;
        for (int i = 0; i < cityList.length - 1; i++) {
            double dist = cities[cityList[i]].proximity(cities[cityList[i + 1]]);
            cost += dist;
        }

        cost += cities[cityList[0]].proximity(cities[cityList[cityList.length - 1]]); //Adding return home
    }

    /**
     * Get the cost for this chromosome. This is the amount of distance that
     * must be traveled.
     */
    double getCost() {
        return cost;
    }

    /**
     * @param i The city you want.
     * @return The ith city.
     */
    int getCity(int i) {
        return cityList[i];
    }

    /**
     * Set the order of cities that this chromosome would visit.
     *
     * @param list A list of cities.
     */
    void setCities(int[] list) {
        for (int i = 0; i < cityList.length; i++) {
            cityList[i] = list[i];
        }
    }

    /**
     * Set the index'th city in the city list.
     *
     * @param index The city index to change
     * @param value The city number to place into the index.
     */
    void setCity(int index, int value) {
        cityList[index] = value;
    }

    /**
     * Sort the chromosomes by their cost.
     *
     * @param chromosomes An array of chromosomes to sort.
     * @param num         How much of the chromosome list to sort.
     */
    public static void sortChromosomes(Chromosome chromosomes[], int num) {
        Chromosome ctemp;
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < num - 1; i++) {
                if (chromosomes[i].getCost() > chromosomes[i + 1].getCost()) {
                    ctemp = chromosomes[i];
                    chromosomes[i] = chromosomes[i + 1];
                    chromosomes[i + 1] = ctemp;
                    swapped = true;
                }
            }
        }
    }
}
