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
public class Edge implements Serializable, Element, Cloneable {

    private static final long serialVersionUID = 2345541351034924475L;

    public enum PathMode {

        LINE("gar nicht"), QUADRATIC_BEZIER("stark"), CUBIC_BEZIER("leicht");

        public fsm.Edge.PathMode getNext() {
            if (this.equals(CUBIC_BEZIER)) {
                return LINE;
            } else if (this.equals(LINE)) {
                return QUADRATIC_BEZIER;
            } else {
                return CUBIC_BEZIER;
            }
        }

        private PathMode(String name) {
            this.name = name;
        }
        private final String name;

        @Override
        public String toString() {
            return name;
        }
    }
    
    public static final double AUTOMATIC = Double.NaN;
    public static final double NONE = Double.NaN;
    public static final double NORTH = 270;
    public static final double WEST = 0;
    public static final double SOUTH = 90;
    public static final double EAST = 180;
    private Graph parent;
    private Vertex from;
    private Vertex to;
    private boolean directed;// = defDirected;
    private double weight = NONE;
    private String name = "";
    private String comment = "";
    private int index;//index, for constructing automaton piece by pieve.
    private JLabel label = new JLabel();
    private boolean autoSP = true;
    private ArrayList<Point> supportPoints = new ArrayList<Point>();
    private Path2D path;
    private Path2D pathOpen;
    private static boolean perpendicular = false;
    //private static boolean autoSP = true;
    //graphical properties
    private double degIn = AUTOMATIC;
    private double degOut = AUTOMATIC;
    //private boolean inherit = true;
    private double labelRotDeg = 0;
    private boolean labelRot;// = true;
    private Color color;// = defColor;
    private PathMode pathMode;// = PathMode.QUADRATIC_BEZIER;

    public void setAutomaticSupportPoints() {
        if (from == to) {
            supportPoints.clear();
            supportPoints.add(new Point((int) (from.getShape().getX() + from.getPreferredWidth() / 4), (int) (from.getShape().getCenterY() + from.getShape().getHeight())));
            supportPoints.add(new Point((int) (from.getShape().getX() + 3 * from.getPreferredWidth() / 4), (int) (from.getShape().getCenterY() + from.getShape().getHeight())));
        } else if (perpendicular) {
            if (!(from.getShape().getCenterY() == to.getShape().getCenterY() && from.getShape().getCenterX() == to.getShape().getCenterX())) {
                supportPoints.clear();
                supportPoints.add(new Point((int) to.getShape().getCenterX(), (int) from.getShape().getCenterY()));
            }
        } else { //hin und her
            for (Edge e: to.getEdges()) {
                if (e.getTo() == from) {
                    if (e.isAutoSP()) {
                        supportPoints.clear();
                        Point p = new Point((int) (0.5 * (to.getShape().getCenterX() - from.getShape().getCenterX())), (int) (0.5 * (to.getShape().getCenterY() - from.getShape().getCenterY())));
                        double l = p.distance(0, 0);
                        supportPoints.add(new Point((int) (to.getShape().getCenterX() - p.x + p.y / l * 10), (int) (to.getShape().getCenterY() - p.y - p.x / l * 20)));
                        e.getSupportPoints().clear();
                        e.getSupportPoints().add(new Point((int) (to.getShape().getCenterX() - p.x - p.y / l * 10), (int) (to.getShape().getCenterY() - p.y + p.x / l * 20)));
                        e.setAutoSP(false);
                        e.rebuildPath();
                        e.setAutoSP(true);
                    }
                }
            }
        }
        autoSP = true;
    }
    
    public Edge(Graph g, Vertex from, Vertex to) {
        this.parent = g;
        this.from = from;
        this.to = to;
        //rebuildPath();
        setDefaultValues();
    }

    public static boolean isPerpendicular() {
        return perpendicular;
    }

    public static void setPerpendicular(boolean perpendicular) {
        Edge.perpendicular = perpendicular;
    }

    ////////////////////////////Getter and Setter///////////////////////////////
    //sollte readonly sein. nur zum malen;
    public Path2D getPath(boolean closed) {
        if (closed) {
            return path;
        } else {
            return pathOpen;
        }
    }

    public boolean isAutoSP() {
        return autoSP;
    }

    public void setAutoSP(boolean autoSP) {
        this.autoSP = autoSP;
    }

    public Vertex getFrom() {
        return from;
    }

