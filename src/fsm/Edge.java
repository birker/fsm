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
import java.util.ListIterator;
import javax.swing.JLabel;

/**
 *
 * @author Konnarr
 */
public class Edge implements Serializable, Element {
    private static final long serialVersionUID = 2345541351034924475L;    

    /*public static final int LINE = 0;
    public static final int QUADRATIC_BEZIER = 1;
    public static final int CUBIC_BEZIER =2;*/
    
    public static final double AUTOMATIC = Double.NaN;
    public static final double NORTH = 270;
    public static final double WEST = 0;
    public static final double SOUTH = 90;
    public static final double EAST = 180;
    
    private static PathMode defPathMode = PathMode.QUADRATIC_BEZIER;
    private static Color defColor = Color.BLACK;
    private static boolean defLabelRot = true;
    private static boolean perpendicular = false;
    
    private Vertex from;
    private Vertex to;
    private JLabel label = new JLabel();
    private double labelRotDeg = 0;
    private boolean labelRot = true;
    
    private ArrayList<String> trans = new ArrayList<String>();
    private double weight;
    
    protected boolean directed;
    //graphical properties
    private double degIn = AUTOMATIC;
    private double degOut = AUTOMATIC;
    private ArrayList<Point> supportPoints = new ArrayList<Point>();
    private Path2D path;
    private Path2D pathOpen;
    private boolean inherit = true;
    private PathMode pathMode = PathMode.QUADRATIC_BEZIER;
    private Color color = defColor;
    
    //index, for constructing automaton piece by pieve.
    private int index;
    
    public Edge(Vertex from, Vertex to, boolean autoSP) {
        this.from = from;
        this.to = to;
        if (autoSP) {
        if (from==to) {
            supportPoints.add(new Point((int)(from.getShape().getX()+from.getPreferredWidth()/4),(int)(from.getShape().getCenterY()+from.getShape().getHeight())));
            supportPoints.add(new Point((int)(from.getShape().getX()+3*from.getPreferredWidth()/4),(int)(from.getShape().getCenterY()+from.getShape().getHeight())));
        } else if (perpendicular) {
            if (!(from.getShape().getCenterY()==to.getShape().getCenterY() && from.getShape().getCenterX()==to.getShape().getCenterX())) {
                supportPoints.add(new Point((int)to.getShape().getCenterX(),(int)from.getShape().getCenterY()));
            }
        } else {
        for (int i = 0; i < to.getEdges().size(); i++) {
            if (to.getEdges().get(i).getTo()==from) {
                Point p = new Point((int)(0.5*(to.getShape().getCenterX() - from.getShape().getCenterX())),(int)(0.5*(to.getShape().getCenterY() - from.getShape().getCenterY())));
                double l = p.distance(0, 0);
                supportPoints.add(new Point((int)(to.getShape().getCenterX()-p.x+p.y/l*10),(int)(to.getShape().getCenterY()-p.y-p.x/l*20)));
                if (to.getEdges().get(i).getSupportPoints().isEmpty()) {
                    to.getEdges().get(i).getSupportPoints().add(new Point((int)(to.getShape().getCenterX()-p.x-p.y/l*10),(int)(to.getShape().getCenterY()-p.y+p.x/l*20)));
                    to.getEdges().get(i).rebuildPath();
                }
            }
        }
        }
        }
        label.setVisible(true);
    }

    public static boolean isPerpendicular() {
        return perpendicular;
    }

    public static void setPerpendicular(boolean perpendicular) {
        Edge.perpendicular = perpendicular;
    }
    
    public static Color getDefColor() {
        return defColor;
    }

    public static void setDefColor(Color defColor) {
        Edge.defColor = defColor;
    }

    public static PathMode getDefPathMode() {
        return defPathMode;
    }

    public static void setDefPathMode(PathMode defPathMode) {
        Edge.defPathMode = defPathMode;
    }

    public static boolean isDefLabelRot() {
        return defLabelRot;
    }

    public static void setDefLabelRot(boolean defLabelRot) {
        Edge.defLabelRot = defLabelRot;
    }
    
    ////////////////////////////Getter and Setter///////////////////////////////
    
    //sollte readonly sein. nur zum malen;
    public Path2D getPath(boolean closed) {
        if (closed) return path;
        else return pathOpen;
    }
    
    public Vertex getFrom() {
        return from;
    }
    
    public void setFrom(Vertex node) {
        from.getEdges().remove(this);
        from = node;
        from.getEdges().add(this);
    }
    
