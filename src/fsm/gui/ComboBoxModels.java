/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Edge;
import fsm.Fsm;
import fsm.Graph;
import fsm.Vertex;
import java.awt.Component;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Konnarr
 */
public class ComboBoxModels {
     ///////////////////////////////////ComboBoxRenderer/////////////////////////
    static class ElementComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setOpaque(true);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            if (value == null) {
                setText(""); 
            } else if (value instanceof Vertex) {
                setText(((Vertex)value).getName());
            } else if (value instanceof Edge){
                setText(((Edge)value).getName()+" ("+((Edge)value).getFrom().getName()+", "+((Edge)value).getTo().getName()+")");
            } else {
                setText(value.toString());
            }
            return this;
        }

    }
    ///////////////////Combobox Model - just vertices/////////////////////////////
    static class VertexComboBoxModel extends AbstractListModel<Vertex> implements ComboBoxModel<Vertex> {
        private static final long serialVersionUID = 1L;
        Graph g;
        Vertex sel;

        public VertexComboBoxModel(Graph g) {
            this.g = g;
        }


        @Override
        public int getSize() {
            return g.getVertices().size();
        }

        @Override
        public Vertex getElementAt(int index) {
            fireContentsChanged(g.getVertices().get(index), index, index);
            return g.getVertices().get(index);
        }

        @Override
        public void setSelectedItem(Object anItem) {
            sel = (Vertex)anItem;
        }

        @Override
        public Object getSelectedItem() {
            return sel;
        }

    }
    ////////////////////combobox model - complete//////////////////////////////
    static class ElementComboBoxModel extends AbstractListModel<Object> implements ComboBoxModel<Object> {
        private static final long serialVersionUID = 1L;
        Graph g;
        Object sel;

        public ElementComboBoxModel(Graph g) {
            this.g = g;
        }

        @Override
        public int getSize() {
            return (g.getVertices().size()+g.getEdges().size()+3);
        }

        @Override
        public Object getElementAt(int index) {
            //int orig = index;
            if (index == 0) return (g instanceof Fsm?"Automat":"Graph");
            if (index == 1) return "--Knoten--";
            //index -= 2;
            if (index >= 2 && index < g.getVertices().size()+2) {
                fireContentsChanged(g.getVertices().get(index-2), index, index);
                return g.getVertices().get(index-2);
            } 
            //index -= g.getVertices().size()-1;
            if (index == g.getVertices().size()+2) return "--Kanten--";
            if (index >= g.getVertices().size()+3 && index < g.getVertices().size()+3+g.getEdges().size()) {
                fireContentsChanged(g.getEdges().get(index-(g.getVertices().size()+3)), index, index);
                return g.getEdges().get(index-(g.getVertices().size()+3));
            }
            return null;
        }

        @Override
        public void setSelectedItem(Object anItem) {
            sel = anItem;
        }

        @Override
        public Object getSelectedItem() {
            return sel;
        }

    }
}
