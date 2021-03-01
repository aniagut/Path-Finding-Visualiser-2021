package Main;

import Visualisation.VisualisationFrame;

import javax.swing.*;


public class PathFindingLauncher {

    public static void main(String[] args){
        VisualisationFrame frame = new VisualisationFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Pathfinding Visualiser");
        frame.setVisible(true);
    }

}
