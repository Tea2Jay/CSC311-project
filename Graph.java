import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graph {
    private int numOfVertices;
    private List<ArrayList<Vertix>> adjList;
    private Edge[][] adjacencyMatrix;
    Random rnd = new Random();

    public Graph(int n) {
        adjList = new ArrayList<ArrayList<Vertix>>();
        this.numOfVertices = n;
        adjacencyMatrix = new Edge[n][n];
        generateGraph(n);
    }

    /** Generate random graph with the n vertices , n > 3 */
    private void generateGraph(int n) {
        int max = (numOfVertices * (numOfVertices - 1)) / 2; // complete graph
        int min = ((numOfVertices - 1) * (numOfVertices - 2) / 2) + 1; // least number of edges to be connected graph

        // initialize the graph with random points and fully connected
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            int x1 = rnd.nextInt(16);
            int y1 = rnd.nextInt(16);
            Vertix v1 = new Vertix(i, x1, y1);
            for (int j = i + 1; j < adjacencyMatrix.length; j++) {
                int x2 = rnd.nextInt(16);
                int y2 = rnd.nextInt(16);
                Vertix v2 = new Vertix(i + 1, x2, y2);
                adjacencyMatrix[i][j] = new Edge(v1, v2);
                ;
                adjacencyMatrix[j][i] = adjacencyMatrix[i][j];
            }
        }
        // delete some edges but with constrains
        int count = max;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i + 1; j < adjacencyMatrix.length; j++) {
                if (count <= max && count >= min)
                    if (rnd.nextDouble() >= 0.5) {
                        adjacencyMatrix[i][j].setExsistInGraph(false);
                        adjacencyMatrix[j][i].setExsistInGraph(false);
                        count--;
                    }
            }
        }
    }
    /** print the matrix n*n  */
    public void printMatrix() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (i != j)
                    System.out.print(" " + adjacencyMatrix[i][j].isExsistInGraph() + " ");
                else {
                    System.out.print(" null ");
                }
            }
            System.out.println();
        }
    }
}
