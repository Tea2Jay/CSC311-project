public class Vertix {
    private int name;
    private int x;
    private int y;

    /**Enter x , y coordinates */
   public Vertix(int name,int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // setters and getters 
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getName() {
        return this.name;
    }

    public void setName(int name) {
        this.name = name;
    }



}