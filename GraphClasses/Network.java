package GraphClasses;
import PathFindingAlgorithms.PathFinder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Network extends JPanel implements MouseListener{

    private int networkWidth;

    private int networkHeight;

    private int rowHeight;

    private int columnWidth;

    private int numberOfRows;

    private int numberOfColumns;

    private boolean isActive;

    private boolean startModifier;

    private boolean goalModifier;

    private boolean wallModifier;

    private boolean requiredvertexModifier;

    private Slot slots[][];

    private Slot startSlot;

    private Slot goalSlot;

    private Slot mustPassNode;

    private ArrayList<Slot> notAvailableList;

    private ArrayList<Slot> shortestPath;

    private PathFinder pathfinder;

    private boolean showStartAndGoalPosition;

    public Network(int networkWidth,int networkHeight, int numberOfRows, int numberOfColumns){
        this.networkWidth = networkWidth;

        this.networkHeight = networkHeight;

        this.numberOfRows = numberOfRows;

        this.numberOfColumns = numberOfColumns;

        rowHeight = networkHeight/numberOfRows;

        columnWidth = networkWidth/numberOfColumns;

        isActive = false;
        startModifier = true;
        goalModifier = false;
        wallModifier=false;
        requiredvertexModifier=false;
        mustPassNode=null;

        notAvailableList=new ArrayList<Slot>();
        shortestPath=new ArrayList<Slot>();

        showStartAndGoalPosition=true;

        buildGraph();

        addMouseListener(this);

        this.setPreferredSize(new Dimension(networkWidth,networkHeight));
    }

    public void startSearching(int algorithmType){
        isActive = true;
        if(mustPassNode!=null){
            ArrayList<Slot>shortest1=pathfinder.findShortestPath(startSlot,mustPassNode,this,algorithmType);
            ArrayList<Slot> shortest2=pathfinder.findShortestPath(mustPassNode,goalSlot,this,algorithmType);
            shortestPath.addAll(shortest1);
            shortestPath.addAll(shortest2);
            this.update();
        }
        else{
            shortestPath.addAll(pathfinder.findShortestPath(startSlot,goalSlot,this,algorithmType));
            this.update();
        }
    }

    public void update(){
        this.repaint();
    }

    public void resetSearching(){
        isActive = false;
        pathfinder.stop();
        mustPassNode=null;
        notAvailableList.clear();
        shortestPath.clear();
        pathfinder = new PathFinder();
        buildGraph();
        this.repaint();
    }

    public void buildGraph(){
        pathfinder = new PathFinder();

        slots = new Slot[numberOfColumns][numberOfRows];
        for(int i = 0; i < numberOfColumns; i++){
            for(int j = 0; j < numberOfRows; j++){
                slots[i][j] = new Slot(new Point(i * columnWidth,j * rowHeight),columnWidth,rowHeight);
            }
        }

        for(int i = 0; i < numberOfColumns; i++){
            for(int j = 0; j < numberOfRows; j++){
                if(j + 1 < numberOfRows)
                    slots[i][j].addEdge(new Edge(columnWidth, slots[i][j+1]));
                if(i + 1 < numberOfColumns)
                    slots[i][j].addEdge(new Edge(rowHeight, slots[i+1][j]));
                if(j - 1 >= 0)
                    slots[i][j].addEdge(new Edge(columnWidth, slots[i][j-1]));
                if(i- 1 >= 0)
                    slots[i][j].addEdge(new Edge(rowHeight, slots[i-1][j]));
            }
        }

        startSlot = slots[0][0];
        goalSlot = slots[numberOfColumns-1][numberOfRows- 1];
        startSlot.setColor(Color.RED);
        goalSlot.setColor(Color.RED);
        update();
    }

    public void setModifier(int slotToModify){
        switch(slotToModify){
            case 0:
                startModifier = true;
                goalModifier = false;
                wallModifier=false;
                requiredvertexModifier=false;
                break;
            case 1:
                goalModifier = true;
                startModifier = false;
                wallModifier=false;
                requiredvertexModifier=false;
                break;
            case 2:
                wallModifier=true;
                goalModifier = false;
                startModifier = false;
                requiredvertexModifier=false;
                break;
            case 3:
                requiredvertexModifier=true;
                wallModifier=false;
                goalModifier = false;
                startModifier = false;
                break;
        }
    }
    public void paintComponent(Graphics g){

        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, networkWidth, networkHeight);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, networkWidth, networkHeight);
        g.setColor(Color.BLACK);


        if(!this.shortestPath.isEmpty()){

            for(int i = 0; i < numberOfColumns; i++) {
                for (int j = 0; j < numberOfRows; j++) {
                    slots[i][j].setColor(Color.DARK_GRAY);
                }
            }
            int len=shortestPath.size();
            for(int i=0;i<len;i++){
                shortestPath.get(i).setColor(Color.RED);
            }
        }
        if(mustPassNode!=null)
            mustPassNode.setColor(Color.YELLOW);
        int len=notAvailableList.size();
        for(int i=0;i<len;i++){
            notAvailableList.get(i).setColor(Color.BLACK);
        }
        if(showStartAndGoalPosition){
            startSlot.setColor(Color.RED);
            goalSlot.setColor(Color.RED);
        }

        for(int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                slots[i][j].draw(g);
            }
        }
    }

    public void mouseClicked(MouseEvent e){

        if(!isActive){
            Point mousePosition = new Point(e.getX(),e.getY());
            if(startModifier){
                if(startSlot != null)
                    startSlot.setColor(Color.LIGHT_GRAY);
                startSlot = slots[(int)(mousePosition.x/columnWidth)][(int)(mousePosition.y/rowHeight)];
                startSlot.setColor(Color.RED);
            }

            if(goalModifier){
                if(goalSlot != null)
                    goalSlot.setColor(Color.LIGHT_GRAY);
                goalSlot = slots[(int)(mousePosition.x/columnWidth)][(int)(mousePosition.y/rowHeight)];
                goalSlot.setColor(Color.RED);
            }
            if(wallModifier){
                Slot wallCell=slots[(int)(mousePosition.x/columnWidth)][(int)(mousePosition.y/rowHeight)];
                wallCell.setColor(Color.BLACK);
                notAvailableList.add(wallCell);
            }
            if(requiredvertexModifier){
                if(mustPassNode!=null){
                    mustPassNode.setColor(Color.LIGHT_GRAY);
                }
                mustPassNode=slots[(int)(mousePosition.x/columnWidth)][(int)(mousePosition.y/rowHeight)];
                mustPassNode.setColor(Color.YELLOW);
            }
            update();
        }
    }

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mousePressed(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}

    public void setNetworkWidth(int networkWidth){
        this.networkWidth=networkWidth;
    }
    public int getNetworkWidth(){
        return networkWidth;
    }
    public void setNetworkHeight(int networkHeight){
        this.networkHeight=networkHeight;
    }
    public int getNetworkHeight(){
        return networkHeight;
    }
    public int getRowHeight(){
        return rowHeight;
    }
    public void setRowHeight(int rowHeight){
        this.rowHeight=rowHeight;
    }
    public int getColumnWidth(){
        return columnWidth;
    }
    public void setColumnWidth(int columnWidth){
        this.columnWidth=columnWidth;
    }
    public int getNumberOfRows(){
        return numberOfRows;
    }
    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows=numberOfRows;
    }
    public int getNumberOfColumns(){
        return numberOfColumns;
    }
    public void setNumberOfColumns(int numberOfColumns){
        this.numberOfColumns=numberOfColumns;
    }

    public Slot[][] getSlots(){
        return slots;
    }

    public Slot getStartSlot(){
        return startSlot;
    }

    public Slot getGoalSlot(){
        return goalSlot;
    }
    public Slot getMustPassNode(){
        return mustPassNode;
    }
    public void setMustPassNode(Slot mustPassNode){
        this.mustPassNode=mustPassNode;
    }
    public ArrayList<Slot> getNotAvailableList(){
        return notAvailableList;
    }
    public PathFinder getPathfinder(){
        return pathfinder;
    }
    public boolean getShowStartAndGoalPosition(){
        return showStartAndGoalPosition;
    }
    public void setShowStartAndGoalPosition(boolean showStartAndGoalPosition){
        this.showStartAndGoalPosition=showStartAndGoalPosition;
    }

}
