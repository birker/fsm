/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;

/**
 *
 * @author Konnarr
 */
public class Edge implements Serializable, Element {
    public static final int LINE = 0;
    public static final int QUADRATIC_BEZIER = 1;
    public static final int CUBIC_BEZIER =2;
    public static final int AUTOMATIC = -1;
    public static final int NORTH = 270;
    public static final int WEST = 0;
    public static final int SOUTH = 90;
    public static final int EAST = 180;
    
    
    private static final long serialVersionUID = 2345541351034924475L;    

    private Node from;
    private Node to;
    private ArrayList<String> trans = new ArrayList<String>();
    private JLabel label = new JLabel();
    
    //graphical properties
    private int degIn = AUTOMATIC;
    private int degOut = AUTOMATIC;
    private int pathMode = QUADRATIC_BEZIER;
    private ArrayList<Point> supportPoints = new ArrayList<Point>();
    private boolean inherit = true;
    private Color color;
    private Path2D path;
    
    //index, for constructing automaton piece by pieve.
    private int index;
    
    public Edge(Node from, Node to) {
        this.from = from;
        from.getEdges().add(this);
        this.to = to;
        
        rebuildPath();
        label.setText("");
        label.setSize(label.getPreferredSize());
        label.setVisible(true);
    }
    
    //sollte readonly sein. nur zum malen;
    public Path2D getPath() {
        return path;
    }
    
    public Node getFrom() {
        return from;
    }
    
    public void setFrom(Node node) {
        from.getEdges().remove(this);
        from = node;
        from.getEdges().add(this);
        rebuildPath();
    }
    
    public Node getTo() {
        return to;
    }
    
    public void setTo(Node node) {
        to = node;
        rebuildPath();
    }
    
    /*/zu public
    public ArrayList<Point> getSupportPoints() {
        return supportPoints;
    }//*/
    
    public void addSupportPoint(Point p) {
        if (p == null) throw new IllegalArgumentException("Can't add NULL");
        supportPoints.add(p);
        rebuildPath();
    }

    public void addSupportPoint(int index, Point p) {
        if (p == null) throw new IllegalArgumentException("Can't add NULL");
        supportPoints.add(index, p);
        rebuildPath();
    }    
    
    public void removeSupportPoint(int index) {
        supportPoints.remove(index);
        rebuildPath();
    }
    
    public void removeSupportPoint(Point p) {
        supportPoints.remove(p);
        rebuildPath();
    }
    
    public Point getSupportPoint(int index) {
        return supportPoints.get(index);
    }    
    
    public int getSupportPointCount() {
        return supportPoints.size();
    }
    
    public Iterator getSupportPointIterator() {
        return supportPoints.iterator();
    }
    
    //zu public
    public ArrayList<String> getTransitions() {
        return trans;
    }//*/
    
    private void updateLabel() {
        label.setText("");
        for (String s : trans) {
            label.setText(label.getText()+(label.getText().equals("")?"":",")+s);
        }
        label.setSize(label.getPreferredSize());
    }
    
    public void addTransition(String s) {
        if (s == null) throw new IllegalArgumentException("Can't add NULL");
        trans.add(s);
        updateLabel();
    }
    
    public void removeTransition(int index) {
        trans.remove(index);
        updateLabel();
    }
    
    public void removeTransition(String s) {
        trans.remove(s);
        updateLabel();
    }
    
    public String getTransition(int index) {
        return trans.get(index);
    }    
    
    public int getTransitionCount() {
        return trans.size();
    }
    
    public Iterator getTransitionIterator() {
        return trans.iterator();
    }
    
    public boolean containsTransition(String s) {
        return trans.contains(s);
    }

    //sollte readonly sein. nur zu lesen zum malen.
    public JLabel getLabel() {
        return label;
    }

    public int getDegIn() {
        return degIn;
    }

    public void setDegIn(int degIn) {
        if (degIn<-1 || degIn>359) throw new IllegalArgumentException("0 to 359 or -1");
        this.degIn = degIn;
        rebuildPath();
    }

    public int getDegOut() {
        return degOut;
    }

    public void setDegOut(int degOut) {
        if (degOut<-1 || degOut>359) throw new IllegalArgumentException("0 to 359 or -1");
        this.degOut = degOut;
        rebuildPath();
    }
    
    public boolean isInherit() {
        return inherit;
    }
    
