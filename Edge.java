
public class Edge {
    private Point a;
    private Point b;
    private double weight;

    /**
     * Enter Point a , b to create an edge between them
     * 
     */
    public Edge(Point a, Point b) {
        this.a = a;
        this.b = b;
        double tmp = Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2);
        weight = Math.sqrt(tmp);
    }

    public double getWeight() {
        return this.weight;
    }

}