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
import java.util.ListIterator;
import javax.swing.JLabel;

/**
 *
 * @author Konnarr
 */
public class Edge implements Serializable, Element {
    private static final long serialVersionUID = 2345541351034924475L;    

    public static final int LINE = 0;
    public static final int QUADRATIC_BEZIER = 1;
    public static final int CUBIC_BEZIER =2;
    public static final int AUTOMATIC = -1;
    public static final int NORTH = 270;
    public static final int WEST = 0;
    public static final int SOUTH = 90;
    public static final int EAST = 180;
    
    private static int defPathMode = QUADRATIC_BEZIER;
    private static Color defColor = Color.BLACK;
    
    private Node from;
    private Node to;
    private ArrayList<String> trans = new ArrayList<String>();
    private JLabel label = new JLabel();
    
    //graphical properties
    private int degIn = AUTOMATIC;
    private int degOut = AUTOMATIC;
    private ArrayList<Point> supportPoints = new ArrayList<Point>();
    private Path2D path;
    private boolean inherit = true;
    private int pathMode = QUADRATIC_BEZIER;
    private Color color = defColor;
    
    //index, for constructing automaton piece by pieve.
    private int index;
    
    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        //TODO automatische SupportPoints
//        java.awt.EventQueue.invokeLater(new Runnable() {

//            @Override
//            public void run() {
                rebuildPath();
//            }
//        });
        