    public void setInherit() {
        inherit = true;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    static private Point2D getIntersectionPoint(Point2D p, RectangularShape s) {
        Point2D line = new Point2D.Double(p.getX()-s.getCenterX(),p.getY()-s.getCenterY());
        //normalize line (so length is 1 and we can walk pixel by pixel)
//        double length = line.distance(0, 0);
        double length = Math.abs(Math.abs(line.getX())>Math.abs(line.getY())?line.getX():line.getY());
        line.setLocation(line.getX()/length, line.getY()/length);
        //get starting point
        double x = (Math.abs(line.getX())>Math.abs(line.getY())?s.getWidth()/2:s.getHeight()/2);
        Point2D q = new Point2D.Double(s.getCenterX() + line.getX()*x, s.getCenterY() + line.getY()*x);
        //while outside walk to the center
        while (!s.contains(q)) {
            q.setLocation(q.getX() - line.getX(), q.getY() - line.getY());
        }
        //walk just outside the border
        while (s.contains(q)) {
            q.setLocation(q.getX() + line.getX(), q.getY() + line.getY());
        }
        q.setLocation(q.getX() + line.getX(), q.getY() + line.getY());
        return q;
    }
    
    public void rebuildPath() {
        Iterator<Point> itr = supportPoints.iterator();
        Point2D element;
        //starting point
        if (degOut != AUTOMATIC) element = new Point2D.Double(from.getShape().getCenterX()+Math.cos(degOut*Math.PI/180),from.getShape().getCenterY()+Math.sin(degOut*Math.PI/180));
        else if (!supportPoints.isEmpty()) element = supportPoints.get(0);
        else element = new Point2D.Double(to.getShape().getCenterX(),to.getShape().getCenterY()+0.1);
        Point2D start = getIntersectionPoint(element, from.getShape());
        //finish point
        if (degIn != AUTOMATIC) element = new Point2D.Double(to.getShape().getCenterX()+Math.cos(degIn*Math.PI/180),to.getShape().getCenterY()+Math.sin(degIn*Math.PI/180));
        else if (!supportPoints.isEmpty()) element = supportPoints.get(supportPoints.size()-1);
        else element = new Point2D.Double(from.getShape().getCenterX(),from.getShape().getCenterY()+0.1);
        Point2D finish = getIntersectionPoint(element, to.getShape());
        
        path = new Path2D.Float();
        path.moveTo(start.getX(), start.getY());
        if (pathMode==LINE) {
            while (itr.hasNext()) {
                element = itr.next();
                path.lineTo(element.getX(), element.getY());    
            }
            
        } else if (pathMode == QUADRATIC_BEZIER || pathMode == CUBIC_BEZIER) {
            if (itr.hasNext()) {
                element = itr.next();
            Point2D h = new Point2D.Double((path.getCurrentPoint().getX()+element.getX())/2,(path.getCurrentPoint().getY()+element.getY())/2);
            path.lineTo(h.getX(), h.getY());
            while (itr.hasNext()) {
                Point2D b = itr.next();
                h.setLocation((b.getX()+element.getX())/2,(b.getY()+element.getY())/2);
                if (pathMode == QUADRATIC_BEZIER) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                element = b;
            }
            h.setLocation((finish.getX()+element.getX())/2,(finish.getY()+element.getY())/2);
            if (pathMode == QUADRATIC_BEZIER) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
            else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
            element = h;
            }
        }
        path.lineTo(finish.getX(), finish.getY());
        //arrow
        Point2D t = new Point2D.Double(finish.getX()-element.getX(),finish.getY()-element.getY());
        double n = Math.tan(25*Math.PI/180);
        double m = Math.sqrt(1+n*n)*t.distance(0, 0)/10;
        path.lineTo(finish.getX()-((finish.getX()-element.getX())-n*(finish.getY()-element.getY()))/m,
                finish.getY()-((finish.getY()-element.getY())+n*(finish.getX()-element.getX()))/m);
        path.moveTo(finish.getX(),finish.getY());
        path.lineTo(finish.getX()-((finish.getX()-element.getX())+n*(finish.getY()-element.getY()))/m,
                finish.getY()-((finish.getY()-element.getY())-n*(finish.getX()-element.getX()))/m);
        repositionLabel();
    }
    
    public void repositionLabel() {
        Point2D line = new Point2D.Double(
                (supportPoints.isEmpty()?to.getShape().getCenterX():supportPoints.get(0).x) - from.getShape().getCenterX(),
                (supportPoints.isEmpty()?to.getShape().getCenterY():supportPoints.get(0).y) - from.getShape().getCenterY()); 
        Point2D p = new Point2D.Double(from.getShape().getCenterX() + line.getX()/2, from.getShape().getCenterY() + line.getY()/2);
        label.setLocation((int)p.getX(), (int)p.getY());
    }
    
    @Override
    public String toString() {
        return "Edge: " + label/*.getText()*/ + "@"+from+","+to;
    }
}
