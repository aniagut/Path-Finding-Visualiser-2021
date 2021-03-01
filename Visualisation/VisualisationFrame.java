package Visualisation;
import GraphClasses.Edge;
import GraphClasses.Slot;
import GraphClasses.Network;


import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.swing.*;
public class VisualisationFrame extends JFrame implements ActionListener,KeyListener{

    private Network network;
    private Legend legend;

    private JPanel container;
    private JPanel controlPanel;
    private JPanel buttonPanel;
    private JPanel optionPanel;

    private JButton playButton;
    private JButton resetButton;
    private JButton wallButton;
    private JButton bombButton;
    private JButton generateMaze;
    private JButton stopButton;
    private JButton continueButton;

    private JComboBox algorithmList;
    private  JComboBox mazealgorithmsList;
    private JComboBox gridEditorList;

    private JLabel algorithmListLabel;
    private JLabel mazealgorithmsListLabel;
    private JLabel gridEditorListLabel;

    private JTextField rowtextfield;
    private JTextField columntextfield;

    private int mazepositionx;
    private int mazepositiony;

    private Slot mazeend;

    private ArrayList<Slot> mazevisited;

    public VisualisationFrame(){

        network = new Network(750,550,30,40);

        container = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new BorderLayout());

        playButton = new JButton("Find path");
        playButton.setMnemonic(KeyEvent.VK_S);
        playButton.setActionCommand("start");
        playButton.addActionListener(this);

