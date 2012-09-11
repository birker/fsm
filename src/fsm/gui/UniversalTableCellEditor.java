/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Edge;
import fsm.Graph;
import fsm.Vertex;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RectangularShape;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Konnarr
 */
public class UniversalTableCellEditor extends AbstractCellEditor implements TableCellEditor{
        private static final long serialVersionUID = 1L;
        
        private final JTextField text = new JTextField();
        private final JComboBox<Edge.PathMode> comboPath = new JComboBox<Edge.PathMode>(Edge.PathMode.values());
        private final JComboBox<Vertex.ShapeType> comboShape = new JComboBox<Vertex.ShapeType>(Vertex.ShapeType.values());
        private final JComboBox<Vertex> comboVertex = new JComboBox<Vertex>();
        private final JLabel color = new JLabel();
        private final JSpinner spinnerInt = new JSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        private final JSpinner spinnerIntx = new JSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        private final JSpinner spinnerInty = new JSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        private final JSpinner spinnerDouble = new JSpinner(new SpinnerNumberModel(Double.valueOf(0), null, null, Double.valueOf(1)));
        private final JTextField charField = new JTextField();
        private final JPanel point = new JPanel();
        private final CollectionEditor coll;
        
        private Component active;
        private final ArrayList<Component> focus = new ArrayList<Component>();
        
        private Timer autostop = new Timer();
                
