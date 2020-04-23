import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Graph {
    Random rnd = new Random(7);
    int numOfVertices, numberOfEdges;
    Edge edge[];
    int grid;
    double[][] adjMatrix;

    // Creates a graph with V vertices and E edges
    Graph(int numOfVertices, int grid) {

        this.numOfVertices = numOfVertices;
        int max = (numOfVertices * (numOfVertices - 1)) / 2;
        int min = ((numOfVertices - 1) * (numOfVertices - 2) / 2) + 1;
        this.grid = grid;
        numberOfEdges = max;
        edge = new Edge[max];

        for (int i = 0; i < edge.length; i++) {
            edge[i] = new Edge();
        }
        int counter = 0;
        for (int i = 0; i < numOfVertices; i++) {
            int x1 = rnd.nextInt(grid + 1);
            int y1 = rnd.nextInt(grid + 1);
            Vertix v1 = new Vertix(i, x1, y1);
            for (int j = i + 1; j < numOfVertices; j++) {
                int x2 = rnd.nextInt(grid + 1);
                int y2 = rnd.nextInt(grid + 1);
                Vertix v2 = new Vertix(j, x2, y2);
                edge[counter++] = new Edge(v1, v2);
            }
            // System.out.println(edge[i].weight);
        }
        // System.out.println(edge.length);

        for (int i = 0; i < numberOfEdges; i++) {
            if (numberOfEdges <= max && numberOfEdges > min)
                if (rnd.nextDouble() >= 0.5) {
                    edge[i] = edge[--numberOfEdges];
                }
        }

        double[][] m = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < edge.length; i++) {
            m[edge[i].src.name][edge[i].dest.name] = edge[i].weight;
            m[edge[i].dest.name][edge[i].src.name] = edge[i].weight;
        }
        adjMatrix = m;
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
        double[][] mat = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < result.length; i++) {
            mat[result[i].src.name][result[i].dest.name] = result[i].weight;
            mat[result[i].dest.name][result[i].src.name] = result[i].weight;
        }
        return mat;

    }

    void Christofides() {
        double[][] minmumSpaningTree = KruskalMST();// MST
        double[][] oddGraph = oddDgree(minmumSpaningTree);// oddGraph is the set of vertices with odd dgree in MST.
        double[][] minmumPerfectWeight = MWPM(oddGraph);// Minimum Weight Perfect Matching.
        double[][] eulerianMultigraph = Union(minmumSpaningTree, minmumPerfectWeight);

        findPath(eulerianMultigraph);

    }

    private void findPath(double[][] eulerianMultigraph) {
        int countEdges = calcEdges(eulerianMultigraph);
        int i = 0;
        int j = 0;
        Queue<Integer> qPath = new LinkedList<Integer>();
        while (countEdges > 0) {
            if (eulerianMultigraph[i][j] > 0.0) {
                eulerianMultigraph[i][j] = 0.0;
                eulerianMultigraph[j][i] = 0.0;
                if (!qPath.contains(i))
                    qPath.add(i);
                countEdges--;
                i = j;
            } else if (eulerianMultigraph[i][j] > Math.sqrt(2) * grid) {
                eulerianMultigraph[i][j] -= Math.sqrt(2) * grid;
                eulerianMultigraph[j][i] -= Math.sqrt(2) * grid;
                if (!qPath.contains(i))
                    qPath.add(i);
                countEdges--;
                i = j;
            }
            j = ++j % numOfVertices;

        }
        qPath.add(0);
        double cost = calCost(qPath);
        System.out.print(qPath.poll());
        while (!qPath.isEmpty())
            System.out.print(" --> " + qPath.poll());
        System.out.println("\ncost = " + cost);

    }

    double calCost(Queue<Integer> qPath) {
        double cost = 0;
        Vertix[] vertcz = new Vertix[numOfVertices + 1];
        int count = 0;
        int size = qPath.size();
        while (size > 0) {
            int tmp = qPath.poll();
            for (int j = 0; j < edge.length; j++) {
                if (edge[j].src.name == tmp && !exist(vertcz, count, tmp)) {
                    vertcz[count++] = new Vertix(edge[j].src.name, edge[j].src.x, edge[j].src.y);
                } else if (edge[j].dest.name == tmp && !exist(vertcz, count, tmp)) {
                    vertcz[count++] = new Vertix(edge[j].dest.name, edge[j].dest.x, edge[j].dest.y);
                }
            }
            size--;
            qPath.add(tmp);
        }
        vertcz[count++] = vertcz[0];
        // for (int i = 0; i < count; i++) {
        // System.out.println(vertcz[i].name);
        // }

        Edge[] edgz = new Edge[count];
        for (int i = 0; i < edgz.length; i += 2) {
            edgz[i] = new Edge(vertcz[i], vertcz[i + 1]);
            cost += edgz[i].weight;
        }

        return (Math.round(cost * 100.0) / 100.0);
    }

    private int calcEdges(double[][] eulerianMultigraph) {
        int count = 0;
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if (eulerianMultigraph[i][j] > 0) {
                    count++;
                }
            }
        }
        return count / 2;
    }

    private double[][] Union(double[][] minmumSpaningTree, double[][] minmumPerfectWeight) {
        double[][] result = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = minmumSpaningTree[i][j];
                if (minmumPerfectWeight[i][j] != 0 && result[i][j] == 0)
                    result[i][j] = minmumPerfectWeight[i][j];
                else if (minmumPerfectWeight[i][j] != 0 && result[i][j] != 0)
                    result[i][j] += Math.round(Math.sqrt(2) * grid * 100) / 100;

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
        int[] kmnt = new int[numOfVertices];
        for (int i = 0; i < kmnt.length; i++) {
            kmnt[i] = -1;
        }
        int count = 0;
        for (int i = 0; i < numOfVertices; i++) {
            indecies = findMin(tmp);
            if (!isExist(kmnt, indecies)) {
                for (int j = 0; j < 2; j++) {
                    kmnt[count++] = indecies[j];
                }
            }

        }
        for (int i = 0; i < count; i = i + 2) {
            result[kmnt[i]][kmnt[i + 1]] = oddGraph[kmnt[i]][kmnt[i + 1]];
            result[kmnt[i + 1]][kmnt[i]] = oddGraph[kmnt[i + 1]][kmnt[i]];
        }
        return result;
    }

    private boolean isExist(int[] a1, int[] a2) {
        for (int i = 0; i < a1.length; i++) {
            for (int j = 0; j < a2.length; j++) {
                if (a1[i] == a2[j])
                    return true;
            }
        }

        return false;
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
        // System.out.println(result[0] + " " + result[1]);

        return result;
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

        int complete = (count * (count - 1)) / 2;
        Edge[] edgz = new Edge[complete];
        int c = 0;

        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                edgz[c++] = new Edge(vertcz[i], vertcz[j]);
            }
        }
        double[][] m = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < edgz.length; i++) {
            m[edgz[i].src.name][edgz[i].dest.name] = edgz[i].weight;
            m[edgz[i].dest.name][edgz[i].src.name] = edgz[i].weight;
        }
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
        System.out.println();
    }

    private void print(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.print(" " + m[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    void print(Edge[] edge) {
        for (int i = 0; i < edge.length; i++) {
            System.out.println(edge[i].weight);
        }

    }

}
