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

/**
 *
 * @author Konnarr
 */
public class Fsm implements Serializable{
    private static final long serialVersionUID = 2345541351034924475L;
    private ArrayList<Node> states = new ArrayList<Node>();
    private ArrayList<Edge> transitions = new ArrayList<Edge>();
    //redundant, but nice to have
    private String alphabet;
    
    //special Symbols
    private char anySymbol = '*'; //we can use that transistion with any read Symbol
    private char epsSymbol = 'e'; //spontantious transition
    private HashMap<Character, String> shortSymbols; //a list of short cuts (e.g. x stands for 0 or 1); should work in Blocks, too.
    
    //for universial levenshtein automatas, we have to be able to read input in blocks. 0 should be automatic
    private int blocksize = 1;
    //private ? simNonDeterminism
    
    //Simulation etc.
    private Element choice;
    private ArrayList<Element> active = new ArrayList<Element>();

    public Node addState(Point position) {
        Node n = new Node(Node.getDefShape(), position);
        states.add(n);
        return n;
    }
    
    public Edge addTransition(Node from, Node to) {
        Edge e = new Edge(from, to);
        transitions.add(e);
        return e;
    }
    
    public void removeTransition(Edge e) {
        e.getFrom().getEdges().remove(e);
        if (choice == e) choice = null;
        active.remove(e);
        transitions.remove(e);
        e.getLabel().getParent().remove(e.getLabel());
    }
    
    public void removeState(Node n) {
        if (choice == n) choice = null;
        active.remove(n);
        for (Edge e: (ArrayList<Edge>) transitions.clone()) {
            if (e.getFrom() == n || e.getTo() == n) removeTransition(e);
        }
        states.remove(n);
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

    public void setActive(ArrayList<Element> active) {
        this.active = active;
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

    public HashMap<Character, String> getShortSymbols() {
        return shortSymbols;
    }
    
    public boolean accept(String input) {
        ArrayList<Node> state = new ArrayList<Node>();
        for (Node n: getStates()) {
            if (n.isInitial()) state.add(n);
        }
        //System.out.print("Ableitung: "+state);
        for (int i = 0; i < input.length(); i++) { //Eingabe durchlaufen
            for (Node n: (ArrayList<Node>) state.clone()) { //von jedem zustand suchen
                state.remove(n);
                for (Edge e: n.getEdges()) { //jede kante betrachten
                    for (String s: e.getTransitions()) { //jeden übergang der kante
                        if (s.equals(""+input.charAt(i))) {
                            state.add(e.getTo());
                            break;
                        }
                    }
                    
                }
            }
            if (state.isEmpty()) return false;
            //remove duplicates - i need them during work process
            HashSet hs = new HashSet();
            hs.addAll(state);
            state.clear();
            state.addAll(hs);
        }
        for (Node n: state) {
            if (n.isFinal()) return true;
        }
        return false;
    }
}
