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
import fsm.EdgeFsm;
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
import java.awt.Shape;
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
import javax.swing.JLabel;

/**
 *
 * @author Konnarr
 */
public class GraphPanel extends JComponent implements Observer {
    private static final long serialVersionUID = 1L;

    private Graph graph;
    private ArrayList<RectangularShape> sp = new ArrayList<RectangularShape>();
    private Point translate = new Point(0,0);

    /** Creates new form Graph */
    public GraphPanel(Graph graph) {
        this.graph = graph;
        //setLayout(null);
        setFocusable(true);
        //setOpaque(false);
        initComponents();
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        //super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(translate.x, translate.y);
        for (Edge element: graph.getEdges()) {
            if (element.getIndex() > graph.getIndex()) continue;
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
            if (graph.getChoice() == element) {
                //draw Support Points
                g2d.setColor(Color.blue);
                for (RectangularShape s: sp) {
                    g2d.fill(s);
                }
            }
        }
        for (Vertex element: graph.getVertices()) {
            if (element.getIndex() > graph.getIndex()) continue;
            //fill
            if (graph.getActive().contains(element)) {
                g2d.setColor(Color.CYAN);
                g2d.fill(element.getShape());
            } else if (graph instanceof Fsm && ((Fsm)graph).getActiveEps().contains(element)) {
                g2d.setColor(Color.MAGENTA);
                g2d.fill(element.getShape());                
            } else if (element.isFillVertex()) {
              g2d.setColor(element.getFillColor());
              g2d.fill(element.getShape());
            }
            //border
            if (graph.getChoice() == element) {
                g2d.setColor(Color.red);
                element.getLabel().setForeground(Color.red);
            } else {
                g2d.setColor(element.getColor());
                element.getLabel().setForeground(element.getColor());
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
            //if (element.isLabelOutside()) {
                g2d.translate(element.getLabel().getX(), element.getLabel().getY());
                element.getLabel().paint(g);
                g2d.translate(-element.getLabel().getX(), -element.getLabel().getY());
            //} else {
            //    g2d.translate(element.getShape().getX(), element.getShape().getY());
            //    element.getLabel().paint(g);
            //    g2d.translate(-element.getShape().getX(), -element.getShape().getY());
            //}
        }
        
        jLabel1.paint(g);

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
                sp.add(new Rectangle.Double(p.x-2.5, p.y-2.5, 5, 5));
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

        jLabel1 = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );
    }// </editor-fold>//GEN-END:initComponents
    private Point mouseStart;
    private Element mouseElement;
    private Element lastElement;
    private Object moveElement;

    private JLabel hitLabel() {
        Edge element = (Edge) mouseElement;
        Shape s;
        if (element.isLabelRot()) {
            AffineTransform af = AffineTransform.getRotateInstance(element.getLabelRotDeg(),element.getLabel().getX(),element.getLabel().getY());
            if (element.getFrom().getShape().getCenterX()>(element.getSupportPoints().isEmpty()?element.getTo().getShape().getCenterX():element.getSupportPoints().get(0).x))
                af.translate(-element.getLabel().getWidth(), 0);

            s = af.createTransformedShape(element.getLabel().getBounds());
        } else {
            s = element.getLabel().getBounds();
        }                
        if (s.contains(mouseStart)) {
               return element.getLabel();
        }
        return null;
    }
    
