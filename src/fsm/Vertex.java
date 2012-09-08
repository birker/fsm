/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
public class Vertex implements Serializable, Element, Cloneable {

    public enum ShapeType {

        ELLIPSE("Ellipse"), RECTANGLE("Rechteck"), ROUNDRECT("abger. Rechteck");

        private ShapeType(String name) {
            this.name = name;
        }
        
        static public ShapeType getEnum(RectangularShape s) {
            if (s instanceof Ellipse2D) return ELLIPSE;
            else if (s instanceof Rectangle2D) return RECTANGLE;
            else if (s instanceof RoundRectangle2D) return ROUNDRECT;
            return null;
        }
        
        public Class<?> getShapeClass() {
            if (this.equals(ELLIPSE)) return Ellipse2D.Double.class;
            else if (this.equals(RECTANGLE)) return Rectangle2D.Double.class;
            else return RoundRectangle2D.Double.class;
        }
        
        private final String name;

        @Override
        public String toString() {
            return name;
        }
    }
    
    private static final long serialVersionUID = 2345541351034924475L;
    private Graph parent;
    private JLabel label = new JLabel("");
    private String name = "";
    private boolean init = false;
    private boolean fin = false;
    private ArrayList<Edge> edges = new ArrayList<Edge>(); //redundant but good for simulation
    private String comment = "";
    private int index;
    //private boolean inherit = true;
    //graphical properties    
    private boolean labelOutside;// = defLabelOutside;
    private Color color;// = defColor;
    private Color fillColor;// = Color.WHITE;
    private boolean fillVertex;// = false;
    private RectangularShape shape;
    private boolean autoWidth;// = true;
    private int preferredWidth;

    ////////////////Constructor///////////////////////////////////////////
    public Vertex(Graph g, RectangularShape shape, Point pos, String name) {
        this.parent = g;
        this.shape = (RectangularShape) shape.clone();
        this.shape.setFrame(pos.x, pos.y, shape.getWidth(), shape.getHeight());
        preferredWidth = (int) shape.getWidth();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        setName(name);
        labelOutside = parent.isDefLabelOutsideVertex();
        color = parent.getDefVertexColor();
        fillColor = parent.getDefFillVertexColor();
        fillVertex = parent.isDefFillVertex();
        autoWidth = parent.isDefVertexAutoWidth();
    }
    //////////////////Getter and Setter for local values///////////////////////

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public void setName(String text) {
        this.name = text;
        updateLabel();
    }

    public JLabel getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public RectangularShape getShape() {
        return shape;
    }

    public void setDefaultValues() {
        Point pos = shape.getBounds().getLocation();
        shape = (RectangularShape) parent.getDefVertexShape().clone();
        shape.setFrame(pos.x, pos.y, shape.getWidth(), shape.getHeight());
        preferredWidth = (int) shape.getWidth();
        color = parent.getDefVertexColor();
        fillColor = parent.getDefFillVertexColor();
        fillVertex = parent.isDefFillVertex();
        labelOutside = parent.isDefLabelOutsideVertex();
        autoWidth = parent.isDefVertexAutoWidth();
        label.setVerticalAlignment(SwingConstants.CENTER);
        updateLabel();
    }
    
    public void setShape(RectangularShape shape) {
        this.shape = shape;
        preferredWidth = (int) shape.getWidth();
        updateLabel();
    }

    public Color getColor() {
            return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getFillColor() {
            return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public boolean isFillVertex() {
            return fillVertex;
    }

    public void setFillVertex(boolean fillNode) {
        this.fillVertex = fillNode;
    }

    public boolean isLabelOutside() {
        return labelOutside;
    }

    public void setLabelOutside(boolean labelOutside) {
        this.labelOutside = labelOutside;
    }

    public boolean isAutoWidth() {
            return autoWidth;
    }

    public void setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
        updateLabel();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /*public boolean isInherit() {
        return inherit;
    }

    public void setInherit() {
        inherit = true;
        setDefaultShape(shape.getBounds().getLocation());
    }*/

    public int getPreferredWidth() {
        return preferredWidth;
    }
    
    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
        updateLabel();
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void updateLabel() {
        label.setText(parseString(name));
        if (isLabelOutside()) {
            label.setLocation((int) getShape().getX(), (int) getShape().getY());
        } else {
            if (isAutoWidth()) {
                getShape().setFrame(getShape().getX(), getShape().getY(), Math.max(getPreferredWidth(), label.getPreferredSize().getWidth() + 10), getShape().getHeight());
                for (Edge e: getEdges()) {
                    e.rebuildPath();
                }
            } else if (getShape().getWidth() != getPreferredWidth()) {
                getShape().setFrame(getShape().getX(), getShape().getY(), getPreferredWidth(), getShape().getHeight());
                for (Edge e: getEdges()) {
                    e.rebuildPath();
                }
            }
            label.setBounds(getShape().getBounds()); //.setSize((int) getShape().getWidth(), (int) getShape().getHeight());
        }
    }

    public static String parseString(String s) {
        StringBuilder t = new StringBuilder();
        Stack<String> open = new Stack<String>();
        boolean esc = false;
        boolean ins = false;
        boolean html = false;
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (esc) {
                t.append(c);
                esc = false;
                if (ins) {
                    t.append(open.pop());
                    ins = false;
                }
            } else if (c == '\\') {
                esc = true;
            } else if (ins && c != '{') {
                t.append(c).append(open.pop());
                ins = false;
            } else if (c == '_') {
                t.append("<sub>");
                open.add("</sub>");
                ins = true;
                html = true;
            } else if (c == '^') {
                t.append("<sup>");
                open.add("</sup>");
                ins = true;
                html = true;
            } else if (c == '{') {
                if (!ins) {
                    t.append(c);
                } else {
                    ins = false;
                }
            } else if (c == '}') {
                if (open.isEmpty()) {
                    t.append(c);
                } else {
                    t.append(open.pop());
                }
            } else {
                t.append(c);
            }
        }
        while (open.size() > 1) {
            t.append(open.pop());
        }
        if (ins) {
            if (open.pop().equals("</sub>")) {
                t.append('_');
            } else {
                t.append('^');
            }
        } else if (!open.isEmpty()) {
            t.append(open.pop());
        } else if (esc) {
            t.append('\\');
        }
        if (html) {
            t.append("</nobr></html>");
            t.insert(0, "<html><nobr>");
        }
        return t.toString();
    }

    @Override
    public String toString() {
        return "Node: " + name + (init ? " i" : "") + (fin ? " f" : "") + "@" + getShape().getCenterX() + "," + getShape().getCenterY();
    }

    /*@Override
     protected Object clone() throws CloneNotSupportedException {
     Vertex tmp = (Vertex) super.clone();
     tmp.
     return tmp;
     }*/

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vertex other = (Vertex) obj;
        return this == other;
    }
    
}
