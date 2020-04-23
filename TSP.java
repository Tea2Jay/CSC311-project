
import java.util.ArrayList;
import java.util.List;

public class TSP {
	// Distance lookup table
	private static double[][] distances ;

	// Generic variables
	// Populate a list with the cities
	private static List<City> cities;

	// Brute force (BF) variables
	private static List<Route> BFRoutePerms = new ArrayList<Route>();
	private static double BFcheapestCost = Double.MAX_VALUE;
	private static Route BFcheapestRoute;

	// Branch and bound (BaB) variables
	private static List<Route> BaBRoutePerms = new ArrayList<Route>();
	private static double BaBcheapestCost = Double.MAX_VALUE;
	private static Route BaBcheapestRoute;

	/**
	 * Main function
	 * 
	 * @param args
	 */
	// public static void main(String[] args) {
	// 	// Used to calculate average execution times
	// 	long time1 = 0;
	// 	long time2 = 0;
	// 	long time3 = 0;
	// 	// Used to determine number of times the three algorithms should run
	// 	int numIterations = 1;

	// 	// Only individual algorithms should be run during profiling
	// 	for (int i = 0; i < numIterations; i++) {
	// 		long time = System.currentTimeMillis();
	// 		// Run brute force
	// 		bruteForce();
	// 		System.out.println("\tTime:" + (System.currentTimeMillis() - time) + "ms");
	// 		time1 += System.currentTimeMillis() - time;

	// 		time = System.currentTimeMillis();
			// // Run nearest neighbour
			// nearestNeighbour();
			// System.out.println("\tTime:" + (System.currentTimeMillis() - time) + "ms");
			// time2 += System.currentTimeMillis() - time;

			// time = System.currentTimeMillis();
			// // Run branch and bound
			// branchAndBound();
			// System.out.println("\tTime:" + (System.currentTimeMillis() - time) + "ms");
			// time3 += System.currentTimeMillis() - time;
		

		// Output average time for functions
		// System.out.println("\n\tBF:" + time1 / numIterations + "ms");

		// Output rough memory usage (profiler is more accurate)
		// System.out.println(
		// "KB: " + (double) (Runtime.getRuntime().totalMemory() -
		// Runtime.getRuntime().freeMemory()) / 1024);
	

	/************************************************************************************************************/
	/**
	 * Calculate the shortest route using the brute force algorithm
	 */
	public static void bruteForce() {
		System.out.println("bruteForce:");
		// Setup city list
		resetLists();

		// Remove stoke from permutations as always start and end
		List<Integer> cityNums = new ArrayList<Integer>();
		for (int i = 0; i < distances.length - 1; i++) {
			cityNums.add(i);
		}

		// Calculate
		permute(new Route(), cityNums, true);
		// Output the number of permutations generated
		System.out.println("\tComplete Permutations: " + BFRoutePerms.size());
		findShortestPermutation(BFRoutePerms);
	}

	/**
	 *
	 */
	private static void resetLists() {
		BFRoutePerms = new ArrayList<Route>();
		BaBRoutePerms = new ArrayList<Route>();

		cities = new ArrayList<City>();

		// Populate City list
		// cities.add(new City("0", 0, false));
		// cities.add(new City("1", 1, false));
		// cities.add(new City("2", 2, false));
		// cities.add(new City("3", 3, false));
		// cities.add(new City("4", 4, false));
		// cities.add(new City("5", 5, false));
		// cities.add(new City("6", 6, false));
		// cities.add(new City("7", 7, false));
		// cities.add(new City("8", 8, true));
		int i = 0;
		for (i = 0; i < distances.length - 1; i++) {
			cities.add(new City(Integer.toString(i), i, false));
		}
		cities.add(new City(Integer.toString(i), i, true));
	}

	/**
	 * Generates all permutations in lexicographic order
	 * 
	 * @param r
	 * @param notVisited
	 */
	private static void permute(Route r, List<Integer> notVisited, boolean isBruteForce) {
		if (!notVisited.isEmpty()) {

			for (int i = 0; i < notVisited.size(); i++) {
				// Pointer to first city in list
				int temp = notVisited.remove(0);

				Route newRoute = new Route();
				// Lazy copy
				for (City c1 : r.getRoute()) {
					newRoute.getRoute().add(c1);
				}

				// Add the first city from notVisited to the route
				newRoute.getRoute().add(cities.get(temp));

				if (isBruteForce) {
					// Recursive call
					permute(newRoute, notVisited, true);
				} else {
					// If a complete route has not yet been created keep permuting
					if (BaBRoutePerms.isEmpty()) {
						// Recursive call
						permute(newRoute, notVisited, false);
					} else if (getRouteCost(newRoute) < BaBcheapestCost) {
						// Current route cost is less than the best so far so keep permuting
						permute(newRoute, notVisited, false);
					}
				}
				// Add first city back into notVisited list
				notVisited.add(temp);
			}
		} else {
			// Route is complete
			if (isBruteForce) {
				BFRoutePerms.add(r);
			} else {
				// Add stoke to start and end of route
				r.getRoute().add(0, cities.get(distances.length - 1));
				r.getRoute().add(cities.get(distances.length - 1));

				BaBRoutePerms.add(r);

				// If shorter than best so far, update best cost
				if (getRouteCost(r) < BaBcheapestCost) {
					BaBcheapestRoute = r;
					BaBcheapestCost = getRouteCost(r);
				}
			}
		}
	}

	/**
	 * Gets the cost of all the routes in the list and outputs the cheapest
	 * 
	 * @param routeList
	 */
	private static void findShortestPermutation(List<Route> routeList) {
		// Loop through all the permutations
		for (Route r : routeList) {
			// Only used by brute force so add stoke to start and end of route
			appendStoke(r);

			if (getRouteCost(r) < BFcheapestCost) {
				BFcheapestCost = getRouteCost(r);
				BFcheapestRoute = r;
			}
		}

		System.out.println("\t" + BFcheapestRoute.toString() + "\n\tCost: " + BFcheapestCost);
	}

	/**
	 * Adds stoke to start and finish of route
	 * 
	 * @param r route
	 */
	private static void appendStoke(Route r) {
		r.getRoute().add(0, cities.get(distances.length - 1));
		r.getRoute().add(cities.get(distances.length - 1));
	}

	/**
	 * Gets the cost of traveling between the cities in the route
	 * 
	 * @param r
	 * @return tempCost
	 */
	private static Double getRouteCost(Route r) {
		double tempCost = 0;
		// Add route costs
		for (int i = 0; i < r.getRoute().size() - 1; i++) {
			tempCost += distances[r.getRoute().get(i).getID()][r.getRoute().get(i + 1).getID()];
		}
		return tempCost;
	}

	public void setDistances(double[][] d) {
		this.distances = d;
	}
}
