/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Graph;
import fsm.Vertex;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Konnarr
 */
public class CollectionEditor extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    class ListRenderer extends JLabel implements ListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setOpaque(true);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            //setText("");
            //if (c instanceof ArrayList) {
                if (value instanceof Point) {
                    //supportpoints
                    setText(((Point)value).x+", "+((Point)value).y);// (((ArrayList)c).get(index));
                } else if (value instanceof Vertex) {
                    //QxS-Entry
                    setText(Vertex.parseString(((Vertex)value).getName()));
                } else if (value instanceof Map.Entry) {
                    //shortsymbols
                    setText(((Map.Entry)value).getKey().toString()+" \u21D2 "+((Map.Entry)value).getValue().toString());
                } else {
                    //transitions, other
                    setText(value.toString());
                }
            //} else if (c instanceof HashSet) {
                //transitions
            //    setText(value.toString());//return ((HashSet)c).toArray()[index];
            //} else if (c instanceof HashMap) {
                //shortsymbols
                //return ((HashMap)c).entrySet().toArray()[index];
            //}
            return this;
        }
        
    }
    
    class ListModel extends AbstractListModel<Object> {
        private static final long serialVersionUID = 1L;

        @Override
        public int getSize() {
            if (c instanceof Collection) {
                return ((Collection)c).size();
            } else if (c instanceof Map) {
                return ((Map)c).size();
            }
            return 0;
        }

        @Override
        public Object getElementAt(int index) {
            fireContentsChanged(c, 0, getSize());
            if (index >= getSize()) return null;
            if (c instanceof ArrayList) {
                return (((ArrayList)c).get(index));
            } else if (c instanceof HashSet) {
                return ((HashSet)c).toArray()[index];
            } else if (c instanceof HashMap) {
                return ((HashMap)c).entrySet().toArray()[index];
            }
            return null;
        }
        
    }
    
    Object c;
    JComponent editor;
    Class<?> type;
    
    public void setCollection(Object c, Class<?> type) {
        this.c = c;
        this.type = type;
        editor = jTextField1;
        if (c instanceof ArrayList) {
            jButton2.setVisible(true);
            jButton3.setVisible(true);
            //ParameterizedType pt = (ParameterizedType)c.getClass().getGenericSuperclass();
            //System.out.println(pt);
            //type = (Class<?>)pt.getActualTypeArguments()[0];
            if (type == Point.class) {
                editor = jPanel1;
            } else if (type == Vertex.class) {
                editor = jComboBox1;
            }
        } else {
            jButton2.setVisible(false);
            jButton3.setVisible(false);
            if (c instanceof HashMap) {
                editor = jPanel2;
            }
        }
        jPanel3.removeAll();
        jPanel3.add(editor, BorderLayout.CENTER);
        
        jList1.getModel().getElementAt(0);
        jList1.revalidate();
        jList1.repaint();
    }
    
    /**
     * Creates new form CollectionEditor
     */
    public CollectionEditor(Graph g) {
        initComponents();
        jComboBox1.setModel(new ComboBoxModels.VertexComboBoxModel(g));
        ((AbstractDocument)jTextField2.getDocument()).setDocumentFilter(new DocumentFilter(){
                @Override
                public void insertString(DocumentFilter.FilterBypass fb, int offset,
                    String text, AttributeSet attr) throws BadLocationException {
                    fb.remove(0, fb.getDocument().getLength());
                    fb.insertString(0, ""+text.charAt(text.length()-1), attr);
                }

                @Override
                public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    insertString(fb, offset, text, attrs);
                }
                
            });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        jPanel1.add(jSpinner1);

        jLabel1.setText(", ");
        jPanel1.add(jLabel1);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        jPanel1.add(jSpinner2);

        jLabel2.setText(" \u21D2 ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jComboBox1.setRenderer(new ComboBoxModels.ElementComboBoxRenderer());

        jList1.setModel(new ListModel());
        jList1.setCellRenderer(new ListRenderer());
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("-");
        jButton1.setToolTipText("löschen");
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(100, 19));
        jButton1.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton1.setPreferredSize(new java.awt.Dimension(19, 19));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("\u2191");
        jButton2.setToolTipText("nach oben verschieben");
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.setMaximumSize(new java.awt.Dimension(100, 19));
        jButton2.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton2.setPreferredSize(new java.awt.Dimension(19, 19));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("\u2193");
        jButton3.setToolTipText("nach unten verschieben");
        jButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(100, 19));
        jButton3.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton3.setPreferredSize(new java.awt.Dimension(19, 19));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("+");
        jButton4.setToolTipText("hinzufügen");
        jButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton4.setMaximumSize(new java.awt.Dimension(100, 19));
        jButton4.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton4.setPreferredSize(new java.awt.Dimension(19, 19));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("\u2192");
        jButton5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton5.setMaximumSize(new java.awt.Dimension(100, 19));
        jButton5.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton5.setPreferredSize(new java.awt.Dimension(19, 19));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jPanel3.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //add
        if (c instanceof HashSet) {
            ((HashSet)c).add(jTextField1.getText());
        } else if (c instanceof ArrayList) {
            
            if (type == Point.class) {
                try {
                    jSpinner1.commitEdit();
                    jSpinner2.commitEdit();
                } catch (ParseException ex) {
                }
                ((ArrayList)c).add(new Point((Integer)jSpinner1.getValue(),(Integer)jSpinner2.getValue()));
            } else if (type == Vertex.class) {
                ((ArrayList)c).add(jComboBox1.getSelectedItem());
            }
        } else if (c instanceof HashMap) {
            ((HashMap)c).put(jTextField2.getText().charAt(0), jTextField3.getText());
        }
        jList1.getModel().getElementAt(0);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //change
        if (c instanceof HashSet) {
            ((HashSet)c).remove(jList1.getSelectedValue());
            ((HashSet)c).add(jTextField1.getText());
        } else if (c instanceof ArrayList) {
            if (type == Point.class) {
                try {
                    jSpinner1.commitEdit();
                    jSpinner2.commitEdit();
                } catch (ParseException ex) {
                }
                ((ArrayList)c).set(jList1.getSelectedIndex(), new Point((Integer)jSpinner1.getValue(),(Integer)jSpinner2.getValue()));
            } else if (type == Vertex.class) {
                ((ArrayList)c).set(jList1.getSelectedIndex(), jComboBox1.getSelectedItem());
            }
        } else if (c instanceof HashMap) {
            ((HashMap)c).remove(((Map.Entry)jList1.getSelectedValue()).getKey());
            ((HashMap)c).put(jTextField2.getText().charAt(0), jTextField3.getText());
        }
        jList1.getModel().getElementAt(0);
        //jList1.revalidate();
        //jList1.repaint();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //remove
        if (c instanceof HashSet) {
            ((HashSet)c).remove(jList1.getSelectedValue());
        }  if (c instanceof ArrayList) {
            ((ArrayList)c).remove(jList1.getSelectedValue());
        } else if (c instanceof HashMap) {
            ((HashMap)c).remove(((Map.Entry)jList1.getSelectedValue()).getKey());
        }
        jList1.getModel().getElementAt(0);
        //jList1.revalidate();
        //jList1.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //move up
        if (c instanceof ArrayList) {
            Object tmp = jList1.getSelectedValue();
            int index = jList1.getSelectedIndex()-1;
            if (index < 0) index = jList1.getModel().getSize();
            ((ArrayList)c).set(jList1.getSelectedIndex(), jList1.getModel().getElementAt(index));
            ((ArrayList)c).set(index, tmp);
        }
        jList1.getModel().getElementAt(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (c instanceof ArrayList) {
            Object tmp = jList1.getSelectedValue();
            int index = (jList1.getSelectedIndex()+1) % jList1.getModel().getSize();
            ((ArrayList)c).set(jList1.getSelectedIndex(), jList1.getModel().getElementAt(index));
            ((ArrayList)c).set(index, tmp);
        jList1.getModel().getElementAt(0);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        jTextField1.setText(jList1.getSelectedValue().toString());
    }//GEN-LAST:event_jList1ValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
