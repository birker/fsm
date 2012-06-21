/*
 * This class represents a finite state machine in a way, that is well suitable 
 * for illustration. It is not designed for pratical use with good time and space
 * performance.
 */
package fsm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Konnarr
 */
public class Fsm extends Graph {
    private static final long serialVersionUID = 2345541351034924476L;
    //redundant, but nice to have
    private String alphabet;
    
    //special Symbols
    private char anySymbol = '?'; //we can use that transition with any read Symbol
    private char elseSymbol = '!'; //we can use that transition when there is no other
    private char epsSymbol = '#'; //spontantious transition
    private HashMap<Character, String> shortSymbols = new HashMap<Character,String>(); //a list of short cuts (e.g. x (char) stands for 0 or 1 (String: 01)); should work in Blocks, too.
    
    //for universial levenshtein automatas, we have to be able to read input in blocks. 0 should be automatic
    private int blocksize = 1;
    //private int simNonDeterminism
    
    //Simulation etc.
    private ArrayList<Element> activeEps = new ArrayList<Element>();

    public Fsm() {
        super(true);
    }
    
    @Override
    public Vertex addVertex(Point position) {
        Vertex n = new Vertex(Vertex.getDefShape(), position, "q_{"+getVertices().size()+"}");
        getVertices().add(n);
        return n;
    }
    