    public void setFrom(Vertex node) {
        from.getEdges().remove(this);
        from = node;
        from.getEdges().add(this);
        rebuildPath();
    }

    public Vertex getTo() {
        return to;
    }

    public void setTo(Vertex node) {
        to = node;
        rebuildPath();
    }

    //zu public
    public ArrayList<Point> getSupportPoints() {
        return supportPoints;
    }//*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setText();
    }
    
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        setText();
    }

    public void setText() {
        label.setText(getName()+(Double.isNaN(weight)?"":(getName().equals("")?"":": ")+weight));
        label.setSize(label.getPreferredSize());
        repositionLabel();
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
        rebuildPath();
    }

    public double getDegOut() {
        return degOut;
    }

    public boolean isDegOutAutomatic() {
        return Double.isNaN(degOut);
    }

    public void setDegOut(double degOut) {
        this.degOut = degOut;
        rebuildPath();
    }

    /*public boolean isInherit() {
        return inherit;
    }

    public void setInherit() {
        inherit = true;
        rebuildPath();
    }*/
    public void setDefaultValues() {
        directed = parent.isDefDirected();
        color = parent.getDefEdgeColor();
        pathMode = parent.getDefPathMode();
        labelRot = parent.isDefEdgeLabelRot();
        rebuildPath();
        repositionLabel();
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
        rebuildPath();
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

    public PathMode getPathMode() {
        return pathMode;
    }

    public void setPathMode(PathMode pathMode) {
        this.pathMode = pathMode;
        rebuildPath();
    }

    public double getLabelRotDeg() {
        return labelRotDeg;
    }

    public void setLabelRotDeg(double labelrot) {
        this.labelRotDeg = labelrot;
        
    }

    public boolean isLabelRot() {
        return labelRot;
    }

    public void setLabelRot(boolean labelRot) {
        this.labelRot = labelRot;
        repositionLabel();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean hit(Point2D p, int distance) {
        return path.intersects(p.getX() - distance, p.getY() - distance, 2 * distance, 2 * distance);
    }

    static private Point2D getIntersectionPoint(Point2D p, RectangularShape s) {
        if (p.getX() == s.getCenterX() && p.getY() == s.getCenterY()) {
            p.setLocation(p.getX(), p.getY()+0.2);
        }
        Point2D line = new Point2D.Double(p.getX() - s.getCenterX(), p.getY() - s.getCenterY());
        //normalize line (so length is 1 and we can walk pixel by pixel)
        double length = Math.abs(Math.abs(line.getX()) > Math.abs(line.getY()) ? line.getX() : line.getY());
        line.setLocation(line.getX() / length, line.getY() / length);
        double x = (Math.abs(line.getX()) > Math.abs(line.getY()) ? s.getWidth() / 2 : s.getHeight() / 2);
        Point2D q = new Point2D.Double(s.getCenterX() + line.getX() * x, s.getCenterY() + line.getY() * x);
        //while outside walk to the center
        double dist = q.distanceSq(s.getCenterX(), s.getCenterY());
        while (!s.contains(q) && q.distanceSq(s.getCenterX(), s.getCenterY()) <= dist) {
            dist = q.distanceSq(s.getCenterX(), s.getCenterY());
            q.setLocation(q.getX() - line.getX(), q.getY() - line.getY());
        }
        //walk just outside the border
        while (s.contains(q) && q.distanceSq(s.getCenterX(), s.getCenterY()) >= dist) {
            dist = q.distanceSq(s.getCenterX(), s.getCenterY());
            q.setLocation(q.getX() + line.getX(), q.getY() + line.getY());
        }
        return q;
    }

    private void drawArrow(Path2D path, Point2D pos, Point2D from, double angleDeg, double length) {
        final double n = Math.tan(angleDeg * Math.PI / 180);
        final double m = Math.sqrt(1 + n * n) * pos.distance(from) / length;
        path.lineTo(pos.getX() - ((pos.getX() - from.getX()) - n * (pos.getY() - from.getY())) / m,
                pos.getY() - ((pos.getY() - from.getY()) + n * (pos.getX() - from.getX())) / m);
        path.lineTo(pos.getX() - ((pos.getX() - from.getX()) + n * (pos.getY() - from.getY())) / m,
                pos.getY() - ((pos.getY() - from.getY()) - n * (pos.getX() - from.getX())) / m);
        path.lineTo(pos.getX(), pos.getY());
    }

    public void rebuildPath() {
        if (isAutoSP()) {
            setAutomaticSupportPoints();
        }
        ListIterator<Point> itr = supportPoints.listIterator();
        Point2D element;
        //starting point
        if (!isDegOutAutomatic()) {
            element = new Point2D.Double(from.getShape().getCenterX() + Math.cos(getDegOut() * Math.PI / 180), from.getShape().getCenterY() + Math.sin(degOut * Math.PI / 180));
        } else if (!supportPoints.isEmpty()) {
            element = supportPoints.get(0);
        } else {
            element = new Point2D.Double(to.getShape().getCenterX(), to.getShape().getCenterY());
        }
        element.setLocation(element.getX(),element.getY()+0.2);
        Point2D start = getIntersectionPoint(element, from.getShape());
        start.setLocation(start.getX() + 0.2, start.getY());
        //finish point
        if (!isDegInAutomatic()) {
            element = new Point2D.Double(to.getShape().getCenterX() + Math.cos(getDegIn() * Math.PI / 180), to.getShape().getCenterY() + Math.sin(degIn * Math.PI / 180));
        } else if (!supportPoints.isEmpty()) {
            element = supportPoints.get(supportPoints.size() - 1);
        } else {
            element = new Point2D.Double(from.getShape().getCenterX(), from.getShape().getCenterY());
        }
        element.setLocation(element.getX(),element.getY()-0.2);
        Point2D finish = getIntersectionPoint(element, to.getShape());
        finish.setLocation(finish.getX()-0.2, finish.getY());
        path = new Path2D.Float();
        element = start;
        path.moveTo(element.getX(), element.getY());
        if (!supportPoints.isEmpty()) {
            if (getPathMode().equals(PathMode.LINE)) {
                while (itr.hasNext()) {
                    element = itr.next();
                    path.lineTo(element.getX(), element.getY());
                }
            } else if (getPathMode().equals(PathMode.QUADRATIC_BEZIER) || getPathMode().equals(PathMode.CUBIC_BEZIER)) {
                element = itr.next();
                Point2D h = new Point2D.Double((path.getCurrentPoint().getX() + element.getX()) / 2, (path.getCurrentPoint().getY() + element.getY()) / 2);
                path.lineTo(h.getX(), h.getY());
                while (itr.hasNext()) {
                    Point2D b = itr.next();
                    h.setLocation((b.getX() + element.getX()) / 2, (b.getY() + element.getY()) / 2);
                    if (getPathMode().equals(PathMode.QUADRATIC_BEZIER)) {
                        path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                    } else {
                        path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                    }
                    element = b;
                }
                h.setLocation((finish.getX() + element.getX()) / 2, (finish.getY() + element.getY()) / 2);
                if (getPathMode().equals(PathMode.QUADRATIC_BEZIER)) {
                    path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                } else {
                    path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                }
            }
        }
        path.lineTo(finish.getX(), finish.getY());

        //arrow
        if (isDirected()) {
            if (Math.abs(finish.distance(element)) < 1E-10) {
                Point2D t = new Point2D.Double(finish.getX() - from.getShape().getCenterX(), finish.getY() - from.getShape().getCenterY());
                t.setLocation(finish.getX() + 2 * t.getX(), finish.getY() + 2 * t.getY());
                drawArrow(path, finish, t, 25, 10);
            } else {
                drawArrow(path, finish, element, 25, 10);
            }
        }

        pathOpen = (Path2D) path.clone();

        /* There seems to be no elegant way to determine if a Point lies 
         * within a specific distance of a line but to close the whole path 
         * and then intersect. This has to be done manually all the way back, 
         * because he would just lineTo the starting point, if done automatically.*/
        if (!supportPoints.isEmpty()) {
            if (getPathMode().equals(PathMode.LINE)) {
                while (itr.hasPrevious()) {
                    element = itr.previous();
                    path.lineTo(element.getX(), element.getY());
                }
            } else if (getPathMode().equals(PathMode.QUADRATIC_BEZIER) || getPathMode().equals(PathMode.CUBIC_BEZIER)) {
                element = itr.previous();
                Point2D h = new Point2D.Double((path.getCurrentPoint().getX() + element.getX()) / 2, (path.getCurrentPoint().getY() + element.getY()) / 2);
                path.lineTo(h.getX(), h.getY());
                while (itr.hasPrevious()) {
                    Point2D b = itr.previous();
                    h.setLocation((b.getX() + element.getX()) / 2, (b.getY() + element.getY()) / 2);
                    if (getPathMode().equals(PathMode.QUADRATIC_BEZIER)) {
                        path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                    } else {
                        path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                    }
                    element = b;
                }
                h.setLocation((start.getX() + element.getX()) / 2, (start.getY() + element.getY()) / 2);
                if (getPathMode().equals(PathMode.QUADRATIC_BEZIER)) {
                    path.quadTo(element.getX(), element.getY(), h.getX(), h.getY());
                } else {
                    path.curveTo(element.getX(), element.getY(), element.getX(), element.getY(), h.getX(), h.getY());
                }
            }
        }
        path.closePath();
        if (supportPoints.isEmpty()) {
            repositionLabel(start, finish);
        } else {
            repositionLabel(start, supportPoints.get(0));
        }

    }

    public void repositionLabel() {
        Point2D element;
        if (!isDegOutAutomatic()) {
            element = new Point2D.Double(from.getShape().getCenterX() + Math.cos(getDegOut() * Math.PI / 180), from.getShape().getCenterY() + Math.sin(degOut * Math.PI / 180));
        } else if (!supportPoints.isEmpty()) {
            element = supportPoints.get(0);
        } else {
            element = new Point2D.Double(to.getShape().getCenterX(), to.getShape().getCenterY() + 0.1);
        }
        Point2D start = getIntersectionPoint(element, from.getShape());
        if (supportPoints.isEmpty()) {
            if (!isDegInAutomatic()) {
                element = new Point2D.Double(to.getShape().getCenterX() + Math.cos(getDegIn() * Math.PI / 180), to.getShape().getCenterY() + Math.sin(degIn * Math.PI / 180));
            } else if (!supportPoints.isEmpty()) {
                element = supportPoints.get(supportPoints.size() - 1);
            } else {
                element = new Point2D.Double(from.getShape().getCenterX(), from.getShape().getCenterY());
            }
            Point2D finish = getIntersectionPoint(element, to.getShape());
            repositionLabel(start, finish);
        } else {
            repositionLabel(start, supportPoints.get(0));
        }
    }

    private void repositionLabel(Point2D start, Point2D finish) {
        Point2D line = new Point2D.Double(finish.getX() - start.getX(), finish.getY() - start.getY());
        if (from == to && supportPoints.size() == 2) {
            labelRot = false;
            line.setLocation(supportPoints.get(1).x - supportPoints.get(0).x, supportPoints.get(1).y - supportPoints.get(0).y);
            label.setLocation(supportPoints.get(0).x + (int) line.getX() / 2 - label.getWidth() / 2, supportPoints.get(0).y + 1);
            if (path.intersects(label.getBounds())) {
                label.setLocation(label.getX(), label.getY() - label.getHeight() - 1);
            }
        } else if (labelRot) {
            double length = line.distance(0, 0);
            line.setLocation(line.getX() / length, line.getY() / length);
            labelRotDeg = Math.signum(line.getX()) * Math.signum(line.getY()) * Math.acos(Math.signum(line.getX()) * line.getX());
            label.setLocation((int) start.getX() + 1, (int) start.getY() + 1);
        } else {
            Point2D p = new Point2D.Double(start.getX() + line.getX() / 2, start.getY() + line.getY() / 2);
            label.setLocation((int) p.getX() + 1, (int) p.getY() + 1);
            if (path.intersects(label.getBounds())) {
                label.setLocation(label.getX(), label.getY() - label.getHeight() - 1);
            }
        }
    }

    @Override
    public String toString() {
        return "Edge: " + (label.getText()) + "@" + from + "," + to;
    }

    /*@Override
     protected Object clone() throws CloneNotSupportedException {
     Edge tmp = (Edge) super.clone();
     tmp.from = from.clone();
     return tmp;
     }*/

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.from != null ? this.from.hashCode() : 0);
        hash = 29 * hash + (this.to != null ? this.to.hashCode() : 0);
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
        final Edge other = (Edge) obj;
        if (this.from != other.from && (this.from == null || !this.from.equals(other.from))) {
            return false;
        }
        if (this.to != other.to && (this.to == null || !this.to.equals(other.to))) {
            return false;
        }
        return true;
    }

    
}
