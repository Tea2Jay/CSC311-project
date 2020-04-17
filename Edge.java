
public class Edge {
    private Vertix v1;
    private Vertix v2;
    private double weight;
    private boolean exsistInGraph;

    /**
     * Enter Vertix v1 , b to create an edge between them
     * 
     */
    public Edge(Vertix v1, Vertix v2) {
        this.v1 = v1;
        this.v2 = v2;
        double tmp = Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2);
        this.weight = Math.sqrt(tmp);
        this.weight = Math.round(weight * 100.0) / 100.0;
        // System.out.println(weight);
        this.exsistInGraph = true;
    }

    // setters and getters

    public double getWeight() {
        this.weight = Math.round(this.weight * 100.0) / 100.0;
        return this.weight;
    }

    public Vertix getv1() {
        return this.v1;
    }

    public Vertix getv2() {
        return this.v2;
    }

    public boolean isExsistInGraph() {
        return this.exsistInGraph;
    }

    public void setExsistInGraph(boolean exsistInGraph) {
        this.exsistInGraph = exsistInGraph;
    }

}