        label.setText("");
        label.setSize(label.getPreferredSize());
        label.setVisible(true);
    }

    public static Color getDefColor() {
        return defColor;
    }

    public static void setDefColor(Color defColor) {
        Edge.defColor = defColor;
    }

    public static int getDefPathMode() {
        return defPathMode;
    }

    public static void setDefPathMode(int defPathMode) {
        if (defPathMode<0 || defPathMode > 2) throw new IllegalArgumentException("pathMode must be of 0, 1 or 2");
        Edge.defPathMode = defPathMode;
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
        if (inherit) return defColor;
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        inherit = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPathMode() {
        if (inherit) return defPathMode;
        return pathMode;
    }

    public void setPathMode(int pathMode) {
        if (pathMode<0 || pathMode>2) throw new IllegalArgumentException("pathMode must be 0, 1 ot 2");
        this.pathMode = pathMode;
        inherit = false;
        rebuildPath();
    }
    
    public boolean hit(Point2D p, int distance) {
        return path.intersects(p.getX()-distance, p.getY()-distance, 2*distance, 2*distance);
    }
    
    static private Point2D getIntersectionPoint(Point2D p, RectangularShape s) {
        Point2D line = new Point2D.Double(p.getX()-s.getCenterX(),p.getY()-s.getCenterY());
        //normalize line (so length is 1 and we can walk pixel by pixel)
        double length = Math.abs(Math.abs(line.getX())>Math.abs(line.getY())?line.getX():line.getY());
        if (length==0) line.setLocation(0,1); //if we're right at the center walk downwards
        else line.setLocation(line.getX()/length, line.getY()/length);
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
        return q;
    }
    
    private void drawArrow(Path2D path, Point2D pos, Point2D from, double angleDeg, double length) {
        final double n = Math.tan(angleDeg*Math.PI/180); 
        final double m = Math.sqrt(1+n*n)*pos.distance(from)/length;
        //System.out.println(n+ " "+m);
        path.lineTo(pos.getX()-((pos.getX()-from.getX())-n*(pos.getY()-from.getY()))/m,
                pos.getY()-((pos.getY()-from.getY())+n*(pos.getX()-from.getX()))/m);
        path.lineTo(pos.getX()-((pos.getX()-from.getX())+n*(pos.getY()-from.getY()))/m,
                pos.getY()-((pos.getY()-from.getY())-n*(pos.getX()-from.getX()))/m);
        path.lineTo(pos.getX(), pos.getY());
    }
    
    public void rebuildPath() {
        //Iterator<Point> itr = supportPoints.iterator();
        ListIterator<Point> itr = supportPoints.listIterator();
        Point2D element;
        //starting point
        if (degOut != AUTOMATIC) element = new Point2D.Double(from.getShape().getCenterX()+Math.cos(degOut*Math.PI/180),from.getShape().getCenterY()+Math.sin(degOut*Math.PI/180));
        else if (!supportPoints.isEmpty()) element = supportPoints.get(0);
        else element = new Point2D.Double(to.getShape().getCenterX(),to.getShape().getCenterY()+0.1);
        Point2D start = getIntersectionPoint(element, from.getShape());
        //finish point
        if (degIn != AUTOMATIC) element = new Point2D.Double(to.getShape().getCenterX()+Math.cos(degIn*Math.PI/180),to.getShape().getCenterY()+Math.sin(degIn*Math.PI/180));
        else if (!supportPoints.isEmpty()) element = supportPoints.get(supportPoints.size()-1);
        else element = new Point2D.Double(from.getShape().getCenterX(),from.getShape().getCenterY());
        Point2D finish = getIntersectionPoint(element, to.getShape());
        
        path = new Path2D.Float();
        element = start;
        path.moveTo(element.getX(), element.getY());
        if (!supportPoints.isEmpty()) {
            if (pathMode==LINE) {
                while (itr.hasNext()) {
                    element = itr.next();
                    path.lineTo(element.getX(), element.getY());    
                }
            } else if (pathMode == QUADRATIC_BEZIER || pathMode == CUBIC_BEZIER) {
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
            }
        }
        path.lineTo(finish.getX(), finish.getY());
        
        //arrow
        
        if (Math.abs(finish.distance(element))<1E-10) {
            Point2D t = new Point2D.Double(finish.getX()-from.getShape().getCenterX(),finish.getY()-from.getShape().getCenterY());
            t.setLocation(finish.getX()+2*t.getX(), finish.getY()+2*t.getY());
            drawArrow(path,finish,t,25,10);
        }   
        else drawArrow(path,finish,element,25,10);
        
        /* There seems to be no elegant way to determine if a Point lies 
         * within a specific distance of a line but to close the whole path 
         * and then intersect. This has to be done manually all the way back, 
         * because he would just lineTo the starting point, if done automatically.*/
        //Path2D path2 = (Path2D) path.clone();
        //ListIterator<Point> itr = supportPoints.listIterator(supportPoints.size()-1);
        if (!supportPoints.isEmpty()) {
            if (pathMode==LINE) {
                while (itr.hasPrevious()) {
                    element = itr.previous();
                    path.lineTo(element.getX(), element.getY());    
                }
            } else if (pathMode == QUADRATIC_BEZIER || pathMode == CUBIC_BEZIER) {
                element = itr.previous();
                Point2D h = new Point2D.Double((path.getCurrentPoint().getX()+element.getX())/2,(path.getCurrentPoint().getY()+element.getY())/2);
                path.lineTo(h.getX(), h.getY());
                while (itr.hasPrevious()) {
                    Point2D b = itr.previous();
                    h.setLocation((b.getX()+element.getX())/2,(b.getY()+element.getY())/2);
                    if (pathMode == QUADRATIC_BEZIER) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                    else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                    element = b;
                }
                h.setLocation((start.getX()+element.getX())/2,(start.getY()+element.getY())/2);
                if (pathMode == QUADRATIC_BEZIER) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
            }
        }
        path.closePath();
        
        repositionLabel();
    }
    
    public void repositionLabel() {
        Point2D line = new Point2D.Double(
                (supportPoints.isEmpty()?to.getShape().getCenterX():supportPoints.get(0).x) - from.getShape().getCenterX(),
                (supportPoints.isEmpty()?to.getShape().getCenterY():supportPoints.get(0).y) - from.getShape().getCenterY()); 
        Point2D p = new Point2D.Double(from.getShape().getCenterX() + line.getX()/2, from.getShape().getCenterY() + line.getY()/2);
        label.setLocation((int)p.getX()+1, (int)p.getY()+1);
        if (path.intersects(label.getBounds()))
            label.setLocation((int)p.getX(), (int)p.getY()-label.getHeight()-1);
        
    }
    
    @Override
    public String toString() {
        return "Edge: " + label.getText() + "@"+from+","+to;
    }
}
