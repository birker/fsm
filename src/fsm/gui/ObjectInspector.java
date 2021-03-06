/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Edge;
import fsm.EdgeFsm;
import fsm.Element;
import fsm.Fsm;
import fsm.Graph;
import fsm.Vertex;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Konnarr
 */
public class ObjectInspector extends javax.swing.JPanel implements Observer {
private static final long serialVersionUID = 1L;
    
    class ObjectInspectorTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        Object[][] dataGraph = {
            {"Name", null},
            {"Kommentar", null},
            {"<html><p style=\"color:red\"><u>Default-Werte</u></p></html>", null},
            {"<html><p style=\"color:red\"><i>Kanten</i></p></html>", null},
            {"Ecken abrunden", null},
            {"Kantenfarbe", null},
            {"Kantenrotation", null},
            {"gerichtet", null},
            {"<html><p style=\"color:red\"><i>Knoten</i></p></html>", null},
            {"Knotenfarbe", null},
            {"Knotenfüllfarbe", null},
            {"Knoten füllen", null},
            {"autom. Knotenbreite", null},
            {"Knotenbeschr. außen", null},
            {"Knotenform", null},
            {"Knotenbreite", null},
            {"Knotenhöhe", null},
            {"Knoten - Bogenbreite", null},
            {"Knoten - Bogenhöhe", null},
            //Fsm specific
            {"<html><p style=\"color:red\"><u>Automat</u></p></html>", null},
            {"Any-Symbol", null},
            {"Else-Symbol", null},
            {"spontanes Smybol", null},
            {"Symbolabkürzungen", null}
        };
        Object[][] dataVertex = {
            {"Name", null},
            {"Kommentar", null},
            {"Index", null},
            {"Startzustand", null},
            {"Endzustand", null},
            {"<html><p style=\"color:red\"><u>grafische&nbsp;Eigenschaften</u></p></html>", null},
            {"Farbe", null},
            {"Füllfarbe", null},
            {"füllen", null},
            {"autom. Breite", null},
            {"Beschriftung außen", null},
            {"Beschriftungsposition", null},
            {"Form", null},
            {"Position", null},
            {"Breite", null},
            {"Höhe", null},
            {"Bogenbreite", null},
            {"Bogenhöhe", null}
        };
        Object[][] dataEdge = {
            {"Name", null},
            {"Kommentar", null},
            {"Index", null},
            {"Start", null},
            {"Ziel", null}, 
            {"Gewicht", null},
            {"gerichtet", null},
            //{"Übergänge", null},  
            {"<html><p style=\"color:red\"><u>grafische&nbsp;Eigenschaften</u></p></html>", null},
            {"Farbe", null},
            {"Ecken abrunden", null},
            {"Beschriftungsposition", null},
            {"Beschriftungsrotation", null},
            {"Beschriftungswinkel", null},
            {"ausgehender Winkel", null},
            {"eingehender Winkel", null},
            {"automatische Hilfspunkte", null},
            {"Hilfspunkte", null}
        };
        private Graph g;
        private int spCount;

