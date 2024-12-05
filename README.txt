# Monte Carlo Mini Programs

Welcome to the Monte Carlo Mini Programs repository! This repository contains three Java programs that utilize the Monte Carlo method for various tasks. Below, you'll find information about each program, its features, and how to use them.

## Program 1: MonteCarloMinimizationParallel

### Description
The `MonteCarloMinimizationParallel` program performs Monte Carlo searches on a terrain area to find the global minimum of a given function. It uses parallel processing to accelerate the search process.

### Features
- Parallelized Monte Carlo searches using Fork/Join framework
- Efficient minimization of a given function over a terrain area
- Utilizes multiple search tasks for improved performance

### Usage
1. Use the provided `Makefile` for compilation.
Compile: `make`
2.Navigate to the bin Folder:
After compiling your code, navigate to the bin directory using the terminal or command prompt. You can use the cd command to change the directory. For example:
		cd path/to/your/project/bin
3. Run the program: `java MonteCarloMini.MonteCarloMinimizationParallel <rows> <columns> <xmin> <xmax> <ymin> <ymax> <searches_density>`
4. Replace `<rows>`, `<columns>`, `<xmin>`, `<xmax>`, `<ymin>`, `<ymax>`, and `<searches_density>` with appropriate values.

## Program 2: SearchParallel

### Description
The `SearchParallel` program implements parallelized Monte Carlo searches to find valleys on a terrain area. It uses the Fork/Join framework to accelerate the search process.

### Features
- Parallelized Monte Carlo valley searches using Fork/Join framework
- Efficient identification of valleys on a given terrain area

### Usage
1. Use the provided `Makefile` for compilation.
Compile: `make`

## Program 3: TerrainArea

### Description
The `TerrainArea` program defines a class that represents a terrain area with height data. It includes methods to calculate terrain heights, perform searches, and display terrain-related information.

### Features
- Terrain area representation with height data
- Methods for evaluating terrain heights and performing searches
- Displaying height and visited data in a textual format

### Usage
1. Use the provided `Makefile` for compilation.
Compile: `make`


