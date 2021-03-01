package Visualisation;

import javax.swing.*;
import java.awt.*;

public class Legend extends JPanel {

    public void paintComponent(Graphics g){
        g.setColor(Color.BLACK);
        g.drawString("Visited nodes",10,15);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(100,5,13,13);
        g.setColor(Color.BLACK);
        g.drawString("Currently visited",10,35);
        g.setColor(Color.PINK);
        g.fillRect(100,25,13,13);
        g.setColor(Color.BLACK);
        g.drawString("Path ends",10,55);
        g.setColor(Color.RED);
        g.fillRect(100,45,13,13);
    }
}
