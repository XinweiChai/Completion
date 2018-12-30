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
public class Process {
    private Sort sort;
    private int level;

    public Process(Sort sort, int level) {
        this.sort = sort;
        this.level = level;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
     public boolean sameSort(Process process){
        return this.sort.getSortName().equals(process.getSort().getSortName());
    }
     /*Test if 2 processes are the same*/
    public boolean equals(Process process){
        return (sameSort(process))&&(this.level==process.level);
    }
    /*Print a process on screen, in form a i*/
    public void printProcess(){
        System.out.print(sort.getSortName()+" ");
        System.out.print(level+" ");
    }
   
    /*returns the set of actions with certain bounce*/
    public Vector<Action> hasAction(Vector<Action> actions){
        Vector<Action> relatedActions = new Vector<>();
        for (Action i : actions){
            if (i.getBounce().equals(this))
                relatedActions.add(i);
        }
        return relatedActions;
    }
}
