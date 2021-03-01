package GraphClasses;

import java.awt.*;
public class Slot extends Node {

    private final int slotWidth;

    private final int slotHeight;

    private Color color;

    public Slot(Point position, int slotWidth, int slotHeight){
        super(position);

        this.slotHeight = slotHeight;
        this.slotWidth = slotWidth;
        this.color = Color.LIGHT_GRAY;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void draw(Graphics g){
        g.setColor(color);
        g.fillRect(getPosition().x, getPosition().y, slotWidth, slotHeight);
        g.setColor(Color.BLACK);
        g.drawRect(getPosition().x, getPosition().y, slotWidth, slotHeight);
    }

}