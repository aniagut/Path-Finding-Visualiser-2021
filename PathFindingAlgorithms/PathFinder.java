package PathFindingAlgorithms;

import GraphClasses.Edge;
import GraphClasses.Slot;
import GraphClasses.Network;

import java.awt.Color;
import java.awt.Point;
import java.util.*;


public class PathFinder {

    public static final int DIJKSTRA = 0;
    public static final int ASTAR = 1;
    public static final int BFS = 2;

    private ArrayList<Slot> processedNodes;

    private PriorityQueue<Slot> nodesToProcess;

    private LinkedList<Slot> nodestToProcessBFS;

    private boolean isPaused;

    private boolean isActive;

    public PathFinder() {
        nodesToProcess = new PriorityQueue<Slot>();
        nodestToProcessBFS=new LinkedList<Slot>();
        processedNodes = new ArrayList<Slot>();
        isPaused=false;
    }

    public ArrayList<Slot> findShortestPath(Slot startSlot, Slot goalSlot, Network network,int algorithmType){
        isActive = true;

        startSlot.setDistance(0);

        if (algorithmType == ASTAR)
            startSlot.setCost(euclideanDistance(startSlot.getPosition(), goalSlot.getPosition()));
        if (algorithmType == DIJKSTRA)
            startSlot.setCost(0);
        nodesToProcess.clear();
        processedNodes.clear();
        nodesToProcess.clear();
        if(algorithmType==DIJKSTRA || algorithmType==ASTAR)
            nodesToProcess.add(startSlot);
        if(algorithmType==BFS)
            nodestToProcessBFS.add(startSlot);

        while ((!nodesToProcess.isEmpty() || !nodestToProcessBFS.isEmpty()) && isActive) {
            network.update();
            long currentTime = System.currentTimeMillis();
            while(System.currentTimeMillis()-currentTime<=1){
                while(isPaused);
            }


            Slot current =nodesToProcess.poll();
            if(current==null){
                current=nodestToProcessBFS.get(0);
                nodestToProcessBFS.remove(0);
            }

            current.setColor(Color.DARK_GRAY);

            if (algorithmType == ASTAR || algorithmType==BFS)
                processedNodes.add(current);

            if(current==startSlot){
                startSlot.setColor(Color.RED);
            }
            if (current == goalSlot){
                goalSlot.setColor(Color.RED);
                network.update();
                isActive=false;
                break;
            }
            for (Edge e : current.getEdges()) {
                switch (algorithmType) {
                    case DIJKSTRA: {
                        Slot nextSlot = (Slot) e.getNeighbour();

                        if (!network.getNotAvailableList().contains(nextSlot)) {
                            int distance = current.getDistance() + e.getCost();

                            if (nextSlot.getColor()!=Color.DARK_GRAY && nextSlot.getColor() != Color.YELLOW && nextSlot.getColor() != Color.RED)
                                nextSlot.setColor(Color.PINK);

                            if (distance < nextSlot.getDistance()) {
                                nodesToProcess.remove(nextSlot);
                                nextSlot.setDistance(distance);
                                nextSlot.setCost(distance);
                                nextSlot.setParent(current);
                                nodesToProcess.add(nextSlot);
                            }
                        }
                        break;
                    }

                    case ASTAR: {
                        Slot nextSlot = (Slot) e.getNeighbour();

                        if (processedNodes.contains(nextSlot))
                            break;
                        if (!network.getNotAvailableList().contains(nextSlot)) {
                            int distanceFromStart= current.getDistance() + e.getCost();

                            int distanceToGoal = euclideanDistance(nextSlot.getPosition(), goalSlot.getPosition());

                            int distanceSum = distanceFromStart + distanceToGoal;
                            if (nextSlot.getColor()!=Color.DARK_GRAY && nextSlot.getColor()!=Color.RED && nextSlot.getColor()!=Color.YELLOW)
                                nextSlot.setColor(Color.PINK);

                            if (!nodesToProcess.contains(nextSlot) || distanceFromStart < nextSlot.getDistance()) {
                                nextSlot.setDistance(distanceFromStart);
                                nextSlot.setCost(distanceSum);
                                nextSlot.setParent(current);
                                nodesToProcess.add(nextSlot);
                            }
                        }
                        break;

                    }
                    case BFS: {
                        Slot nextSlot= (Slot) e.getNeighbour();
                        if (processedNodes.contains(nextSlot))
                            break;
                        if (!network.getNotAvailableList().contains(nextSlot)) {
                            if (nextSlot.getColor()!=Color.DARK_GRAY && nextSlot.getColor() != Color.YELLOW && nextSlot.getColor() != Color.RED)
                                nextSlot.setColor(Color.PINK);
                            nextSlot.setParent(current);
                            nodestToProcessBFS.add(nextSlot);
                            processedNodes.add(nextSlot);
                        }
                        break;
                    }
                }
            }
        }

        Slot current = goalSlot;
        ArrayList<Slot> shortestpath = new ArrayList<>();
        while (current.getParent()!= startSlot) {
            shortestpath.add((Slot) current.getParent());
            current = (Slot) current.getParent();
        }

        Collections.reverse(shortestpath);
        return shortestpath;
    }
    public void stop () {
        isActive = false;
    }

    public boolean getisPaused(){
        return isPaused;
    }

    public void setisPaused(boolean isPaused){
        this.isPaused=isPaused;
    }

    public int euclideanDistance (Point p, Point q){
        return (int)(Math.sqrt(Math.pow(q.getX() - p.getX(), 2) + Math.pow(q.getY() - p.getY(), 2)));
    }
}