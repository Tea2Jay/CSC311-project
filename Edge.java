class Edge implements Comparable<Edge> {
    Vertix src;
    Vertix dest;
    double weight;

    Edge() {
        src = new Vertix();
        dest = new Vertix();
        weight = 0;
    }

    Edge(Vertix src, Vertix dest) {
        this.src = src;
        this.dest = dest;
        this.weight = calcWeight();
    }

    double calcWeight() {
        return this.weight = Math.sqrt(Math.pow(src.x - dest.x, 2) + Math.pow(src.y - dest.y, 2));
    }

    public int compareTo(Edge compareEdge) {
        return (int) (this.weight * 100 - compareEdge.weight * 100);
    }
}