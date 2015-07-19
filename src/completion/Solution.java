/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;

import java.util.Vector;

/**
 *
 * @author chai
 */
public class Solution implements Cloneable{
    private Vector<Process> proc;

    public Solution() {
        this.proc = new Vector<Process>();
    }

    public Solution(Solution s, Process p) {
        this.proc = s.getProc();
        this.proc.add(p);
    }

    public Vector<Process> getProc() {
        return proc;
    }

    public void setProc(Vector<Process> proc) {
        this.proc = proc;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {  
        Solution newSol = (Solution) super.clone();
        newSol.proc = (Vector) proc.clone();
        return newSol;  
    }  
}
