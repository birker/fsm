/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

/**
 *
 * @author Konnarr
 */
public class Graph extends Observable implements Serializable {
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    
    private boolean directed;
    
    private Element choice;
    private ArrayList<Element> active = new ArrayList<Element>();
    
    public Graph(boolean directed) {
        this.directed = directed;
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

    public Vertex addVertex(Point position) {
        Vertex n = new Vertex(Vertex.getDefShape(), position, "v_{"+vertices.size()+"}");
        vertices.add(n);
        //notifyObservers();
        return n;
    }
    
    public Edge addEdge(Vertex from, Vertex to, boolean autoSP) {
        Edge e = new Edge(from, to, autoSP);
        e.directed = directed;
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
        
    public void notifyObs() {
        setChanged();
        notifyObservers();
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
        for (Edge e: getEdges()) {
            e.directed = directed;
        }
    }
}
