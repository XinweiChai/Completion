/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;

import java.util.Vector;

/**
 *
 * @author RoshanMayCry
 */


/*Used in line 24 in Data*/
public class ProcessWithActions{
    Process process;
    Vector<Action> actions;

    public ProcessWithActions(Process process, Vector<Action> actions) {
        this.process = process;
        this.actions = actions;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Vector<Action> getActions() {
        return actions;
    }

    public void setActions(Vector<Action> actions) {
        this.actions = actions;
    }
    
}
