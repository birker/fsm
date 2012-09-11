/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Main.java
 *
 * Created on 11.06.2012, 03:53:43
 */
package fsm.gui;

import fsm.DistanceAutomata;
import fsm.Edge;
import fsm.EdgeFsm;
import fsm.Fsm;
import fsm.Graph;
import fsm.Vertex;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author Konnarr
 */
public class Main extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private JFrame codingWindow;
    //private ArrayList<JFrame> graphWindows = new ArrayList<JFrame>();
    //private ArrayList<JFrame> simulationWindows = new ArrayList<JFrame>();
    private JFileChooser fs = new JFileChooser();
    private ArrayList<File> lastFiles;
    private int numFsm = 0;
    private int numGraph = 0;
    
    private Graph getSelectedGraph() {
        return ((GraphTab) jTabbedPane1.getSelectedComponent()).getGraph();
    }
    
    /*private void initGraphWindow(Graph g) {
        JFrame graphWindow = new JFrame("Graph");
        graphWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        graphWindow.setSize(400, 400);
        graphWindow.setLocation(200, 200);
        GraphPanel gr = new GraphPanel(g);
        g.addObserver(gr);
        graphWindow.getContentPane().add(gr);
        graphWindow.setVisible(true);
        graphWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                jCheckBoxMenuItem1.setSelected(false);
            }
        });
        graphWindows.add(graphWindow);
    }*/
    
    private void initCodingWindow() {
        codingWindow = new JFrame("Eingabe kodieren für universelle Abstandsautomaten");
        codingWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        codingWindow.setSize(550, 300);
        codingWindow.setLocation(200, 200);
        InputCoder ic = new InputCoder();
        codingWindow.getContentPane().add(ic);
        codingWindow.setVisible(true);
        codingWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                jCheckBoxMenuItem4.setSelected(false);
            }
        });
    }
    
    /*private void initSimulationWindow(Fsm fsm) {
        JFrame simulationWindow = new JFrame("Simulation");
        simulationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        simulationWindow.setLocation(550, 50);
        SimulationPanel s = new SimulationPanel(fsm);
        simulationWindow.setSize(s.getPreferredSize());
        simulationWindow.getContentPane().add(s);
        simulationWindow.setVisible(true);
        /*simulationWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                jCheckBoxMenuItem1.setSelected(false);
            }
        });
        simulationWindows.add(simulationWindow);
    }    */
    
    static private byte[] object2Bytes(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        return baos.toByteArray();
    }
    
    static private Object bytes2Object(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        return o;
    }
        
    /** Creates new form Main */
    public Main() {
        initComponents();
        //jTabbedPane1.add("Automat 0",new GraphTab(new Fsm()));
        jMenuItem8ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                mLooknFeel.add(new JMenuItem(info.getName()) {
                    private static final long serialVersionUID = 1L;

                    {
                        addActionListener(new java.awt.event.ActionListener() {

                            @Override
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                try {
                                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                                        if (((AbstractButton) evt.getSource()).getText().equals(info.getName())) {
                                            UIManager.setLookAndFeel(info.getClassName());
                                            break;
                                        }
                                    }
                                } catch (Exception f) {
                                }

                                SwingUtilities.updateComponentTreeUI(Main.this);
                                //SwingUtilities.updateComponentTreeUI(graphWindow);
                                pack();
                            }
                        });
                    }
                });
            }
        } catch (Exception e) {
        }
        
        Preferences pref = Preferences.userRoot().node("Fsm");
        try {
            @SuppressWarnings("unchecked")
            ArrayList<File> tmp = (ArrayList<File>) bytes2Object(pref.getByteArray("lastFiles", object2Bytes(new ArrayList<File>(11))));
            lastFiles = tmp;
        } catch (Exception ex) {
            lastFiles = new ArrayList<File>(11);
        }
        buildLastFilesMenu();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jCheckBox1 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        mLooknFeel = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem5 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem43 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem16 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem20 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem4 = new javax.swing.JCheckBoxMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jCheckBox1.setText("jCheckBox1");

        jMenu1.setText("Datei");

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("neuer Automat");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText("neuer Graph");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);
        jMenu1.add(jSeparator2);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("speichern");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("laden");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenu9.setText("zuletzt geöffnete Dateien");

        jMenuItem33.setText("jMenuItem33");
        jMenuItem33.setActionCommand("0");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem33);

        jMenuItem34.setText("jMenuItem34");
        jMenuItem34.setActionCommand("1");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem34);

        jMenuItem35.setText("jMenuItem35");
        jMenuItem35.setActionCommand("2");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem35);

        jMenuItem36.setText("jMenuItem36");
        jMenuItem36.setActionCommand("3");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem36);

        jMenuItem37.setText("jMenuItem37");
        jMenuItem37.setActionCommand("4");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem37);

        jMenuItem38.setText("jMenuItem38");
        jMenuItem38.setActionCommand("5");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem38);

        jMenuItem39.setText("jMenuItem39");
        jMenuItem39.setActionCommand("6");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem39);

        jMenuItem40.setText("jMenuItem40");
        jMenuItem40.setActionCommand("7");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem40);

        jMenuItem41.setText("jMenuItem41");
        jMenuItem41.setActionCommand("8");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem41);

        jMenuItem42.setText("jMenuItem42");
        jMenuItem42.setActionCommand("9");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem42);

        jMenu1.add(jMenu9);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Graphen als PNG exportieren");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator3);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        jMenuItem11.setText("schließen");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItem1.setText("beenden");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ansicht");

        jCheckBoxMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Graph");
        jCheckBoxMenuItem1.setEnabled(false);
        jCheckBoxMenuItem1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxMenuItem1ItemStateChanged(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItem1);

        mLooknFeel.setText("Look & Feel");
        jMenu2.add(mLooknFeel);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Graph");

        jMenuItem9.setText("Beispielgraph erzeugen");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem7.setText("Graph am Raster ausrichten");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem32.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem32.setText("SpringEmbedder");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem32);

        jCheckBoxMenuItem5.setSelected(true);
        jCheckBoxMenuItem5.setText("automatische Hilfspunkte");
        jCheckBoxMenuItem5.setEnabled(false);
        jCheckBoxMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jCheckBoxMenuItem5);

        jCheckBoxMenuItem2.setText("rechtwinklige Kanten");
        jCheckBoxMenuItem2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxMenuItem2ItemStateChanged(evt);
            }
        });
        jMenu4.add(jCheckBoxMenuItem2);

        jMenuBar1.add(jMenu4);

        jMenu6.setText("Automat");

        jMenu7.setText("Elimination spontaner Übergänge");

        jMenuItem21.setText("von links (default)");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem21);

        jMenuItem22.setText("von rechts");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem22);

        jMenuItem23.setText("beidseitig");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem23);

        jMenu6.add(jMenu7);

        jMenuItem25.setText("Entfernen unerreichbarer Zustände");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem25);

        jMenuItem28.setText("Unnötige Kanten entfernen");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem28);

        jMenuItem26.setText("Entfernen von unproduktiven Zuständen");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem26);

        jMenuItem27.setText("Minimieren (für DEAs)");
        jMenuItem27.setEnabled(false);
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem27);

        jMenu6.add(jMenu8);

        jMenuItem24.setText("Potenzmengenkonstruktion (NEA->DEA)");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem24);

        jMenuItem30.setText("Transformieren in dualen Automaten");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem30);

        jMenuItem31.setText("Hotel California Zustand hinzufügen");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem31);

        jMenuItem43.setText("Sprache identifizieren (Kleene, regEx)");
        jMenuItem43.setEnabled(false);
        jMenu6.add(jMenuItem43);

        jMenuItem29.setText("Automat untersuchen");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem29);

        jMenuBar1.add(jMenu6);

        jMenu5.setText("Abstandsautomaten");

        jMenuItem13.setText("spezielle Automaten");
        jMenuItem13.setEnabled(false);
        jMenu5.add(jMenuItem13);

        jMenuItem12.setText("Hamming");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem12);

        jMenuItem14.setText("Levenshtein");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem14);

        jMenuItem15.setText("Damerau-Levenshtein");
        jMenuItem15.setEnabled(false);
        jMenu5.add(jMenuItem15);
        jMenu5.add(jSeparator1);

        jMenuItem16.setText("universelle Automaten");
        jMenuItem16.setEnabled(false);
        jMenu5.add(jMenuItem16);

        jCheckBoxMenuItem3.setText("deterministisch");
        jCheckBoxMenuItem3.setEnabled(false);
        jMenu5.add(jCheckBoxMenuItem3);

        jMenuItem17.setText("Hamming");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem17);

        jMenuItem18.setText("Levenshtein");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem18);

        jMenuItem19.setText("Damerau-Levenshtein");
        jMenuItem19.setEnabled(false);
        jMenu5.add(jMenuItem19);
        jMenu5.add(jSeparator4);

        jMenuItem20.setText("univ. Levenshtein nach  Mihov & Schulz");
        jMenuItem20.setEnabled(false);
        jMenu5.add(jMenuItem20);
        jMenu5.add(jSeparator5);

        jCheckBoxMenuItem4.setText("Eingabekodierung");
        jCheckBoxMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem4ActionPerformed(evt);
            }
        });
        jMenu5.add(jCheckBoxMenuItem4);

        jMenuBar1.add(jMenu5);

        jMenu3.setText("Hilfe");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Bedienung Graph");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Info");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jCheckBoxMenuItem1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ItemStateChanged
        if (jCheckBoxMenuItem1.isSelected()) {
//            initGraphWindow(jTabbedPane1);
        } else {
//            graphWindow.dispose();
        }        
    }//GEN-LAST:event_jCheckBoxMenuItem1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Graph g = ((GraphTab) jTabbedPane1.getSelectedComponent()).getGraph();
        //Edge.setDefDirected(true);
        Vertex n = g.addVertex(new Point(200, 100));
        n.setFinal(true);
        n.setInitial(true);
        n.setName("q_0");
        Vertex n2 = g.addVertex(new Point(5, 100));
        n2.setName("q_1");
        n2.setAutoWidth(true);
        
        Edge e = g.addEdge(n, n2);
        e.getSupportPoints().add(new Point(125,90));
        if (g instanceof Fsm)
        ((EdgeFsm)e).getTransitions().add("0");
        e.setText();
        
        e = g.addEdge(n2, n);
        //e.getSupportPoints().add(new Point(25,30));
        //e.getSupportPoints().add(new Point(320,30));
        //e.getSupportPoints().add(new Point(320,120));
        e.setPathMode(Edge.PathMode.CUBIC_BEZIER);
        if (g instanceof Fsm)
        ((EdgeFsm)e).getTransitions().add("1");
        e.setText();
        
        e = g.addEdge(n, n);
        //e.getSupportPoints().add(new Point(210,160));
        //e.getSupportPoints().add(new Point(230,160));
        if (g instanceof Fsm)
        ((EdgeFsm)e).getTransitions().add("1");
        e.setText();
        n = g.addVertex(new Point(100,220));
        n.setName("o_ o");
        e = g.addEdge(n, n);
        e.getSupportPoints().add(new Point((int)n.getShape().getCenterX(),(int)(n.getShape().getY()+n.getShape().getHeight()-1)));
        e = g.addEdge(n, n);
          e.getSupportPoints().add(new Point((int)n.getShape().getCenterX()+10,(int)(n.getShape().getY()+n.getShape().getHeight()-10)));
        e.setDegOut(Edge.NORTH - 30);
        e.setDegIn(Edge.NORTH - 30);
        e = g.addEdge(n, n);
          e.getSupportPoints().add(new Point((int)n.getShape().getCenterX()-100,(int)(n.getShape().getY()+n.getShape().getHeight()-10)));
        e.setDegOut(Edge.NORTH + 30);
        e.setDegIn(Edge.NORTH + 30);
        g.notifyObs();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JOptionPane.showMessageDialog(this, "<html><h1 align=\"center\">FSM</h1><ul>"
                +"<p align=\"center\">Konstruktion und Simulation endlicher Automaten in Java"
                +"<br>mit besonderer Berücksichtigung universeller Abstandsautomaten"
                +"<br><br>Bachelorarbeit<br>am Institut für Theoretische Informatik"
                +"<br>an der TU Braunschweig<br>Juni bis Oktober 2012"
                +"<br><br>Konstantin Birker<br>Betreuer: Dr. Jürgen Koslowski</p>"
                +"</html>", "Bedienung Graph", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JOptionPane.showMessageDialog(this, "<html>"
                + "<i>Hinweis: Drag bedeutet klicken, nicht los lassen und ziehen.</i>"                
                +"<h3 style=\"margin:0;\">Zustände</h3><ul style=\"margin-top:0;margin-bottom:0\">"
                +"<li><u>auswählen:</u> Linksklick</li>"
                +"<li><u>neu:</u> Rechtsklick oder Doppelklick mit links ins Leere</li>"
                +"<li><u>löschen:</u> Auswählen, Entf drücken</li>"
                +"<li><u>verschieben:</u> Links Drag</li>"
                +"<li><u>Start-/Endzustand durchschalten:</u> Mittelklick</li>"
                +"<li><u>beschriften:</u> Auswählen, Tastatureingabe (Backspace/Rücktaste zum Löschen)</li>"
                +"</ul><h3 style=\"margin:0;\">Kanten</h3><ul style=\"margin-top:0;margin-bottom:0\">"
                +"<li><u>auswählen:</u> Linksklick</li>"
                +"<li><u>neu:</u> Startknoten auswählen, Rechtsklick oder Doppelklick mit links auf den Zielknoten</li>"
                +"<li><u>löschen:</u> Auswählen, Entf drücken</li>"
                +"<li><u>Ziel ändern:</u> Auswählen, Rechtsklick oder Doppelklick mit links auf neuen Zielknoten</li>"
                +"<li><u>Übergang/Beschriftung ändern:</u> Auswählen, Tastatureingabe (Backspace/Rücktaste zum Löschen)</li>"
                +"<li><u>Beschriftung verschieben:</u> Links Drag auf Beschriftung</li>"
                +"<li><u>Beschriftungsrotation (de)aktivieren:</u> Mittelklick auf Beschriftung</li>"
                +"<li>Hilfspunkte</li><ul style=\"margin-top:0;margin-bottom:0\">"
                +"<li><u>neu:</u> Links Drag an gewünschter Stelle</li>"
                +"<li><u>löschen:</u> Kante auswählen, Mittelklick auf Hilfspunkt</li>"
                +"<li><u>verschieben:</u> Links Drag auf Hilfspunkt</li>"
                +"<li><u>Abrundung der Ecken:</u> Mittelklick auf die Kante (gar nicht, leicht, stark)</li></ul>"
                +"</ul><h3 style=\"margin:0;\">sonstiges</h3><ul style=\"margin-top:0;\">"
                +"<li><u>neue Kante mit Knoten:</u> Rechts Drag vom Startknoten zum Zielknoten; existiert am Ziel keiner, wird er erstellt.</li>"
                +"<li><u>nichts auswählen:</u> Linksklick ins Leere</li>"
                +"<li><u>alles verschieben:</u> Rechts Drag im Leeren</li>"
                +"</ul></html>", "Bedienung Graph", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (fs.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            try {
                ((GraphTab) jTabbedPane1.getSelectedComponent()).getGraphPanel().saveToPNG(fs.getSelectedFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Konnte die Datei nicht speichern", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        fs.setSelectedFile(new File(getSelectedGraph().getName()));
        if (fs.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            try {
                if ("".equals(getSelectedGraph().getName())) {
                    getSelectedGraph().setName(fs.getSelectedFile().getName());
                }
                FileOutputStream fout = new FileOutputStream(fs.getSelectedFile());
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(getSelectedGraph());
                oos.close();
                lastFiles.remove(fs.getSelectedFile());
                lastFiles.add(0,fs.getSelectedFile());
                buildLastFilesMenu();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Konnte die Datei nicht speichern", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if (fs.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream fin = new FileInputStream(fs.getSelectedFile());
                ObjectInputStream ois = new ObjectInputStream(fin);
                Graph g = (Graph) ois.readObject();
                jTabbedPane1.add(g.getName(),new GraphTab(g));
                ois.close();
                lastFiles.remove(fs.getSelectedFile());
                lastFiles.add(0,fs.getSelectedFile());
                buildLastFilesMenu();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Konnte die Datei nicht öffnen", "Fehler beim Laden", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Klasse nicht gefunden", "Fehler beim Laden", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Graph g = getSelectedGraph();
        g.alignVertices();
        g.notifyObs();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jCheckBoxMenuItem2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem2ItemStateChanged
        Edge.setPerpendicular(jCheckBoxMenuItem2.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItem2ItemStateChanged

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        Fsm fsm = new Fsm();
        fsm.setName("Automat "+numFsm++);
        jTabbedPane1.add(fsm.getName(),new GraphTab(fsm));
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getComponentCount()-1);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        jTabbedPane1.remove(jTabbedPane1.getSelectedComponent());
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        Graph g = new Graph();
        g.setName("Graph "+numGraph++);
        jTabbedPane1.add(g.getName(),new GraphTab(g));
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getComponentCount()-1);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        String s = JOptionPane.showInputDialog("Musterwort?");
        if (s == null) return;
        int k;
        try {
            k = Integer.parseInt(JOptionPane.showInputDialog("Abstand?"));
        } catch (NumberFormatException e) {
            return;
        }
        if (k<0) return;
        Fsm fsm = DistanceAutomata.distanceAutomaton(k, s, DistanceAutomata.DistanceType.getByNumber(evt.getSource()==jMenuItem12?0:1));
        fsm.setName(((JMenuItem)evt.getSource()).getText()+" "+s+" ("+k+")");
        jTabbedPane1.add(fsm.getName(),new GraphTab(fsm));
        fsm.notifyObs();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        int k;
        try {
            k = Integer.parseInt(JOptionPane.showInputDialog("Abstand?"));
        } catch (NumberFormatException e) {
            return;
        }
        if (k<0) return;
        Fsm fsm = DistanceAutomata.uniHamming(k);
        fsm.setName("univ. "+(jCheckBoxMenuItem3.isSelected()?"det. ":"")+((JMenuItem)evt.getSource()).getText()+" ("+k+")");
        jTabbedPane1.add(fsm.getName(),new GraphTab(fsm));
        fsm.notifyObs();
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        int k;
        try {
            k = Integer.parseInt(JOptionPane.showInputDialog("Abstand?"));
        } catch (NumberFormatException e) {
            return;
        }
        if (k<0) return;
        Fsm fsm = DistanceAutomata.uniNLevenshtein(k);
        fsm.setName("univ. "+(jCheckBoxMenuItem3.isSelected()?"det. ":"")+((JMenuItem)evt.getSource()).getText()+" ("+k+")");
        jTabbedPane1.add(fsm.getName(),new GraphTab(fsm));
        fsm.notifyObs();
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jCheckBoxMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem4ActionPerformed
        if (jCheckBoxMenuItem4.isSelected()) {
            initCodingWindow();
        } else {
            codingWindow.dispose();
        }
    }//GEN-LAST:event_jCheckBoxMenuItem4ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        ((Fsm) getSelectedGraph()).epsElimination(Fsm.EpsEliminationType.FROM_LEFT);
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        ((Fsm) getSelectedGraph()).epsElimination(Fsm.EpsEliminationType.FROM_RIGHT);
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        ((Fsm) getSelectedGraph()).epsElimination(Fsm.EpsEliminationType.FROM_BOTH);
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        ((Fsm) getSelectedGraph()).removeUnreachableStates();
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        ((Fsm) getSelectedGraph()).removeUnnecessaryEdges();
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        ((Fsm) getSelectedGraph()).removeUnproductiveStates();
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        ((Fsm) getSelectedGraph()).minimize();
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        Fsm fsm = ((Fsm) getSelectedGraph()).powersetAutomaton();
        fsm.setName("Potenzmengenautomat");
        jTabbedPane1.add("Potenzmengenautomat",new GraphTab(fsm));
        fsm.notifyObs();
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        ((Fsm) getSelectedGraph()).transformToDualAutomaton();
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        ((Fsm) getSelectedGraph()).addHCZ(true);
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        boolean dea = ((Fsm) getSelectedGraph()).isDeterministic();
        boolean complete = ((Fsm) getSelectedGraph()).isComplete();
        boolean spontanious = ((Fsm) getSelectedGraph()).isSpontanious();
        //int blocksize = 0;
        String alpha = ((Fsm) getSelectedGraph()).calcAlphabeth(true);
        JOptionPane.showMessageDialog(this, "<html>Der Automat A = \u3008Q, X, &delta;, I, F\u3009 ist ein "+(complete?"v":"")+(dea?"DEA":"NEA")
                +(spontanious?" mit spontanten Übergängen.":" ohne spontane Übergänge.")
                +"<br>Zustände Q: {"+Fsm.getPowersetName(new HashSet<Vertex>(((Fsm) getSelectedGraph()).getVertices()))+"}"
                +"<br>Anzahl Zustände |Q|: "+((Fsm) getSelectedGraph()).getVertices().size()
                +"<br>minimales Alphabet X: {"+alpha+"}"
                +"<br>Startzustände I: {"+Fsm.getPowersetName(((Fsm) getSelectedGraph()).getInitialStates()) +"}"
                +"<br>Endzustände F: {"+Fsm.getPowersetName(((Fsm) getSelectedGraph()).getFinalStates())+"}"
                +"</html>", "Automateneigenschaften", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jCheckBoxMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem5ActionPerformed
        //Edge.setAutoSP(jCheckBoxMenuItem5.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItem5ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        getSelectedGraph().springEmbedder2();
        getSelectedGraph().notifyObs();
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        try {
            File f = lastFiles.get(Integer.parseInt(evt.getActionCommand()));
            FileInputStream fin = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Graph g = (Graph) ois.readObject();
            jTabbedPane1.add(g.getName(),new GraphTab(g));
            ois.close();
            lastFiles.remove(f);
            lastFiles.add(0,f);
            buildLastFilesMenu();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Konnte die Datei nicht öffnen", "Fehler beim Laden", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Klasse nicht gefunden", "Fehler beim Laden", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem4;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenu mLooknFeel;
    // End of variables declaration//GEN-END:variables

    private void buildLastFilesMenu() {
        while (lastFiles.size() > 10) {
            lastFiles.remove(10);
        }
        Preferences pref = Preferences.userRoot().node("Fsm");
        try {
            pref.putByteArray("lastFiles", object2Bytes(lastFiles));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < lastFiles.size(); i++) {
            jMenu9.getItem(i).setText(lastFiles.get(i).getName());
            jMenu9.getItem(i).setVisible(true);
        }
        for (int i = lastFiles.size(); i < 10 ; i++) {
            jMenu9.getItem(i).setVisible(false);
        }
    }

}
