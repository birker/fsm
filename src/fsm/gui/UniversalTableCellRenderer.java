/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Edge;
import fsm.Vertex;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class UniversalTableCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            setText("");
            setHorizontalAlignment(SwingConstants.CENTER);
            if (column == 1)
            if (table.getRowHeight(row)!=table.getRowHeight()) table.setRowHeight(row, table.getRowHeight());
            if (value instanceof Boolean) {
                if ((Boolean) value) {
                    setText("\u2713");
                } else {
                    setText("\u2717");
                }
            } else if (value instanceof Color) {
                Color c = (Color) value;
                setBackground(c);
                setForeground(new Color(c.getRed()>128?0:255,c.getGreen()>128?0:255,c.getBlue()>128?0:255));
                if (ObjectInspector.ColorNames.containsKey(c)) setText(ObjectInspector.ColorNames.get(c));
                else setText(c.getRed()+", "+c.getGreen()+", "+c.getBlue());
            } else if (value instanceof RectangularShape) {
                setText(Vertex.ShapeType.getEnum((RectangularShape)value).toString());
            } else if (value instanceof Vertex) {
                setText(Vertex.parseString(((Vertex)value).getName()));
            } else if (value instanceof Point) {
                setText(((Point)value).x+", "+((Point)value).y);
            } else if (value instanceof Double) {
                setText(""+(double)(int)(((Double)value)*1000)/1000);
            } else if (value instanceof ArrayList || value instanceof HashSet) {
                setText("<html>");
                for (Object o: (Collection) value) {
                    if (o instanceof Point) {
                        setText(getText()+((Point)o).x+", "+((Point)o).y+"<br>");
                    } else if (o instanceof Vertex) {
                        setText(getText()+Vertex.parseString(((Vertex)o).getName())+"<br>");
                    } else {
                        setText(getText()+o.toString()+"<br>");
                    }
                }
                setText(getText()+"</html>");
                table.setRowHeight(row, Math.max(Math.max(table.getRowHeight(),table.getRowHeight(row)), (int) getPreferredSize().getHeight()));
            } else if (value instanceof HashMap) {
                setText("<html>");
                for (Object o: ((HashMap) value).keySet()) {
                    setText(getText()+o.toString()+": ");
                    String s = ((HashMap) value).get(o).toString();
                    for (int i = 0; i < s.length()-1; i++) {
                        setText(getText()+s.charAt(i)+", ");
                    }
                    setText(getText()+s.charAt(s.length()-1)+"<br>");
                }
                setText(getText()+"</html>");
                table.setRowHeight(row, Math.max(table.getRowHeight(), (int) getPreferredSize().getHeight()));
            } else if (value instanceof Edge) {
                setText(((Edge)value).getLabel().getText());
            } else if (value == null) {
                setBackground(getBackground().darker());
            } else {
                super.setValue(value);
            }
            return this;
        }
    }