    public Vertex getTo() {
        return to;
    }
    
    public void setTo(Vertex node) {
        to = node;
    }
    
    //zu public
    public ArrayList<Point> getSupportPoints() {
        return supportPoints;
    }//*/
    
    //zu public
    public ArrayList<String> getTransitions() {
        return trans;
    }//*/

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public void setText(boolean weight) {
        if (weight) {
            setText(""+this.weight);
        } else {
            String s = "";
            for (String t: trans) {
                s += (s.equals("")?"":", ")+t;
            }
            setText(s);
        }
    }
    
    public void setText(String text) {
        label.setText(text);
        label.setSize(label.getPreferredSize());
    }
    
    public String getText() {
        return label.getText();
    }
   
    //sollte readonly sein. nur zu lesen zum malen.
    public JLabel getLabel() {
        return label;
    }

    public boolean isDegInAutomatic() {
        return Double.isNaN(degIn);
    }
    
    public double getDegIn() {
        return degIn;
    }

    public void setDegIn(double degIn) {
        this.degIn = degIn;
    }

    public double getDegOut() {
        return degOut;
    }
    
    public boolean isDegOutAutomatic() {
        return Double.isNaN(degOut);
    }

    public void setDegOut(double degOut) {
        this.degOut = degOut;
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

    public PathMode getPathMode() {
        if (inherit) return defPathMode;
        return pathMode;
    }

    public void setPathMode(PathMode pathMode) {
        this.pathMode = pathMode;
        inherit = false;
    }

    public double getLabelRotDeg() {
        return labelRotDeg;
    }

    public void setLabelRotDeg(double labelrot) {
        this.labelRotDeg = labelrot;
    }

    public boolean isLabelRot() {
        if (inherit && from != to) return defLabelRot;
        return labelRot;
    }

    public void setLabelRot(boolean labelRot) {
        this.labelRot = labelRot;
        inherit = false;
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
        if (!isDegOutAutomatic()) element = new Point2D.Double(from.getShape().getCenterX()+Math.cos(degOut*Math.PI/180),from.getShape().getCenterY()+Math.sin(degOut*Math.PI/180));
        else if (!supportPoints.isEmpty()) element = supportPoints.get(0);
        else element = new Point2D.Double(to.getShape().getCenterX(),to.getShape().getCenterY()+0.1);
        Point2D start = getIntersectionPoint(element, from.getShape());
        //finish point
        if (!isDegInAutomatic()) element = new Point2D.Double(to.getShape().getCenterX()+Math.cos(degIn*Math.PI/180),to.getShape().getCenterY()+Math.sin(degIn*Math.PI/180));
        else if (!supportPoints.isEmpty()) element = supportPoints.get(supportPoints.size()-1);
        else element = new Point2D.Double(from.getShape().getCenterX(),from.getShape().getCenterY());
        Point2D finish = getIntersectionPoint(element, to.getShape());
        
        path = new Path2D.Float();
        element = start;
        path.moveTo(element.getX(), element.getY());
        if (!supportPoints.isEmpty()) {
            if (pathMode.equals(PathMode.LINE)) {
                while (itr.hasNext()) {
                    element = itr.next();
                    path.lineTo(element.getX(), element.getY());    
                }
            } else if (pathMode.equals(PathMode.QUADRATIC_BEZIER) || pathMode.equals(PathMode.CUBIC_BEZIER)) {
                element = itr.next();
                Point2D h = new Point2D.Double((path.getCurrentPoint().getX()+element.getX())/2,(path.getCurrentPoint().getY()+element.getY())/2);
                path.lineTo(h.getX(), h.getY());
                while (itr.hasNext()) {
                    Point2D b = itr.next();
                    h.setLocation((b.getX()+element.getX())/2,(b.getY()+element.getY())/2);
                    if (pathMode.equals(PathMode.QUADRATIC_BEZIER)) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                    else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                    element = b;
                }
                h.setLocation((finish.getX()+element.getX())/2,(finish.getY()+element.getY())/2);
                if (pathMode.equals(PathMode.QUADRATIC_BEZIER)) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
            }
        }
        path.lineTo(finish.getX(), finish.getY());
        
        //arrow
        if (directed) {
        if (Math.abs(finish.distance(element))<1E-10) {
            Point2D t = new Point2D.Double(finish.getX()-from.getShape().getCenterX(),finish.getY()-from.getShape().getCenterY());
            t.setLocation(finish.getX()+2*t.getX(), finish.getY()+2*t.getY());
            drawArrow(path,finish,t,25,10);
        }   
        else drawArrow(path,finish,element,25,10);
        }
        
        pathOpen = (Path2D) path.clone();
        
        /* There seems to be no elegant way to determine if a Point lies 
         * within a specific distance of a line but to close the whole path 
         * and then intersect. This has to be done manually all the way back, 
         * because he would just lineTo the starting point, if done automatically.*/
        if (!supportPoints.isEmpty()) {
            if (pathMode.equals(PathMode.LINE)) {
                while (itr.hasPrevious()) {
                    element = itr.previous();
                    path.lineTo(element.getX(), element.getY());    
                }
            } else if (pathMode.equals(PathMode.QUADRATIC_BEZIER) || pathMode.equals(PathMode.CUBIC_BEZIER)) {
                element = itr.previous();
                Point2D h = new Point2D.Double((path.getCurrentPoint().getX()+element.getX())/2,(path.getCurrentPoint().getY()+element.getY())/2);
                path.lineTo(h.getX(), h.getY());
                while (itr.hasPrevious()) {
                    Point2D b = itr.previous();
                    h.setLocation((b.getX()+element.getX())/2,(b.getY()+element.getY())/2);
                    if (pathMode.equals(PathMode.QUADRATIC_BEZIER)) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                    else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                    element = b;
                }
                h.setLocation((start.getX()+element.getX())/2,(start.getY()+element.getY())/2);
                if (pathMode.equals(PathMode.QUADRATIC_BEZIER)) path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                else path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
            }
        }
        path.closePath();
        
        if (supportPoints.isEmpty()) {
            repositionLabel(start, finish);
        } else{
            repositionLabel(start, supportPoints.get(0));
        }
        
    }
    
    public void repositionLabel() {
        Point2D element;
        if (!isDegOutAutomatic()) element = new Point2D.Double(from.getShape().getCenterX()+Math.cos(degOut*Math.PI/180),from.getShape().getCenterY()+Math.sin(degOut*Math.PI/180));
        else if (!supportPoints.isEmpty()) element = supportPoints.get(0);
        else element = new Point2D.Double(to.getShape().getCenterX(),to.getShape().getCenterY()+0.1);
        Point2D start = getIntersectionPoint(element, from.getShape());
        if (supportPoints.isEmpty()) {
            if (!isDegInAutomatic()) element = new Point2D.Double(to.getShape().getCenterX()+Math.cos(degIn*Math.PI/180),to.getShape().getCenterY()+Math.sin(degIn*Math.PI/180));
            else if (!supportPoints.isEmpty()) element = supportPoints.get(supportPoints.size()-1);
            else element = new Point2D.Double(from.getShape().getCenterX(),from.getShape().getCenterY());
            Point2D finish = getIntersectionPoint(element, to.getShape());          
            repositionLabel(start, finish);
        } else{
            repositionLabel(start, supportPoints.get(0));
        }        
    }
    
    private void repositionLabel(Point2D start, Point2D finish) {
        Point2D line = new Point2D.Double(finish.getX()-start.getX(),finish.getY()-start.getY());
        if (from == to && supportPoints.size() == 2) {
            labelRot = false;
            line.setLocation(supportPoints.get(1).x-supportPoints.get(0).x, supportPoints.get(1).y-supportPoints.get(0).y);
            label.setLocation(supportPoints.get(0).x+(int)line.getX()/2-label.getWidth()/2,supportPoints.get(0).y+1);
            if (path.intersects(label.getBounds()))
                label.setLocation(label.getX(), label.getY()-label.getHeight()-1);  
        }
        else if (labelRot) {
            double length = line.distance(0,0);
            line.setLocation(line.getX()/length,line.getY()/length);
            labelRotDeg = Math.signum(line.getX()) * Math.signum(line.getY()) * Math.acos(Math.signum(line.getX()) * line.getX());         
            label.setLocation((int)start.getX()+1, (int)start.getY()+1);   
        } else {
            Point2D p = new Point2D.Double(start.getX() + line.getX()/2, start.getY() + line.getY()/2);
            label.setLocation((int)p.getX()+1, (int)p.getY()+1);
            if (path.intersects(label.getBounds()))
                label.setLocation(label.getX(), label.getY()-label.getHeight()-1);            
        }
    }
    
    @Override
    public String toString() {
        return "Edge: " + label.getText() + "@"+from+","+to;
    }
}
