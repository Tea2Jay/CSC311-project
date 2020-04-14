import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graph {
    // private int NumOfVertices;
    // private int[][] adjacencyMatrix;
    private int numOfVertices;
    private List<ArrayList<Vertix>> adjList;
    private double[][] adjacencyMatrix;
    Random rnd = new Random();

    public Graph(int n) {
        adjList = new ArrayList<ArrayList<Vertix>>();
        this.numOfVertices = n;
        adjacencyMatrix = new double[n][n];
        generateGraph(numOfVertices);
    }

    /** Generate random graph with the n vertices */
    private void generateGraph(int n) {
        this.numOfVertices = n;
        int max = (numOfVertices * (numOfVertices - 1)) / 2; // complete graph
        int min = numOfVertices - 1; // least number of edges to be connected graph
        int numberOfEdges = rnd.nextInt((max - min) + 1) + min;

        // connect all vertices with each other to creat a connected graph
        for (int i = 0; i < numOfVertices; i++) {
            int x = rnd.nextInt(16);
            int y = rnd.nextInt(16);
            ArrayList<Vertix> tmp = new ArrayList<Vertix>();
            Vertix v =  new Vertix(i + "", x, y);

            tmp.add(v);
            adjList.add(tmp);
        }
        
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i+1; j < adjacencyMatrix.length; j++) {
                if(adjacencyMatrix[i][j]== 0.0){
                    adjacencyMatrix[i][j] = getWeight(adjList.get(i).get(0), adjList.get(j).get(0));
                    adjacencyMatrix[j][i] = adjacencyMatrix[i][j];
                }
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

    private double getWeight(Vertix v1, Vertix v2) {
        double tmp = Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2);
        double weight = Math.sqrt(tmp);
        return weight;
    }

}
