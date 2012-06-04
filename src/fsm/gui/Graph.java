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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    static {
        text = new JLabel();
        text.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /** Creates new form Graph */
    public Graph(Fsm fsm) {
        this.fsm = fsm;
        this.setLayout(null);
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
            if (fsm.getChoice() instanceof Edge && fsm.getChoice() == element) {
                g2d.setColor(Color.red);
            } else if (element.isInherit()) {
                g2d.setColor(fsm.getEdgeColor());
            } else {
                g2d.setColor(element.getColor());
            }
            g2d.draw(element.getPath());
            //Beschriftung malen
        }
        Iterator<Node> itr = fsm.getStates().iterator();
        while (itr.hasNext()) {
            Node element = itr.next();
            //fill
            //g2d.setColor(Color.white);
            //g2d.fill(element.getShape());
            //border
            if (fsm.getChoice() instanceof Node && fsm.getChoice() == element) {
                g2d.setColor(Color.red);
            } else if (element.isInherit()) {
                g2d.setColor(fsm.getNodeColor());
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
        f.setLayout(null);
        Fsm a = new Fsm();
        a.addState(new Point(100, 100));
        a.addState(new Point(5, 5));
        a.getStates().get(0).setFinal(true);
        a.getStates().get(0).setInitial(true);
        a.getStates().get(0).setLabel("q0");

        a.addTransition(a.getStates().get(0), a.getStates().get(1));
        a.getTransitions().get(0).addSupportPoint(new Point(220, 120));
        a.getTransitions().get(0).addSupportPoint(new Point(220, 25));
        //a.getTransitions().get(0).addSupportPoint(new Point(120,25));
        //a.getTransitions().get(0).setDegOut(Edge.WEST);
        //a.getTransitions().get(0).setDegIn(45);

        a.addTransition(a.getStates().get(0), a.getStates().get(0));
        a.getTransitions().get(1).addSupportPoint(new Point(120, 180));
        a.getTransitions().get(1).setDegOut(Edge.SOUTH - 10);
        a.getTransitions().get(1).setDegIn(Edge.SOUTH + 10);
        Graph g = new Graph(a);
        g.setBounds(0, 0, 400, 400);
        g.setFocusable(true);
        f.getContentPane().add(g);
        f.setVisible(true);


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();

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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jTextField1.setText("01");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        jTextField1.setLocation(0, 0);
        jTextField1.setSize(100, 21);
        add(jTextField1);
    }// </editor-fold>//GEN-END:initComponents
    private Point mouseStart;
    private Element mouseElement;

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            fsm.setChoice(mouseElement);
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            if (mouseElement == null) { //New State
                fsm.setChoice(fsm.addState(evt.getPoint()));
            } else if (mouseElement instanceof Node && fsm.getChoice() instanceof Node) { //new Edge
                fsm.setChoice(fsm.addTransition((Node) fsm.getChoice(), (Node) mouseElement));
            } else if (mouseElement instanceof Node && fsm.getChoice() instanceof Edge) { //move Edge
                ((Edge) fsm.getChoice()).setTo((Node) mouseElement);
            }
        } else if (evt.getButton() == MouseEvent.BUTTON2) {
            fsm.setChoice(mouseElement);
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

            }
        }
        repaint();
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (evt.getModifiers() == MouseEvent.BUTTON1_MASK) {
            if (mouseElement instanceof Node) {
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
                repaint();
            }
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        mouseStart = evt.getPoint();
        mouseElement = null;
        Iterator itr = fsm.getStates().iterator();
        while (itr.hasNext() && mouseElement == null) {
            Node n = (Node) itr.next();
            if (n.getShape().contains(evt.getPoint())) {
                mouseElement = n;
            }
        }
        itr = fsm.getTransitions().iterator();
        while (itr.hasNext() && mouseElement == null) {
            Edge e = (Edge) itr.next();
            if (e.getPath().contains(evt.getPoint())) {
                mouseElement = e;
            }
            //if (e.getPath().intersects(evt.getPoint().x-1, evt.getPoint().y-1, 2, 2)) element = e; 
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
    }//GEN-LAST:event_formMouseReleased

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
    }//GEN-LAST:event_formKeyTyped

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (fsm.getChoice() instanceof Node) {
            Node n = (Node) fsm.getChoice();
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                fsm.removeState(n);
            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (n.getLabel().length() > 0) {
                    n.setLabel(n.getLabel().substring(0, n.getLabel().length() - 1));
                }
            } else {
                n.setLabel(n.getLabel() + evt.getKeyChar());
            }
            repaint();
        } else if (fsm.getChoice() instanceof Edge) {
            Edge e = (Edge) fsm.getChoice();
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                fsm.removeTransition(e);
            } else if (evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED && evt.getKeyChar() >= KeyEvent.VK_SPACE){
                if (e.containsTransition("" + evt.getKeyChar())) {
                    e.removeTransition("" + evt.getKeyChar());
                    repaint();
                } else {
                    e.addTransition("" + evt.getKeyChar());
                }
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Teste " + jTextField1.getText() + ": " + fsm.accept(jTextField1.getText()));
        }
    }//GEN-LAST:event_jTextField1KeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
