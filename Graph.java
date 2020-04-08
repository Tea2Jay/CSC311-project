import java.util.Random;

public class Graph {
    private int NumOfVertices;
    private int[][] adjacencyMatrix;
    private Random r;

    public Graph(int n) {
        NumOfVertices = n;
        adjacencyMatrix = new int[n][n];
        r = new Random();
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

    public boolean removeEdge(int src, int dest) {
        adjacencyMatrix[src][dest] = 0;
        return true;
    }

    public void createRandomGraph() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i+1; j < adjacencyMatrix.length; j++) {
                adjacencyMatrix[i][j] = r.nextInt(19);
            }
        }
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i+1; j < adjacencyMatrix.length; j++) {
                adjacencyMatrix[j][i] = adjacencyMatrix[i][j]; 
            }
        }
    }

    public void printMatrix(){
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                System.out.print(adjacencyMatrix[i][j]+" ");
            }
            System.out.println();
        }
    }

}