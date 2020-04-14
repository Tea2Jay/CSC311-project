import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graph {
    // private int NumOfVertices;
    // private int[][] adjacencyMatrix;
    private List<Edge> edges;
    private int numOfVertices;
    Random rnd = new Random();

    public Graph(int numOfVertices) {
        edges = new ArrayList<Edge>();
        this.numOfVertices = numOfVertices;
        generateGraph(this.numOfVertices);
    }
    /** Generate random graph with the n vertices*/
    public void generateGraph(int n) {
        this.numOfVertices = n;
        int max = (numOfVertices * (numOfVertices - 1)) / 2;
        int min = numOfVertices - 1;
        int numberOfEdges = rnd.nextInt((max - min) + 1) + min;

    }


}
