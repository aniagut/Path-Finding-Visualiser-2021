package GraphClasses;

public class Edge {

    private Node neighbour;
    private int cost;

    public Edge(int cost, Node neighbour){
        this.neighbour= neighbour;
        this.cost=cost;
    }

    public void setCost(int cost){
        this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public void setNeighbour(Node neighbour){
        this.neighbour=neighbour;
    }
    
    public Node getNeighbour(){
        return neighbour;
    }

}
