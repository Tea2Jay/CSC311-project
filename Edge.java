
public class Edge {
    private Vertix v1;
    private Vertix v2;
    private double weight;

    /**
     * Enter Vertix v1 , b to create an edge between them
     * 
     */
    public Edge(Vertix v1, Vertix v2) {
        this.v1 = v1;
        this.v2 = v2;
        double tmp = Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2);
        weight = Math.sqrt(tmp);
    }

    //getters only

    public double getWeight() {
        return this.weight;
    }

    public double getWeight(Vertix v1, Vertix v2) {
        double tmp = Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2);
        weight = Math.sqrt(tmp);
        return this.weight;
    }

    public Vertix getv1() {
        return this.v1;
    }

    public Vertix getv2() {
        return this.v2;
    }

}