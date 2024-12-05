package MonteCarloMini;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import MonteCarloMini.SearchParallel.SearchResult;

class MonteCarloMinimizationParallel {
    static final boolean DEBUG = false;

    static long startTime = 0;
    static long endTime = 0;

    // Timer functions - record start and end times in milliseconds
    private static void tick() {
        startTime = System.currentTimeMillis();
    }

    private static void tock() {
        endTime = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        int rows, columns; // Dimensions of the grid
        double xmin, xmax, ymin, ymax; // Limits for x and y terrain
        TerrainArea terrain;  // Object to store terrain heights and grid points visited by searches
        double searches_density;    // Density - number of Monte Carlo searches per grid position - usually less than 1!
        
        int num_searches;        // Number of searches
        SearchParallel[] searches;        // Array of search tasks
        Random rand = new Random();  // Random number generator
        
        if (args.length != 7) {  
            System.out.println("Incorrect number of command line arguments provided.");      
            System.exit(0);
        }
        
        // Parse command line arguments
        rows = Integer.parseInt(args[0]);
        columns = Integer.parseInt(args[1]);
        xmin = Double.parseDouble(args[2]);
        xmax = Double.parseDouble(args[3]);
        ymin = Double.parseDouble(args[4]);
        ymax = Double.parseDouble(args[5]);
        searches_density = Double.parseDouble(args[6]);     
  
        if (DEBUG) {
            /* Print command line arguments */
            System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
            System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax );
            System.out.printf("Arguments, searches_density: %f\n", searches_density );
            System.out.printf("\n");
        }
        
        // Initialize terrain and search parameters
        terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
        num_searches = (int) (rows * columns * searches_density);
        searches = new SearchParallel[num_searches];
        for (int i = 0; i < num_searches; i++)
            searches[i] = new SearchParallel(i + 1, rand.nextInt(rows), rand.nextInt(columns), terrain);
        
        if (DEBUG) {
            /* Print initial values */
            System.out.printf("Number searches: %d\n", num_searches);
            terrain.print_heights();
        }
        
        // Start timer
        tick();
        
        try (// Perform searches using Fork/Join
        ForkJoinPool forkJoinPool = new ForkJoinPool()) {
            SearchParallel mainTask = new SearchParallel(searches, 0, num_searches, terrain);
            
            SearchResult result = forkJoinPool.invoke(mainTask);
            int min = result.getMin();
            int finder = result.getFinder();
        
            // End timer
            tock();
            
            if (DEBUG) {
                /* Print final state */
                terrain.print_heights();
                terrain.print_visited();
            }

            // Display run parameters and results
            System.out.printf("Run parameters\n");
            System.out.printf("\t Rows: %d, Columns: %d\n", rows, columns);
            System.out.printf("\t x: [%f, %f], y: [%f, %f]\n", xmin, xmax, ymin, ymax );
            System.out.printf("\t Search density: %f (%d searches)\n", searches_density, num_searches );
            
            /* Calculate and display computation time */
            System.out.printf("Time: %d ms\n", endTime - startTime );
            int tmp = terrain.getGrid_points_visited();
            System.out.printf("Grid points visited: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");
            tmp = terrain.getGrid_points_evaluated();
            System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");
            
            /* Display results */
            System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n", min, terrain.getXcoord(searches[finder].getPos_row()), terrain.getYcoord(searches[finder].getPos_col()));
        }
    }
}
