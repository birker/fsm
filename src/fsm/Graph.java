/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import fsm.Edge.PathMode;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
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
    
    private int index = Integer.MAX_VALUE;
    private Element choice;
    private ArrayList<Element> active = new ArrayList<Element>();
    
    //Default Values Edge
    private Edge.PathMode defPathMode = Edge.PathMode.QUADRATIC_BEZIER;
    private Color defEdgeColor = Color.BLACK;
    private boolean defEdgeLabelRot = false;
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
        for (Edge e: getEdges()) e.rebuildPath();
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
        for (Edge e: getEdges()) {
            e.rebuildPath();
        }
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Vertex addVertex(Point position) {
        Vertex n = new Vertex(this, position, "v_{"+vertices.size()+"}");
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
        for (Edge e: new ArrayList<Edge>(edges)) {
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
            n.updateLabel();
        }
        for (Edge e: getEdges()) {
            e.rebuildPath();                
        }        
    }
    
    private static final int iterations = 10;
    private static final int deflength = 100;
    //private static final int delta = 3;
    
    private Point2D calcFrep(Vertex n1, Vertex n2) {
        Point2D p = new Point2D.Double(n2.getShape().getFrame().getCenterX() - n1.getShape().getFrame().getCenterX(),
                n2.getShape().getFrame().getCenterY() - n1.getShape().getFrame().getCenterY());
        double c = deflength * deflength / p.distance(new Point2D.Double(0, 0));
        p.setLocation(c*p.getX(),c*p.getY());
        return p;
    }

    private Point2D calcFattr(Vertex n1, Vertex n2) {
        Point2D p = new Point2D.Double(n2.getShape().getFrame().getCenterX() - n1.getShape().getFrame().getCenterX(),
                n2.getShape().getFrame().getCenterY() - n1.getShape().getFrame().getCenterY());
        System.out.println(p);
        double c = p.distanceSq(new Point2D.Double(0, 0)) / deflength;
        System.out.println(" "+c);
        p.setLocation(-c*p.getX(),-c*p.getY());
        return p;
    }    
    
    private Point2D calcForce(Vertex v) {
        Point2D f = new Point2D.Double(0, 0);
        for (Vertex n1: getVertices()) {
                if (n1 == v) continue;
                Point2D frep = calcFrep(n1,v);
                f.setLocation(f.getX()+frep.getX(),f.getY()+frep.getY());
                for (Edge e: n1.getEdges()) {
                    if (e.getTo() == v) {
                        Point2D fattr = calcFattr(n1,v);
                        f.setLocation(f.getX()+fattr.getX(),f.getY()+fattr.getY());                        
                    }
                }
                for (Edge e: v.getEdges()) {
                    if (e.getTo() == n1) {
                        Point2D fattr = calcFattr(n1,v);
                        f.setLocation(f.getX()+fattr.getX(),f.getY()+fattr.getY());     
                    }
                }
            //}
        }
        return f;
    }
    
    public void springEmbedder() {
        Point2D[] forces = new Point2D[getVertices().size()];
        
        for (int t=1; t <= iterations; t++) {
            for (int v = 0; v < getVertices().size(); v++) {
                forces[v] =  calcForce(getVertices().get(v));
            }
            for (int v = 0; v < getVertices().size(); v++) {
                getVertices().get(v).getShape().setFrame(getVertices().get(v).getShape().getX() + 0.01*forces[v].getX(),  getVertices().get(v).getShape().getY() + 0.01*forces[v].getY(),
                        getVertices().get(v).getShape().getWidth(), getVertices().get(v).getShape().getHeight());
            }
        }
        for (Edge e: getEdges()) {
            e.rebuildPath();
        }
        
    }
    
    public void springEmbedder2() {
        Point[] forces = new Point[getVertices().size()];
        //Point[] velocity = new Point[forces.length];
        for (int t=1; t <= iterations; t++) {
            for (int i = 0; i < forces.length; i++) {
                Vertex v = getVertices().get(i);
                forces[i] = new Point(0,0);
                for (int j = 0; j < forces.length; j++) {
                    if (i == j) continue;
                    Vertex u = getVertices().get(j);
                    //repulsion 200
                    Point p = new Point((int)v.getShape().getFrame().getCenterX() - (int)u.getShape().getFrame().getCenterX(),
                            (int)v.getShape().getFrame().getCenterY() - (int)u.getShape().getFrame().getCenterY());
                    int sqd = p.x * p.x + p.y * p.y;
                    forces[i].translate( (int) (900.0 * (p.x)/sqd),  (int) (900.0 * (p.y)/sqd));
                    //attraction 0.06
                    for (Edge e: v.getEdges()) {
                        p.setLocation((int)e.getTo().getShape().getFrame().getCenterX() - (int)v.getShape().getFrame().getCenterX(),
                            (int)e.getTo().getShape().getFrame().getY() - (int)v.getShape().getFrame().getY());
                        forces[i].translate((int)(0.10 * (p.x)), (int)(0.10 * (p.y)));
                    }
                    for (Edge e: u.getEdges()) {
                        if (e.getTo() == v) {
                            p.setLocation((int)e.getFrom().getShape().getFrame().getCenterX() - (int)v.getShape().getFrame().getCenterX(),
                                (int)e.getFrom().getShape().getFrame().getCenterY() - (int)v.getShape().getFrame().getCenterY());
                            forces[i].translate((int) (0.10 * (p.x)), (int) (0.10 * (p.y)));
                        }
                    }
                    //damping 0.85
                    forces[i].setLocation(forces[i].x*0.85, forces[i].y*0.85);
                }
            }
            for (int v = 0; v < getVertices().size(); v++) {
                getVertices().get(v).getShape().setFrame(
                        getVertices().get(v).getShape().getX()  + forces[v].getX(),  
                        getVertices().get(v).getShape().getY() + forces[v].getY(),
                        getVertices().get(v).getShape().getWidth(), getVertices().get(v).getShape().getHeight());
            }
        }
            for (Vertex n: getVertices()) {
                n.updateLabel();
            }
            for (Edge e: getEdges()) {
                e.rebuildPath();
            }
    }    
    
    public void springEmbedder4() {
        Point2D[] forces = new Point2D[getVertices().size()];
        //Point[] velocity = new Point[forces.length];
        for (int t=1; t <= iterations; t++) {
            for (int i = 0; i < forces.length; i++) {
                Vertex v = getVertices().get(i);
                forces[i] = new Point2D.Double(0,0);
                for (int j = 0; j < forces.length; j++) {
                    if (i == j) continue;
                    Vertex u = getVertices().get(j);
                    //repulsion 200
                    Point2D p = new Point2D.Double(v.getShape().getFrame().getX() - u.getShape().getFrame().getX(),
                            v.getShape().getFrame().getY() - u.getShape().getFrame().getY());
                    double sqd = p.getX() * p.getX() + p.getY() * p.getY();
                    forces[i].setLocation( forces[i].getX() +(900.0 * (p.getX())/sqd), forces[i].getY() + (900.0 * (p.getY())/sqd));
                    //attraction 0.06
                    for (Edge e: v.getEdges()) {
                        p.setLocation(e.getTo().getShape().getFrame().getX() - v.getShape().getFrame().getX(),
                            e.getTo().getShape().getFrame().getY() - v.getShape().getFrame().getY());
                        forces[i].setLocation( forces[i].getX() +(0.10 * (p.getX())),  forces[i].getY() + (0.10 * (p.getY())));
                    }
                    for (Edge e: u.getEdges()) {
                        if (e.getTo() == v) {
                            p.setLocation(e.getFrom().getShape().getFrame().getX() - v.getShape().getFrame().getX(),
                                e.getFrom().getShape().getFrame().getY() - v.getShape().getFrame().getY());
                            forces[i].setLocation( forces[i].getX() +(0.10 * (p.getX())),  forces[i].getY() + (0.10 * (p.getY())));
                        }
                    }
                    //damping 0.85
                    forces[i].setLocation(forces[i].getX()*0.85, forces[i].getY()*0.85);
                }
            }
            for (int v = 0; v < getVertices().size(); v++) {
                getVertices().get(v).getShape().setFrame(
                        getVertices().get(v).getShape().getX() - getVertices().get(v).getShape().getWidth()/2 + forces[v].getX(),  
                        getVertices().get(v).getShape().getY() - getVertices().get(v).getShape().getHeight()/2 + forces[v].getY(),
                        getVertices().get(v).getShape().getWidth(), getVertices().get(v).getShape().getHeight());
            }
        }
            for (Vertex n: getVertices()) {
                n.updateLabel();
            }
            for (Edge e: getEdges()) {
                e.rebuildPath();
            }
    }

    public void springEmbedder3() {
        Point[] forces = new Point[getVertices().size()];
        //Point[] velocity = new Point[forces.length];
        for (int t=1; t <= iterations; t++) {
            for (int i = 0; i < forces.length; i++) {
                Vertex v = getVertices().get(i);
                forces[i] = new Point(0,0);
                for (int j = 0; j < forces.length; j++) {
                    if (i == j) continue;
                    Vertex u = getVertices().get(j);
                    //repulsion 200
                    Point p = new Point((int)v.getShape().getFrame().getX() - (int)u.getShape().getFrame().getX(),
                            (int)v.getShape().getFrame().getY() - (int)u.getShape().getFrame().getY());
                    double sqd = deflength*deflength/p.distance(0, 0);//p.x * p.x + p.y * p.y;
                    forces[i].translate( (int) ((p.x)*sqd),  (int) ((p.y)*sqd));
                    //attraction 0.06
                    for (Edge e: v.getEdges()) {
                        p.setLocation((int)e.getTo().getShape().getFrame().getX() - (int)v.getShape().getFrame().getX(),
                            (int)e.getTo().getShape().getFrame().getY() - (int)v.getShape().getFrame().getY());
                        sqd = p.distanceSq(0, 0) / deflength;
                        forces[i].translate((int)(sqd * (p.x)), (int)(sqd * (p.y)));
                    }
                    for (Edge e: u.getEdges()) {
                        if (e.getTo() == v) {
                            p.setLocation((int)e.getFrom().getShape().getFrame().getX() - (int)v.getShape().getFrame().getX(),
                                (int)e.getFrom().getShape().getFrame().getY() - (int)v.getShape().getFrame().getY());
                            sqd = p.distanceSq(0, 0) / deflength;
                            forces[i].translate((int)(sqd * (p.x)), (int)(sqd * (p.y)));
                        }
                    }
                    //damping 0.85
                    forces[i].setLocation(forces[i].x*0.85, forces[i].y*0.85);
                }
            }
            for (int v = 0; v < getVertices().size(); v++) {
                getVertices().get(v).getShape().setFrame(getVertices().get(v).getShape().getX() + forces[v].getX(),  getVertices().get(v).getShape().getY() + forces[v].getY(),
                        getVertices().get(v).getShape().getWidth(), getVertices().get(v).getShape().getHeight());
            }
        }
            for (Vertex n: getVertices()) {
                n.updateLabel();
            }
            for (Edge e: getEdges()) {
                e.rebuildPath();
            }
    }    
    
    private static final float SPEED_DIVISOR = 800;
    private static final float AREA_MULTIPLICATOR = 10000;
    //Properties
    //private float area = 10000f;
    private double gravity = 10;
    private double speed = 1;
    
    public void springEmbedder6() {
        float[] dx = new float[getVertices().size()];
        float[] dy = new float[getVertices().size()];

        float maxDisplace = (float) (Math.sqrt(AREA_MULTIPLICATOR * 10000) / 10f);					// Déplacement limite : on peut le calibrer...
        float k = (float) Math.sqrt((AREA_MULTIPLICATOR * 10000) / (1f + getVertices().size()));		// La variable k, l'idée principale du layout.

        for (Vertex N1 : getVertices()) {
            for (Vertex N2 : getVertices()) {	// On fait toutes les paires de noeuds
                if (N1 != N2) {
                    float xDist = (float) (N1.getShape().getCenterX() - N2.getShape().getCenterX());	// distance en x entre les deux noeuds
                    float yDist = (float) (N1.getShape().getCenterY() - N2.getShape().getCenterY());
                    float dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);	// distance tout court

                    if (dist > 0) {
                        float repulsiveF = k * k / dist;			
                        dx[getVertices().indexOf(N1)] += xDist / dist * repulsiveF;		// on l'applique...
                        dy[getVertices().indexOf(N1)] += yDist / dist * repulsiveF;	// Force de répulsion
                    }
                }
            }
        }
        for (Edge E : getEdges()) {
            // Idem, pour tous les noeuds on applique la force d'attraction

            Vertex Nf = E.getFrom();
            Vertex Nt = E.getTo();

            float xDist = (float)(Nf.getShape().getCenterX() - Nt.getShape().getCenterX());
            float yDist = (float)(Nf.getShape().getCenterY() - Nt.getShape().getCenterY());
            float dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);

            float attractiveF = dist * dist / k;

            if (dist > 0) {
                dx[getVertices().indexOf(Nf)] -= xDist / dist * attractiveF;
                dy[getVertices().indexOf(Nf)] -= yDist / dist * attractiveF;
                dx[getVertices().indexOf(Nt)] += xDist / dist * attractiveF;
                dy[getVertices().indexOf(Nt)] += yDist / dist * attractiveF;
            }
        }
        // gravity
        for (Vertex n : getVertices()) {
            float d = (float) Math.sqrt(n.getShape().getCenterX() * n.getShape().getCenterX() + n.getShape().getCenterY() * n.getShape().getCenterY());
            float gf = 0.01f * k * (float) gravity * d;
            dx[getVertices().indexOf(n)] -= gf * n.getShape().getCenterX() / d;
            dy[getVertices().indexOf(n)] -= gf * n.getShape().getCenterY() / d;
        }
        // speed
        for (Vertex n : getVertices()) {
            dx[getVertices().indexOf(n)] *= speed / SPEED_DIVISOR;
            dy[getVertices().indexOf(n)] *= speed / SPEED_DIVISOR;
        }
        for (Vertex n : getVertices()) {
            // Maintenant on applique le déplacement calculé sur les noeuds.
            // nb : le déplacement à chaque passe "instantanné" correspond à la force : c'est une sorte d'accélération.
            
            float xDist = dx[getVertices().indexOf(n)];
            float yDist = dy[getVertices().indexOf(n)];
            float dist = (float) Math.sqrt(dx[getVertices().indexOf(n)] * dx[getVertices().indexOf(n)] + dy[getVertices().indexOf(n)] * dy[getVertices().indexOf(n)]);
            System.out.println(dist);
            if (dist > 0 ) {
                float limitedDist = Math.min(maxDisplace * ((float) speed / SPEED_DIVISOR), dist);
                n.getShape().setFrame(n.getShape().getX()+ xDist / dist * limitedDist,
                        n.getShape().getY()+ yDist / dist * limitedDist,
                        n.getShape().getWidth(), n.getShape().getHeight());
            }
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
