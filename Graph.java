import java.util.Random;

public class Graph {
    private int NumOfVertices;
    private int[][] adjacencyMatrix;

    public Graph(int n) {
        NumOfVertices = n;
        adjacencyMatrix = new int[n][n];
    }

    public int getWeight(int src, int dest) {
        return adjacencyMatrix[src][dest];
    }

    public boolean addEdge(int src, int dest, int weight) {
        if (src == dest)
            return false;
        adjacencyMatrix[src][dest] = weight;
        adjacencyMatrix[dest][src] = weight;
        return true;
    }

}