import java.util.Arrays;
import java.util.Random;

public class Graph {
    Random rnd = new Random();
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

}