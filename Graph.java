import java.util.PriorityQueue;
import java.util.Random;

public class Graph {
    private int numOfVertices;
    private Edge[][] adjacencyMatrix;
    private Random rnd = new Random();
    private int numberOfEdges;
    private int[][] mat;

    /**
     * Generate random graph with the n vertices , n > 3
     * 
     * @param n number of vertices
     */
    public Graph(int n) {
        this.numOfVertices = n;
        adjacencyMatrix = new Edge[n][n];
        mat = new int[n][n];
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
    public void printMatrixWeightExist() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (i != j)
                    System.out.print(
                            " " + adjacencyMatrix[i][j].getWeight() + "-" + adjacencyMatrix[i][j].isExsistInGraph());
                else {
                    System.out.print(" 0-false ");
                }
            }
            System.out.println();
        }
    }

    public void printMatrixExist() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (i != j)
                    System.out.print(" " + adjacencyMatrix[i][j].isExsistInGraph() + " ");
                else {
                    System.out.print(" 0 ");
                }
            }
            System.out.println();
        }
    }

    public void printMatrixMAT() {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (i != j)
                    System.out.print(" " + mat[i][j] + " ");
                else {
                    System.out.print(" 0 ");
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
                (e1, e2) -> (int) (e1.getWeight() * 100 - e2.getWeight() * 100));
        // printMatrixWeightExist();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i + 1; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j].isExsistInGraph()) {
                    pEdges.add(adjacencyMatrix[i][j]);
                    adjacencyMatrix[i][j].setExsistInGraph(false);
                    adjacencyMatrix[j][i].setExsistInGraph(false);
                }
            }
        }
        // while (!pEdges.isEmpty())
        // System.out.println(pEdges.poll().getWeight());

        Edge[] tmp = new Edge[pEdges.size()];
        int count = 0;
        // for (int i = 0; i < 2; i++) {
        // tmp[count] = pEdges.poll();
        // int v1 = tmp[count].getv1().getName();
        // int v2 = tmp[count].getv2().getName();
        // adjacencyMatrix[v1][v2].setExsistInGraph(true);
        // adjacencyMatrix[v2][v1].setExsistInGraph(true);
        // mat[v1][v2] = 1;
        // mat[v2][v1] = 1;
        // count++;
        // System.out.println(v1+" added "+v2);
        // }
        while (!pEdges.isEmpty()) {
            tmp[count] = pEdges.poll();
            int v1 = tmp[count].getv1().getName();
            int v2 = tmp[count].getv2().getName();
            adjacencyMatrix[v1][v2].setExsistInGraph(true);
            adjacencyMatrix[v2][v1].setExsistInGraph(true);
            mat[v1][v2] = 1;
            mat[v2][v1] = 1;
            count++;
            // System.out.println(v1 + " added " + v2);
            if (isAddingMakeCycle(count)) {
                adjacencyMatrix[v1][v2].setExsistInGraph(false);
                adjacencyMatrix[v2][v1].setExsistInGraph(false);
                mat[v1][v2] = 0;
                mat[v2][v1] = 0;
                count--;
                // System.out.println(v1 + " deleted " + v2);
                System.out.println(count);
            }
        }
        printMatrixMAT();
        System.out.println(count);
    }

    private boolean isAddingMakeCycle(int count) {
        int[][] degMat = calDegree(mat);
        int[][] L = minusMatrix(degMat, mat);
        // print(L);
        int trace = getTrace(L);
        int rank = rankOfMatrix(L);
        System.out.println("trace = " + trace + " rank = " + rank);
        if (trace >= 2 * (rank + 1))
            return true;

        return false;
    }

    private int rankOfMatrix(int mat[][]) {
        int rank = mat.length - 1;
        for (int row = 0; row < rank; row++) {
            if (mat[row][row] != 0) {
                for (int col = 0; col < mat.length - 1; col++) {
                    if (col != row) {
                        double mult = (double) mat[col][row] / mat[row][row];
                        for (int i = 0; i < rank; i++)
                            mat[col][i] -= mult * mat[row][i];
                    }
                }
            } else {
                boolean reduce = true;
                for (int i = row + 1; i < mat.length - 1; i++) {
                    if (mat[i][row] != 0) {
                        swap(mat, row, i, rank);
                        reduce = false;
                        break;
                    }
                }
                if (reduce) {
                    rank--;
                    for (int i = 0; i < mat.length - 1; i++)
                        mat[i][row] = mat[i][rank];
                }
                row--;
            }
        }
        return rank;
    }

    private void swap(int mat[][], int row1, int row2, int col) {
        for (int i = 0; i < col; i++) {
            int temp = mat[row1][i];
            mat[row1][i] = mat[row2][i];
            mat[row2][i] = temp;
        }
    }

    private int[][] calDegree(int[][] m) {
        int[][] t = new int[m.length][m.length];
        for (int i = 0; i < m.length; i++) {
            int deg = 0;
            for (int j = 0; j < m.length; j++) {
                if (m[i][j] != 0) {
                    t[i][i] = ++deg;
                }
            }

        }
        return t;
    }

    private int[][] minusMatrix(int[][] m1, int[][] m2) {
        int[][] result = new int[m1.length][m2.length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = m1[i][j] - m2[i][j];
            }
        }

        return result;
    }

    private int getTrace(int[][] l) {
        int sum = 0;
        for (int i = 0; i < l.length; i++) {
            sum += l[i][i];
        }
        return sum;
    }

    private void print(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.print(" " + m[i][j] + " ");
            }
            System.out.println();
        }
    }
}
