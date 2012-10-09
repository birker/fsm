/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

import java.awt.Point;

/**
 *
 * @author Konnarr
 */
public class DistanceAutomata {
    public static enum DistanceType {
        HAMMING, LEVENSHTEIN, DAMERAU;
        static public DistanceType getByNumber(int i) {
            if (i%3==0) return HAMMING;
            else if (i%3==1) return LEVENSHTEIN;
            else return DAMERAU;
        }
    }
     
    /**
     * Computes a distance automaton (Hamming, Levenshtein, Damerau-Levenshtein) of the 
     * specified distance for a certain strin pattern. Only Hamming is deterministic.
     * Note: The distance between two strings is defined as the minimum number of edits
     * needed to transform one string into the other, with the allowable edit operations being 
     * substitution (Hamming, Levenshtein, Damerau-Levenshtein), insertion and deletion
     * (Levensheitn, Damerau-Levenshtein) of a single character
     * and transposition of neighboured characters (Damerau-Levenshtein). [edited from wikipedia]
     * @param distance the number of "mistakes" that are allowed.
     * @param pattern the string without "mistakes"
     * @param type 0: Hamming
     *             2: Levenshtein
     *             3: Damerau-Levenshtein
     * @return the computed distance automaton
     */
    public static Fsm distanceAutomaton(int distance, String pattern, DistanceType type) {
        //TODO damerau levenshtein
        Fsm fsm = new Fsm();
        for (int i=0; i <= distance; i++) {
            for (int j = 0; j <= pattern.length(); j++) {
                    Vertex n = fsm.addVertex(new Point(30+j*3*(int)fsm.getDefVertexShape().getWidth(),10+(distance-i)*3*(int)fsm.getDefVertexShape().getHeight()));
                    n.setName(i+"-"+j);
                    if (j==pattern.length()) n.setFinal(true);
                    if (j>0) {
                        EdgeFsm e = fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2), n);
                        e.getTransitions().add(""+pattern.charAt(j-1));
                        e.setText();
                    }
                    if (j>0 && i>0) {
                        EdgeFsm e = fsm.addEdge(fsm.getVertices().get((i-1)*(pattern.length()+1)+j-1), n);
                        e.getTransitions().add(""+fsm.getElseSymbol());
                        e.setText();
                    }
                    if (type.equals(DistanceType.LEVENSHTEIN) || type.equals(DistanceType.DAMERAU)) {
                        if (j>0 && i>0) {
                            EdgeFsm e = (EdgeFsm) fsm.getEdges().get(fsm.getEdges().size()-1);
                            e.getTransitions().add(""+fsm.getEpsSymbol());
                            e.setText();
                        }
                        if (i>0) {
                            EdgeFsm e = fsm.addEdge(fsm.getVertices().get((i-1)*(pattern.length()+1)+j), n);
                            e.getTransitions().add(""+fsm.getElseSymbol());
                            e.setText();
                        }
                    }
            }
        }
        if (type.equals(DistanceType.HAMMING)) {
            for (int i = distance; i>0; i--) {
                for (int j = i-1; j >= 0; j--) {
                    fsm.removeVertex(fsm.getVertices().get(i*(pattern.length()+1)+j));
                }
            }
        }
        fsm.getVertices().get(0).setInitial(true);
        return fsm;
    }
    
    private static StringBuilder prepareString(int k) {
        StringBuilder s = new StringBuilder(2*k+1);
        for (int j = -k; j<=k; j++) {
            s.append('_');
        }
        return s;
    }   
    
    /**
     * Returnes a _universial_ Hamming Automaton of the specified distance.
     * The automaton is deterministic and has no practical use, as the
     * efford of the required encoding is greater then the computation of the distance itself.
     * Note: The input has to be encoded, e.g. with the InputCoder class.
     * Note: Use powerset construction to get a deterministic version.
     * Note: The Hamming distance between two strings is defined as the minimum number of edits
     * needed to transform one string into the other, with the allowable edit operations being 
     * only substitution of a single character. [edited from wikipedia]
     * @param distance the number of "mistakes" that are allowed.
     * @return the computed non deterministic universial Levenshtein Automaton
     */
    public static Fsm uniHamming(int distance) {
        Fsm fsm = new Fsm();
        for (int i=0; i <= distance; i++) {
            Vertex n = fsm.addVertex(new Point(30+i*3*(int)fsm.getDefVertexShape().getWidth(),10));
            n.setName("I^{"+i+"}");
            n.setFinal(true);
            EdgeFsm e = fsm.addEdge(n, n);
            e.getTransitions().add("1");
            e.setText();
            if (i>0) {
                e = fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2), n);
                e.getTransitions().add("0");
                e.setText();
            }
        }
        fsm.getVertices().get(0).setInitial(true);
        return fsm;
    }
    
    /**
     * Returns a _universial_ non deterministic Levenshtein Automaton of the specified distance.
     * Note: The input has to be encoded, e.g. with the InputCoder class.
     * Note: Use powerset construction to get a deterministic version.
     * Note: The Levenshtein distance between two strings is defined as the minimum number of edits
     * needed to transform one string into the other, with the allowable edit operations being 
     * insertion, deletion, or substitution of a single character. [from wikipedia]
     * @param distance the number of "mistakes" that are allowed.
     * @return the computed non deterministic Levenshtein Automaton
     */
    public static Fsm uniNLevenshtein(int distance) {
        Fsm fsm = new Fsm();
        fsm.getShortSymbols().put('_', "01");
        fsm.setDefEdgeLabelRot(true);
        //fsm.setBlocksize(distance*2+1);
        for (int i=0; i <= distance; i++) {
            for (int j = -i; j<=i; j++) {
                Vertex n = fsm.addVertex(new Point(30+i*3*(int)fsm.getDefVertexShape().getWidth(),10+(distance+j)*2*(int)fsm.getDefVertexShape().getHeight()));
                n.setName("I"+(j==0?"":(j>0?"+"+j:j))+"^{"+i+"}");
                n.setFinal(true);
                EdgeFsm e = fsm.addEdge(n, n);
                e.rebuildPath();
                e.getTransitions().add(prepareString(distance).replace(j+distance, j+distance+1, "1").toString());
                e.setText();
                if (i>0) {
                    if (j < i-1) {
                        e =fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2*i), n);
                        e.getTransitions().add(prepareString(distance).replace(distance-(-j)+1, distance-(-j)+2, "0").toString());
                        e.setText();
                    }
                    if (i != Math.abs(j)) {
                        e =fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2*i-1), n);
                        e.getTransitions().add(prepareString(distance).replace(distance-(-j), distance-(-j)+1, "0").toString());
                        e.setText();                        
                    }
                    if (j > -i+1) {
                        e =fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2*i-2), n);
                        //e.getTransitions().add(prepareString(distance).replace(distance-(-j)-1, distance-(-j), "0").replace(distance-(-j), distance-(-j)+1, "1").toString());
                        e.getTransitions().add(""+fsm.getEpsSymbol());
                        e.setText();                         
                    }
                     
                }
            }
            
        }
        fsm.getVertices().get(0).setInitial(true);
        return fsm;
    }
    
    public static Fsm uniNDamerau(int distance) {
        return null;
    }
}
