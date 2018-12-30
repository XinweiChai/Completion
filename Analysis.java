/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;

import java.util.Random;
import java.util.Vector;


/**
 *
 * @author RoshanMayCry
 */
public class Analysis {
    private Vector<Process> state;
    private Data data;

    public Analysis(Data data) {
        this.state = data.getInitialState();
        this.data = data;
    }

    public Vector<Process> getState() {
        return state;
    }

    public void setState(Vector<Process> state) {
        this.state = state;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    /*Return true if certain process is in the state*/
    public boolean findProcess(Process process){
        for (Process i : state){
            if (i.equals(process)){
                return true;
            }
        }
        return false;
    }
    
    /*For recursive use, as the state changes in different ways*/
    public boolean findProcess(Vector<Process> state, Process process){
        for (Process i : state){
            if (i.equals(process)){
                return true;
            }
        }
        return false;
    }
    /*Return a possible future process for the next step,
    return null if no action can be taken, used in void goBySteps(int steps)*/
    public Process transitRandomly(){
        Vector<Action> availableActions = new Vector<>();
        for (Action a : data.getActions()){
            if ((findProcess(a.getHitter()))&&(findProcess(a.getTarget())))
                availableActions.add(a);
        }
        if (availableActions.isEmpty()){
            return null;
        }
        else if (availableActions.size()==1){
            return availableActions.get(0).getBounce();
        }
        else{
            Random rand =new Random();
            return availableActions.get(rand.nextInt(availableActions.size())).getBounce();
        }
    }
    /*Return all the possible future processes for the next step,
    used in int reachability(Vector<Process> state,Vector<Process> sequence, int steps)*/
    public Vector<Process> transit(){
        Vector<Action> availableActions = new Vector<>();
        Vector<Process> futureProcess = new Vector<>();
        for (Action a : data.getActions()){
            if ((findProcess(a.getHitter()))&&(findProcess(a.getTarget())))
                availableActions.add(a);
        }
        if (availableActions.isEmpty()){
            return null;
        }
        else if (availableActions.size()==1){
            futureProcess.add(availableActions.get(0).getBounce());
            return futureProcess;
        }
        else{
            for (Action i : availableActions)
                futureProcess.add(i.getBounce());
                return futureProcess;
        }
    }
    /*Match with Process transitRandomly()*/
    public void changeState(Process process){
        for (Process i : state){
            if (i.sameSort(process)){
                state.add(process);
                state.remove(i);
                break;
            }
        }
    }
    /*Match with Vector<Process> transit()*/
    public Vector<Process> changeState(Vector<Process> state,Process process){
        for (Process i : state){
            if (i.sameSort(process)){
                state.add(process);
                state.remove(i);
                break;
            }
        }
        return state;
    }
    /*Simulate the transition of Process Hitting in certain steps*/
    public void goBySteps(int steps){
        System.out.print("Initial state: ");
        printState();
        Process p;
        for (int i=0;i<steps;i++){
            if ((p=transitRandomly())==null){
                System.out.println("Stable state!");
                break;
            }
            changeState(p);
            printState();
        }
    }
    
    public void printState(){
        for (Process i : state){
            i.printProcess();
        }
        System.out.println();
    }
    
    /*Classify the unreachable processes, see line 21 and line 24 in Data.java*/
    public void classify(Process restToReach){
        Vector<Action> relatedActions = restToReach.hasAction(data.getActions());
        //if ((restToReach.getSort().isObservable())||(relatedActions.isEmpty()))
        if (relatedActions.isEmpty())
            data.getUnreachableProcess1().add(restToReach);
        else{
            data.getUnreachableProcess2().add(new ProcessWithActions(restToReach,relatedActions));
        }
    }
    /*For the case that reachability returns 1 */
    public int case1(Vector<Process> restToReach){
        Process q = restToReach.get(0);
        classify(q);
        q.printProcess();
        System.out.println("not reachable");
        return 1;
    }
   
    /*Returns 0 if reachable, returns 1 if certain process in the given sequence is not reachable,
    returns 2 if there are occurence order problems in the given sequence*/
    public int reachability(Vector<Process> state,Vector<Process> sequence, int steps){
        Vector<Process> restToReach=sequence;
        if (restToReach.isEmpty())
            return 0;
        else{
            if (findProcess(state,restToReach.get(0))){
                restToReach.remove(0);
                return reachability(state,restToReach,steps);
            }
            else{
                for (int i=1; i<restToReach.size();i++){
                    if (findProcess(state,restToReach.get(i))){
                        if (!restToReach.get(i).equals(restToReach.get(0))){
                            System.out.println("Wrong order");
                            classify(restToReach.get(0));
                            return 2;
                        }
                    }
                }
                Vector<Process> p=transit();
                if ((p==null)||(steps==0)){
                    return case1(restToReach);
                }
                else{ 
                    if (p.size()==1){
                        if (p.get(0).getSort().isObservable()&&(!p.get(0).equals(sequence.get(0)))){
                            for (int i=1; i<restToReach.size();i++){
                                if (p.get(0).equals(restToReach.get(i))){
                                    if (!restToReach.get(i).equals(restToReach.get(0))){
                                    System.out.println("Wrong order");
                                    classify(restToReach.get(0));
                                    return 2;
                                    }
                                }
                            }
                            p.get(0).printProcess();
                            System.out.println(" isn't in the sequence");
                            classify(restToReach.get(0));
                            return 2;
                        }
                        else return reachability(changeState(state,p.get(0)),restToReach,steps-1);
                    }
                    else{
                        /*Mark true if there is a reachable branch*/
                        boolean mark=false;
                        for (int i=0;i<p.size();i++){
                            if ((reachability(changeState(state,p.get(i)),restToReach,steps-1))==0)
                                return 0;
                            else
                                if ((reachability(changeState(state,p.get(i)),restToReach,steps-1))==1)
                                    mark=true;
                        }
                        if (mark){
                            return case1(restToReach);
                        }
                        else{
                            for(Process i : p)
                                i.printProcess();
                            System.out.println(" isn't in the sequence");
                            classify(restToReach.get(0));
                            return 2;
                        }    
                    } 
                }
            }
        }
    }
    /*Possible actions to be added for unreachableProcess1 according to relations*/
    public Vector<Action> addAction1(Process unreachableProcess){
        Vector<Action> addActions = new Vector<>();
        Process addProcess = null;
        for (int i=0;i<=unreachableProcess.getSort().getProcessMaxLevel();i++){
            for (Relation j : data.getRelations()){
                if (j.getTarget().getSortName().equals(unreachableProcess.getSort().getSortName())){
                    for (int k=0;k<=j.getHitter().getProcessMaxLevel();k++){
                        if ((i<unreachableProcess.getLevel())&&(j.getRegulation())&&(k!=0)){
                            addProcess = new Process(unreachableProcess.getSort(),i);
                            addActions.add(new Action(new Process(j.getHitter(),k),addProcess,unreachableProcess));
                        }
                        else if ((i<unreachableProcess.getLevel())&&(!j.getRegulation())&&(k!=j.getTarget().getProcessMaxLevel())){
                            addProcess = new Process(unreachableProcess.getSort(),i);
                            addActions.add(new Action(new Process(j.getHitter(),k),addProcess,unreachableProcess));
                        }
                        else if ((i>unreachableProcess.getLevel())&&(j.getRegulation())&&(k!=j.getTarget().getProcessMaxLevel())){
                            addProcess = new Process(unreachableProcess.getSort(),i);
                            addActions.add(new Action(new Process(j.getHitter(),k),addProcess,unreachableProcess));
                        }
                        else if ((i>unreachableProcess.getLevel())&&(!j.getRegulation())&&(k!=0)){
                            addProcess = new Process(unreachableProcess.getSort(),i);
                            addActions.add(new Action(new Process(j.getHitter(),k),addProcess,unreachableProcess));
                        }
                    }
                }
            }
        }
        for (Action i : data.getActions())
            for (int j=0; j<addActions.size();j++)
                if (i.getHitter().equals(addActions.get(j).getHitter())&&(i.getTarget().equals(addActions.get(j).getTarget())))
                    addActions.remove(j);
        return addActions;        
    }

    /*Possible actions to be added for unreachableProcess2.
    counter for iterations prevents the endless loop of finding hitter,
    e.g.there is an action a i->b j k, and b k not reachable, of course a i is not reachable,
    so we want to find solutions for the completion of a i, but there may exist another action with bounce a i,etc
    */
    int iterations=0;
    public Vector<Action> addAction2(ProcessWithActions unreachableProcess){
        if (unreachableProcess.getActions().isEmpty()){
            return addAction1(unreachableProcess.getProcess());
        }
        Action firstAction = unreachableProcess.getActions().get(0);
        unreachableProcess.getActions().remove(0);
        boolean Prec=false;
        if (iterations>100)
            return addAction1(unreachableProcess.getProcess());
        iterations++;
        for (int i=0;i<data.getSequence().size();i++){
            if (data.getSequence().get(i).equals(firstAction.getBounce())){
                if (Prec){
                    if (firstAction.getHitter().hasAction(data.getActions())!=null){
                        return addAction2(new ProcessWithActions(firstAction.getHitter(),firstAction.getHitter().hasAction(data.getActions())));
                    }
                    else{
                        return addAction1(firstAction.getHitter());
                    }
                }
                else
                    return addAction2(unreachableProcess);
                //return addAction1(unreachableProcess.getProcess());
            }
            else if (data.getSequence().get(i).equals(firstAction.getTarget())){
                Prec=true;
                
            }
            else if(data.getSequence().get(i).getSort().equals(unreachableProcess.getProcess().getSort())&&
                    (!data.getSequence().get(i).equals(firstAction.getTarget()))&&
                    (!data.getSequence().get(i).equals(firstAction.getBounce()))){
                Prec=false;
            }
                    
                    
                
        }
        return addAction2(unreachableProcess);
        //return addAction1(unreachableProcess.getProcess());
    }
    
}
