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
        for (int j = 0; j < numberOfEdges; j++) {
            System.out.println(edge[j].src.name + " " + edge[j].dest.name);
        }

    }

    void KruskalMST() {
        Edge[] result = new Edge[numOfVertices]; // This will store the resultant MST
        int e = 0; // An index variable, used for result[]
        int i = 0; // An index variable, used for sorted edges
        for (i = 0; i < numOfVertices; ++i)
            result[i] = new Edge();

        Edge[] sortedEdges = new Edge[numberOfEdges];
        for (int j = 0; j < sortedEdges.length; j++) {
            sortedEdges[j] = edge[j];
        }
        Arrays.sort(sortedEdges);
        // for (int j = 0; j < sortedEdges.length; j++) {
        // System.out.println(sortedEdges[j].src.name + " " + sortedEdges[j].dest.name);
        // }

        // Allocate memory for creating V subsets
        Subset subsets[] = new Subset[numOfVertices];
        for (i = 0; i < numOfVertices; ++i)
            subsets[i] = new Subset();

        // Create V subsets with single elements
        for (int v = 0; v < numOfVertices; ++v) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0; // Index used to pick next edge

        // Number of edges to be taken is equal to V-1
        while (e < numOfVertices - 1) {
            // Step 2: Pick the smallest edge. And increment
            // the index for next iteration
            Edge next_edge = new Edge();

            // System.out.println("i = " + ++i + " lengh = " + sortedEdges.length + " e = "
            // + e + " V=" + numOfVertices);
            next_edge = sortedEdges[i++];

            int x = find(subsets, next_edge.src.name);
            int y = find(subsets, next_edge.dest.name);

            // If including this edge does't cause cycle,
            // include it in result and increment the index
            // of result for next edge
            // System.out.println(x + " " + y);
            if (x != y) {
                result[e++] = next_edge;
                Union(subsets, x, y);
            }
            // Else discard the next_edge
        }

        // print the contents of result[] to display
        // the built MST
        System.out.println("Following are the edges in " + "the constructed MST");
        for (i = 0; i < e; ++i)
            System.out.println(result[i].src.name + " -- " + result[i].dest.name + " == " + result[i].weight);
    }

    int find(Subset[] subsets, int i) {
        // find root and make root as parent of i (path compression)
        if (subsets[i].parent != i)
            subsets[i].parent = find(subsets, subsets[i].parent);

        return subsets[i].parent;
    }

    void Union(Subset subsets[], int x, int y) {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        // Attach smaller rank tree under root of high rank tree
        // (Union by Rank)
        if (subsets[xroot].rank < subsets[yroot].rank)
            subsets[xroot].parent = yroot;
        else if (subsets[xroot].rank > subsets[yroot].rank)
            subsets[yroot].parent = xroot;

        // If ranks are same, then make one as root and increment
        // its rank by one
        else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

}