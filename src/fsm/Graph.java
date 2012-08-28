/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import fsm.Edge.PathMode;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

/**
 *
 * @author Konnarr
 */
public class Graph extends Observable implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    
    private String comment = "";
    private String name = "";
    
    private Element choice;
    private ArrayList<Element> active = new ArrayList<Element>();
    
    //Default Values Edge
    private Edge.PathMode defPathMode = Edge.PathMode.QUADRATIC_BEZIER;
    private Color defEdgeColor = Color.BLACK;
    private boolean defEdgeLabelRot = true;
    private boolean defDirected = true;
    //Default Values Vertex
    private Color defVertexColor = Color.BLACK;
    private Color defFillVertexColor = Color.WHITE;
    private boolean defFillVertex = false;
    private boolean defVertexAutoWidth = true;
    private boolean defLabelOutsideVertex = false;
    private RectangularShape defVertexShape = new RoundRectangle2D.Double(0,0,40,40,40,40);    

    //////////////////////Getter and Setter for default values/////////////////
        
    public PathMode getDefPathMode() {
        return defPathMode;
    }

    public void setDefPathMode(PathMode defPathMode) {
        this.defPathMode = defPathMode;
    }

    public Color getDefEdgeColor() {
        return defEdgeColor;
    }

    public void setDefEdgeColor(Color defEdgeColor) {
        this.defEdgeColor = defEdgeColor;
    }

    public boolean isDefEdgeLabelRot() {
        return defEdgeLabelRot;
    }

    public void setDefEdgeLabelRot(boolean defEdgeLabelRot) {
        this.defEdgeLabelRot = defEdgeLabelRot;
    }

    public boolean isDefDirected() {
        return defDirected;
    }

    public void setDefDirected(boolean defDirected) {
        this.defDirected = defDirected;
    }

    public Color getDefVertexColor() {
        return defVertexColor;
    }

    public void setDefVertexColor(Color defVertexColor) {
        this.defVertexColor = defVertexColor;
    }

    public Color getDefFillVertexColor() {
        return defFillVertexColor;
    }

    public void setDefFillVertexColor(Color defFillVertexColor) {
        this.defFillVertexColor = defFillVertexColor;
    }

    public boolean isDefFillVertex() {
        return defFillVertex;
    }

    public void setDefFillVertex(boolean defFillVertex) {
        this.defFillVertex = defFillVertex;
    }

    public boolean isDefVertexAutoWidth() {
        return defVertexAutoWidth;
    }

    public void setDefVertexAutoWidth(boolean defVertexAutoWidth) {
        this.defVertexAutoWidth = defVertexAutoWidth;
    }

    public boolean isDefLabelOutsideVertex() {
        return defLabelOutsideVertex;
    }

    public void setDefLabelOutsideVertex(boolean defLabelOutsideVertex) {
        this.defLabelOutsideVertex = defLabelOutsideVertex;
    }

    public RectangularShape getDefVertexShape() {
        return defVertexShape;
    }

    public void setDefVertexShape(RectangularShape defVertexShape) {
        this.defVertexShape = defVertexShape;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    public Graph() {
        
    }
    
    public ArrayList<Element> getActive() {
        return active;
    }

    public Element getChoice() {
        return choice;
    }

    public void setChoice(Element choice) {
        this.choice = choice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vertex addVertex(Point position) {
        Vertex n = new Vertex(this, getDefVertexShape(), position, "v_{"+vertices.size()+"}");
        vertices.add(n);
        //notifyObservers();
        return n;
    }
    
    public Edge addEdge(Vertex from, Vertex to) {
        Edge e = new Edge(this, from, to);
        edges.add(e);
        from.getEdges().add(e);
        return e;
    }
    
    public void removeEdge(Edge e) {
        e.getFrom().getEdges().remove(e);
        if (choice == e) choice = null;
        active.remove(e);
        edges.remove(e);
    }
    
    public void removeVertex(Vertex n) {
        if (choice == n) choice = null;
        active.remove(n);
        for (Edge e: (ArrayList<Edge>) edges.clone()) {
            if (e.getFrom() == n || e.getTo() == n) removeEdge(e);
        }
        vertices.remove(n);
    }
    
    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
    
    public ArrayList<Edge> getEdges() {
        return edges;
    }
    
    public void alignVertices() {
        Iterator<Vertex> it = getVertices().iterator();
        if (!it.hasNext()) return;
        Vertex n0 = it.next();
        while (it.hasNext()) {
            Vertex n = it.next();
            double offx = n0.getShape().getX();
            double offy = n0.getShape().getY();
            double facx = Math.round((n.getShape().getX() - offx) / (3*n0.getPreferredWidth())) ;
            double facy = Math.round((n.getShape().getY() - offy) / (3*n0.getShape().getHeight())) ;
            double x = offx+facx*3*n0.getPreferredWidth();
            double y = offy+facy*3*n0.getShape().getHeight();
            //check if there is already a node;
            boolean found = true;
            while (found) {
                found = false;
            for (Vertex n2: getVertices()) {
                if (n==n2) break;
                if (Math.abs(n2.getShape().getX() - x)<1 && Math.abs(n2.getShape().getY() - y)<1) {
                    facx++;
                    x = offx+facx*3*n0.getPreferredWidth();
                    found = true;
                }
            }
            }
            n.getShape().setFrame(x,y,n.getShape().getWidth(),n.getShape().getHeight());
        }
        for (Edge e: getEdges()) {
            e.rebuildPath();                
        }        
    }
        
    public void notifyObs(Element o) {
        setChanged();
        notifyObservers(o);
    }

    public void notifyObs() {
        notifyObs(null);
    }
    
    public Vertex getVertexByName(String name) {
        for (Vertex v: vertices) {
            if (v.getName().equals(name)) return v;
        }
        return null;
    }
    
    public Vertex getVertexByPosition(Point p) {
        for (Vertex v: vertices) {
            if (v.getShape().contains(p)) return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Graph{" + "vertices=" + vertices + ", edges=" + edges + ", comment=" + comment + ", name=" + name + ", choice=" + choice + ", active=" + active + ", defPathMode=" + defPathMode + ", defEdgeColor=" + defEdgeColor + ", defEdgeLabelRot=" + defEdgeLabelRot + ", defDirected=" + defDirected + ", defVertexColor=" + defVertexColor + ", defFillVertexColor=" + defFillVertexColor + ", defFillVertex=" + defFillVertex + ", defVertexAutoWidth=" + defVertexAutoWidth + ", defLabelOutsideVertex=" + defLabelOutsideVertex + ", defVertexShape=" + defVertexShape + '}';
        //return "";
    }
    
    
    
}