        class Listener implements FocusListener, ActionListener, ChangeListener, KeyListener, MouseListener {
            
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource() instanceof JComboBox) {
                    autostop.cancel();
                } else {
                    startTimer();
                }
            }
 
            @Override
            public void focusLost(FocusEvent e) {
                    if (autostop != null) {
                        autostop.cancel();
                    }
                    if (!(e.getOppositeComponent() instanceof JTable) && !focus.contains(e.getOppositeComponent())) {
                        stopCellEditing();
                    }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
            }

            private void startTimer() {
                autostop.cancel();
                autostop = new Timer();
                autostop.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                stopCellEditing();
                            }
                        }, 3000);
            }
            

            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JSpinner) {
                    ((JSpinner.DefaultEditor) ((JSpinner)e.getSource()).getEditor()).getTextField().requestFocusInWindow();
                    startTimer();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                startTimer();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                startTimer();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (autostop != null) autostop.cancel();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startTimer();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
            
        } 
        
        public UniversalTableCellEditor(Graph g) {
            point.setLayout(new BoxLayout(point, BoxLayout.LINE_AXIS));
            point.add(spinnerIntx);
            point.add(new JLabel(", "));
            point.add(spinnerInty);
            ((AbstractDocument)charField.getDocument()).setDocumentFilter(new DocumentFilter(){
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
            coll = new CollectionEditor(g);            
            comboVertex.setModel(new ComboBoxModels.VertexComboBoxModel(g));
            comboVertex.setRenderer(new ComboBoxModels.ElementComboBoxRenderer());
            Listener listener = new Listener();
            focus.add(comboVertex);
            focus.add(comboPath);
            focus.add(comboPath);
            focus.add(comboShape);
            focus.add(text);
            focus.add(((JSpinner.DefaultEditor)spinnerInt.getEditor()).getTextField());
            focus.add(((JSpinner.DefaultEditor)spinnerIntx.getEditor()).getTextField());
            focus.add(((JSpinner.DefaultEditor)spinnerInty.getEditor()).getTextField());
            focus.add(((JSpinner.DefaultEditor)spinnerDouble.getEditor()).getTextField());
            for (Component c: focus) {
                c.addFocusListener(listener);
                if (c instanceof JComboBox) {
                    ((JComboBox)c).addActionListener(listener);
                } else {
                    c.addKeyListener(listener);
                    c.addMouseListener(listener);
                }
            }
            spinnerInt.addChangeListener(listener);
            spinnerIntx.addChangeListener(listener);
            spinnerInty.addChangeListener(listener);
            spinnerDouble.addChangeListener(listener);
        }
        
        
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {
            if (value instanceof Edge.PathMode) {
                comboPath.setSelectedItem(value);
                active = comboPath;
            } else if (value instanceof RectangularShape) {
                comboShape.setSelectedItem(Vertex.ShapeType.getEnum((RectangularShape)value));
                active =  comboShape;
            } else if (value instanceof Color) {
                Color c = (Color)value;
                color.setBackground(c);
                color.setText("");
                c = JColorChooser.showDialog(color, "Farbe wählen", (Color)value);
                if (c!=null) {
                    color.setBackground(c);
                }
                active =  color;
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopCellEditing();
                    }
                });
                
            } else if (value instanceof Boolean) {
                color.setText((Boolean)value?"\u2713":"\u2717");
                active =  color;
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopCellEditing();
                    }
                });                
            } else if (value instanceof Integer) {
                spinnerInt.setValue((Integer)value);
                active = spinnerInt;
            } else if (value instanceof Double) {
                spinnerDouble.setValue((Double)value);
                active =  spinnerDouble;
            } else if (value instanceof Point) {
                spinnerIntx.setValue(((Point)value).x);
                spinnerInty.setValue(((Point)value).y);
                active =  point;
            } else if (value instanceof Vertex) {
                comboVertex.setSelectedItem(value);
                active =  comboVertex;
            } else if (value instanceof Edge) {
                text.setText(((Edge)value).getLabel().getText());
                active = text;
            } else if (value instanceof Character) {
                charField.setText(""+(Character) value);
                active = charField;
            } else if (value instanceof Collection || value instanceof Map) {
                if (table.getModel() instanceof TableQxS.QxSTableModel) {
                    coll.setCollection(value, Vertex.class);
                } else if (table.getModel() instanceof TableQxQ.QxQTableModel) {
                    coll.setCollection(value, Edge.class);
                } else {
                    coll.setCollection(value, Point.class);
                }
                
                JOptionPane.showMessageDialog(coll, coll, "", JOptionPane.QUESTION_MESSAGE);
                active = coll;
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopCellEditing();
                    }
                });
            } else {
                if (value != null) text.setText(value.toString());
                active =  text;
            }
            return active;
        }

        @Override
        public Object getCellEditorValue() {
            if (active == comboPath) {
                return comboPath.getSelectedItem();
            } else if (active == comboShape) {
                return ((Vertex.ShapeType)comboShape.getSelectedItem()).getShapeClass();
            } else if (active == color) {
                if (color.getText().equals("")) return color.getBackground();
                else return color.getText().equals("\u2717");
            } else if (active == spinnerInt) {
                try {
                    ((JSpinner.DefaultEditor)spinnerInt.getEditor()).commitEdit();
                } catch (ParseException ex) {
                    Logger.getLogger(ObjectInspector.class.getName()).log(Level.SEVERE, null, ex);
                }
                return spinnerInt.getValue();
            } else if (active == spinnerDouble) {
                try {
                    ((JSpinner.DefaultEditor)spinnerDouble.getEditor()).commitEdit();
                } catch (ParseException ex) {
                    Logger.getLogger(ObjectInspector.class.getName()).log(Level.SEVERE, null, ex);
                }
                return spinnerDouble.getValue();
            } else if (active == point) {
                try {
                    ((JSpinner.DefaultEditor)spinnerIntx.getEditor()).commitEdit();
                    ((JSpinner.DefaultEditor)spinnerInty.getEditor()).commitEdit();
                } catch (ParseException ex) {
                    Logger.getLogger(ObjectInspector.class.getName()).log(Level.SEVERE, null, ex);
                }
                return new Point((Integer)spinnerIntx.getValue(),(Integer)spinnerInty.getValue());
            } else if (active == comboVertex) {
                return comboVertex.getSelectedItem();
            } else if (active == charField) {
                return charField.getText().charAt(0);
            } else if (active == coll) {
                return coll.c;
            } else {
                return text.getText();
            }
        }
    }