        public void fillData() {
            //RowIndexes!
            if (g.getChoice() == null) {
                dataGraph[0][1] = g.getName();
                dataGraph[1][1] = g.getComment();
                //Default-Werte
                //Kanten
                dataGraph[4][1] = g.getDefPathMode();
                dataGraph[5][1] = g.getDefEdgeColor();
                dataGraph[6][1] = g.isDefEdgeLabelRot();
                if (g instanceof Fsm) {
                    dataGraph[7][1] = null;
                } else {
                    dataGraph[7][1] = g.isDefDirected();
                }
                //Knoten
                dataGraph[9][1] = g.getDefVertexColor();
                if (g.isDefFillVertex()) {
                    dataGraph[10][1] = g.getDefFillVertexColor();
                } else {
                    dataGraph[10][1] = null;
                }
                dataGraph[11][1] = g.isDefFillVertex();
                dataGraph[12][1] = g.isDefVertexAutoWidth();
                dataGraph[13][1] = g.isDefLabelOutsideVertex();
                dataGraph[14][1] = g.getDefVertexShape();
                dataGraph[15][1] = (int) g.getDefVertexShape().getWidth();
                dataGraph[16][1] = (int) g.getDefVertexShape().getHeight();
                if (g.getDefVertexShape() instanceof RoundRectangle2D) {
                    dataGraph[17][1] = (int) ((RoundRectangle2D)g.getDefVertexShape()).getArcWidth();
                    dataGraph[18][1] = (int) ((RoundRectangle2D)g.getDefVertexShape()).getArcHeight();
                } else {
                    dataGraph[17][1] = null;
                    dataGraph[18][1] = null;
                }
                //Fsm-specific
                if (g instanceof Fsm) {
                    dataGraph[20][1] = ((Fsm)g).getAnySymbol();
                    dataGraph[21][1] = ((Fsm)g).getElseSymbol();
                    dataGraph[22][1] = ((Fsm)g).getEpsSymbol();
                    dataGraph[23][1] = ((Fsm)g).getShortSymbols();
                }
                
            } else if (g.getChoice() instanceof Vertex) {
                dataVertex[0][1] = ((Vertex)g.getChoice()).getName();
                dataVertex[1][1] = ((Vertex)g.getChoice()).getComment();
                dataVertex[2][1] = ((Vertex)g.getChoice()).getIndex();
                dataVertex[3][1] = ((Vertex)g.getChoice()).isInitial();
                dataVertex[4][1] = ((Vertex)g.getChoice()).isFinal();
                //dataVertex[5][1] = ((Vertex)g.getChoice()).isInherit();
                dataVertex[6][1] = ((Vertex)g.getChoice()).getColor();
                if (((Vertex)g.getChoice()).isFillVertex()) {
                    dataVertex[7][1] = ((Vertex)g.getChoice()).getFillColor();
                } else {
                    dataVertex[7][1] = null;
                }
                dataVertex[8][1] = ((Vertex)g.getChoice()).isFillVertex();
                dataVertex[9][1] = ((Vertex)g.getChoice()).isAutoWidth();
                dataVertex[10][1] = ((Vertex)g.getChoice()).isLabelOutside();
                if (((Vertex)g.getChoice()).isLabelOutside()) {
                    dataVertex[11][1] = ((Vertex)g.getChoice()).getLabel().getBounds().getLocation();
                } else {
                    dataVertex[11][1] = null;
                }
                dataVertex[12][1] = ((Vertex)g.getChoice()).getShape();
                dataVertex[13][1] = ((Vertex)g.getChoice()).getShape().getBounds().getLocation();
                dataVertex[14][1] = (int) ((Vertex)g.getChoice()).getShape().getBounds().getWidth();
                dataVertex[15][1] = (int) ((Vertex)g.getChoice()).getShape().getBounds().getHeight();
                if (((Vertex)g.getChoice()).getShape() instanceof RoundRectangle2D) {
                    dataVertex[16][1] = (int)((RoundRectangle2D)((Vertex)g.getChoice()).getShape()).getArcWidth();
                    dataVertex[17][1] = (int)((RoundRectangle2D)((Vertex)g.getChoice()).getShape()).getArcHeight();
                } else {
                    dataVertex[16][1] = null;
                    dataVertex[17][1] = null;
                }
            } else {
                dataEdge[0][1] = ((Edge)g.getChoice()).getName();
                dataEdge[1][1] = ((Edge)g.getChoice()).getComment();
                dataEdge[2][1] = ((Edge)g.getChoice()).getIndex();
                dataEdge[3][1] = ((Edge)g.getChoice()).getFrom();
                dataEdge[4][1] = ((Edge)g.getChoice()).getTo();
                dataEdge[5][1] = ((Edge)g.getChoice()).getWeight();
                if (g.getChoice() instanceof EdgeFsm) {
                    dataEdge[6][0] = "Übergänge";
                    dataEdge[6][1] = ((EdgeFsm)g.getChoice()).getTransitions();
                } else {
                    dataEdge[6][0] = "gerichtet";
                    dataEdge[6][1] = ((Edge)g.getChoice()).isDirected();
                }
                dataEdge[8][1] = ((Edge)g.getChoice()).getColor();
                dataEdge[9][1] = ((Edge)g.getChoice()).getPathMode();
                dataEdge[10][1] = ((Edge)g.getChoice()).getLabel().getBounds().getLocation();
                dataEdge[11][1] = ((Edge)g.getChoice()).isLabelRot();
                dataEdge[12][1] = ((Edge)g.getChoice()).getLabelRotDeg();
                dataEdge[13][1] = ((Edge)g.getChoice()).getDegOut();
                dataEdge[14][1] = ((Edge)g.getChoice()).getDegIn();
                dataEdge[15][1] = ((Edge)g.getChoice()).isAutoSP();                
                dataEdge[16][1] = ((Edge)g.getChoice()).getSupportPoints();
                spCount = ((Edge)g.getChoice()).getSupportPoints().size();
            }
            fireTableDataChanged();
        }
        