    private Point hitSupportPoint() {
        int nr = 0;
        for (RectangularShape t: sp) {
            if (t.contains(mouseStart)) {
                return ((Edge) mouseElement).getSupportPoints().get(nr);
            }
            nr++;
        }
        return null;
    }
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 1) {
            //choose
            lastElement = graph.getChoice();
            graph.setChoice(mouseElement);
        }
        if (evt.getButton() == MouseEvent.BUTTON3 || (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)) {
            if ((evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)) {
                graph.setChoice(lastElement);
            }
            if (mouseElement == null) { 
                //New State
                graph.setChoice(graph.addVertex(new Point(evt.getPoint().x-(int)graph.getDefVertexShape().getWidth()/2,evt.getPoint().y-(int)graph.getDefVertexShape().getHeight()/2)));
            } else if (mouseElement instanceof Vertex && graph.getChoice() instanceof Vertex) { 
                //new Edge
                graph.setChoice(graph.addEdge((Vertex) graph.getChoice(), (Vertex) mouseElement));
            } else if (mouseElement instanceof Vertex && graph.getChoice() instanceof Edge) { 
                //move Edge
                ((Edge) graph.getChoice()).setTo((Vertex) mouseElement);
                ((Edge) graph.getChoice()).rebuildPath();
            }
        } else if (evt.getButton() == MouseEvent.BUTTON2) {
            graph.setChoice(mouseElement);
            //change init/final-status of vertex
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
                moveElement = hitSupportPoint();
                if (moveElement != null) {
                    element.getSupportPoints().remove((Point)moveElement);
                    element.rebuildPath();
                } else {
                    //search Label and change rotation if found
                    moveElement = hitLabel();
                    if (moveElement != null) {
                        element.setLabelRot(!element.isLabelRot());
                        element.repositionLabel();
                    } else {
                        //change pathmode
                        element.rebuildPath();
                        element.setPathMode(element.getPathMode().getNext());
                    }
                }
            }
            
        }
        setSP();
        graph.notifyObs();
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (evt.getModifiers() == MouseEvent.BUTTON1_MASK) {
            if (mouseElement instanceof Vertex) {
                //move Node
                graph.setChoice(mouseElement);
                Vertex element = (Vertex) mouseElement;
                if (moveElement == null && element.isLabelOutside()) {
                    if (element.getLabel().contains(evt.getPoint())) moveElement = element.getLabel();
                }
                if (moveElement instanceof JLabel) {
                    element.getLabel().setBounds((int) (element.getLabel().getX() + evt.getX() - mouseStart.getX()), (int)(element.getLabel().getY() + evt.getY() - mouseStart.getY()), element.getLabel().getWidth(), element.getLabel().getHeight());
                }
                if (moveElement == null) {
                    moveElement = element.getShape();
                }
                if (moveElement instanceof Shape) {
                    element.getShape().setFrame(element.getShape().getX() + evt.getX() - mouseStart.getX(), element.getShape().getY() + evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                    element.updateLabel();
                    for (Edge e: graph.getEdges()) {
                        if (e.getFrom() == graph.getChoice() || e.getTo() == graph.getChoice()) {
                            e.rebuildPath();
                        }
                    }
                }
                mouseStart = evt.getPoint();
            } else if (mouseElement instanceof Edge) {
                graph.setChoice(mouseElement);
                Edge element = (Edge) mouseElement;
                //move Label
                if (moveElement == null) {//search label
                    moveElement = hitLabel();
                }
                if (moveElement instanceof JLabel) {
                    ((JLabel)moveElement).setLocation((int)(element.getLabel().getX() + evt.getX() - mouseStart.getX()), (int)(element.getLabel().getY() + evt.getY() - mouseStart.getY()));
                }
                //move SupportPoint
                if (moveElement == null) { //search SupportPoint
                    moveElement = hitSupportPoint();
                }
                if (moveElement instanceof Point) {//supportpoint to move
                    ((Point)moveElement).translate((int)(evt.getX() - mouseStart.getX()), (int)(evt.getY() - mouseStart.getY()));
                    element.rebuildPath();
                }
                //Create Supportpoint
                if (moveElement == null) {//create SupportPoint
                    int nr = 0;
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
                        nrsmallest = nr;
                    }
                    element.getSupportPoints().add(nrsmallest, evt.getPoint());
                    moveElement = element.getSupportPoints().get(nrsmallest);
                }
                mouseStart = evt.getPoint();
            
            }
            graph.notifyObs();
            setSP();
        } else if (evt.getModifiers() == MouseEvent.BUTTON3_MASK) {
            if (mouseElement == null) {
                graph.setChoice(mouseElement);
                //move everything
                for (Vertex element :graph.getVertices()) {
                    element.getShape().setFrame(element.getShape().getX()+evt.getX() - mouseStart.getX(), element.getShape().getY()+evt.getY() - mouseStart.getY(), element.getShape().getWidth(), element.getShape().getHeight());
                    element.updateLabel();
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
                graph.notifyObs();
            } else if (mouseElement instanceof Vertex) {
                //create new floating edge
                if (moveElement == null) {
                    Vertex tmp = new Vertex(graph, new Rectangle2D.Double(0,0,10,10), evt.getPoint(), "");
                    Edge e = graph.addEdge((Vertex)mouseElement, tmp);
                    moveElement = e;
                } else {
                    ((Edge)moveElement).getTo().getShape().setFrame(evt.getX(), evt.getY(), 1, 1);
                    ((Edge)moveElement).rebuildPath();
                }
                graph.notifyObs();
            }
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        requestFocusInWindow();
        mouseStart = evt.getPoint();
        mouseElement = null;
        if (graph.getChoice() instanceof Edge) {
            for (RectangularShape s: sp) {
                if (s.contains(mouseStart)) {
                    mouseElement = graph.getChoice();
                    return;
                }
            }
        }
        for (Edge e: graph.getEdges()) {
            mouseElement = e;
            moveElement = hitLabel();
            if (!e.hit(evt.getPoint(), 4) && moveElement == null) mouseElement = null;
            else {
                moveElement = null;
                return;
            }
        }
        for (Vertex n: graph.getVertices()) {
            if (n.getShape().contains(evt.getPoint()) ||
                    (n.isLabelOutside() && n.getLabel().contains(evt.getPoint()))) {
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
                if (n.getName().length() > 0) {
                    n.setName(n.getName().substring(0, n.getName().length() - 1));
                    if (n.isAutoWidth()) {
                    for (Edge e: graph.getEdges()) {
                            if (e.getFrom() == graph.getChoice() || e.getTo() == graph.getChoice()) {
                                e.rebuildPath();
                            }
                        }
                    }
                }
            } else if (evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED && evt.getKeyChar() >= KeyEvent.VK_SPACE) {
                n.setName(n.getName() + evt.getKeyChar());
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
                if (e instanceof  EdgeFsm) {
                    if (((EdgeFsm)e).getTransitions().contains("" + evt.getKeyChar())) {
                        ((EdgeFsm)e).getTransitions().remove("" + evt.getKeyChar());
                    } else {
                        ((EdgeFsm)e).getTransitions().add("" + evt.getKeyChar());
                    }
                    e.setText();
                } else {
                    e.setName(e.getName()+evt.getKeyChar());
                }
                e.repositionLabel();
            } else if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE && !(graph instanceof Fsm)) {
                if (e.getName().length() > 0) 
                    e.setName(e.getName().substring(0, e.getName().length() - 1));
            }
        } else return;
        graph.notifyObs();
    }//GEN-LAST:event_formKeyPressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if (moveElement instanceof Edge) {//floating edge
            mouseElement = null;
            for (Vertex n: graph.getVertices()) {
                if (n.getShape().contains(evt.getPoint())) {
                    mouseElement = n;
                    break;
                }
            }
            if (mouseElement == null) {
                mouseElement = graph.addVertex(new Point(evt.getPoint().x-(int)graph.getDefVertexShape().getWidth()/2,evt.getPoint().y-(int)graph.getDefVertexShape().getHeight()/2));
            }
            ((Edge)moveElement).setTo((Vertex) mouseElement);
            ((Edge)moveElement).rebuildPath();
            graph.setChoice(((Edge)moveElement));
            graph.notifyObs();
        }        
        moveElement = null;        
    }//GEN-LAST:event_formMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        jLabel1.setText("<html>"+((graph.getChoice() == null)?graph.getComment():
                (graph.getChoice() instanceof Vertex?((Vertex)graph.getChoice()).getComment():((Edge)graph.getChoice()).getComment()))+"</html>");
        repaint();
    }
}
