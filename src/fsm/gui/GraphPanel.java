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
import fsm.Graph;
import fsm.Vertex;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Konnarr
 */
public class GraphPanel extends JComponent implements Observer {

    private Graph graph;
    private ArrayList<RectangularShape> sp = new ArrayList<RectangularShape>();
    private Point translate = new Point(0,0);

    /** Creates new form Graph */
    public GraphPanel(Graph graph) {
        this.graph = graph;
        setLayout(null);
        setFocusable(true);
        setOpaque(false);
        initComponents();
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(translate.x, translate.y);
        for (Edge element: graph.getEdges()) {
            if (element.getPath(false)==null) element.rebuildPath();
            //if (element.getLabel().getParent()==null)
            //add(element.getLabel()); bÃ¶se idee, er macht damit ein repaint nach dem anderen
            if (graph.getActive().contains(element)) {
                g2d.setColor(Color.cyan);
                element.getLabel().setForeground(Color.cyan);
            } else if (graph instanceof Fsm && ((Fsm)graph).getActiveEps().contains(element)) {
                g2d.setColor(Color.magenta);
                element.getLabel().setForeground(Color.magenta);
            } else if (graph.getChoice() == element) {
                g2d.setColor(Color.red);
                element.getLabel().setForeground(Color.red);
            } else {
                g2d.setColor(element.getColor());
                element.getLabel().setForeground(element.getColor());
            }
            g2d.translate(element.getLabel().getX(), element.getLabel().getY());
            if (element.isLabelRot()) {
                g2d.rotate(element.getLabelRotDeg());
                if (element.getFrom().getShape().getCenterX()>(element.getSupportPoints().isEmpty()?element.getTo().getShape().getCenterX():element.getSupportPoints().get(0).x))
                g2d.translate(-element.getLabel().getWidth(), 0);
            }
            element.getLabel().paint(g);
            if (element.isLabelRot()) {
                if (element.getFrom().getShape().getCenterX()>(element.getSupportPoints().isEmpty()?element.getTo().getShape().getCenterX():element.getSupportPoints().get(0).x))
                g2d.translate(element.getLabel().getWidth(), 0);
                g2d.rotate(-element.getLabelRotDeg());
            }            
            g2d.translate(-element.getLabel().getX(), -element.getLabel().getY());
            g2d.fill(element.getPath(true));
            g2d.draw(element.getPath(false));
        }
        for (Vertex element: graph.getVertices()) {      
            //fill
            if (graph.getActive().contains(element)) {
                g2d.setColor(Color.CYAN);
                g2d.fill(element.getShape());
            } else if (graph instanceof Fsm && ((Fsm)graph).getActiveEps().contains(element)) {
                g2d.setColor(Color.MAGENTA);
                g2d.fill(element.getShape());                
            } else if (element.isFillNode()) {
              g2d.setColor(element.getFillColor());
              g2d.fill(element.getShape());
            }
            //border
            if (graph.getChoice() == element) {
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
            
            g2d.translate(element.getShape().getX(), element.getShape().getY());
            element.getLabel().paint(g);
            g2d.translate(-element.getShape().getX(), -element.getShape().getY());
        }
        if (graph.getChoice() instanceof Edge) {
            //draw Support Points
            g2d.setColor(Color.blue);
            for (RectangularShape s: sp) {
                g2d.fill(s);
            }
        }

    }
    
    public Rectangle getBoundingBox() {
        Point min = new Point(getWidth(),getHeight());
        Point max = new Point(0,0);
        for (Vertex n: graph.getVertices()) {
            min.setLocation(Math.min(min.getX()-(n.isInitial()?20:0), n.getShape().getX()), Math.min(min.getY(), n.getShape().getY()));
            max.setLocation(Math.max(max.getX(), n.getShape().getX()+n.getShape().getWidth()), Math.max(max.getY(), n.getShape().getY()+n.getShape().getHeight()));            
        }
        for (Edge e: graph.getEdges()) {
            min.setLocation(Math.min(min.x, e.getLabel().getX()), Math.min(min.y, e.getLabel().getY()));
            max.setLocation(Math.max(max.x, e.getLabel().getX()+e.getLabel().getWidth()), Math.max(max.y, e.getLabel().getY()+e.getLabel().getHeight()));            
            for (Point p: e.getSupportPoints()) {
                min.setLocation(Math.min(min.x, p.x), Math.min(min.y, p.y));
                max.setLocation(Math.max(max.x, p.x), Math.max(max.y, p.y));            
            }
        }
        return new Rectangle(min.x, min.y, max.x-min.x+1, max.y-min.y+1);
    }

    public void saveToPNG(File file) throws IOException {
        BufferedImage image;
        Rectangle r = getBoundingBox();
        if (r.getX()<0 || r.getY() < 0 || r.getX()+r.getWidth() > getWidth() || r.getY()+r.getHeight()>getHeight()) {
            GraphPanel g = new GraphPanel(graph);
            if (r.getX()<0 || r.getY() < 0) {
                g.translate = new Point(-(int)r.getX(),-(int)r.getY());
                r.setLocation(0,0);
            }
            g.setSize((int)(r.getX()+r.getWidth()), (int)(r.getY()+r.getHeight()));
            image = new BufferedImage(g.getWidth(), g.getHeight(), BufferedImage.TYPE_INT_ARGB);
            g.print(image.getGraphics());
        } else {
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            print(image.getGraphics());        
        }
        ImageIO.write(image.getSubimage(r.x, r.y, r.width, r.height), "PNG", file);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setLocation(200, 200);
        Fsm a = new Fsm();
        GraphPanel g = new GraphPanel(a);
        g.setBounds(0, 0, 400, 400);
        g.setFocusable(true);
        f.getContentPane().add(g);
        f.setVisible(true);
    }
    
    private void setSP() {
        if (graph.getChoice() instanceof Edge) {
            sp.clear();
            for (Point p: ((Edge)graph.getChoice()).getSupportPoints()) {
                sp.add(new Rectangle2D.Double(p.x-2, p.y-2, 4, 4));
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
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
        requestFocusInWindow();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            //choose
            graph.setChoice(mouseElement);
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            if (mouseElement == null) { 
                //New State
                graph.setChoice(graph.addVertex(evt.getPoint()));
            } else if (mouseElement instanceof Vertex && graph.getChoice() instanceof Vertex) { 
                //new Edge
                graph.setChoice(graph.addEdge((Vertex) graph.getChoice(), (Vertex) mouseElement, true));
            } else if (mouseElement instanceof Vertex && graph.getChoice() instanceof Edge) { 
                //move Edge
                ((Edge) graph.getChoice()).setTo((Vertex) mouseElement);
                ((Edge) graph.getChoice()).rebuildPath();
            }
        } else if (evt.getButton() == MouseEvent.BUTTON2) {
            graph.setChoice(mouseElement);
            //change init/final-status of node
            if (mouseElement instanceof Vertex) {
                Vertex n = (Vertex) mouseElement;
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
                for (RectangularShape s: sp) {
                    if (s.contains(mouseStart)) {
                        found = true;
                        element.getSupportPoints().remove(nr);
                        element.rebuildPath();
                        break;
                    }
                    nr++;
                }
                //change pathmode
                if (!found) {
                    element.setPathMode(element.getPathMode().getNext());
                    element.rebuildPath();
                }
            }
            
        }
        setSP();
        graph.notifyObs();//repaint();
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (evt.getModifiers() == MouseEvent.BUTTON1_MASK) {
            if (mouseElement instanceof Vertex) {
                //move Node
                graph.setChoice(mouseElement);
                Vertex element = (Vertex) mouseElement;
                element.getShape().setFrame(element.getShape().getX() + evt.getX() - mouseStart.getX(), element.getShape().getY() + evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                mouseStart = evt.getPoint();
                for (Edge e: graph.getEdges()) {
                    if (e.getFrom() == graph.getChoice() || e.getTo() == graph.getChoice()) {
                        e.rebuildPath();
                    }
                }
            } else if (mouseElement instanceof Edge) {
                graph.setChoice(mouseElement);
                Edge element = (Edge) mouseElement;
                //move Label
                if (element.getLabel().getBounds().contains(mouseStart)) {
                    element.getLabel().setLocation((int)(element.getLabel().getX() + evt.getX() - mouseStart.getX()), (int)(element.getLabel().getY() + evt.getY() - mouseStart.getY()));
                    mouseStart = evt.getPoint();
                } else {
                    //move supportpoint
                    boolean found = false;
                    int nr = 0;
                    for (RectangularShape s: sp) {
                        if (s.contains(mouseStart)) {
                            found = true;
                            element.getSupportPoints().get(nr).translate((int)(evt.getX() - mouseStart.getX()), (int)(evt.getY() - mouseStart.getY()));
                            element.rebuildPath();
                            break;
                        }
                        nr++;
                    }
                    if (!found) {
                        //add new support point
                        nr = 0;
                        int nrsmallest = 0;
                        double distancesmallest = Double.MAX_VALUE;
                        Point2D p = new Point2D.Double(element.getFrom().getShape().getCenterX(),element.getFrom().getShape().getCenterY());
                        for (Point2D p2: element.getSupportPoints()) {
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
                        element.getSupportPoints().add(nrsmallest, evt.getPoint());
                    }
                    mouseStart = evt.getPoint();
                }
            }if (mouseElement instanceof Vertex) {
                //move Node
                graph.setChoice(mouseElement);
                Vertex element = (Vertex) mouseElement;
                element.getShape().setFrame(element.getShape().getX() + evt.getX() - mouseStart.getX(), element.getShape().getY() + evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                mouseStart = evt.getPoint();
                for (Edge e: graph.getEdges()) {
                    if (e.getFrom() == graph.getChoice() || e.getTo() == graph.getChoice()) {
                        e.rebuildPath();
                    }
                }
            }
            graph.notifyObs();//repaint();
            setSP();
        } else if (evt.getModifiers() == MouseEvent.BUTTON3_MASK) {
            if (mouseElement == null) {
                graph.setChoice(mouseElement);
                //move everything
                for (Vertex element :graph.getVertices()) {
                    element.getShape().setFrame(element.getShape().getX()+evt.getX() - mouseStart.getX(), element.getShape().getY()+evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                }
                for (Edge element: graph.getEdges()) {
                    element.getPath(true).transform(AffineTransform.getTranslateInstance(evt.getX() - mouseStart.getX(), evt.getY() - mouseStart.getY()));
                    element.getPath(false).transform(AffineTransform.getTranslateInstance(evt.getX() - mouseStart.getX(), evt.getY() - mouseStart.getY()));
                    element.getLabel().setLocation((int)(element.getLabel().getX()+evt.getX() - mouseStart.getX()), (int)(element.getLabel().getY()+evt.getY() - mouseStart.getY()));
                    for (Point p: element.getSupportPoints()) {
                        p.translate((int)(evt.getX() - mouseStart.getX()), (int)(evt.getY() - mouseStart.getY()));
                    }
                }
                mouseStart = evt.getPoint();
                graph.notifyObs();//repaint();
            }
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        mouseStart = evt.getPoint();
        mouseElement = null;
        if (graph.getChoice() instanceof Edge) {
            for (RectangularShape s: sp) {
                if (s.contains(mouseStart)) mouseElement = graph.getChoice();
            }
        }
        for (Edge e: graph.getEdges()) {
            if (e.hit(evt.getPoint(), 4) || e.getLabel().getBounds().contains(evt.getPoint())) {
                mouseElement = e;
                return;
            }
        }
        for (Vertex n: graph.getVertices()) {
            if (n.getShape().contains(evt.getPoint())) {
                mouseElement = n;
                return;
            }
        }
    }//GEN-LAST:event_formMousePressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (graph.getChoice() instanceof Vertex) {
            Vertex n = (Vertex) graph.getChoice();
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                graph.removeVertex(n);
            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (n.getText().length() > 0) {
                    n.setText(n.getText().substring(0, n.getText().length() - 1));
                    if (n.isAutoWidth()) {
                    for (Edge e: graph.getEdges()) {
                            if (e.getFrom() == graph.getChoice() || e.getTo() == graph.getChoice()) {
                                e.rebuildPath();
                            }
                        }
                    }
                }
            } else if (evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED && evt.getKeyChar() >= KeyEvent.VK_SPACE) {
                n.setText(n.getText() + evt.getKeyChar());
                    if (n.isAutoWidth()) {
                        for (Edge e: graph.getEdges()) {
                            if (e.getFrom() == graph.getChoice() || e.getTo() == graph.getChoice()) {
                                e.rebuildPath();
                            }
                        }
                    }
            }
        } else if (graph.getChoice() instanceof Edge) {
            Edge e = (Edge) graph.getChoice();
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                graph.removeEdge(e);
            } else if (evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED && evt.getKeyChar() >= KeyEvent.VK_SPACE){
                if (graph instanceof  Fsm) {
                    if (e.getTransitions().contains("" + evt.getKeyChar())) {
                        e.getTransitions().remove("" + evt.getKeyChar());
                    } else {
                        e.getTransitions().add("" + evt.getKeyChar());
                    }
                    e.setText(false);
                } else {
                    e.setText(e.getText()+evt.getKeyChar());
                }
                e.repositionLabel();
            } else if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && !(graph instanceof Fsm)) {
                if (e.getText().length() > 0) 
                    e.setText(e.getText().substring(0, e.getText().length() - 1));
            }
        } else return;
        graph.notifyObs();
    }//GEN-LAST:event_formKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}