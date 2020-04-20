import java.util.Arrays;
import java.util.Random;

public class Graph {
    Random rnd = new Random(7);
    int numOfVertices, numberOfEdges;
    Edge edge[];

    // Creates a graph with V vertices and E edges
    Graph(int numOfVertices) {

        this.numOfVertices = numOfVertices;
        int max = (numOfVertices * (numOfVertices - 1)) / 2;
        int min = ((numOfVertices - 1) * (numOfVertices - 2) / 2) + 1;
        numberOfEdges = max;
        edge = new Edge[max];

        for (int i = 0; i < edge.length; i++) {
            edge[i] = new Edge();
        }
        int counter = 0;
        for (int i = 0; i < numOfVertices; i++) {
            int x1 = rnd.nextInt(16);
            int y1 = rnd.nextInt(16);
            Vertix v1 = new Vertix(i, x1, y1);
            for (int j = i + 1; j < numOfVertices; j++) {
                int x2 = rnd.nextInt(16);
                int y2 = rnd.nextInt(16);
                Vertix v2 = new Vertix(j, x2, y2);
                edge[counter++] = new Edge(v1, v2);
            }
            // System.out.println(edge[i].weight);
        }
        // System.out.println(edge.length);

        for (int i = 0; i < numberOfEdges; i++) {
            if (numberOfEdges <= max && numberOfEdges > min)
                if (rnd.nextDouble() >= 0.5) {
                    // System.out.println(edge.length);
                    // System.out.println("i = " + i + " E= " + numberOfEdges);
                    edge[i] = edge[--numberOfEdges];

                }
        }
        // for (int j = 0; j < numberOfEdges; j++) {
        // System.out.println(edge[j].src.name + " " + edge[j].dest.name);
        // }

    }

    double[][] KruskalMST() {

        Edge[] result = new Edge[numOfVertices];
        Edge[] sortedEdges = new Edge[numberOfEdges];
        Subset[] subsets = new Subset[numOfVertices];

        for (int i = 0; i < numOfVertices; ++i)
            result[i] = new Edge();

        for (int j = 0; j < sortedEdges.length; j++) {
            sortedEdges[j] = edge[j];
        }

        Arrays.sort(sortedEdges);

        for (int i = 0; i < numOfVertices; ++i)
            subsets[i] = new Subset();

        for (int v = 0; v < numOfVertices; ++v) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }
        int e = 0;
        int count = 0;
        while (e < numOfVertices - 1) {

            Edge next_edge = new Edge();
            next_edge = sortedEdges[count++];

            int x = find(subsets, next_edge.src.name);
            int y = find(subsets, next_edge.dest.name);

            if (x != y) {
                result[e++] = next_edge;
                Union(subsets, x, y);
            }

        }

        // System.out.println("Following are the edges in " + "the constructed MST");
        // for (i = 0; i < e; ++i)
        // System.out.println(result[i].src.name + " -- " + result[i].dest.name + " == "
        // + result[i].weight);
        double[][] mat = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < result.length; i++) {
            mat[result[i].src.name][result[i].dest.name] = result[i].weight;
            mat[result[i].dest.name][result[i].src.name] = result[i].weight;
        }

        // for (int j = 0; j < mat.length; j++) {
        // for (int j2 = 0; j2 < mat.length; j2++) {
        // System.out.print(" " + mat[j][j2] + " ");
        // }
        // System.out.println();
        // }

        return mat;

    }

    void Christofides() {
        double[][] minmumSpaningTree = KruskalMST();// MST
        double[][] oddGraph = oddDgree(minmumSpaningTree);// oddGraph is the set of vertices with odd dgree in MST.
        double[][] minmumPerfectWeight = MWPM(oddGraph);// Minimum Weight Perfect Matching.
        double[][] eulerianMultigraph = Union(minmumSpaningTree, minmumPerfectWeight);
        print(eulerianMultigraph);

    }

    private double[][] Union(double[][] minmumSpaningTree, double[][] minmumPerfectWeight) {
        double[][] result = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = minmumSpaningTree[i][j];
                if (minmumPerfectWeight[i][j] == 0)
                    result[i][j] = minmumPerfectWeight[i][j];

            }
        }

        return result;
    }

    private double[][] MWPM(double[][] oddGraph) {
        double[][] result = new double[numOfVertices][numOfVertices];
        double[][] tmp = new double[numOfVertices][numOfVertices];
        int[] indecies = new int[2];

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp.length; j++) {
                tmp[i][j] = oddGraph[i][j];
            }
        }
        // TODO Array to store all findMin but each time checks for existence
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                indecies = findMin(tmp);
                if (!findVertix(result, indecies[0], indecies[1])) {
                    System.out.println("h");
                    result[i][j] = tmp[i][j];
                    result[j][i] = tmp[i][j];
                }
            }
        }
        print(result);
        System.out.println();
        return result;
    }

    private int[] findMin(double[][] matrix) {
        int result[] = new int[2];
        double tmp = Double.MAX_VALUE;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                if (matrix[i][j] < tmp && matrix[i][j] > 0) {
                    tmp = matrix[i][j];
                    result[0] = i;
                    result[1] = j;

                }
            }
        }
        matrix[result[0]][result[1]] = Double.MAX_VALUE;
        System.out.println(result[0] + " " + result[1]);

        return result;
    }

    private boolean findVertix(double[][] result, int i, int j) {
        // System.out.println((int) result[i][j] == 0);
        for (int k = 0; k < result.length; k++) {
            if ((int) result[i][k] != 0 || (int) result[j][k] != 0) {
                System.out.println("dsajik");
                return true;
            }
        }
        return false;
    }

    private double[][] oddDgree(double[][] matrix) {
        int[][] mat = calDegree(matrix);
        Vertix[] vertcz = new Vertix[numOfVertices];
        int count = 0;
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < edge.length; j++) {
                if (mat[i][i] % 2 != 0)
                    if (edge[j].src.name == i && !exist(vertcz, count, i)) {
                        vertcz[count++] = new Vertix(edge[j].src.name, edge[j].src.x, edge[j].src.y);
                    } else if (edge[j].dest.name == i && !exist(vertcz, count, i)) {
                        vertcz[count++] = new Vertix(edge[j].dest.name, edge[j].dest.x, edge[j].dest.y);
                    }
            }

        }
        int complete = count * count - count / 2;
        Edge[] edgz = new Edge[complete];
        int c = 0;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                edgz[c++] = new Edge(vertcz[i], vertcz[j]);
            }
        }

        double[][] m = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < numOfVertices; i++) {
            m[edgz[i].src.name][edgz[i].dest.name] = edgz[i].weight;
            m[edgz[i].dest.name][edgz[i].src.name] = edgz[i].weight;
        }
        print(m);
        System.out.println();
        return m;
    }

    private boolean exist(Vertix[] vertcz, int count, int i) {
        for (int j = 0; j < count; j++) {
            if (vertcz[j].name == i)
                return true;
        }
        return false;
    }

    int find(Subset[] subsets, int i) {

        if (subsets[i].parent != i)
            subsets[i].parent = find(subsets, subsets[i].parent);

        return subsets[i].parent;
    }

    void Union(Subset subsets[], int x, int y) {
        if (subsets[x].rank < subsets[y].rank)
            subsets[x].parent = y;
        else if (subsets[x].rank > subsets[y].rank)
            subsets[y].parent = x;
        else {
            subsets[y].parent = x;
            subsets[x].rank++;
        }
    }

    private int[][] calDegree(double[][] m) {
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

    private void print(double[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.print(" " + m[i][j] + " ");
            }
            System.out.println();
        }
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