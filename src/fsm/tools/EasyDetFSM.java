/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm.tools;

/**
 *
 * @author Konnarr
 */
public class EasyDetFSM {
    //HashMap?
    private int[][] trans;
    private boolean[] fin;
    private char[] alphabet;
    
    public EasyDetFSM(int stateCount, int letterCount) {
        trans = new int[stateCount][letterCount];
        fin = new boolean[stateCount];
        alphabet = new char[letterCount];
        
    }
    
    public void resize(int stateCount, int letterCount) {
//        boolean state = stateCount != fin.length;
//        boolean letter = letterCount != alphabet.length;
        
        int[][] newTrans = new int[stateCount][letterCount];
        boolean[] newFin = new boolean[stateCount];
        char[] newAlphabet = new char[letterCount];
        
        //System.arraycopy(trans, 0, newTrans, 0, Math.min(stateCount, fin.length));
        for (int i = 0; i < Math.min(stateCount, fin.length); i++) {
            System.arraycopy(trans[i], 0, newTrans[i], 0, Math.min(letterCount, alphabet.length));
        }
        System.arraycopy(fin, 0, newFin, 0, Math.min(stateCount, fin.length));
        System.arraycopy(alphabet, 0, newAlphabet, 0, Math.min(letterCount, alphabet.length));
        
        trans = newTrans;
        fin = newFin;
        alphabet = newAlphabet;
        
//        if (!state && !letter) return;
//        newTrans = new int[stateCount][letterCount];
//        if (state)  newFin = new boolean[stateCount];
//        if (letter) newAlphabet = new char[letterCount];
//        for (int i = 0; i < Math.min(stateCount, fin.length); i++) {
            
//        }
    }
    
    public void setTranistion(int state, int letter, int newState) {
        if (letter<0 || letter >= alphabet.length || state < 0 || state >= fin.length || newState < -1 || newState >= fin.length)
            throw new IndexOutOfBoundsException("undefined state or letter");
        trans[state][letter] = newState;
    }
    
    public void setFinal(int state, boolean value) {
        if (state<0 || state >= fin.length) throw new IndexOutOfBoundsException("no state with that number");
        fin[state] = value;
    }
    
    public void setLetter(int letter, char value) {
        //todo doppelte buchstaben
        if (letter<0 || letter >= alphabet.length) throw new IndexOutOfBoundsException("no letter with that index");
        alphabet[letter] = value; 
    }
    
    public void setAlphabet(String value) {
        if (value.length() != alphabet.length) throw new IndexOutOfBoundsException("wrong number of characters");
        for (int i = 0; i < value.length(); i++) {
            for (int j = 0; j < i; j++) if (value.charAt(i) == alphabet[j]) throw new RuntimeException("you can't use a letter twice");
            alphabet[i] = value.charAt(i);
        }
    }
    
    public boolean getFinal(int state) {
        if (state == -1) return false;
        if (state<0 || state >= fin.length) throw new IndexOutOfBoundsException("no state with that number");
        return fin[state];
    }
    
    public char getLetter(int letter) {
        if (letter<0 || letter >= alphabet.length) throw new IndexOutOfBoundsException("no letter with that index");
        return alphabet[letter];
    }
    
    public int getLetterCount() {
        return alphabet.length;
    }
    
    public int getStateCount() {
        return fin.length;
    }
    
    public int getTransition(int state, int letter) {
        if (state == -1) return -1;
        return trans[state][letter];
    }
    
    public void print() {
        System.out.print("Alphabet: ");
        for (int i = 0; i < getLetterCount(); i++) System.out.print(getLetter(i)+",");
        System.out.println();
        System.out.println("Zustände: "+getStateCount());
        System.out.println("Übergänge: ");
        for (int j = 0; j < getStateCount(); j++) {
            System.out.print("Zustand "+j+": ");
        for (int i = 0; i < getLetterCount(); i++) {
            System.out.print(getTransition(j,i)+" ");
        }
        System.out.println(" "+getFinal(j));
        }
    }
    
    public boolean accept(String input) {
        int state = 0;
        System.out.print("Ableitung: "+state);
        for (int i = 0; i < input.length(); i++) {
            int letter = -1;
            for (int j = 0; j < getLetterCount(); j++)
                if (input.charAt(i) ==  getLetter(j)) {
                    letter = j;
                    break;
                }
            if (letter == -1) return false;
            else state = getTransition(state, letter);
            System.out.print(" |-"+getLetter(letter)+"-> "+state);
        }
        System.out.println(getFinal(state));
        return getFinal(state);
    }
}