    public ArrayList<Element> getActiveEps() {
        return activeEps;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public char getAnySymbol() {
        return anySymbol;
    }

    public void setAnySymbol(char anySymbol) {
        this.anySymbol = anySymbol;
    }

    public int getBlocksize() {
        return blocksize;
    }

    public void setBlocksize(int blocksize) {
        this.blocksize = blocksize;
    }
    
    public char getEpsSymbol() {
        return epsSymbol;
    }

    public void setEpsSymbol(char epsSymbol) {
        this.epsSymbol = epsSymbol;
    }

    public char getElseSymbol() {
        return elseSymbol;
    }

    public void setElseSymbol(char elseSymbol) {
        this.elseSymbol = elseSymbol;
    }

    public HashMap<Character, String> getShortSymbols() {
        return shortSymbols;
    }
    
    public void startSim() {
        getActive().clear();
        activeEps.clear();
        for (Vertex n: getVertices()) {
            if (n.isInitial()) {
                getActive().add(n);
                addSpontaniousNodes(n);
            }
        }
        //notifyObservers();
    }
    
    public boolean nextStep(String input) {
        return nextStep(input, true);
    }
    
    private boolean isEqual(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        letter: for (int i = 0; i < s1.length(); i++) {
            if (!shortSymbols.containsKey(s1.charAt(i))) {
                if (s1.charAt(i)!=s2.charAt(i)) return false;
            } else {
                String t = shortSymbols.get(s1.charAt(i));
                for (int j = 0; j < t.length(); j++) {
                    if (t.charAt(j) == s2.charAt(i)) continue letter;
                }
                return false;
            }
        }
        return true;
    }
    
    private boolean nextStep(String input, boolean check) {
        getActive().addAll(activeEps);
        activeEps.clear();
        for (Element n: (ArrayList<Element>) getActive().clone()) { //von jedem zustand suchen
            getActive().remove(n);
            if (n instanceof Vertex) {
                boolean found = false;
                ArrayList<Edge> epot = new ArrayList();
                for (Edge e: ((Vertex)n).getEdges()) { //jede kante betrachten
                    for (String s: e.getTransitions()) { //jeden übergang der kante
                        if (isEqual(s,input.substring(0,Math.min((blocksize==0?s.length():blocksize), input.length())))||s.equals(""+anySymbol)) {
                            getActive().add(e);
                            getActive().add(e.getTo());
                            addSpontaniousNodes(e.getTo());
                            found = true;
                            break;
                        } else if (!found && s.equals(""+elseSymbol)) {
                            epot.add(e);
                        }
                    }

                }
                if (!found && !epot.isEmpty()) {
                    for (Edge e: epot) {
                        getActive().add(e);
                        getActive().add(e.getTo());
                        addSpontaniousNodes(e.getTo());  
                        
                    }                  
                }
            }
        }
        if (getActive().isEmpty()) return false;
        //remove duplicates - i need them during work process
        HashSet hs = new HashSet();
        hs.addAll(getActive());
        getActive().clear();
        getActive().addAll(hs);
        //activeEps.removeAll(getActive());
        if (check) {
            for (Element n: getActive()) {
                if ((n instanceof Vertex)&&(((Vertex)n).isFinal())) return true;
            }
            for (Element n: activeEps) {
                if ((n instanceof Vertex)&&(((Vertex)n).isFinal())) return true;
            }
        } else return true;
        return false;
    }
    
    public boolean accept(String input) {
        startSim();
        for (int i = 0; i < input.length()-blocksize; i += blocksize) {
            if (!nextStep(input.substring(i),false)) return false;
        }
        //TODO wie sieth der input des letzten schrittes aus?
        boolean b = nextStep(input.substring((input.length() / blocksize)-1),true);
        getActive().clear();
        return b;
    }

    private void addSpontaniousNodes(Vertex to) {
        for (Edge e: to.getEdges()) {
            for (String s: e.getTransitions()) {
                if (s.equals(""+epsSymbol)) {
                    activeEps.add(e);
                    if (/*!getActive().contains(e.getTo()) &&*/ !activeEps.contains(e.getTo())) {
                        activeEps.add(e.getTo());
                        addSpontaniousNodes(e.getTo());
                    }
                }
            }
        }
    }
    
    @Override
    public void setDirected(boolean directed) {
        if (directed = false) throw new IllegalArgumentException("Fsm are always directed!");
    }
   
    public static Fsm distanceAutomaton(int distance, String pattern, int type) {
        Fsm fsm = new Fsm();
        for (int i=0; i <= distance; i++) {
            for (int j = 0; j <= pattern.length(); j++) {
                    Vertex n = fsm.addVertex(new Point(30+j*3*(int)Vertex.getDefShape().getWidth(),10+(distance-i)*3*(int)Vertex.getDefShape().getHeight()));
                    n.setText(i+"-"+j);
                    if (j==pattern.length()) n.setFinal(true);
                    if (j>0) {
                        Edge e = fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2), n, true);
                        e.getTransitions().add(""+pattern.charAt(j-1));
                        e.setText(false);
                    }
                    if (j>0 && i>0) {
                        Edge e = fsm.addEdge(fsm.getVertices().get((i-1)*(pattern.length()+1)+j-1), n, true);
                        e.getTransitions().add(""+fsm.getElseSymbol());
                        e.setText(false);
                    }
                    if (type>0) {
                        if (j>0 && i>0) {
                            Edge e = fsm.getEdges().get(fsm.getEdges().size()-1);
                            e.getTransitions().add(""+fsm.getEpsSymbol());
                            e.setText(false);
                        }
                        if (i>0) {
                            Edge e = fsm.addEdge(fsm.getVertices().get((i-1)*(pattern.length()+1)+j), n, true);
                            e.getTransitions().add(""+fsm.getElseSymbol());
                            e.setText(false);
                        }
                    }
            }
        }
        if (type==0) {
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
    
    public static Fsm uniHamming(int distance) {
        Fsm fsm = new Fsm();
        for (int i=0; i <= distance; i++) {
            Vertex n = fsm.addVertex(new Point(30+i*3*(int)Vertex.getDefShape().getWidth(),10));
            n.setText("I^{"+i+"}");
            n.setFinal(true);
            Edge e = fsm.addEdge(n, n, true);
            e.getTransitions().add("1");
            e.setText(false);
            if (i>0) {
                e = fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2), n, true);
                e.getTransitions().add("0");
                e.setText(false);
            }
        }
        fsm.getVertices().get(0).setInitial(true);
        return fsm;
    }
    
    public static Fsm uniNLevenshtein(int distance) {
        Fsm fsm = new Fsm();
        fsm.getShortSymbols().put('_', "01");
        fsm.setBlocksize(distance*2+1);
        for (int i=0; i <= distance; i++) {
            for (int j = -i; j<=i; j++) {
                Vertex n = fsm.addVertex(new Point(30+i*3*(int)Vertex.getDefShape().getWidth(),10+(distance+j)*2*(int)Vertex.getDefShape().getHeight()));
                n.setText("I"+(j==0?"":(j>0?"+"+j:j))+"^{"+i+"}");
                n.setFinal(true);
                Edge e = fsm.addEdge(n, n, true);
                e.getTransitions().add(prepareString(distance).replace(j+distance, j+distance+1, "1").toString());
                e.setText(false);
                if (i>0) {
                    if (j < i-1) {
                        e =fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2*i), n, true);
                        e.getTransitions().add(prepareString(distance).replace(distance-(-j)+1, distance-(-j)+2, "0").toString());
                        e.setText(false);
                    }
                    if (i != Math.abs(j)) {
                        e =fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2*i-1), n, true);
                        e.getTransitions().add(prepareString(distance).replace(distance-(-j), distance-(-j)+1, "0").toString());
                        e.setText(false);                        
                    }
                    if (j > -i+1) {
                        e =fsm.addEdge(fsm.getVertices().get(fsm.getVertices().size()-2*i-2), n, true);
                        e.getTransitions().add(prepareString(distance).replace(distance-(-j)-1, distance-(-j), "0").replace(distance-(-j), distance-(-j)+1, "1").toString());
                        e.setText(false);                         
                    }
                     
                }
            }
            
        }
        //TODO es fehlen ein paar kanten. welche genau?? auf jeden fall i0-->i+22
        fsm.getVertices().get(0).setInitial(true);
        return fsm;
    }
}
