import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Graph {
    private Random rnd = new Random(7);// Random object to generate random vertices and edges
    private int grid; // the maxmimum X and Y for a point
    int numOfVertices, numberOfEdges; // number of edges and vertices in the graph
    Edge edge[]; // array of edges for kruskal algorithm
    double[][] adjMatrix; // adjacency matrix to represent the graph

    /**
     * Creates a graph with V vertices
     * 
     * @param numberOfVertices number of vertices.
     * @param grid             the maxmimum X and Y for a point
     */
    Graph(int numOfVertices, int grid) {
        this.numOfVertices = numOfVertices;

        int max = (numOfVertices * (numOfVertices - 1)) / 2;            // maxmimum number of edges in a graph
        int min = ((numOfVertices - 1) * (numOfVertices - 2) / 2) + 1;  // minimum number of edges to make the graph connected
        this.grid = grid;
        numberOfEdges = max;
        edge = new Edge[max];
        //initializing
        for (int i = 0; i < edge.length; i++) {
            edge[i] = new Edge();
        }
        int counter = 0; // counter for the array

        //generate random points then store them
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
        }
        //delete random edges
        for (int i = 0; i < numberOfEdges; i++) {
            if (numberOfEdges <= max && numberOfEdges > min)
                if (rnd.nextDouble() >= 0.5) {
                    edge[i] = edge[--numberOfEdges];
                }
        }
        //  transform the array to adjacency matrix 
        double[][] m = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < edge.length; i++) {
            m[edge[i].src.name][edge[i].dest.name] = edge[i].weight;
            m[edge[i].dest.name][edge[i].src.name] = edge[i].weight;
        }
        adjMatrix = m;

    }

    /**
     * Apply Christofides algorithm to the graph. and prints the path and the cost
     * 
     */
    void Christofides() {
        double[][] minmumSpaningTree = KruskalMST();// MST
        double[][] oddGraph = oddDgree(minmumSpaningTree);// oddGraph is the set of vertices with odd dgree in MST.
        double[][] minmumPerfectWeight = MWPM(oddGraph);// Minimum Weight Perfect Matching.
        double[][] eulerianMultigraph = Union(minmumSpaningTree, minmumPerfectWeight);// contsruct multigraph

        findPath(eulerianMultigraph);

    }

    /**
     * Apply krsukal algorithm to the graph
     * 
     * @return adjacency matrix
     */
    double[][] KruskalMST() {
        Edge[] result = new Edge[numOfVertices]; // create empty array to store the result later
        Edge[] sortedEdges = new Edge[numberOfEdges]; // create empty array to store the sorted edge
        Subset[] subsets = new Subset[numOfVertices]; // create array of subset to compare the parent of each subset
        // initializing array
        for (int i = 0; i < numOfVertices; ++i)
            result[i] = new Edge();

        // assigning the edge to sort it
        for (int j = 0; j < sortedEdges.length; j++) {
            sortedEdges[j] = edge[j];
        }

        Arrays.sort(sortedEdges);
        // initializing array
        for (int i = 0; i < numOfVertices; ++i)
            subsets[i] = new Subset();
        // make each vertix its own parent
        for (int v = 0; v < numOfVertices; ++v) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }
        int e = 0; // number of edges now
        int count = 0; // counter to travel in sortedEdges
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
        // transform the array result to adjacency matrix the retrun it
        double[][] mat = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < result.length; i++) {
            mat[result[i].src.name][result[i].dest.name] = result[i].weight;
            mat[result[i].dest.name][result[i].src.name] = result[i].weight;
        }
        return mat;

    }

    /**
     * 
     * @param matrix: adjacency matrix
     * @return complete graph with the odd vertices
     */
    double[][] oddDgree(double[][] matrix) {
        int[][] mat = calDegree(matrix); // calculate degree of each vertix
        Vertix[] vertcz = new Vertix[numOfVertices]; // array of verctices to store the odd dgrees
        int countVert = 0; // counter for the array
        // Store the odd degrees in vertcz
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < edge.length; j++) {
                if (mat[i][i] % 2 != 0)
                    if (edge[j].src.name == i && !exist(vertcz, countVert, i)) {
                        vertcz[countVert++] = new Vertix(edge[j].src.name, edge[j].src.x, edge[j].src.y);
                    } else if (edge[j].dest.name == i && !exist(vertcz, countVert, i)) {
                        vertcz[countVert++] = new Vertix(edge[j].dest.name, edge[j].dest.x, edge[j].dest.y);
                    }
            }

        }

        int complete = (countVert * (countVert - 1)) / 2; // maxmimum number of edges in a graph
        Edge[] edgz = new Edge[complete];                 // array of edges to connect the vertices
        int c = 0;                                        // counter for the array

        for (int i = 0; i < countVert; i++) {
            for (int j = i + 1; j < countVert; j++) {
                edgz[c++] = new Edge(vertcz[i], vertcz[j]);
            }
        }
        // transform the array result to adjacency matrix the retrun it
        double[][] m = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < edgz.length; i++) {
            m[edgz[i].src.name][edgz[i].dest.name] = edgz[i].weight;
            m[edgz[i].dest.name][edgz[i].src.name] = edgz[i].weight;
        }
        return m;
    }
    /**
     * 
     * @param oddGraph
     * @return the perfect minimum weight 
     */
    double[][] MWPM(double[][] oddGraph) {
        double[][] result = new double[numOfVertices][numOfVertices]; // matrix to store the result
        double[][] tmp = new double[numOfVertices][numOfVertices];    // tmp for changing any value without the fear of changing the real value
        int[] indecies = new int[2];                                  // the two vertices with the minmum weight
        int[] allvertx = new int[numOfVertices];                      //Array to store all the minimum vertices
        int count = 0;                                                // counter for array

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp.length; j++) {
                tmp[i][j] = oddGraph[i][j];
            }
        }
        for (int i = 0; i < allvertx.length; i++) {
            allvertx[i] = -1;
        }
        // find the minimum  edge. if one of the vertices is alredy in allVertices don't add it
        for (int i = 0; i < numOfVertices; i++) {
            indecies = findMin(tmp);
            if (!isExist(allvertx, indecies)) {
                for (int j = 0; j < 2; j++) {
                    allvertx[count++] = indecies[j];
                }
            }

        }

        //connect the edges found
        for (int i = 0; i < count; i = i + 2) {
            result[allvertx[i]][allvertx[i + 1]] = oddGraph[allvertx[i]][allvertx[i + 1]];
            result[allvertx[i + 1]][allvertx[i]] = oddGraph[allvertx[i + 1]][allvertx[i]];
        }
        return result;
    }
    /**
     * find the euler circut then hamiltonian circuit. print the path and the cost
     * @param eulerianMultigraph
     */
    void findPath(double[][] eulerianMultigraph) {
        int countEdges = calcNumEdges(eulerianMultigraph); //number of edges in the graph
        int i = 0;
        int j = 0;
        Queue<Integer> qPath = new LinkedList<Integer>();
        //find euler path and if the vertix exist dont add it. 
        while (countEdges > 0) {
            if (eulerianMultigraph[i][j] > 0.0 && eulerianMultigraph[i][j] < Math.sqrt(2) * grid) {
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
                //countEdges--;
                i = j;
            }
            j = ++j % numOfVertices;

        }
        qPath.add(0);//add the start vertix
        double cost = calCost(qPath);
        System.out.print(qPath.poll());
        while (!qPath.isEmpty())
            System.out.print(" --> " + qPath.poll());
        System.out.println("\ncost = " + cost);

    }
    /**
     * 
     * @param qPath queue of the vertices
     * @return the path cost
     */
    double calCost(Queue<Integer> qPath) {
        double cost = 0;
        Vertix[] vertcz = new Vertix[numOfVertices + 1]; // +1 for the start and the end vertix
        int count = 0;                                   // counter for the array
        int size = qPath.size();
        //poll the first index and get its (x,y) then create the vertix 
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
        vertcz[count++] = vertcz[0]; // adding the last as the first
        //sum the costs
        for (int i = 0; i < count; i += 2) {
            cost+= (new Edge(vertcz[i], vertcz[i+1])).weight;
        }

        return (Math.round(cost * 100.0) / 100.0);
    }

    /** union the givin graphs to form multigraph
     *  if duplicate exist add the longest possible edge (grid*sqrt(2)) to the duplicate 
     * @param minmumSpaningTree
     * @param minmumPerfectWeight
     * @return mulit graph
     */
    double[][] Union(double[][] minmumSpaningTree, double[][] minmumPerfectWeight) {
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

    private int calcNumEdges(double[][] eulerianMultigraph) {
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
        return result;
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

    /**
     * 
     * @param m matrix
     * @return the dgree of each vertix
     */
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
