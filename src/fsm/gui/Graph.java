/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Graph.java
 *
 * Created on 21.05.2012, 23:12:42
 */
package fsm.gui;

import fsm.Edge;
import fsm.Element;
import fsm.Fsm;
import fsm.Node;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Konnarr
 */
public class Graph extends JPanel {

    private Fsm fsm;
    private static final JLabel text;
    private ArrayList<RectangularShape> sp = new ArrayList<RectangularShape>();

    static {
        text = new JLabel();
        text.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /** Creates new form Graph */
    public Graph(Fsm fsm) {
        this.fsm = fsm;
        setLayout(null);
        setFocusable(true);
        initComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Iterator<Edge> itr2 = fsm.getTransitions().iterator();
        while (itr2.hasNext()) {
            Edge element = itr2.next();
            add(element.getLabel());
            if (fsm.getChoice() == element) {
                g2d.setColor(Color.red);
                element.getLabel().setForeground(Color.red);
            } else {
                g2d.setColor(element.getColor());
                element.getLabel().setForeground(element.getColor());
            }
            g2d.fill(element.getPath());
            g2d.draw(element.getPath());
        }
        Iterator<Node> itr = fsm.getStates().iterator();
        while (itr.hasNext()) {
            Node element = itr.next();
            //fill
            if (element.isFillNode()) {
              g2d.setColor(element.getFillColor());
              g2d.fill(element.getShape());
            }
            //border
            if (fsm.getChoice() instanceof Node && fsm.getChoice() == element) {
                g2d.setColor(Color.red);
            } else {
                g2d.setColor(element.getColor());
            }
            g2d.draw(element.getShape());
            
            if (element.isFinal()) {
                element.getShape().setFrame(element.getShape().getX() + 2, element.getShape().getY() + 2, element.getShape().getWidth() - 4, element.getShape().getHeight() - 4);
                g2d.draw(element.getShape());
                element.getShape().setFrame(element.getShape().getX() - 2, element.getShape().getY() - 2, element.getShape().getWidth() + 4, element.getShape().getHeight() + 4);
            }
            if (element.isInitial()) {
                g2d.drawLine((int) element.getShape().getX() - 20, (int) element.getShape().getCenterY() - 2, (int) element.getShape().getX() - 20, (int) element.getShape().getCenterY() + 2);
                g2d.drawLine((int) element.getShape().getX() - 20, (int) element.getShape().getCenterY(), (int) element.getShape().getX(), (int) element.getShape().getCenterY());
                g2d.drawLine((int) element.getShape().getX() - 10, (int) element.getShape().getCenterY() - 5, (int) element.getShape().getX(), (int) element.getShape().getCenterY());
                g2d.drawLine((int) element.getShape().getX() - 10, (int) element.getShape().getCenterY() + 5, (int) element.getShape().getX(), (int) element.getShape().getCenterY());
            }
            //label
          /* //Alternative with Attributed Strings
            AttributedString as1 = new AttributedString("1234567890");
            as1.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 5, 7);
            g2d.drawString(as1.getIterator(), 15, 60);*/
            text.setText(element.getLabel());
            text.setSize((int) element.getShape().getWidth(), (int) element.getShape().getHeight());
            g2d.translate(element.getShape().getX(), element.getShape().getY());
            text.paint(g);
            g2d.translate(-element.getShape().getX(), -element.getShape().getY());
        }
        if (fsm.getChoice() instanceof Edge) {
            //draw Support Points
            g2d.setColor(Color.blue);
            Iterator<RectangularShape> it = sp.iterator();
            while (it.hasNext()) {
                g2d.fill(it.next());
            }
        }

    }

    public void saveToPNG(String filename) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        paintAll(image.getGraphics());
        ImageIO.write(image, "PNG", new File(filename));
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setLocation(200, 200);
        Fsm a = new Fsm();
        Node n = a.addState(new Point(200, 100));
        n.setFinal(true);
        n.setInitial(true);
        n.setLabel("q0");
        n = a.addState(new Point(5, 100));

        Edge e = a.addTransition(a.getStates().get(0), a.getStates().get(1));
        e.addSupportPoint(new Point(125,90));
        //a.getTransitions().get(0).addSupportPoint(new Point(220, 120));
        //a.getTransitions().get(0).addSupportPoint(new Point(220, 25));
        //a.getTransitions().get(0).addSupportPoint(new Point(120,25));
        //a.getTransitions().get(0).setDegOut(Edge.WEST);
        //a.getTransitions().get(0).setDegIn(45);

        e = a.addTransition(a.getStates().get(0), a.getStates().get(0));
        e.addSupportPoint(new Point(210,160));
        e.addSupportPoint(new Point(230,160));
//        a.getTransitions().get(1).addSupportPoint(new Point(120, 180));
//        a.getTransitions().get(1).setDegOut(Edge.SOUTH - 10);
//        a.getTransitions().get(1).setDegIn(Edge.SOUTH + 10);
        Graph g = new Graph(a);
        g.setBounds(0, 0, 400, 400);
        g.setFocusable(true);
        f.getContentPane().add(g);
        f.setVisible(true);


    }
    
