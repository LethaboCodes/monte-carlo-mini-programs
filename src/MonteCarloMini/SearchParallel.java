package MonteCarloMini;

import java.util.concurrent.RecursiveTask;

public class SearchParallel extends RecursiveTask<SearchParallel.SearchResult> {
    private int id;
    private int pos_row, pos_col;
    private int steps;
    private boolean stopped;

    private static final int SEQUENTIAL_CUTOFF = 500;
    private SearchParallel[] searches;
    private int start;
    private int end;
    private TerrainArea terrain;

    private int globalMin = Integer.MAX_VALUE;
    private int finder = -1;

    enum Direction {
        STAY_HERE,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    // Constructor for individual search task
    public SearchParallel(int id, int pos_row, int pos_col, TerrainArea terrain) {
        this.id = id;
        this.pos_row = pos_row;
        this.pos_col = pos_col;
        this.terrain = terrain;
        this.stopped = false;
    }

    // Constructor for splitting and merging tasks
    public SearchParallel(SearchParallel[] searches, int start, int end, TerrainArea terrain) {
        this.searches = searches;
        this.start = start;
        this.end = end;
        this.terrain = terrain;
    }

    // Find valleys starting from current position
    public int findValleys() {
        int height = Integer.MAX_VALUE;
        Direction next = Direction.STAY_HERE;
        while (terrain.visited(pos_row, pos_col) == 0) {
            height = terrain.get_height(pos_row, pos_col);
            terrain.mark_visited(pos_row, pos_col, id);
            steps++;
            next = terrain.next_step(pos_row, pos_col);
            switch (next) {
                case STAY_HERE:
                    return height;
                case LEFT:
                    pos_row--;
                    break;
                case RIGHT:
                    pos_row++;
                    break;
                case UP:
                    pos_col--;
                    break;
                case DOWN:
                    pos_col++;
                    break;
            }
        }
        stopped = true;
        return height;
    }

    @Override
    protected SearchResult compute() {
        if (end - start <= SEQUENTIAL_CUTOFF) {
            int localMin = Integer.MAX_VALUE;
            int localFinder = -1;

            // Perform searches individually within the threshold
            for (int i = start; i < end; i++) {
                int currentMin = searches[i].findValleys();
                if ((!searches[i].isStopped()) && (currentMin < localMin)) {
                    localMin = currentMin;
                    localFinder = i;
                }
            }

            return new SearchResult(localMin, localFinder);
        } else {
            // Split task into smaller subtasks and merge results
            int mid = (start + end) / 2;
            SearchParallel leftTask = new SearchParallel(searches, start, mid, terrain);
            SearchParallel rightTask = new SearchParallel(searches, mid, end, terrain);

            leftTask.fork();
            SearchResult rightResult = rightTask.compute();
            SearchResult leftResult = leftTask.join();

            // Determine the minimum and the corresponding finder
            int min = Math.min(leftResult.getMin(), rightResult.getMin());
            int finder = (leftResult.getMin() < rightResult.getMin()) ? leftResult.getFinder() : rightResult.getFinder();
            return new SearchResult(min, finder);
        }
    }

    // Getter methods for various attributes
    public int getGlobalMin() {
        return globalMin;
    }

    public int getFinder() {
        return finder;
    }

    public int getId() {
        return id;
    }

    public int getPos_row() {
        return pos_row;
    }

    public int getPos_col() {
        return pos_col;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isStopped() {
        return stopped;
    }

    // Nested class to store search results
    public static class SearchResult {
        private final int min;
        private final int finder;

        public SearchResult(int min, int finder) {
            this.min = min;
            this.finder = finder;
        }

        public int getMin() {
            return min;
        }

        public int getFinder() {
            return finder;
        }
    }
}
