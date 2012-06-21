/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.gui;

import fsm.Fsm;
import fsm.Graph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

    
/**
 *
 * @author Konnarr
 */
public class GraphTab extends JPanel {
    private Graph g;
    private GraphPanel graph;
    private SimulationPanel sim;
    
    public GraphTab(Graph g) {
        this.g = g;
        setLayout(new BorderLayout());
        graph = new GraphPanel(g);
        g.addObserver(graph);
        JTabbedPane tp = new JTabbedPane();
        JPanel p = new JPanel(new BorderLayout());
        p.add(graph);
        tp.add("Graph", p);
        tp.setPreferredSize(new Dimension(400,400));
        if (g instanceof Fsm) {
            Fsm fsm = (Fsm) g;
            sim = new SimulationPanel(fsm);
            JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,sim,tp);
            add(sp);
        } else {
            add(tp);
        }
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

}