    private void setSP() {
        if (fsm.getChoice() instanceof Edge) {
            sp.clear();
            Iterator<Point> it = ((Edge)fsm.getChoice()).getSupportPointIterator();
            while (it.hasNext()) {
                Point p = it.next();
                sp.add(new Rectangle2D.Double(p.x-2, p.y-2, 4, 4));
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents
    private Point mouseStart;
    private Element mouseElement;

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            //choose
            fsm.setChoice(mouseElement);
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            if (mouseElement == null) { 
                //New State
                fsm.setChoice(fsm.addState(evt.getPoint()));
            } else if (mouseElement instanceof Node && fsm.getChoice() instanceof Node) { 
                //new Edge
                fsm.setChoice(fsm.addTransition((Node) fsm.getChoice(), (Node) mouseElement));
            } else if (mouseElement instanceof Node && fsm.getChoice() instanceof Edge) { 
                //move Edge
                ((Edge) fsm.getChoice()).setTo((Node) mouseElement);
            }
        } else if (evt.getButton() == MouseEvent.BUTTON2) {
            fsm.setChoice(mouseElement);
            //change init/final-status of node
            if (mouseElement instanceof Node) {
                Node n = (Node) mouseElement;
                if (!n.isFinal() && !n.isInitial()) {
                    n.setFinal(true);
                } else if (n.isFinal() && !n.isInitial()) {
                    n.setInitial(true);
                } else if (n.isFinal() && n.isInitial()) {
                    n.setFinal(false);
                } else {
                    n.setInitial(false);
                }

            } else if (mouseElement instanceof Edge) {
                Edge element = (Edge) mouseElement;
                //search supportpoint and delete it if found.
                int nr = 0;
                boolean found = false;
                Iterator<RectangularShape> itr = sp.iterator();
                while (itr.hasNext() && !found) {
                    if (itr.next().contains(mouseStart)) {
                        found = true;
                        element.removeSupportPoint(nr);
                        element.rebuildPath();
                    }
                    nr++;
                }
                //change pathmode
                if (!found) {
                    element.setPathMode((element.getPathMode()+1) %3);
                    element.rebuildPath();
                }
            }
            
        }
        setSP();
        repaint();
        
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (evt.getModifiers() == MouseEvent.BUTTON1_MASK) {
            if (mouseElement instanceof Node) {
                //move Node
                fsm.setChoice(mouseElement);
                Node element = (Node) mouseElement;
                element.getShape().setFrame(element.getShape().getX() + evt.getX() - mouseStart.getX(), element.getShape().getY() + evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                mouseStart = evt.getPoint();
                Iterator<Edge> itr = fsm.getTransitions().iterator();
                while (itr.hasNext()) {
                    Edge e = itr.next();
                    if (e.getFrom() == fsm.getChoice() || e.getTo() == fsm.getChoice()) {
                        e.rebuildPath();
                    }
                }
            } else if (mouseElement instanceof Edge) {
                fsm.setChoice(mouseElement);
                Edge element = (Edge) mouseElement;
                //move Label
                if (element.getLabel().getBounds().contains(mouseStart)) {
                    element.getLabel().setLocation((int)(element.getLabel().getX() + evt.getX() - mouseStart.getX()), (int)(element.getLabel().getY() + evt.getY() - mouseStart.getY()));
                    mouseStart = evt.getPoint();
                } else {
                    //move supportpoint
                    boolean found = false;
                    int nr = 0;
                    Iterator<RectangularShape> itr = sp.iterator();
                    while (itr.hasNext() && !found) {
                        if (itr.next().contains(mouseStart)) {
                            found = true;
                            element.getSupportPoint(nr).translate((int)(evt.getX() - mouseStart.getX()), (int)(evt.getY() - mouseStart.getY()));
                            element.rebuildPath();
                        }
                        nr++;
                    }
                    if (!found) {
                        //add new support point
                        Iterator it = element.getSupportPointIterator();
                        nr = 0;
                        int nrsmallest = 0;
                        double distancesmallest = Double.MAX_VALUE;
                        Point2D p = new Point2D.Double(element.getFrom().getShape().getCenterX(),element.getFrom().getShape().getCenterY());
                        while (it.hasNext()) {
                            Point2D p2 = (Point2D) it.next();
                            double distance = Line2D.ptSegDist(p.getX(), p.getY(), p2.getX(), p2.getY(), evt.getX(), evt.getY());
                            if (distance < distancesmallest) {
                                distancesmallest = distance;
                                nrsmallest = nr;
                            }
                            p = p2;
                            nr++;
                        }
                        Point2D p2 = new Point2D.Double(element.getTo().getShape().getCenterX(),element.getTo().getShape().getCenterY());
                        double distance = Line2D.ptSegDist(p.getX(), p.getY(), p2.getX(), p2.getY(), evt.getX(), evt.getY());
                        if (distance < distancesmallest) {
                            distancesmallest = distance;
                            nrsmallest = nr;
                        }
                        element.addSupportPoint(nrsmallest, evt.getPoint());
                    }
                    mouseStart = evt.getPoint();
                }
            }if (mouseElement instanceof Node) {
                //move Node
                fsm.setChoice(mouseElement);
                Node element = (Node) mouseElement;
                element.getShape().setFrame(element.getShape().getX() + evt.getX() - mouseStart.getX(), element.getShape().getY() + evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                mouseStart = evt.getPoint();
                Iterator<Edge> itr = fsm.getTransitions().iterator();
                while (itr.hasNext()) {
                    Edge e = itr.next();
                    if (e.getFrom() == fsm.getChoice() || e.getTo() == fsm.getChoice()) {
                        e.rebuildPath();
                    }
                }
            }
            repaint();
            setSP();
        } else if (evt.getModifiers() == MouseEvent.BUTTON3_MASK) {
            if (mouseElement == null) {
                fsm.setChoice(mouseElement);
                //move everything
                Iterator itr = fsm.getStates().iterator();
                while (itr.hasNext()) {
                    Node element = (Node)itr.next();
                    element.getShape().setFrame(element.getShape().getX()+evt.getX() - mouseStart.getX(), element.getShape().getY()+evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                }
                itr = fsm.getTransitions().iterator();
                while (itr.hasNext()) {
                    Edge element = (Edge) itr.next();
                    element.getPath().transform(AffineTransform.getTranslateInstance(evt.getX() - mouseStart.getX(), evt.getY() - mouseStart.getY()));
                    element.getLabel().setLocation((int)(element.getLabel().getX()+evt.getX() - mouseStart.getX()), (int)(element.getLabel().getY()+evt.getY() - mouseStart.getY()));
                    Iterator<Point> it = element.getSupportPointIterator();
                    while (it.hasNext()) {
                        it.next().translate((int)(evt.getX() - mouseStart.getX()), (int)(evt.getY() - mouseStart.getY()));
                    }
                }
                mouseStart = evt.getPoint();
                repaint();
            }
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        mouseStart = evt.getPoint();
        mouseElement = null;
        Iterator itr;
        if (fsm.getChoice() instanceof Edge) {
            itr = sp.iterator();
            while (itr.hasNext() && mouseElement == null) {
                if (((RectangularShape)itr.next()).contains(mouseStart)) mouseElement = fsm.getChoice();
            }
        }
        itr = fsm.getTransitions().iterator();
        while (itr.hasNext() && mouseElement == null) {
            Edge e = (Edge) itr.next();
            if (e.hit(evt.getPoint(), 4) || e.getLabel().getBounds().contains(evt.getPoint())) {
                mouseElement = e;
            }
        }
        itr = fsm.getStates().iterator();
        while (itr.hasNext() && mouseElement == null) {
            Node n = (Node) itr.next();
            if (n.getShape().contains(evt.getPoint())) {
                mouseElement = n;
            }
        }
    }//GEN-LAST:event_formMousePressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (fsm.getChoice() instanceof Node) {
            Node n = (Node) fsm.getChoice();
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                fsm.removeState(n);
            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (n.getLabel().length() > 0) {
                    n.setLabel(n.getLabel().substring(0, n.getLabel().length() - 1));
                }
            } else if (evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED && evt.getKeyChar() >= KeyEvent.VK_SPACE) {
                n.setLabel(n.getLabel() + evt.getKeyChar());
            }
        } else if (fsm.getChoice() instanceof Edge) {
            Edge e = (Edge) fsm.getChoice();
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                fsm.removeTransition(e);
            } else if (evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED && evt.getKeyChar() >= KeyEvent.VK_SPACE){
                if (e.containsTransition("" + evt.getKeyChar())) {
                    e.removeTransition("" + evt.getKeyChar());
                } else {
                    e.addTransition("" + evt.getKeyChar());
                }
            }
            e.repositionLabel();
        }
        repaint();
    }//GEN-LAST:event_formKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
