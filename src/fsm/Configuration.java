/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Konnarr
 */
public class Configuration implements Serializable{
    private static final long serialVersionUID = 1L;
    public final Vertex q;
    public final String input;
    public final Edge edge;
    private final Fsm g;
    
    public Configuration(Fsm g, Edge edge, String input) {
        this.g = g;
        this.q = edge.getTo();
        this.input = input;
        this.edge = edge;
    }
    
    public Configuration(Fsm g, Vertex q, String input) {
        this.g = g;
        this.q = q;
        this.input = input;
        edge = null;
    }
    
    /**
     * checks if s1 is a valid transition for s2 with respect to blocks and shortSymbols
     * @param s1 the pattern
     * @param s2 the input
     * @return true if the transition s1 is valid for input s2
     */
    private boolean isEqual(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        letter: for (int i = 0; i < s1.length(); i++) {
            if (!g.getShortSymbols().containsKey(s1.charAt(i))) {
                if (s1.charAt(i)!=s2.charAt(i)) return false;
            } else {
                String t = g.getShortSymbols().get(s1.charAt(i));
                for (int j = 0; j < t.length(); j++) {
                    if (t.charAt(j) == s2.charAt(i)) continue letter;
                }
                return false;
            }
        }
        return true;
    }
    
    public ArrayList<Configuration> nextConfigurations() {
        ArrayList<Configuration> al = new ArrayList<Configuration>();
        ArrayList<Configuration> epot = new ArrayList<Configuration>();
        int eps = 0;
        for (Edge e: q.getEdges()) {
            for (String t: ((EdgeFsm)e).getTransitions()) {
                if (t.length()>input.length()) continue;
                if (isEqual(t,input.substring(0, t.length()))) {
                    al.add(new Configuration(g, e, input.substring(t.length())));
                } else if (t.equals(""+g.getAnySymbol())) {
                    al.add(new Configuration(g, e, input.substring(1)));
                } else if (t.equals(""+g.getEpsSymbol())) {
                    al.add(new Configuration(g, e, input));
                    eps++;
                } else if (t.equals(""+g.getElseSymbol())) {
                    epot.add(new Configuration(g, e, input.substring(1)));
                }
            }
        }
        if (al.size() <= eps) {
            al.addAll(epot);
        }
        return al;
    }

    @Override
    public int hashCode() {
        int hash = g.getVertices().indexOf(q)+input.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Configuration other = (Configuration) obj;
        if (this.q != other.q && (this.q == null || !this.q.equals(other.q))) {
            return false;
        }
        if ((this.input == null) ? (other.input != null) : !this.input.equals(other.input)) {
            return false;
        }
        if (this.g != other.g && (this.g == null || !this.g.equals(other.g))) {
            return false;
        }
        return true;
    }
    
    
}
