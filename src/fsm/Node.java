/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Konnarr
 */
public class Node implements Serializable, Element{
    private static final long serialVersionUID = 2345541351034924475L;
    
    private static Color defColor = Color.BLACK;
    private static Color defFillColor = Color.WHITE;
    private static boolean defFillNode = false;
    private static boolean defAutoWidth = false;
    private static RectangularShape defShape = new Ellipse2D.Double(0, 0, 40, 40);
    
    private String label = "";
    private boolean init = false;
    private boolean fin = false;
    private ArrayList<Edge> edges = new ArrayList<Edge>(); //redundant but good for simulation
   
    private int index;
    private boolean inherit = true;
    //graphical properties    
    private Color color;
    private Color fillColor = Color.WHITE;
    private boolean fillNode = false;
    private RectangularShape shape;
    private boolean autoWidth = false;
    
    ////////////////Constructor///////////////////////////////////////////
    
    public Node(RectangularShape shape, Point pos) {
        this.shape = (RectangularShape) shape.clone();
        this.shape.setFrame(pos.x, pos.y, shape.getWidth(), shape.getHeight());
    }

    ////////////////Getter and Setter for static default values////////////////

    public static boolean isDefAutoWidth() {
        return defAutoWidth;
    }

    public static void setDefAutoWidth(boolean defAutoWidth) {
        Node.defAutoWidth = defAutoWidth;
    }

    public static Color getDefColor() {
        return defColor;
    }

    public static void setDefColor(Color defColor) {
        Node.defColor = defColor;
    }

    public static Color getDefFillColor() {
        return defFillColor;
    }

    public static void setDefFillColor(Color defFillColor) {
        Node.defFillColor = defFillColor;
    }

    public static boolean isDefFillNode() {
        return defFillNode;
    }

    public static void setDefFillNode(boolean defFillNode) {
        Node.defFillNode = defFillNode;
    }

    public static RectangularShape getDefShape() {
        return defShape;
    }

    public static void setDefShape(RectangularShape defShape) {
        Node.defShape = defShape;
        //We can't replace the old shapes here.
    }
    
    //////////////////Getter and Setter for local values///////////////////////
    
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

    public RectangularShape getShape() {
        return shape;
    }
    
    public void setDefaultShape(Point pos) {
        shape = (RectangularShape) Node.defShape.clone();
        shape.setFrame(pos.x, pos.y, shape.getWidth(), shape.getHeight());
    }
    
    public void setShape(RectangularShape shape) {
        this.shape = shape;
        inherit = false;
    }
    
    public Color getColor() {
        if (inherit) return Node.defColor;
        else return color;
    }

    public void setColor(Color color) {
        this.color = color;
        inherit = false;
    }

    public Color getFillColor() {
        if (inherit) return Node.defFillColor;
        else return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        inherit = false;
    }

    public boolean isFillNode() {
        if (inherit) return Node.defFillNode;
        else return fillNode;
    }

    public void setFillNode(boolean fillNode) {
        this.fillNode = fillNode;
        inherit = false;
    }
    
    public boolean isAutoWidth() {
        if (inherit) return Node.defAutoWidth;
        else return autoWidth;
    }

    public void setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
        inherit = false;
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
        setDefaultShape(shape.getBounds().getLocation());
    }
        
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Node: " + label + (init?" i":"") + (fin?" f":"") + "@"+getShape().getCenterX()+","+getShape().getCenterY();
    }

}
