/*
 * This class represents a finite state machine in a way, that is well suitable 
 * for illustration. It is not designed for pratical use with good time and space
 * performance.
 */
package fsm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Konnarr
 */
public class Fsm extends Graph {
    private static final long serialVersionUID = 2345541351034924476L;
    
    public enum EpsEliminationType {
        FROM_LEFT, FROM_RIGHT, FROM_BOTH;
        public static EpsEliminationType getDefault() {
            return FROM_LEFT;
        }
    }
    
    //special Symbols
    private char anySymbol = '?'; //we can use that transition with any read Symbol
    private char elseSymbol = '!'; //we can use that transition when there is no other
    private char epsSymbol = '#'; //spontantious transition
    private HashMap<Character, String> shortSymbols = new HashMap<Character,String>(); //a list of short cuts (e.g. x (char) stands for 0 or 1 (String: 01)); should work in Blocks, too.
    
    //for universial levenshtein automatas, we have to be able to read input in blocks. 0 should be automatic
    //there seems to be no need to specify it.
    //private int blocksize = 0;
    
    //Simulation etc.
    private ArrayList<Element> activeEps = new ArrayList<Element>();

    public Fsm() {
        super();
    }
    
    @Override
    public Vertex addVertex(Point position) {
        Vertex n = new Vertex(this, position, "q_{"+getVertices().size()+"}");
        getVertices().add(n);
        return n;
    }
    
    @Override
    public EdgeFsm addEdge(Vertex from, Vertex to) {
        EdgeFsm e = new EdgeFsm(this, from, to);
        getEdges().add(e);
        from.getEdges().add(e);
        return e;
    }

    @Override
    public void setDefDirected(boolean defDirected) {
        //super.setDefDirected(defDirected);
        if (defDirected = false) throw new IllegalArgumentException("Fsm are always directed!");
    }
    
    
    /////////////////////getter and setter//////////////////////////////////////
    
    public ArrayList<Element> getActiveEps() {
        return activeEps;
    }

    public char getAnySymbol() {
        return anySymbol;
    }

