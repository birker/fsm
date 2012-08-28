/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.util.ArrayList;

/**
 *
 * @author Konnarr
 */
public class EdgeFsm extends Edge {
    private static final long serialVersionUID = 1L;
    
    public EdgeFsm(Fsm fsm, Vertex from, Vertex to) {
        super(fsm, from, to);
        setDirected(true);
    }
    
    private ArrayList<String> trans = new ArrayList<String>();
    
    //zu public
    public ArrayList<String> getTransitions() {
        return trans;
    }//*/
    
    @Override
    public void setText() {
        String s = getName();
        if (!s.equals("") && !trans.isEmpty()) s += ": ";
        for (String t: trans) {
            s += (s.equals("")?"":", ")+t;
        }
        getLabel().setText(s);
        //setText(s);
    }
        
     /**
     * disables the functionality of setting the graph undirected, as finite state
     * machines are always directed.
     * @throws IllegalArgumentException if directed is true
     * @param directed must be false for Fsm
     */
    @Override
    public void setDirected(boolean directed) {
        if (directed = false) throw new IllegalArgumentException("Fsm are always directed!");
    }
    
}
