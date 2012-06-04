/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Konnarr
 */
public class Node implements Serializable, Element{
    private static final long serialVersionUID = 2345541351034924475L;
    
    private String label = "";
    private boolean init = false;
    private boolean fin = false;
    //redundant
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    
    //graphical properties    
    private boolean inherit = true;
    private Color color;
    private RectangularShape shape;
        
    private int index;
    
    public Node(RectangularShape shape, Point pos) {
        this.shape = (RectangularShape) shape.clone();
        this.shape.setFrame(pos.x, pos.y, shape.getWidth(), shape.getHeight());
    }
    
    public RectangularShape getShape() {
        return shape;
    }
    
    public void setShape(RectangularShape shape) {
        this.shape = shape;
    }
    
    public boolean isFinal() {
        return fin;
    }
    
    public void setFinal(boolean value) {
        fin = value;
    }
    
    public boolean isInitial() {
        return init;
    }
    
    public void setInitial(boolean value) {
        init = value;
    }
    
    public void setLabel(String text) {
        label = text;
    }
    
    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        inherit = false;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isInherit() {
        return inherit;
    }
    
    public void setInherit() {
        inherit = true;
    }

    @Override
    public String toString() {
        return "Node: " + label + (init?" i":"") + (fin?" f":"") + "@"+getShape().getCenterX()+","+getShape().getCenterY();
    }

   
    
}