        public ObjectInspectorTableModel(Graph g) {
            this.g = g;
            fillData();

        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            if (g.getChoice() == null) {
                if (g instanceof Fsm) return dataGraph.length;
                else return dataGraph.length - 5;//rowcount Fsm-specific
            } else if (g.getChoice() instanceof Vertex) {
                return dataVertex.length;
            } else {
                if (g instanceof Fsm) return dataEdge.length;
                else return dataEdge.length - 1;//Rowcount Fsm-specific
            }
        }

        @Override
        public String getColumnName(int col) {
            if (col == 0) return "Eigenschaft";
            return "Wert";
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (g.getChoice() == null) return dataGraph[row][col];
            else if (g.getChoice() instanceof Vertex) return dataVertex[row][col];
            else return dataEdge[row][col];
        }

        /**
         * the first column contains String-Identifyer, the second column conatins 
         * a variety of objects.
         * @param c
         * @return 
         */
        @Override
        public Class<?> getColumnClass(int c) {
            if (c == 0) {
                return String.class;
            }
            return Object.class;
        }

        /**
         * The second column is editable. Null-values are not editable.
         * @param row
         * @param col
         * @return 
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 1 && getValueAt(row, col) != null) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Add changed data of the Table to the underlying Graph.
         * This method is called automatically by the Table.
         * @param value the new Value of the cell
         * @param row the row-coordinate of the changed cell
         * @param col the column-coordinate of the changed cell
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            //NameIndexes!
            if (g.getChoice() == null) {
                if (getValueAt(row,0).equals("Name")) {//row == 0) {
                    g.setName((String)value);
                } else if (getValueAt(row,0).equals("Kommentar")) {//row == 1) {
                    g.setComment((String)value);
                } else if (getValueAt(row,0).equals("Ecken abrunden")) {//row == 3) {
                    g.setDefPathMode((Edge.PathMode)value);
                } else if (getValueAt(row,0).equals("Kantenfarbe")) {//row == 4) {
                    g.setDefEdgeColor((Color)value);
                } else if (getValueAt(row,0).equals("Kantenrotation")) {//row == 5) {
                    g.setDefEdgeLabelRot((Boolean)value);
                } else if (getValueAt(row,0).equals("gerichtet")) {//row == 6) {
                    g.setDefDirected((Boolean)value);
                } else if (getValueAt(row,0).equals("Knotenfarbe")) {//row == 7) {
                    g.setDefVertexColor((Color)value);
                } else if (getValueAt(row,0).equals("Knotenfüllfarbe")) {//row == 8) {
                    g.setDefFillVertexColor((Color)value);
                } else if (getValueAt(row,0).equals("Knoten füllen")) {//row == 9) {
                    g.setDefFillVertex((Boolean)value);
                } else if (getValueAt(row,0).equals("autom. Knotenbreite")) {//row == 10) {
                    g.setDefVertexAutoWidth((Boolean)value);
                } else if (getValueAt(row,0).equals("Knotenbeschr. außen")) {//row == 11) {
                    g.setDefLabelOutsideVertex((Boolean)value);
                } else if (getValueAt(row,0).equals("Knotenform")) {//row == 12) {
                    if (g.getDefVertexShape().getClass() != (Class)value) {
                        RectangularShape s;
                        try {
                            s = (RectangularShape)((Class<?>)value).newInstance();
                            s.setFrame(g.getDefVertexShape().getFrame());                    
                            g.setDefVertexShape(s);
                        } catch (InstantiationException ex) {
                            Logger.getLogger(ObjectInspector.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(ObjectInspector.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (getValueAt(row,0).equals("Knotenbreite")) {//row == 13) {
                    g.getDefVertexShape().setFrame(g.getDefVertexShape().getX(),g.getDefVertexShape().getY(), (Integer) value, g.getDefVertexShape().getHeight());
                } else if (getValueAt(row,0).equals("Knotenhöhe")) {//row == 14) {
                    g.getDefVertexShape().setFrame(g.getDefVertexShape().getX(),g.getDefVertexShape().getY(), g.getDefVertexShape().getWidth(), (Integer) value);
                } else if (getValueAt(row,0).equals("Knoten - Bogenbreite")) {//row == 15) {
                    ((RoundRectangle2D)g.getDefVertexShape()).setRoundRect(g.getDefVertexShape().getX(),
                            g.getDefVertexShape().getY(), g.getDefVertexShape().getWidth(),
                            g.getDefVertexShape().getHeight(),(Integer) value,
                            ((RoundRectangle2D)g.getDefVertexShape()).getArcHeight());
                } else if (getValueAt(row,0).equals("Knoten - Bogenhöhe")) {//row == 16) {
                    ((RoundRectangle2D)g.getDefVertexShape()).setRoundRect(g.getDefVertexShape().getX(),
                            g.getDefVertexShape().getY(), g.getDefVertexShape().getWidth(),
                            g.getDefVertexShape().getHeight(),((RoundRectangle2D)g.getDefVertexShape()).getArcWidth(),
                            (Integer) value);
                } else if (getValueAt(row,0).equals("Any-Symbol")) {
                    ((Fsm)g).setAnySymbol((Character) value);
                } else if (getValueAt(row,0).equals("Else-Symbol")) {
                    ((Fsm)g).setElseSymbol((Character) value);
                } else if (getValueAt(row,0).equals("spontanes Symbol")) {
                    ((Fsm)g).setEpsSymbol((Character) value);
                } else if (getValueAt(row,0).equals("Symbolabkürzungen")) {
                    //((Fsm)g).setAnySymbol((Character) value);
                } 
            } else if (g.getChoice() instanceof Vertex) {
                Vertex n = (Vertex) g.getChoice();
                if (getValueAt(row,0).equals("Name")) {//row == 0) {
                    n.setName((String)value);
                } else if (getValueAt(row,0).equals("Kommentar")) {//row == 1) {
                    n.setComment((String)value);
                } else if (getValueAt(row,0).equals("Index")) {//row == 2) {
                    n.setIndex((Integer)value);
                } else if (getValueAt(row,0).equals("Startzustand")) {//row == 3) {
                    n.setInitial((Boolean)value);
                } else if (getValueAt(row,0).equals("Endzustand")) {//row == 4) {
                    n.setFinal((Boolean)value);
                //} else if (getValueAt(row,0).equals("Default-Werte benutzen")) {//row == 5) {
                //    if ((Boolean)value) n.setInherit();
                } else if (getValueAt(row,0).equals("Farbe")) {//row == 6) {
                    n.setColor((Color)value);
                } else if (getValueAt(row,0).equals("Füllfarbe")) {//row == 7) {
                    n.setFillColor((Color)value);
                } else if (getValueAt(row,0).equals("füllen")) {//row == 8) {
                    n.setFillVertex((Boolean)value);
                } else if (getValueAt(row,0).equals("autom. Breite")) {//row == 9) {
                    n.setAutoWidth((Boolean)value);
                } else if (getValueAt(row,0).equals("Beschriftung außen")) {//row == 10) {
                    n.setLabelOutside((Boolean)value);
                } else if (getValueAt(row,0).equals("Beschriftungsposition")) {//row == 11) {
                    n.getLabel().setLocation((Point)value);
                } else if (getValueAt(row,0).equals("Form")) {//row == 12) {
                    if (n.getShape().getClass() != (Class)value) {
                        RectangularShape s;
                        try {
                            s = (RectangularShape)((Class<?>)value).newInstance();
                            s.setFrame(n.getShape().getFrame());                    
                            n.setShape(s);
                        } catch (InstantiationException ex) {
                            Logger.getLogger(ObjectInspector.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(ObjectInspector.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (getValueAt(row,0).equals("Position")) {//row == 13) {
                    n.getShape().setFrame(((Point)value).x, ((Point)value).y, n.getShape().getWidth(), n.getShape().getHeight());
                    n.updateLabel();
                } else if (getValueAt(row,0).equals("Breite")) {//row == 14) {
                    n.setPreferredWidth((Integer) value);
                    n.getShape().setFrame(n.getShape().getX(),n.getShape().getY(), (Integer) value, n.getShape().getHeight());
                    n.updateLabel();
                } else if (getValueAt(row,0).equals("Höhe")) {//row == 15) {
                    n.getShape().setFrame(n.getShape().getX(),n.getShape().getY(), n.getShape().getWidth(), (Integer) value);
                    n.updateLabel();
                } else if (getValueAt(row,0).equals("Bogenbreite")) {//row == 16) {
                    ((RoundRectangle2D)n.getShape()).setRoundRect(n.getShape().getX(),
                            n.getShape().getY(), n.getShape().getWidth(),n.getShape().getHeight(),
                            (Integer) value, ((RoundRectangle2D)n.getShape()).getArcHeight());
                } else if (getValueAt(row,0).equals("Bogenhöhe")) {//row == 17) {
                    ((RoundRectangle2D)n.getShape()).setRoundRect(n.getShape().getX(),
                            n.getShape().getY(), n.getShape().getWidth(),n.getShape().getHeight(),
                            ((RoundRectangle2D)n.getShape()).getArcWidth(), (Integer) value);
                }
            } else {
                Edge e = (Edge) g.getChoice();
                if (getValueAt(row,0).equals("Start")) {//row == 0) {
                    e.setFrom((Vertex)value);
                } else if (getValueAt(row,0).equals("Ziel")) {//row == 1) {
                    e.setTo((Vertex)value);
                } else if (getValueAt(row,0).equals("Ecken abrunden")) {//row == 2) {
                    e.setPathMode((Edge.PathMode)value);
                } else if (getValueAt(row,0).equals("Farbe")) {//row == 3) {
                    e.setColor((Color)value);
                } else if (getValueAt(row,0).equals("Name")) {//row == 4) {
                    e.setName((String)value);
                } else if (getValueAt(row,0).equals("Beschriftungsposition")) {//row == 5) {
                    e.getLabel().setLocation((Point)value);
                } else if (getValueAt(row,0).equals("Beschriftungsrotation")) {//row == 6) {
                    e.setLabelRot((Boolean)value);
                } else if (getValueAt(row,0).equals("Beschriftungswinkel")) {//row == 7) {
                    e.setLabelRotDeg((Double)value);
                } else if (getValueAt(row,0).equals("gerichtet")) {//row == 8) {
                    e.setDirected((Boolean)value);
                } else if (getValueAt(row,0).equals("Gewicht")) {//row == 9) {
                    e.setWeight((Double)value);
                } else if (getValueAt(row,0).equals("Index")) {//row == 10) {
                    e.setIndex((Integer)value);
                } else if (getValueAt(row,0).equals("Kommentar")) {//row == 11) {
                    e.setComment((String)value);
                //} else if (getValueAt(row,0).equals("Default-Werte benutzen")) {//row == 12) {
                //    if ((Boolean)value) e.setInherit();
                } else if (getValueAt(row,0).equals("eingehender Winkel")) {//row == 13) {
                    e.setDegIn((Double)value);
                } else if (getValueAt(row,0).equals("ausgehender Winkel")) {//row == 14) {
                    e.setDegOut((Double)value);
                } else if (getValueAt(row,0).equals("automatische Hilfspunkte")) {//row == 14) {
                    e.setAutoSP((Boolean)value);
                } else if (getValueAt(row,0).equals("Hilfspunkte")) {//row == 15) {
                    //Hilfspunkte
                    if (spCount != ((Edge)g.getChoice()).getSupportPoints().size()) {
                        ((Edge)g.getChoice()).setAutoSP(false);
                    }
                    e.rebuildPath();
                } else if (getValueAt(row,0).equals("Übergänge")) {//row == 16) {
                    //Übergänge
                    e.setText();
                }
            }
            //fireTableCellUpdated(row, col);
            g.notifyObs();
        }
        
    }

    public static final HashMap<Color, String> ColorNames = new HashMap<Color, String>(25,0.76f);
    {
        ColorNames.put(Color.BLACK,"schwarz");
        ColorNames.put(Color.WHITE,"weiß");
        ColorNames.put(Color.RED,"rot");
        ColorNames.put(Color.GREEN,"hellgrün");
        ColorNames.put(Color.BLUE,"blau");
        ColorNames.put(Color.CYAN,"cyan");
        ColorNames.put(Color.YELLOW,"gelb");
        ColorNames.put(Color.MAGENTA,"magenta");
        ColorNames.put(Color.LIGHT_GRAY,"hellgrau");//192
        ColorNames.put(Color.GRAY,"grau");//128
        ColorNames.put(Color.DARK_GRAY,"dunkelgrau"); //64
        ColorNames.put(Color.ORANGE,"orange");//255, 200, 0
        ColorNames.put(Color.PINK,"rosa");//255, 175, 175
        //additional colors
        ColorNames.put(new Color(128, 0, 0),"dunkelrot");
        ColorNames.put(new Color(0,128,0), "dunkelgrün");
        ColorNames.put(new Color(0,0,128), "dunkelblau");
        ColorNames.put(new Color(128,128,0), "olivgrün");
        ColorNames.put(new Color(128,0,128), "lila");
        ColorNames.put(new Color(0,128,128), "aquamarin");
    }
    
    private final Graph g;
    
    /**
     * Creates new form ObjectInspector
     */
    public ObjectInspector(Graph g) {
        this.g = g;
        initComponents();
        jTable1.setDefaultRenderer(Object.class, new UniversalTableCellRenderer());
        jTable1.setDefaultEditor(Object.class, new UniversalTableCellEditor(g));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<Object>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jComboBox1.setModel(new ComboBoxModels.ElementComboBoxModel(g));
        jComboBox1.setRenderer(new ComboBoxModels.ElementComboBoxRenderer());
        jComboBox1.setSelectedIndex(0);
        jComboBox1.setMinimumSize(new java.awt.Dimension(0, 0));
        jComboBox1.setPreferredSize(new java.awt.Dimension(0, 23));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 23));

        jTable1.setModel(new ObjectInspectorTableModel(g));
        jTable1.setRowHeight(25);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSurrendersFocusOnKeystroke(true);
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("löschen");
        jButton1.setToolTipText("löscht das ausgewählte Element (und bei Knoten inzidente Kanten)");
        jButton1.setEnabled(false);
        jButton1.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton1.setPreferredSize(new java.awt.Dimension(0, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("neuer Knoten");
        jButton2.setToolTipText("erstellt einen neuen Knoten (an (0,0))");
        jButton2.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton2.setPreferredSize(new java.awt.Dimension(0, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("neue Kante");
        jButton3.setToolTipText("erstellt eine neue Kante (Start und Ziel der erste Knoten)");
        jButton3.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton3.setPreferredSize(new java.awt.Dimension(0, 23));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Default");
        jButton4.setToolTipText("setzt graphische Eigenschaften zurück auf die Werte die im Graphen definiert sind.");
        jButton4.setEnabled(false);
        jButton4.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton4.setPreferredSize(new java.awt.Dimension(0, 23));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        g.setChoice(g.addVertex(new Point(0,0)));
        g.notifyObs();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (g.getVertices().isEmpty()) {
            Vertex n = g.addVertex(new Point(0,0));
            g.setChoice(g.addEdge(n, n));
        } else {
            g.setChoice(g.addEdge(g.getVertices().get(0), g.getVertices().get(0)));
        }
        g.notifyObs();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (g.getChoice() instanceof Vertex) {
            g.removeVertex((Vertex)g.getChoice());
        } else if (g.getChoice() instanceof Edge) {
            g.removeEdge((Edge) g.getChoice());
        }
        g.notifyObs();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost
        //don't - table looses focus when editor gets it.
        //if (jTable1.getDefaultEditor(Object.class).stopCellEditing());
        //else jTable1.getDefaultEditor(Object.class).cancelCellEditing();
    }//GEN-LAST:event_jTable1FocusLost

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (g.getChoice() instanceof Vertex) {
            ((Vertex)g.getChoice()).setDefaultValues();
        } else if (g.getChoice() instanceof Edge) {
            ((Edge)g.getChoice()).setDefaultValues();
        }
        g.notifyObs();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedItem() instanceof String) {
            g.setChoice(null);
        } else {
            g.setChoice((Element) jComboBox1.getSelectedItem());
        }
        g.notifyObs();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<Object> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

@Override
    public void update(Observable o, Object arg) {
        if (g.getChoice() == null) {
            jComboBox1.setSelectedIndex(0);
            jComboBox1.revalidate();
            jComboBox1.repaint();
        } else {
            jComboBox1.setSelectedItem(g.getChoice());
        }
        ((ObjectInspectorTableModel)jTable1.getModel()).fillData();
        jButton1.setEnabled(g.getChoice() != null);
        jButton4.setEnabled(g.getChoice() != null); 
    }

   
}
