
public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(11, 15);
        TSP tsp = new TSP();
        tsp.setDistances(g.adjMatrix);
        tsp.bruteForce();
        g.Christofides();
    }

}