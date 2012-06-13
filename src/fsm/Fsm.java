/*
 * This class represents a finite state machine in a way, that is well suitable 
 * for illustration. It is not designed for pratical use with good time and space
 * performance.
 */
package fsm;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;

/**
 *
 * @author Konnarr
 */
public class Fsm  extends Observable implements Serializable{
    private static final long serialVersionUID = 2345541351034924475L;
    private ArrayList<Node> states = new ArrayList<Node>();
    private ArrayList<Edge> transitions = new ArrayList<Edge>();
    //redundant, but nice to have
    private String alphabet;
    
    //special Symbols
    private char anySymbol = '?'; //we can use that transition with any read Symbol
    private char elseSymbol = '!'; //we can use that transition when there is no other
    private char epsSymbol = '#'; //spontantious transition
    private HashMap<Character, String> shortSymbols; //a list of short cuts (e.g. x (char) stands for 0 or 1 (String: 01)); should work in Blocks, too.
    
    //for universial levenshtein automatas, we have to be able to read input in blocks. 0 should be automatic
    private int blocksize = 1;
    //private int simNonDeterminism
    
    //Simulation etc.
    private Element choice;
    private ArrayList<Element> active = new ArrayList<Element>();

    public Node addState(Point position) {
        Node n = new Node(Node.getDefShape(), position, "q_"+states.size());
        states.add(n);
        //notifyObservers();
        return n;
    }
    
    public Edge addTransition(Node from, Node to, boolean autoSP) {
        Edge e = new Edge(from, to, autoSP);
        transitions.add(e);
        from.getEdges().add(e);
        //notifyObservers();
        return e;
    }
    
    public void removeTransition(Edge e) {
        e.getFrom().getEdges().remove(e);
        if (choice == e) choice = null;
        active.remove(e);
        transitions.remove(e);
        //e.getLabel().getParent().remove(e.getLabel());        
        //notifyObservers();
    }
    
    public void removeState(Node n) {
        if (choice == n) choice = null;
        active.remove(n);
        for (Edge e: (ArrayList<Edge>) transitions.clone()) {
            if (e.getFrom() == n || e.getTo() == n) removeTransition(e);
        }
        states.remove(n);
        //notifyObservers();
    }
    
    public ArrayList<Node> getStates() {
        return states;
    }
    
    public ArrayList<Edge> getTransitions() {
        return transitions;
    }
 
    
    public ArrayList<Element> getActive() {
        return active;
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

    public Element getChoice() {
        return choice;
    }

    public void setChoice(Element choice) {
        this.choice = choice;
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
    
    public void alignNodes() {
        Iterator<Node> it = getStates().iterator();
        if (!it.hasNext()) return;
        Node n0 = it.next();
        while (it.hasNext()) {
            Node n = it.next();
            double offx = n0.getShape().getX();
            double offy = n0.getShape().getY();
            double facx = Math.round((n.getShape().getX() - offx) / (3*n0.getPreferredWidth())) ;
            double facy = Math.round((n.getShape().getY() - offy) / (3*n0.getShape().getHeight())) ;
            double x = offx+facx*3*n0.getPreferredWidth();
            double y = offy+facy*3*n0.getShape().getHeight();
            //check if there is already a node;
            boolean found = true;
            while (found) {
                found = false;
            for (Node n2: getStates()) {
                if (n==n2) break;
                if (Math.abs(n2.getShape().getX() - x)<1 && Math.abs(n2.getShape().getY() - y)<1) {
                    facx++;
                    x = offx+facx*3*n0.getPreferredWidth();
                    found = true;
                }
            }
            }
            n.getShape().setFrame(x,y,n.getShape().getWidth(),n.getShape().getHeight());
        }
        Iterator<Edge> it2 = getTransitions().iterator();
        while (it2.hasNext()) {
            it2.next().rebuildPath();                
        }
        //notifyObservers();
        
    }
    
    public void notifyObs() {
        setChanged();
        notifyObservers();
    }
    
    public void startSim() {
        active.clear();
        for (Node n: getStates()) {
            if (n.isInitial()) active.add(n);
        }
        //notifyObservers();
    }
    
    public boolean nextStep(String input) {
        return nextStep(input, true);
    }
    
    private boolean nextStep(String input, boolean check) {
        for (Element n: (ArrayList<Element>) active.clone()) { //von jedem zustand suchen
            active.remove(n);
            if (n instanceof Node) {
            for (Edge e: ((Node)n).getEdges()) { //jede kante betrachten
                for (String s: e.getTransitions()) { //jeden übergang der kante
                    if (s.equals(input)) {
                        active.add(e);
                        active.add(e.getTo());
                        break;
                    }
                }

            }
            }
        }
        //if (active.isEmpty()) return false;
        //remove duplicates - i need them during work process
        HashSet hs = new HashSet();
        hs.addAll(active);
        active.clear();
        active.addAll(hs);
        if (check) {
            //notifyObservers();
            for (Element n: active) {
                if ((n instanceof Node)&&(((Node)n).isFinal())) return true;
            }
        }
        return false;
    }
    
    public boolean accept(String input) {
        startSim();
        for (int i = 0; i < input.length()-1; i++) {
            nextStep(""+input.charAt(i),false);
        }
        boolean b = nextStep(""+input.charAt(input.length()-1),true);
        active.clear();
        return b;
    }
}