    public void setAnySymbol(char anySymbol) {
        this.anySymbol = anySymbol;
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
       
    /////////////////////////Simulation/////////////////////////////////////////
    
    /* *
     * initialisizes the simulation process.
     * /
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
    
    /* *
     * computes the next simulation step based on the active states and the remaining input
     * @param input the remaining input
     * @return true if the at least one active state is final independent from the remaining input.
     * /
    public boolean nextStep(String input) {
        return nextStep(input, true);
    }
    
    /**
     * checks if s1 is a valid transition for s2 with respect to blocks and shortSymbols
     * @param s1 the pattern
     * @param s2 the input
     * @return true if the transition s1 is valid for input s2
     * /
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
                ArrayList<Edge> epot = new ArrayList<Edge>();
                for (Edge e: ((Vertex)n).getEdges()) { //jede kante betrachten
                    for (String s: ((EdgeFsm) e).getTransitions()) { //jeden übergang der kante
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
        HashSet<Element> hs = new HashSet<Element>();
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
    
    /**
     * Checks, wether a String input is accepted by the automaton
     * @param input the string to be tested.
     * @return true, if the calculation ends in a final state.
     * /
    public boolean accept(String input) {
        startSim();
        for (int i = 0; i < input.length()-blocksize; i += blocksize) {
            if (!nextStep(input.substring(i),false)) return false;
        }
        //TODO wie sieth der input des letzten schrittes aus?
        boolean b = nextStep(input.substring((input.length() / blocksize)-1),true);
        getActive().clear();
        return b;
    }*/
    
    private ArrayList<ArrayList<Configuration>> simulation = new ArrayList<ArrayList<Configuration>>();
    
    public  ArrayList<ArrayList<Configuration>> getSimulation() {
        return simulation;
    }
    
    public void startSimulation(String input) {
        simulation.clear();
        getActive().clear();
        for (Vertex v: getVertices()) {
            if (v.isInitial()) {
                ArrayList<Configuration> al = new ArrayList<Configuration>();
                al.add(new Configuration(this, v, input));
                simulation.add(al);
                getActive().add(v);
            }
        }
    }
    
    /**
     * Calculates the next step of the simulation
     * @return true, if the simulation is ready (that means we didn't find a new configuration)
     */
    public boolean nextStep() {
        boolean ready = true;
        getActive().clear();          
        for (ArrayList<Configuration> sim: new ArrayList<ArrayList<Configuration>>(simulation)) {
            Configuration c = sim.get(sim.size()-1);
            ArrayList<Configuration> next = c.nextConfigurations();
            if (next.isEmpty()) continue;      
            simulation.remove(sim);
            newconf: for (Configuration d: next) {
                //we have to ensure, that the configuration doesnt close a spontanious ring
                int i = sim.size()-1;
                while (i>=0 && d.input.equals(sim.get(i).input)) {
                    if (d.q == sim.get(i).q) {
                        continue newconf;
                    }
                    i--;
                }
                ArrayList<Configuration> newSim = new ArrayList<Configuration>(sim);
                newSim.add(d);
                simulation.add(newSim);
                getActive().add(d.q);
                getActive().add(d.edge);
                ready = false;
            }
        }
        for (ArrayList<Configuration> sim: simulation) {
            Configuration c = sim.get(sim.size()-1);
            if (c.input.length() == 0)
                getActive().add(c.q);
        }                
        return ready;
    }
    
    public void previousStep() {
        for (ArrayList<Configuration> sim: simulation) {
            if (sim.size() > 1) {
                sim.remove(sim.size()-1);
            }
        }
        //remove duplicate entries.
        simulation = new ArrayList<ArrayList<Configuration>>(new HashSet<ArrayList<Configuration>>(simulation));
    }

    public boolean accept() {
        for (ArrayList<Configuration> sim: simulation) {
            Configuration c = sim.get(sim.size()-1);
            if (c.input.length() == 0 && c.q.isFinal()) return true;
        }
        return false;
    }
    
    public void stopSimulation() {
        getActive().clear();
        simulation.clear();
    }
    //////////////////////Algorithms////////////////////////////////////////////
    
    private void addSpontaniousNodes(Vertex n) {
        for (Edge e: n.getEdges()) {
            //for (String s: ((EdgeFsm) e).getTransitions()) {
                if (((EdgeFsm) e).getTransitions().contains(""+epsSymbol)) {
                    activeEps.add(e);
                    if (!activeEps.contains(e.getTo())) {
                        activeEps.add(e.getTo());
                        addSpontaniousNodes(e.getTo());
                    }
                }
//            }
        }
    }
   
    /**
     * Removes spontanious transitions by absorption.
     * Note: Removes unnecessary edges.
     * @param type 0: absorb spontanious transitions from left
     *             1: absorb spontanious transitions from right
     *             2: absorb spontanious transitions from both sides
     */
    public void epsElimination(EpsEliminationType type) {
        ArrayList<Edge> newEdges = new ArrayList<Edge>();
        for (Vertex n: getVertices()) {
            activeEps.clear();
            if (!type.equals(EpsEliminationType.FROM_RIGHT) || n.isInitial()) addSpontaniousNodes(n);
            if (!type.equals(EpsEliminationType.FROM_LEFT)) {
                for (Element el: activeEps) {
                    if (el instanceof Vertex) {
                        ((Vertex)el).setInitial(true);
                    }
                }
                if (type.equals(EpsEliminationType.FROM_RIGHT)) activeEps.clear();
                activeEps.add(n);
            }
            for (Element el: new ArrayList<Element>(activeEps)) {
                if (!(el instanceof Vertex)) continue;
                if (!type.equals(EpsEliminationType.FROM_RIGHT) && ((Vertex) el).isFinal()) n.setFinal(true);
                for (Edge e: new ArrayList<Edge>(((Vertex) el).getEdges())) {
                    activeEps.clear();
                    if (!type.equals(EpsEliminationType.FROM_LEFT)) addSpontaniousNodes(e.getTo());
                    if (!type.equals(EpsEliminationType.FROM_RIGHT)) activeEps.add(e.getTo());
                    for (Element el2: activeEps) {
                       if (!(el2 instanceof Vertex)) continue;
                       if (!newEdges.contains(e) && !(el2 == e.getTo() && el == n)) { //er soll keine neue kante benutzt haben und nicht keinen spontanen übergang benutzt haben
                            EdgeFsm newE = addEdge(n, (Vertex)el2);
                            newE.getTransitions().addAll(((EdgeFsm)e).getTransitions()); //kanten in der kopie hinzufügen
                            while (newE.getTransitions().remove(""+getEpsSymbol())) {}
                            newE.setText();
                            newEdges.add(newE);
                       } 
                    }
                }
                            
                
            }
        }
        activeEps.clear();
        for (Edge e: getEdges()) { //spontane übergänge entfernen
            while (((EdgeFsm) e).getTransitions().remove(""+getEpsSymbol())) {
                e.setText();//if (((EdgeFsm) e).getTransitions().isEmpty()) removeEdge(e);
            }

        }
        removeUnnecessaryEdges();
    }
    
    /**
     * Removes edges without transitions and merges multiple edges with the same vertices.
     */
    public void removeUnnecessaryEdges() {
        for (int i = getEdges().size() - 1; i>=0; i--) {
            if (((EdgeFsm)getEdges().get(i)).getTransitions().isEmpty()) {
                removeEdge(getEdges().get(i));
            } else {
                boolean found = false;
                for (int j = i-1; j>=0; j--) {
                    if (getEdges().get(i).getFrom()==getEdges().get(j).getFrom() && getEdges().get(i).getTo()==getEdges().get(j).getTo()) {
                        ((EdgeFsm)getEdges().get(j)).getTransitions().addAll(((EdgeFsm)getEdges().get(i)).getTransitions());
                        getEdges().get(j).setText();
                        found = true;
                    }
                }
                if (found) removeEdge(getEdges().get(i));
            }
        }
    }
    
    /**
     * Removes States that are not reachable from any initial state and therefore unnecessary.
     */
    public void removeUnreachableStates() {
        boolean[] visited = new boolean[getVertices().size()];
        boolean[] processed = new boolean[getVertices().size()];
        boolean changes = true;
        while (changes) {
            changes = false;
            for (int i = getVertices().size() -1; i>=0; i--) {
                if (getVertices().get(i).isInitial()) {
                    visited[i] = true;
                }
                if (visited[i] && !processed[i]) {
                    for (Edge e: getVertices().get(i).getEdges()) {
                        visited[getVertices().indexOf(e.getTo())] = true;
                    }
                    processed[i] = true;
                    changes = true;
                }
            }
        } 
        for (int i = getVertices().size() -1; i>=0; i--) {
            if (!visited[i]) {
                removeVertex(getVertices().get(i));
            }
        }
    }
    
    /**
     * Removes States, from which there is no way to a final state and therefore
     * doesn't contribute to the recognised language. This includes the Hotel
     * California State.
     * Note: This method doesn't remove unreachable states. Use the removeUnreachableStates
     * method for this purpose. For performance reasons this should be done prior to calling
     * Note: This method does not check edges for empty transitions. use removeUnnecessaryEdges
     * mehod prior to delete them.
     * this method.
     */
     public void removeUnproductiveStates() {
        boolean[] visited = new boolean[getVertices().size()];
        boolean[] processed = new boolean[getVertices().size()];
        boolean changes = true;
        while (changes) {
            changes = false;
            for (int i = getVertices().size() -1; i>=0; i--) {
                if (getVertices().get(i).isFinal()) {
                    visited[i] = true;
                }
                if (visited[i] && !processed[i]) {
                    for (Edge e: getEdges()) {
                        if (e.getTo() == getVertices().get(i)) {
                            visited[getVertices().indexOf(e.getFrom())] = true;
                        }
                        processed[i] = true;
                        changes = true;
                    }
                }
            }            
        }
        for (int i = getVertices().size() -1; i>=0; i--) {
            if (!visited[i]) {
                removeVertex(getVertices().get(i));
            }
        }
    }
    
    static public String getPowersetName(Collection<Vertex> s) {
        String result = "";
        for (Vertex n: s) {
            result += n.getName() + ", ";
        }
        if (result.length()>2) return result.substring(0, result.length()-2);
        else return result;
    }
    
    /**
     * Computes the powerset automaton.
     * That is a deterministic automaton equivalent to the non deterministic input.
     * It works with spontanius-, any- and else-transitions, short symbols and varying blocksize.
     * If input already is deterministic, the result will be the same automaton,
     * but probably differently arranged.
     * @return the computed DEA
     */
    public Fsm powersetAutomatonBak() {
        Fsm p = new Fsm();
        //TODO else, any, short und blöcke
        try {
            //zuordnung zustandsmenge auf zustand
            HashMap<HashSet<Vertex>,Vertex> states = new HashMap<HashSet<Vertex>,Vertex>();
            //zu behandelnde vertices
            LinkedBlockingQueue<HashSet<Vertex>> queue = new LinkedBlockingQueue<HashSet<Vertex>>();
            //startzustand suchen
            activeEps.clear();
            HashSet<Vertex> oldVertices = new HashSet<Vertex>();
            for (Vertex n: getVertices()) {
                if (n.isInitial()) {
                    oldVertices.add(n);
                    addSpontaniousNodes(n);
                }
            }
            //spontane dazu
            for (Element e: activeEps) {
                if (e instanceof Vertex) oldVertices.add((Vertex) e);
            }
            //neuen Knoten anlegen
            Vertex newVertex = p.addVertex(new Point(100,100));
            newVertex.setInitial(true);
            newVertex.setName(getPowersetName(oldVertices));
            states.put(oldVertices, newVertex);
            queue.put(oldVertices);
            while (!queue.isEmpty()) {
                oldVertices = queue.remove();
                newVertex = states.get(oldVertices);
                //zuordnung übergangslabel, zustände
                HashMap<String,HashSet<Vertex>> newTrans = new HashMap<String,HashSet<Vertex>>();
                for (Vertex oldVertex: oldVertices) {
                    for (Edge e: oldVertex.getEdges()) {
                        for (String oldTrans: ((EdgeFsm)e).getTransitions()) {
                            if (oldTrans.equals(""+getEpsSymbol())) continue;
                            if (oldTrans.equals(""+getAnySymbol())) {
                                
                            } else if (oldTrans.equals(""+getElseSymbol())) {
                                
                            } else if (oldTrans.length()==1 && getShortSymbols().containsKey(oldTrans.charAt(0))) {
                                
                            } else if (newTrans.containsKey(oldTrans)) {
                                newTrans.get(oldTrans).add(e.getTo());
                            } else {
                                HashSet<Vertex> tmp = new HashSet<Vertex>();
                                tmp.add(e.getTo());
                                newTrans.put(oldTrans, tmp);
                            }
                        }
                    }
                }
                for (String u: newTrans.keySet()) {
                    activeEps.clear();
                    for (Vertex n: newTrans.get(u)) {
                        addSpontaniousNodes(n);
                    }
                    for (Element e: activeEps) {
                        if (e instanceof Vertex) newTrans.get(u).add((Vertex) e);
                    }
                    if (states.containsKey(newTrans.get(u))) {
                        //zustand gibts schon --> kante anlegen
                        EdgeFsm e = p.addEdge(newVertex, states.get(newTrans.get(u)));
                        e.getTransitions().add(u);
                        e.setText();
                    } else {
                        int x = (int)newVertex.getShape().getX()+3*newVertex.getPreferredWidth();
                        int y = (int)newVertex.getShape().getY();
                        while (p.getVertexByPosition(new Point(x+(int)newVertex.getShape().getWidth()/2,y+(int)newVertex.getShape().getHeight()/2))!=null) y +=  3*(int)newVertex.getShape().getHeight();
                        Vertex v = p.addVertex(new Point(x,y));
                        for (Vertex n: newTrans.get(u)) {
                            if (n.isFinal()) {
                                v.setFinal(true);
                                break;
                            }
                        }
                        v.setName(getPowersetName(newTrans.get(u)));
                        states.put(newTrans.get(u), v);
                        queue.put(newTrans.get(u));
                        EdgeFsm e = p.addEdge(newVertex, v);
                        e.getTransitions().add(u);
                        e.setText();
                    }
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Fsm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    
    ArrayList<String> getRemainingTrans(String s1, String s2) {
        //if (!(parent instanceof Fsm)) return null;
        ArrayList<String> al = new ArrayList<String>();
        HashMap<Integer,HashSet<Character>> remain = new HashMap<Integer, HashSet<Character>>();
        for (int i = 0; i < s1.length(); i++) {
            if (s2.length() < i) {
                //s2 ist kürzer, zeichen(menge) bleibt komplett erhalten
                HashSet<Character> tmp = new HashSet<Character>();
                tmp.add(s1.charAt(i));
                remain.put(i, tmp);
            } else {
                if (s1.charAt(i) == s2.charAt(i)) {
                    //zeichen(menge) gleich, für diese(s) zeichen(menge) bleibt nichts übrig
                    continue;
                } else {
                    if (!getShortSymbols().containsKey(s1.charAt(i)) && !getShortSymbols().containsKey(s2.charAt(i))) {
                        //keine übereinstimmung, ganzer string bleibt übrig
                        al.add(s1);
                        return al;
                    }
                    HashSet<Character> tmp = new HashSet<Character>();
                    if (getShortSymbols().containsKey(s1.charAt(i))) {
                        String s = getShortSymbols().get(s1.charAt(i));
                        for (int j = 0; j < s.length(); j++) {
                            tmp.add(s.charAt(j));
                        }
                    } else {
                        tmp.add(s1.charAt(i));
                    }
                    if (getShortSymbols().containsKey(s2.charAt(i))) {
                        String s = getShortSymbols().get(s2.charAt(i));
                        for (int j = 0; j < s.length(); j++) {
                            tmp.remove(s.charAt(j));
                        }
                    } else {
                        tmp.remove(s2.charAt(i));
                    }
                    remain.put(i, tmp);
                }
            }
        }
        //strings bauen
        for (int i: remain.keySet()) {
            for (char c: remain.get(i)) {
                String s = "";
                for (int j = 0; j < s2.length(); j++) {
                    if (i == j) s += c;
                    else s += s1.charAt(j);                           
                }
                al.add(s);
            }
        }
        return al;
    }
    
    ArrayList<String> getIntersectionTrans(String s1, String s2) {
        //if (!(parent instanceof Fsm)) return null;
        ArrayList<String> result = new ArrayList<String>();
        letter: for (int i = 0; i < Math.min(s1.length(),s2.length()); i++) {
            if (s1.charAt(i)==s2.charAt(i)) {
                if (i == 0) result.add(""+s1.charAt(i));
                else for (String s: new ArrayList<String>(result)) {
                    result.remove(s);
                    result.add(s.concat(""+s1.charAt(i)));
                }
            } else {
                //passt nicht
                if (!getShortSymbols().containsKey(s1.charAt(i)) && !getShortSymbols().containsKey(s2.charAt(i))) return null;
                //beide shortsymbol --> gegenseitig testen
                boolean found = false;
                ArrayList<String> list = new ArrayList<String>(result);
                if (getShortSymbols().containsKey(s1.charAt(i)) && getShortSymbols().containsKey(s2.charAt(i))) {
                    for (int j = 0; j < getShortSymbols().get(s1.charAt(i)).length(); j++ ) {
                        for (int k = 0; k < getShortSymbols().get(s2.charAt(i)).length(); k++) {
                            if (getShortSymbols().get(s1.charAt(i)).charAt(j)==getShortSymbols().get(s2.charAt(i)).charAt(k)) {
                                if (i == 0) result.add(""+getShortSymbols().get(s1.charAt(i)).charAt(j));
                                else for (String s: list) {
                                    result.remove(s);
                                    result.add(s.concat(""+getShortSymbols().get(s1.charAt(i)).charAt(j)));
                                }
                                found = true;
                            }
                        }
                    }
                    if (!found) return null;
                //einer shortsymbol --> eine richtung testen
                } else {
                    if (getShortSymbols().containsKey(s1.charAt(i))) {
                        for (int j = 0; j < getShortSymbols().get(s1.charAt(i)).length(); j++) {
                            if (getShortSymbols().get(s1.charAt(i)).charAt(j)==s2.charAt(i)) {
                                if (i == 0) result.add(""+s2.charAt(i));
                                else for (String s: new ArrayList<String>(result)) {
                                    result.remove(s);
                                    result.add(s.concat(""+s2.charAt(i)));
                                }
                                continue letter;
                            }
                        }
                        return null;
                    } else {
                        for (int j = 0; j < getShortSymbols().get(s2.charAt(i)).length(); j++) {
                            if (getShortSymbols().get(s2.charAt(i)).charAt(j)==s1.charAt(i)) {
                                if (i == 0) result.add(""+s1.charAt(i));
                                else for (String s: new ArrayList<String>(result)) {
                                    result.remove(s);
                                    result.add(s.concat(""+s1.charAt(i)));
                                }
                                continue letter;
                            }
                        }                      
                        return null;
                    }
                }
            }
        }
        return result;
    }    
    
     
    public Fsm powersetAutomaton() {
        for (Vertex v: getVertices()) {
            v.calcReachable();
        }
        Fsm p = new Fsm();
        HashMap<HashSet<Vertex>,Vertex> metastatemap = new HashMap<HashSet<Vertex>, Vertex>();
        LinkedBlockingQueue<HashSet<Vertex>> queue = new LinkedBlockingQueue<HashSet<Vertex>>();
        HashSet<Vertex> oldVertices = getInitialStates();
        Vertex newVertex = p.addVertex(new Point(100,100));
        newVertex.setInitial(true);
        newVertex.setName(getPowersetName(oldVertices));
        metastatemap.put(oldVertices, newVertex);
        queue.add(oldVertices);
        while (!queue.isEmpty()) {
            oldVertices = queue.poll();
            newVertex = metastatemap.get(oldVertices);
            HashMap<String,HashSet<Vertex>> trans = new HashMap<String, HashSet<Vertex>>();
            for (Vertex v: oldVertices) { //transitions zusammenfassen
                if (v.reachable.keySet().contains(""+getAnySymbol()) && v.reachable.keySet().contains(""+getElseSymbol())) {
                    v.reachable.remove(""+getElseSymbol());
                }
                for (String t: v.reachable.keySet()) {
                    //if (t.equals(""+getElseSymbol()) && v.reachable.keySet().contains(""+getAnySymbol())) continue;
                    boolean found = false;
                    for (String s: new HashSet<String>(trans.keySet())) {
                        if (s.equals(t)) {
                            trans.get(t).addAll(v.reachable.get(s));
                            found = true;
                        }
                        else {
                            ArrayList<String> intersection = getIntersectionTrans(s, t);
                            if (intersection != null && !intersection.isEmpty()) {
                                HashSet<Vertex> tmp = trans.get(s);
                                trans.remove(s);
                                //rest 1
                                ArrayList<String> remain = getRemainingTrans(s, t);
                                for (String u: remain) {
                                    HashSet<Vertex> tmp2 = new HashSet<Vertex>(tmp);
                                    if (trans.containsKey(u)) {
                                        tmp2.addAll(trans.get(u));
                                    }
                                    trans.put(u, tmp2);
                                }
                                //intersection
                                tmp.addAll(v.reachable.get(t));
                                for (String u: intersection) {
                                    HashSet<Vertex> tmp2 = new HashSet<Vertex>(tmp);
                                    if (trans.containsKey(u)) {
                                        tmp2.addAll(trans.get(u));
                                    }
                                    trans.put(u, tmp2);
                                }
                                //rest 2
                                remain = getRemainingTrans(t, s);
                                for (String u: remain) {
                                    tmp = new HashSet<Vertex>(v.reachable.get(t));
                                    if (trans.containsKey(u)) {
                                        tmp.addAll(trans.get(u));
                                    }
                                    trans.put(u, tmp);
                                }
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        trans.put(t, new HashSet<Vertex>(v.reachable.get(t)));
                    }
                }
            }
            //any und else behandeln
            for (Vertex v: oldVertices) {
                boolean els = v.reachable.containsKey(""+getElseSymbol());
                boolean any = v.reachable.containsKey(""+getAnySymbol());
                if (any && els) System.out.println("Das darf nicht sein.");
                if (els || any) {
                    for (String t: trans.keySet()) {
                        //anyübergänge an jeden knoten geben
                        if (any) trans.get(t).addAll(v.reachable.get(""+getAnySymbol()));
                        //elseübergänge an alle übergänge der anderen knoten geben
                        if (els && !v.reachable.containsKey(t)) trans.get(t).addAll(v.reachable.get(""+getElseSymbol()));
                    }
                }
            }
            if (trans.containsKey(""+getAnySymbol())) {
                HashSet<Vertex> tmp = trans.remove(""+getAnySymbol());
                if (trans.containsKey(""+getElseSymbol())) {
                    trans.get(""+getElseSymbol()).addAll(tmp);
                } else {
                    trans.put(""+getElseSymbol(), tmp);
                }
            }
            //kanten anlegen und ggf. zustände bauen
            for (String t: trans.keySet()) {
                if (!metastatemap.containsKey(trans.get(t))) {
                    int x = (int)newVertex.getShape().getX()+3*newVertex.getPreferredWidth();
                    int y = (int)newVertex.getShape().getY();
                    while (p.getVertexByPosition(new Point(x+(int)newVertex.getShape().getWidth()/2,y+(int)newVertex.getShape().getHeight()/2))!=null) y +=  3*(int)newVertex.getShape().getHeight();
                    Vertex v = p.addVertex(new Point(x,y));
                    for (Vertex n: trans.get(t)) {
                        if (n.isFinal()) {
                            v.setFinal(true);
                            break;
                        }
                    }
                    v.setName(getPowersetName(trans.get(t)));
                    metastatemap.put(trans.get(t), v);
                    try {
                        queue.put(trans.get(t));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Fsm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                EdgeFsm e = p.addEdge(newVertex, metastatemap.get(trans.get(t)));
                e.getTransitions().add(t);
                e.setText();
            }
        }
        p.removeUnnecessaryEdges();
        return p;
    }
    
    public void transformToDualAutomaton() {
        for (Vertex n: getVertices()) {
            if (n.isInitial()) {
                if (!n.isFinal()) {
                    n.setInitial(false);
                    n.setFinal(true);
                }                
            } else if (n.isFinal()) {
                n.setInitial(true);
                n.setFinal(false);
            }
            n.getEdges().clear();
        }
        for (Edge e: getEdges()) {
            Vertex n = e.getFrom();
            e.setFrom(e.getTo());
            e.setTo(n);
            e.getFrom().getEdges().add(e);
            e.rebuildPath();
        }
    }
    
    /**
     * Minimizes a deterministic automaton. If input is non deterministic the result
     * might be reduced but not necessaryly minimal.
     */
    public void minimize() {
        removeUnreachableStates();
        removeUnproductiveStates();
        boolean[][] bis = new boolean[getVertices().size()][getVertices().size()];
        //todo algo
    }
    
    public String kleeneAlgorithm() {
        //TODO implement
        return "";
    }
    
    /**
     * Calculates the minimal alphabeth of the automaton, which means the aggregation
     * of all characters used for transitions.
     * @return a String in which each letter represents one character used as transition.
     */
    public HashSet<Character> calcAlphabet() {
        HashSet<Character> letters = new HashSet<Character>();
        for (Edge e: getEdges()) {
            for (String s: ((EdgeFsm)e).getTransitions()) {
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i)==getElseSymbol() || s.charAt(i)==getAnySymbol() || s.charAt(i)==getEpsSymbol()) continue;
                    if (getShortSymbols().containsKey(s.charAt(i))) {
                        for (int j = 0; j < getShortSymbols().get(s.charAt(i)).length(); i++) {
                            letters.add(getShortSymbols().get(s.charAt(i)).charAt(j));
                        }
                    } else {
                        letters.add(s.charAt(i));
                    }
                }
            }
        }
        return letters;
    }
    
    public String calcAlphabeth(boolean delimiter) {
        String result = "";
        for (Character c: calcAlphabet()) {
            result += c;
            if (delimiter) result += ", ";
        }
        if (delimiter && result.length()>2) result = result.substring(0, result.length()-2);
        return result;
    }
    
    /**
     * Checks wether the automaton is deterministic, which means, there is only
     * one starting state and there is at most one transition from one state with
     * the same symbol.
     * @return if the automaton is deterministic
     */
    public boolean isDeterministic() {
        ArrayList<Element> activeBak= new ArrayList<Element>(getActive());
        ArrayList<ArrayList<Configuration>> simulationBak = new ArrayList<ArrayList<Configuration>>(simulation);
        boolean starting = false;
        for (Vertex n: getVertices()) {
            if (n.isInitial()) {
                if (starting) return false;
                else starting = true;
            }
            /*activeEps.clear();
            activeEps.add(n);
            addSpontaniousNodes(n);
            HashMap<String,Vertex> trans = new HashMap<String,Vertex>();
            for (Element el: activeEps) {
                if (!(el instanceof Vertex)) continue;
                for (Edge e: ((Vertex)el).getEdges()) {
                    for (String s: ((EdgeFsm)e).getTransitions()) {
                        if (s.equals(""+getElseSymbol())) continue; //Else kann dterminismus nicht kaputt machen
                        else if (s.equals(""+getAnySymbol()) || trans.containsKey(""+getAnySymbol())) { //any darf nur mit (unnötigen) übergängen zum selben zustand stehen
                            for (String key: trans.keySet()) {
                                if (e.getTo() != trans.get(key)) return false;
                            }
                        } else if (s.length()>1) { //Blöcke dürfen keine echten präfixe haben -> jeden substring testen
                            //for (int i = 1; i < s.length(); i++) {
                                //TODO blöcke testen
                            //}
                        } else if (getShortSymbols().containsKey(s.charAt(0))) { //shortsymbols einzeln testen
                            for (int i = getShortSymbols().get(s.charAt(0)).length(); i >= 0; i--) {
                                if (trans.containsKey(""+getShortSymbols().get(s.charAt(0)).charAt(i)) && trans.get(""+getShortSymbols().get(s.charAt(0)).charAt(i)) != e.getTo()) return false;
                                trans.put(s, e.getTo());
                            }
                        } else if (trans.containsKey(s) && trans.get(s) != e.getTo()) return false; //übergang zu anderem zustand
                        trans.put(s, e.getTo());
                    }
                }
            }*/
            //TODO: shortsymbols und besondere symbole müssen übersetzt werden!
            for (Edge e: n.getEdges()) {
                for (String t: ((EdgeFsm)e).getTransitions()) {
                    //getActive().clear();
                    simulation.clear();
                    getActive().clear();
                    ArrayList<Configuration> al = new ArrayList<Configuration>();
                    al.add(new Configuration(this, n, t));
                    simulation.add(al);
                    getActive().add(n);
                    while (!nextStep()) {}
                    if (getActive().size()>1) {
                        getActive().clear();
                        getActive().addAll(activeBak);
                        simulation.clear();
                        simulation.addAll(simulationBak);
                        return false;
                    }
                }
            }
        }
        getActive().clear();
        getActive().addAll(activeBak);
        simulation.clear();
        simulation.addAll(simulationBak);
        return true;
    }
    
    /**
     * Chekcs wether the automaton is complete, that means there is always a transition to a next state.
     * Note: uses calcAlphabet to calculate the alphabet.
     * @return true iff there is a transition with every letter of the alphabet from every state.
     */
    public boolean isComplete() {
        HashSet<Character> alpha = calcAlphabet();
        v: for (Vertex n: getVertices()) {
            HashSet<Character> alpha2 = new HashSet<Character>(alpha);
            for (Edge e: n.getEdges()) {
                for (String s: ((EdgeFsm)e).getTransitions()) {
                    if (s.equals(""+getAnySymbol())||s.equals(""+getElseSymbol())) continue v;
                    if (s.equals(""+getEpsSymbol())) continue;
                    if (s.length() == 1 && getShortSymbols().containsKey(s.charAt(0))) {
                        for (int i = 0; i < getShortSymbols().get(s.charAt(0)).length(); i++) {
                            alpha2.remove(getShortSymbols().get(s.charAt(0)).charAt(i));
                        }
                    } else if (s.length()>1) {
                        //TODO Blöcke???
                    } else {
                        alpha2.remove(s.charAt(0));
                    }
                }
            }
            if (!alpha2.isEmpty()) return false;
        }
        return true;
    }
    
    /**
     * Checks wether the the automaton has epsilon transitions.
     * @return true iff there is at least one spontanious transition
     */
    public boolean isSpontanious() {
        for (Edge e: getEdges()) {
            for (String s: ((EdgeFsm)e).getTransitions()) {
                if (s.equals(""+getEpsSymbol())) return true;
            }   
        }
        return false;
    }

    public void addHCZ(boolean useElse) {
        //todo not else
        if (useElse) {
            Vertex hcz = addVertex(new Point(10,10));
            hcz.setName("HCZ");
            for (Vertex n: getVertices()) {
                EdgeFsm e = addEdge(n, hcz);
                e.getTransitions().add(""+getElseSymbol());
                e.setText();
            }
        }
    }
    
    public HashSet<Vertex> getInitialStates() {
        HashSet<Vertex> result = new HashSet<Vertex>();
        for (Vertex n: getVertices()) {
            if (n.isInitial()) result.add(n);
        }
        return result;
    }
    
    public HashSet<Vertex> getFinalStates() {
        HashSet<Vertex> result = new HashSet<Vertex>();
        for (Vertex n: getVertices()) {
            if (n.isFinal()) result.add(n);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = this.getEdges().size()-this.getVertices().size();
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
        final Fsm other = (Fsm) obj;
        return other == this;
    }
    
}
