/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Edge;
import fsm.EdgeFsm;
import fsm.Fsm;
import fsm.Graph;
import fsm.Vertex;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Konnarr
 */
public class TableQxS extends javax.swing.JPanel implements Observer, ListSelectionListener {
    private static final long serialVersionUID = 1L;

    private Fsm g;
    
    @Override
    public void update(Observable o, Object arg) {
        for (Edge e: g.getEdges()) {
            for (String s: ((EdgeFsm)e).getTransitions()) {
                ((QxSTableModel)jTable1.getModel()).addLetter(s);
            }
        }
        ((QxSTableModel)jTable1.getModel()).fireTableStructureChanged();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (jTable1.getSelectedRow() <= 0) g.setChoice(null);
        else {
            g.setChoice(g.getVertices().get(jTable1.getSelectedRow()-1));
        }
        g.notifyObs();
    }

    
    class QxSTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private Graph g;
        private ArrayList<String> letter = new ArrayList<String>();

        public void addLetter(String s) {
            if (!letter.contains(s))
            letter.add(s);
        }

        public void removeLetter(int index) {
            letter.remove(index);
        }        
        
        public void removeLetter(String s) {
            letter.remove(s);
        }
        
        public QxSTableModel(Graph g) {
            this.g = g;
        }
                
        @Override
        public int getRowCount() {
            return g.getVertices().size()+1;
        }

        @Override
        public int getColumnCount() {
            return letter.size()+3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex == 0 && columnIndex == 0) return null;
            if (rowIndex == 0) {
                if (columnIndex <= letter.size()) {
                    return letter.get(columnIndex-1);
                } else if (columnIndex == letter.size()+1) {
                    return "I";
                } else {
                    return "F";
                }
            }
            Vertex n = g.getVertices().get(rowIndex-1);
            if (columnIndex == 0) {
                return Vertex.parseString(n.getName());
            }
            if (columnIndex == letter.size() + 1) {
                return n.isInitial();
            } else if (columnIndex == letter.size() + 2) {
                return n.isFinal();
            }
            String l = letter.get(columnIndex-1);
            //HashMap<Vertex,Edge> s = new HashMap<Vertex,Edge>();
            ArrayList<Vertex> s = new ArrayList<Vertex>();
            for (Edge e: n.getEdges()) {
                if (e instanceof EdgeFsm)
                for (String t: ((EdgeFsm)e).getTransitions()) {
                    if (t.equals(l)) {
                        //s.put(n,e);
                        s.add(e.getTo());
                    }
                }
            }
            return s;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (rowIndex == 0 /*&& (columnIndex == 0
                    || columnIndex > g.getVertices().size())*/) return false;
            return true;
        }
        
        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            if (rowIndex == 0 && columnIndex == 0) return;
            Vertex n = g.getVertices().get(rowIndex-1);
            if (columnIndex == 0) {
                n.setName(value.toString());
            } else if (columnIndex == letter.size()+1) {
                n.setInitial(((Boolean)value));
            } else if (columnIndex == letter.size()+2) {
                n.setFinal(((Boolean)value));
            } else {
                String s = letter.get(columnIndex-1);
                ArrayList<Vertex> newV = (ArrayList<Vertex>) value;
                ArrayList<Vertex> oldV = (ArrayList<Vertex>) getValueAt(rowIndex, columnIndex);
                ArrayList<Vertex> oldV2 = (ArrayList<Vertex>) getValueAt(rowIndex, columnIndex);
                oldV.removeAll(newV);
                newV.removeAll(oldV2);
                for (Edge e: (ArrayList<Edge>)n.getEdges().clone()) {
                    if (oldV.contains(e.getTo())) {
                        ((EdgeFsm)e).getTransitions().remove(s);
                        if (((EdgeFsm)e).getTransitions().isEmpty()) {
                            g.removeEdge(e);
                        }
                    } else if (newV.contains(e.getTo())) {
                        ((EdgeFsm)e).getTransitions().add(s);
                        e.setText();
                        newV.remove(e.getTo());
                    }
                }
                for (Vertex v: newV) {
                    EdgeFsm e = ((Fsm)g).addEdge(n, v);
                    e.getTransitions().add(s);
                    e.setText();
                }
                g.notifyObs();
            }
        }
        
        
    }
    
    /**
     * Creates new form TableQxS
     */
    public TableQxS(Fsm g) {
        this.g = g;
        initComponents();
        jTable1.setModel(new QxSTableModel(g));
        jTable1.setDefaultRenderer(Object.class, new UniversalTableCellRenderer());
        jTable1.setDefaultEditor(Object.class, new UniversalTableCellEditor(g));
        //jTable1.getSelectionModel().addListSelectionListener(this);
        g.addObserver(this);
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
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

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

        jButton1.setText("Übergang hinzufügen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Übergang löschen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ((QxSTableModel)jTable1.getModel()).addLetter(jTextField1.getText());
        ((QxSTableModel)jTable1.getModel()).fireTableStructureChanged();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ((QxSTableModel)jTable1.getModel()).removeLetter(jTextField1.getText());
        ((QxSTableModel)jTable1.getModel()).fireTableStructureChanged();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