        resetButton = new JButton("Reset");
        resetButton.setMnemonic(KeyEvent.VK_R);
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);

        wallButton=new JButton("Add wall");
        wallButton.setMnemonic(KeyEvent.VK_C);
        wallButton.setActionCommand("wall");
        wallButton.addActionListener(this);

        bombButton=new JButton("Add bomb");
        bombButton.setMnemonic(KeyEvent.VK_F);
        bombButton.setActionCommand("bomb");
        bombButton.addActionListener(this);

        generateMaze=new JButton("Generate maze");
        generateMaze.setMnemonic(KeyEvent.VK_D);
        generateMaze.setActionCommand("maze");
        generateMaze.addActionListener(this);

        stopButton=new JButton("Stop");
        stopButton.setMnemonic(KeyEvent.VK_E);
        stopButton.setActionCommand("stop");
        stopButton.addActionListener(this);

        continueButton=new JButton("Continue");
        continueButton.setMnemonic(KeyEvent.VK_S);
        continueButton.setActionCommand("continue");
        continueButton.addActionListener(this);

        rowtextfield=new JTextField("Number of rows");
        columntextfield=new JTextField("Number of columns");
        rowtextfield.addKeyListener(this);
        columntextfield.addKeyListener(this);

        String mazealgorithms[]={"Dijkstra","BFS","DFS"};
        mazealgorithmsList=new JComboBox(mazealgorithms);
        mazealgorithmsListLabel=new JLabel("Maze algorithm: ");
        mazealgorithmsListLabel.setLabelFor(mazealgorithmsList);
        mazealgorithmsListLabel.setHorizontalAlignment(JLabel.RIGHT);

        String algorithms[] = {"Dijkstra" , "A*", "BFS"};
        algorithmList = new JComboBox(algorithms);
        algorithmListLabel = new JLabel("Finding algorithm: ");
        algorithmListLabel.setLabelFor(algorithmList);
        algorithmListLabel.setHorizontalAlignment(JLabel.RIGHT);

        String editList[] = {"Set Start Position", "Set Goal Position","Add Wall","Add Bomb"};
        gridEditorList = new JComboBox(editList);
        gridEditorList.addActionListener(this);
        gridEditorListLabel = new JLabel("Modify: ");
        gridEditorListLabel.setLabelFor(gridEditorList);
        gridEditorListLabel.setHorizontalAlignment(JLabel.RIGHT);

        buttonPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        buttonPanel.add(playButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(wallButton);
        buttonPanel.add(bombButton);
        buttonPanel.add(generateMaze);
        buttonPanel.add(stopButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(rowtextfield);
        buttonPanel.add(columntextfield);

        controlPanel.add(buttonPanel, BorderLayout.WEST);

        optionPanel = new JPanel(new GridLayout(3, 2, 0, 5));

        legend=new Legend();

        optionPanel.add(mazealgorithmsListLabel);
        optionPanel.add(mazealgorithmsList);
        optionPanel.add(algorithmListLabel);
        optionPanel.add(algorithmList);
        optionPanel.add(gridEditorListLabel);
        optionPanel.add(gridEditorList);

        controlPanel.add(optionPanel,BorderLayout.EAST);
        controlPanel.add(legend,BorderLayout.CENTER);
        controlPanel.setPreferredSize(new Dimension(500,75));

        container.add(network,BorderLayout.CENTER);
        container.add(controlPanel,BorderLayout.SOUTH);

        this.mazepositionx=-1;
        this.mazepositiony=-1;
        this.mazeend=null;
        this.mazevisited=new ArrayList<Slot>();

        this.addKeyListener(this);
        setFocusable(true);
        requestFocus();
        this.add(container);
        this.setResizable(false);
        this.pack();
    }

    public void actionPerformed(ActionEvent e){
        if("start".equals(e.getActionCommand())){
            network.setShowStartAndGoalPosition(true);
            SwingWorker worker = new SwingWorker<Void,Void>(){
                protected Void doInBackground(){
                    network.startSearching(algorithmList.getSelectedIndex());
                    return null;
                }
            };
            playButton.setEnabled(false);
            wallButton.setEnabled(false);
            bombButton.setEnabled(false);
            generateMaze.setEnabled(false);
            worker.execute();
        }

        if("reset".equals(e.getActionCommand())){
            mazepositiony=-1;
            mazepositionx=-1;
            mazevisited.clear();
            mazeend=null;
            network.resetSearching();
            playButton.setEnabled(true);
            stopButton.setEnabled(true);
            continueButton.setEnabled(true);
            wallButton.setEnabled(true);
            bombButton.setEnabled(true);
            generateMaze.setEnabled(true);
            network.getPathfinder().setisPaused(false);
        }
        if("wall".equals(e.getActionCommand())){
            int i=(int)(Math.random()*network.getNumberOfColumns());
            int j=(int)(Math.random()*network.getNumberOfRows());
            while(network.getSlots()[i][j]==network.getStartSlot() || network.getSlots()[i][j]==network.getGoalSlot() || network.getSlots()[i][j]==network.getMustPassNode()){
                i=(int)(Math.random()*network.getNumberOfColumns());
                j=(int)(Math.random()*network.getNumberOfRows());
            }
            network.getNotAvailableList().add(network.getSlots()[i][j]);
            network.getSlots()[i][j].setColor(Color.BLACK);
            network.update();
        }
        if("bomb".equals(e.getActionCommand())){
            int i=(int)(Math.random()*network.getNumberOfColumns());
            int j=(int)(Math.random()*network.getNumberOfRows());

            while(network.getSlots()[i][j]==network.getStartSlot()|| network.getSlots()[i][j]==network.getGoalSlot() || network.getNotAvailableList().contains(network.getSlots()[i][j])){
                i=(int)(Math.random()*network.getNumberOfColumns());
                j=(int)(Math.random()*network.getNumberOfRows());
            }
            if(network.getMustPassNode()!=null)
                network.getMustPassNode().setColor(Color.LIGHT_GRAY);
            network.setMustPassNode(network.getSlots()[i][j]);
            network.getMustPassNode().setColor(Color.YELLOW);
            network.update();
        }
        if("maze".equals(e.getActionCommand())){
            network.resetSearching();
            network.setShowStartAndGoalPosition(false);
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
            continueButton.setEnabled(false);
            wallButton.setEnabled(false);
            bombButton.setEnabled(false);
            network.getPathfinder().setisPaused(false);
            for(int i=0;i<network.getNumberOfColumns();i++){
                for(int j=0;j<network.getNumberOfRows();j++){
                    network.getSlots()[i][j].setColor(Color.BLACK);
                }
            }
            int i=(int)(Math.random()*network.getNumberOfColumns());
            int j=(int)(Math.random()* network.getNumberOfRows());
            this.mazepositionx=i;
            this.mazepositiony=j;
            network.getSlots()[i][j].setColor(Color.RED);
            if(mazealgorithmsList.getSelectedIndex()==0){
                Dijkstra(network.getSlots()[i][j],mazevisited);
            }
            else if (mazealgorithmsList.getSelectedIndex()==1){
                BFS(network.getSlots()[i][j],mazevisited);
            }
            else{
                DFS(network.getSlots()[i][j],mazevisited);
            }

            network.update();
        }
        if("stop".equals(e.getActionCommand())){
            network.getPathfinder().setisPaused(true);
        }
        if("continue".equals(e.getActionCommand())){
            network.getPathfinder().setisPaused(false);
        }

        if(e.getSource() == gridEditorList){
            network.setModifier(gridEditorList.getSelectedIndex());
        }
        setFocusable(true);
        requestFocus();
    }
    public void BFS(Slot from,ArrayList<Slot> visited){
        mazeend=from;
        while(mazeend==from){
            int i=(int)(Math.random()*network.getNumberOfColumns());
            int j=(int)(Math.random()*network.getNumberOfRows());
            mazeend=network.getSlots()[i][j];
        }
        LinkedList<Slot> openlist=new LinkedList<Slot>();
        LinkedList<Slot> closedlist=new LinkedList<Slot>();
        openlist.add(from);
        while(!openlist.isEmpty()){
            Slot current=openlist.get(0);
            openlist.remove(0);
            closedlist.add(current);
            for(Edge e : current.getEdges()){
                Slot next = (Slot) e.getNeighbour();
                if(closedlist.contains(next)){
                    continue;
                }
                next.setParent(current);
                openlist.add(next);
                closedlist.add(next);
            }
            if(current==mazeend){
                break;
            }
        }
        Slot current=mazeend;
        visited.add(current);
        mazeend.setColor(Color.BLUE);
        while(current.getParent()!=from){
            visited.add((Slot) current.getParent());
            current=(Slot) current.getParent();
            current.setColor(Color.WHITE);
        }
    }
    public void Dijkstra(Slot from,ArrayList<Slot> visited){
        mazeend=from;
        while(mazeend==from){
            int i=(int)(Math.random()*network.getNumberOfColumns());
            int j=(int)(Math.random()*network.getNumberOfRows());
            mazeend=network.getSlots()[i][j];
        }
        for(int i=0;i<network.getNumberOfColumns();i++){
            for(int j=0;j<network.getNumberOfRows();j++){
                network.getSlots()[i][j].setDistance(Integer.MAX_VALUE);
            }
        }
        PriorityQueue<Slot> openlist=new PriorityQueue<Slot>();
        from.setDistance(0);
        from.setCost(0);
        openlist.add(from);
        while(!openlist.isEmpty()) {
            Slot current = openlist.poll();
            for (Edge e : current.getEdges()) {
                Slot next = (Slot) e.getNeighbour();
                int distanceFromStart = current.getDistance()+e.getCost();
                if (distanceFromStart < next.getDistance()) {
                    openlist.remove(next);
                    next.setDistance(distanceFromStart);
                    next.setCost(distanceFromStart);
                    next.setParent(current);
                    openlist.add(next);
                }
                if (current == mazeend) {
                    break;
                }
            }
        }
        Slot current=mazeend;
        visited.add(current);
        mazeend.setColor(Color.BLUE);
        while(current.getParent()!=from){
            visited.add((Slot) current.getParent());
            current=(Slot) current.getParent();
            current.setColor(Color.WHITE);
        }
    }
    public void DFS(Slot from,ArrayList<Slot> visited){
        LinkedList<Slot> queue=new LinkedList<Slot>();
        queue.add(from);
        visited.add(from);
        Slot current=from;
        while(queue.size()!=0){
            current=queue.pollFirst();
            LinkedList<Edge> neighbours=current.getEdges();
            boolean choosed=false;
            while(!choosed && neighbours.size()>0){
                Edge edge=neighbours.get((int)(Math.random()*neighbours.size()));
                Slot cell=(Slot) edge.getNeighbour();
                if(!visited.contains(cell)){
                    visited.add(cell);
                    queue.add(cell);
                    choosed=true;
                    cell.setColor(Color.WHITE);
                }
                neighbours.remove(edge);
            }
        }
        current.setColor(Color.BLUE);
        mazeend=current;
    }
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            if(rowtextfield.getText()!=null && columntextfield.getText()!=null){
                int row=Integer.parseInt(rowtextfield.getText());
                int column=Integer.parseInt(columntextfield.getText());
                network.setNumberOfRows(row);
                network.setNumberOfColumns(column);
                network.setRowHeight(network.getNetworkHeight() / network.getNumberOfRows());
                network.setColumnWidth(network.getNetworkWidth()/ network.getNumberOfColumns());
                network.buildGraph();
                network.repaint();
            }
        }
        if(mazepositionx!=-1) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(mazepositiony+1<network.getNumberOfRows()) {
                    if (mazevisited.contains(network.getSlots()[mazepositionx][mazepositiony + 1])) {
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.ORANGE);
                        mazepositiony += 1;
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.RED);
                        network.update();
                        if (network.getSlots()[mazepositionx][mazepositiony] == mazeend) {
                            mazevisited.clear();
                            for (int i = 0; i < network.getNumberOfColumns(); i++) {
                                for (int j = 0; j < network.getNumberOfRows(); j++) {
                                    network.getSlots()[i][j].setColor(Color.BLACK);
                                }
                            }
                            int i = (int) (Math.random() * network.getNumberOfColumns());
                            int j = (int) (Math.random() * network.getNumberOfRows());
                            this.mazepositionx = i;
                            this.mazepositiony = j;
                            network.getSlots()[i][j].setColor(Color.RED);
                            if(mazealgorithmsList.getSelectedIndex()==0){
                                Dijkstra(network.getSlots()[i][j],mazevisited);
                            }
                            else if (mazealgorithmsList.getSelectedIndex()==1){
                                BFS(network.getSlots()[i][j],mazevisited);
                            }
                            else{
                                DFS(network.getSlots()[i][j],mazevisited);
                            }

                            network.update();
                        }
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if(mazepositiony-1>=0) {
                    if (mazevisited.contains(network.getSlots()[mazepositionx][mazepositiony - 1])) {
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.ORANGE);
                        mazepositiony -= 1;
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.RED);
                        network.update();
                        if (network.getSlots()[mazepositionx][mazepositiony] == mazeend) {
                            mazevisited.clear();
                            for (int i = 0; i < network.getNumberOfColumns(); i++) {
                                for (int j = 0; j < network.getNumberOfRows(); j++) {
                                    network.getSlots()[i][j].setColor(Color.BLACK);
                                }
                            }
                            int i = (int) (Math.random() * network.getNumberOfColumns());
                            int j = (int) (Math.random() * network.getNumberOfRows());
                            this.mazepositionx = i;
                            this.mazepositiony = j;
                            network.getSlots()[i][j].setColor(Color.RED);
                            if(mazealgorithmsList.getSelectedIndex()==0){
                                Dijkstra(network.getSlots()[i][j],mazevisited);
                            }
                            else if (mazealgorithmsList.getSelectedIndex()==1){
                                BFS(network.getSlots()[i][j],mazevisited);
                            }
                            else{
                                DFS(network.getSlots()[i][j],mazevisited);
                            }

                            network.update();
                        }
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (mazepositionx - 1 >= 0) {
                    if (mazevisited.contains(network.getSlots()[mazepositionx - 1][mazepositiony])) {
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.ORANGE);
                        mazepositionx -= 1;
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.RED);
                        network.update();
                        if (network.getSlots()[mazepositionx][mazepositiony] == mazeend) {
                            mazevisited.clear();
                            for (int i = 0; i < network.getNumberOfColumns(); i++) {
                                for (int j = 0; j < network.getNumberOfRows(); j++) {
                                    network.getSlots()[i][j].setColor(Color.BLACK);
                                }
                            }
                            int i = (int) (Math.random() * network.getNumberOfColumns());
                            int j = (int) (Math.random() * network.getNumberOfRows());
                            this.mazepositionx = i;
                            this.mazepositiony = j;
                            network.getSlots()[i][j].setColor(Color.RED);
                            if(mazealgorithmsList.getSelectedIndex()==0){
                                Dijkstra(network.getSlots()[i][j],mazevisited);
                            }
                            else if (mazealgorithmsList.getSelectedIndex()==1){
                                BFS(network.getSlots()[i][j],mazevisited);
                            }
                            else{
                                DFS(network.getSlots()[i][j],mazevisited);
                            }

                            network.update();
                        }
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (mazepositionx + 1 <network.getNumberOfColumns()) {
                    if (mazevisited.contains(network.getSlots()[mazepositionx + 1][mazepositiony])) {
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.ORANGE);
                        mazepositionx += 1;
                        network.getSlots()[mazepositionx][mazepositiony].setColor(Color.RED);
                        network.update();
                        if (network.getSlots()[mazepositionx][mazepositiony] == mazeend) {
                            mazevisited.clear();
                            for (int i = 0; i < network.getNumberOfColumns(); i++) {
                                for (int j = 0; j < network.getNumberOfRows(); j++) {
                                    network.getSlots()[i][j].setColor(Color.BLACK);
                                }
                            }
                            int i = (int) (Math.random() * network.getNumberOfColumns());
                            int j = (int) (Math.random() * network.getNumberOfRows());
                            this.mazepositionx = i;
                            this.mazepositiony = j;
                            network.getSlots()[i][j].setColor(Color.RED);
                            if(mazealgorithmsList.getSelectedIndex()==0){
                                Dijkstra(network.getSlots()[i][j],mazevisited);
                            }
                            else if (mazealgorithmsList.getSelectedIndex()==1){
                                BFS(network.getSlots()[i][j],mazevisited);
                            }
                            else{
                                DFS(network.getSlots()[i][j],mazevisited);
                            }

                            network.update();
                        }
                    }
                }
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e){
    }
    @Override
    public void keyReleased(KeyEvent e){
    }
}
