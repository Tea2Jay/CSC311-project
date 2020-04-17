import java.util.PriorityQueue;
import java.util.Random;

public class Graph {
    private int numOfVertices;
    private Edge[][] adjacencyMatrix;
    private Random rnd = new Random(12345);
    private int numberOfEdges;

    /**
     * Generate random graph with the n vertices , n > 3
     * 
     * @param n number of vertices
     */
    public Graph(int n) {
        this.numOfVertices = n;
        adjacencyMatrix = new Edge[n][n];
        generateGraph(n);
    }

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
                Vertix v2 = new Vertix(j, x2, y2);
                adjacencyMatrix[i][j] = new Edge(v1, v2);
                adjacencyMatrix[j][i] = new Edge(v2, v1);
            }
        }
        // delete some edges but with constrains
        numberOfEdges = max;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i + 1; j < adjacencyMatrix.length; j++) {
                if (numberOfEdges <= max && numberOfEdges > min)
                    if (rnd.nextDouble() >= 0.5) {
                        adjacencyMatrix[i][j].setExsistInGraph(false);
                        adjacencyMatrix[j][i].setExsistInGraph(false);
                        numberOfEdges--;
                    }
            }
        }
    }

    /** print the matrix n*n */
    public void printMatrixW() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (i != j)
                    System.out.print(" " + adjacencyMatrix[i][j].getWeight() + " ");
                else {
                    System.out.print(" 0 ");
                }
            }
            System.out.println();
        }
    }

    public void printMatrixE() {
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

    /**
     * Apply Kruskal algorithm to the g graph and return minmum spannig tree graph
     * 
     * @return minmum spannig tree
     */
    public void applyKruskal() {
        PriorityQueue<Edge> pEdges = new PriorityQueue<Edge>(11,
                (e1, e2) -> (int) (e1.getWeight() - e2.getWeight()) * 100);

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i + 1; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j].isExsistInGraph()) {
                    pEdges.add(adjacencyMatrix[i][j]);
                    adjacencyMatrix[i][j].setExsistInGraph(false);
                    adjacencyMatrix[j][i].setExsistInGraph(false);
                }
            }
        }

        Edge[] tmp = new Edge[pEdges.size()];
        int count = 0;
        for (int i = 0; i < 2; i++) {
            tmp[count] = pEdges.poll();
            adjacencyMatrix[tmp[count].getv1().getName()][tmp[count].getv2().getName()].setExsistInGraph(true);
            adjacencyMatrix[tmp[count].getv2().getName()][tmp[count].getv1().getName()].setExsistInGraph(true);
            count++;
        }
        printMatrixE();
        while (!pEdges.isEmpty()) {
            tmp[count] = pEdges.poll();
            if (!isAddingMakeCycle(tmp)) {
                adjacencyMatrix[tmp[count].getv1().getName()][tmp[count].getv2().getName()].setExsistInGraph(true);
                adjacencyMatrix[tmp[count].getv2().getName()][tmp[count].getv1().getName()].setExsistInGraph(true);
            }
        }
    }

    private boolean isAddingMakeCycle(Edge[] e) {
        if (!isConnected(e))
            return false;
        int source = e[0].getv1().getName();
        int destination = e[0].getv2().getName();
        boolean flag = false;
        for (int i = 1; i < e.length; i++) {

        }
        return false;
    }

    private boolean isConnected(Edge[] e) {
        boolean flag = false;
        for (int i = 0; i < e.length; i++) {
            for (int j = i + 1; j < e.length; j++) {
                if ((e[i].getv1().getName() != e[j].getv1().getName()
                        || e[i].getv2().getName() != e[j].getv1().getName())
                        && (e[i].getv1().getName() != e[j].getv1().getName()
                                || e[i].getv2().getName() != e[i].getv2().getName())) {
                    flag = true;

                }
            }
        }
        return flag;
    }
}
