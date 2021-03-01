package GraphClasses;
import java.awt.*;
import java.util.*;

public class Node implements Comparable<Node>{

    private Point position;

    private int distance;

    private Node parent;

    private int cost;

    private LinkedList<Edge> edges;

    public Node(Point position){
        this.position = position;
        this.distance=Integer.MAX_VALUE;
        this.edges = new LinkedList<>();
    }

    public void setPosition(Point position){
        this.position=position;
    }

    public Point getPosition(){
        return position;
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public int getDistance(){
        return distance;
    }

    public void setCost(int cost){
        this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public void setParent(Node parent){
        this.parent= parent;
    }

    public Node getParent(){
        return parent;
    }

    public Edge getEdge(int index){
        return edges.get(index);
    }

    public LinkedList<Edge> getEdges(){
        return edges;
    }

    public int compareTo(Node other){
        return Integer.compare(cost, other.cost);
    }

}
