/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
    private static RectangularShape defShape = new RoundRectangle2D.Double(0,0,40,40,40,40);//new Ellipse2D.Double(0, 0, 40, 40);
    
    private String text = "";
    private JLabel label = new JLabel("");
    private boolean init = false;
    private boolean fin = false;
    private ArrayList<Edge> edges = new ArrayList<Edge>(); //redundant but good for simulation
   
    private int index;
    private boolean inherit = true;
    //graphical properties    
    private Color color = defColor;
    private Color fillColor = Color.WHITE;
    private boolean fillNode = false;
    private RectangularShape shape;
    private boolean autoWidth = false;
    private int preferredWidth;
    
    ////////////////Constructor///////////////////////////////////////////
    
    public Node(RectangularShape shape, Point pos, String name) {
        this.shape = (RectangularShape) shape.clone();
        this.shape.setFrame(pos.x, pos.y, shape.getWidth(), shape.getHeight());
        preferredWidth = (int)shape.getWidth();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        setText(name);
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
    
    public void setText(String text) {
        this.text = text;
        updateLabel();
    }
    
    public JLabel getLabel() {
        return label;
    }
    
    public String getText() {
        return text;
    }

    public RectangularShape getShape() {
        return shape;
    }
    
    public void setDefaultShape(Point pos) {
        shape = (RectangularShape) Node.defShape.clone();
        shape.setFrame(pos.x, pos.y, shape.getWidth(), shape.getHeight());
        preferredWidth = (int)shape.getWidth();
        updateLabel();
    }
    
    public void setShape(RectangularShape shape) {
        this.shape = shape;
        preferredWidth = (int)shape.getWidth();
        inherit = false;
        updateLabel();
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
        updateLabel();
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

    public int getPreferredWidth() {
        return preferredWidth;
    }
        
    public ArrayList<Edge> getEdges() {
        return edges;
    }
    
    private void updateLabel() {
        label.setText(parseString(text));
        if (isAutoWidth()) {
                getShape().setFrame(getShape().getX(), getShape().getY(), Math.max(getPreferredWidth(), label.getPreferredSize().width+4), getShape().getHeight());                
        } else if (getShape().getWidth()!=getPreferredWidth()) {
            getShape().setFrame(getShape().getX(), getShape().getY(), getPreferredWidth(), getShape().getHeight());
        }
        label.setSize((int) getShape().getWidth(), (int) getShape().getHeight());
    }
    
   public static String parseString(String s) {
        StringBuilder t = new StringBuilder();
        Stack<String> open = new Stack();
        boolean esc = false;
        boolean ins = false;
        boolean html = false;
        for (int i = 0; i< s.length(); i++) {
            final char c = s.charAt(i);
            if (esc) {
                t.append(c);
                esc = false;
                if (ins) {
                    t.append(open.pop());
                    ins = false;
                }
            } else if (c=='\\') {
                esc = true;
            } else if (ins && c!='{') {
                t.append(c).append(open.pop());
                ins = false;
            } else if (c=='_') {
                t.append("<sub>");
                open.add("</sub>");
                ins = true;
                html = true;
            } else if (c=='^') {
                t.append("<sup>");
                open.add("</sup>");
                ins = true;
                html = true;
            } else if (c=='{') {
                if (!ins) t.append(c);
                else ins = false;
            } else if (c=='}') {
                if (open.isEmpty()) t.append(c);
                else t.append(open.pop());
            } else {
                t.append(c);
            }
        }
        while (open.size()>1) t.append(open.pop());
        if (ins) {
            if (open.pop().equals("</sub>")) t.append('_');
            else t.append('^');
        } else if (!open.isEmpty()) t.append(open.pop());
        else if (esc) t.append('\\');
        if (html) {
            t.append("</html>");
            t.insert(0, "<html>");
        }
        return t.toString();
    }

    @Override
    public String toString() {
        return "Node: " + label + (init?" i":"") + (fin?" f":"") + "@"+getShape().getCenterX()+","+getShape().getCenterY();
    }

}
