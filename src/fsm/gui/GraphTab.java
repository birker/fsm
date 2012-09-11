/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Fsm;
import fsm.Graph;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

    
/**
 *
 * @author Konnarr
 */
public class GraphTab extends JPanel  {
    private static final long serialVersionUID = 1L;
    private Graph g;
    private GraphPanel graph;
    private ObjectInspector oi;
    private SimulationPanel sim;
    private TableQxQ table;
    private TableQxS table2;
    
    public GraphTab(Graph g) {
        this.g = g;
        setLayout(new BorderLayout());
        oi = new ObjectInspector(g);
        g.addObserver(oi);
        JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,new JScrollPane(oi),null);
        graph = new GraphPanel(g);
        g.addObserver(graph);
        JTabbedPane tp = new JTabbedPane();
        JPanel p = new JPanel(new BorderLayout());
        p.add(graph);
        tp.add("Graph", p);
        tp.setPreferredSize(new Dimension(400,400));
        table = new TableQxQ(g);
        g.addObserver(table);
        tp.add("QxQ Tabelle",table);
        if (g instanceof Fsm) {
            table2 = new TableQxS((Fsm)g);
            g.addObserver(table2);
            g.notifyObs();
            tp.add("QxS Tabelle",table2);
        }
        if (g instanceof Fsm) {
            Fsm fsm = (Fsm) g;
            sim = new SimulationPanel(fsm);
            JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,sim,tp);
            //add(sp);
            sp2.setRightComponent(sp);
        } else {
            //add(tp);
            sp2.setRightComponent(tp);
        }
        add(sp2);
        sp2.setDividerLocation(200);
    }

    public Graph getGraph() {
        return g;
    }

    public GraphPanel getGraphPanel() {
        return graph;
    }

    public SimulationPanel getSimPanel() {
        return sim;
    }
    
    public ObjectInspector getObjectInspector() {
        return oi;
    }

    public void setComment(String val) {
        
    }
}
