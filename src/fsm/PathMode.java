/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsm;

/**
 *
 * @author Konnarr
 */
public enum PathMode {
    LINE, QUADRATIC_BEZIER, CUBIC_BEZIER;
    public PathMode getNext() {
        if (this.equals(CUBIC_BEZIER)) return LINE;
        else if (this.equals(LINE)) return QUADRATIC_BEZIER;
        else return CUBIC_BEZIER;
    }
}
