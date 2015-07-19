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
public class OverApproximation{
    private Data data;
    private Process proc;
    private Vector<Solution> sol;
    public OverApproximation(Data data) throws CloneNotSupportedException {
        this.data = data;
        this.proc = data.getSequence().get(0);
        sol = new Vector<>();
        findSol(proc,new Solution(), new Vector<Process>());
    }
    public OverApproximation(Data data, Process proc) throws CloneNotSupportedException {
        this.data = data;
        this.proc = proc;
        sol = new Vector<>();
        findSol(proc,new Solution(), new Vector<Process>());
    }
    public Vector<Action> findAction(Process proc){
        Vector<Action> ac=new Vector<>();
        for (Action a : data.getActions()){
            if (a.getBounce().equals(proc))
                ac.add(a);
        }
        return ac;
    }
    public boolean findProcess(Process process){
        for (Process i : data.getInitialState()){
            if (i.equals(process)){
                return true;
            }
        }
        return false;
    }
    
    public void findSol(Process proc, Solution so, Vector<Process> loopMark) throws CloneNotSupportedException{
        Vector<Action> toBeTreated = findAction(proc);
        Process node = proc;
        loopMark.add(node);
        Solution copySo = (Solution) so.clone();
        for (Action a : toBeTreated){
            if (!loopMark.contains(a.getTarget())){
                node = a.getTarget();
                if (!findProcess(node)){
                    Solution s = new Solution(copySo,a.getHitter());
                    //sol.add(s);
                    findSol(node,s,loopMark);
                }
                else {
                    so.getProc().add(a.getHitter());
                    Solution copySo1 = (Solution) so.clone();
                    sol.add(copySo1);
                    so.getProc().remove(a.getHitter());
                }
            }
            if (toBeTreated.indexOf(a)==toBeTreated.size())
                so.getProc().remove(so.getProc().lastElement());
        }
    }
    public boolean ReachSol(Solution s,Vector<Process> antiLoop) throws CloneNotSupportedException{
        for (Process p : s.getProc()){
            if (antiLoop.contains(p))
                return false;
            antiLoop.add(p);
            if (!findProcess(p)){
                if (p.hasAction(data.getActions()).isEmpty()){
                    antiLoop.remove(p);
                    return false;
                }
                OverApproximation str =  new OverApproximation(data, p);
                if (!Reachability(str.sol, antiLoop)){
                    antiLoop.remove(p);
                    return false;
                }
            }
            antiLoop.remove(p);
        }
        return true;
    }
    public boolean Reachability(Vector<Solution> sol, Vector<Process> antiLoop) throws CloneNotSupportedException{
        if (!antiLoop.contains(proc))
            antiLoop.add(proc);
        for (Solution s : sol){
            if (ReachSol(s, antiLoop)){
                antiLoop.remove(proc);
                return true;
            }
        }
        antiLoop.remove(proc);
        return false;
    }

    public Vector<Solution> getSol() {
        return sol;
    }
    
}
