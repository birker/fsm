/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Edge;
import fsm.EdgeFsm;
import fsm.Graph;
import fsm.Vertex;
import java.awt.EventQueue;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Konnarr
 */
public class TableQxQ extends javax.swing.JPanel implements Observer, ListSelectionListener {
    private static final long serialVersionUID = 1L;
    private final Graph g;

    @Override
    public void update(Observable o, Object arg) {
        ((QxQTableModel)jTable1.getModel()).fireTableStructureChanged();
    }

    @Override
    public void valueChanged(ListSelectionEvent evt) {
        int rowIndex = jTable1.getSelectedRow();
        int columnIndex = jTable1.getSelectedColumn();
        int gIndex = Math.max(rowIndex, columnIndex);
        int lIndex = Math.min(rowIndex, columnIndex);
        if ((lIndex <= 0 && gIndex <= 0) || 
                (gIndex > g.getVertices().size() && lIndex > g.getVertices().size()) ||
                (lIndex <= 0 && gIndex > g.getVertices().size() )) {
            g.setChoice(null);
        } else if (lIndex == 0) {
            g.setChoice(g.getVertices().get(gIndex-1));
        } else if (gIndex >= g.getVertices().size()+1) {
            g.setChoice(g.getVertices().get(lIndex-1));
        } else {
            Vertex n = g.getVertices().get(rowIndex-1);
            for (Edge e: n.getEdges()) {
                if (e.getTo() == g.getVertices().get(columnIndex-1)) g.setChoice(e);
            }
        }
        g.notifyObs();
    }

    class QxQTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        Graph g;

        public QxQTableModel(Graph g) {
            this.g = g;
        }
                
        @Override
        public int getRowCount() {
            return getColumnCount();
        }

        @Override
        public int getColumnCount() {
            return g.getVertices().size() + 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex == 0 && columnIndex == 0) return null;
            if (rowIndex > g.getVertices().size() && columnIndex > g.getVertices().size()) return null;
            int gIndex = Math.max(rowIndex, columnIndex);
            if (rowIndex == 0 || columnIndex == 0) {
                if (gIndex <= g.getVertices().size()) {
                    //if (g.getVertices().get(gIndex-1).getIndex()<g.getIndex()) return "";
                    return Vertex.parseString(g.getVertices().get(gIndex-1).getName());
                } else if (gIndex == g.getVertices().size()+1) {
                    return "I";
                } else {
                    return "F";
                }
            }
            if (gIndex == g.getVertices().size()+1) {
                return g.getVertices().get(Math.min(rowIndex, columnIndex)-1).isInitial();
            } else if (gIndex == g.getVertices().size()+2) {
                return g.getVertices().get(Math.min(rowIndex, columnIndex)-1).isFinal();
            }
            Vertex n = g.getVertices().get(rowIndex-1);
            for (Edge e: n.getEdges()) {
                if (e.getTo() == g.getVertices().get(columnIndex-1)) return e.getLabel().getText();
            }
            return "-";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (Math.min(rowIndex, columnIndex) == 0 && (Math.max(rowIndex, columnIndex) == 0
                    || Math.max(rowIndex, columnIndex) > g.getVertices().size())) return false;
            if (rowIndex > g.getVertices().size() && columnIndex > g.getVertices().size()) return false;
            return true;
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (rowIndex == 0 && columnIndex == 0) return;
            if (rowIndex > g.getVertices().size() && columnIndex > g.getVertices().size()) return;
            int gIndex = Math.max(rowIndex, columnIndex);
            if (rowIndex == 0 || columnIndex == 0) {
                if (gIndex <= g.getVertices().size()) {
                    g.getVertices().get(gIndex-1).setName((String)aValue);
                    return;
                } else {
                    return;
                }
            }
            if (gIndex == g.getVertices().size()+1) {
                g.getVertices().get(Math.min(rowIndex, columnIndex)-1).setInitial(((Boolean)aValue));
                return;
            } else if (gIndex == g.getVertices().size()+2) {
                g.getVertices().get(Math.min(rowIndex, columnIndex)-1).setFinal(((Boolean)aValue));
                return;
            }
            Vertex n = g.getVertices().get(rowIndex-1);
            //Kante aktualisieren
            int k = ((String) aValue).indexOf(": ");
            String name = (k>0?((String) aValue).substring(0,k):"");
            String[] trans = ((String) aValue).substring((k>0?k+2:0)).split(", ");
            double weight;
            try {
                weight = Double.parseDouble(((String) aValue).substring((k>0?k+2:0)));
            } catch (NumberFormatException ex) {
                weight = 0;
            }
            Edge e = null;
            for (Edge e2: n.getEdges()) {
                if (e2.getTo() == g.getVertices().get(columnIndex-1)) {
                    e = e2;
                    break;
                }
            }
            if (name.equals("") && (trans.length==0 || (trans.length == 1 && trans[0].equals("-")))) {
                if (e != null) {
                    g.removeEdge(e);
                }
                g.notifyObs();
                return;
            }
            //neue Kante
            if (e == null)
                e = g.addEdge(n, g.getVertices().get(columnIndex-1));
            e.setName(name);
            if (e instanceof EdgeFsm) {
                ((EdgeFsm)e).getTransitions().clear();
                ((EdgeFsm)e).getTransitions().addAll(Arrays.asList(trans));
            } else {
                e.setWeight(weight);
            }
            e.setText();            
            g.notifyObs();
        }
        
        
    }
    
    /**
     * Creates new form TableQxQ
     */
    public TableQxQ(final Graph g) {
        this.g = g;
        initComponents();
        jTable1.setModel(new QxQTableModel(g));
        jTable1.setDefaultRenderer(Object.class, new UniversalTableCellRenderer());
        jTable1.setDefaultEditor(Object.class, new UniversalTableCellEditor(g));
        //(jTable1.getSelectionModel()).addListSelectionListener(this);
        /*EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                g.addObserver(TableQxQ.this);
            }
        });*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setTableHeader(null);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
