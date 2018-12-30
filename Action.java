/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author RoshanMayCry
 */
public class Action {
    private Process hitter;
    private Process target;
    private Process bounce;

    public Action(Process hitter, Process target, Process bounce) {
        this.hitter = hitter;
        this.target = target;
        this.bounce = bounce;
    }

    public Process getHitter() {
        return hitter;
    }

    public void setHitter(Process hitter) {
        this.hitter = hitter;
    }

    public Process getTarget() {
        return target;
    }

    public void setTarget(Process target) {
        this.target = target;
    }

    public Process getBounce() {
        return bounce;
    }

    public void setBounce(Process bounce) {
        this.bounce = bounce;
    }
    
    /*Print an action on screen, in form a i b j k*/
    public void printAction(){
        this.hitter.printProcess();
        this.target.printProcess();
        System.out.println(bounce.getLevel());
    }
    /*Test if 2 actions are the same*/
    public boolean equals(Action action){
        return (this.hitter.equals(action.getHitter())&&(this.target.equals(action.getTarget()))&&(this.bounce.equals(action.getBounce())));
    }
    public void filePrintAction(BufferedWriter buf){
        try{
           buf.append(this.hitter.getSort().getSortName()+" ");
           buf.append(this.hitter.getLevel()+" -> ");
           buf.append(this.target.getSort().getSortName()+" ");
           buf.append(this.target.getLevel()+" ");
           buf.append(this.bounce.getLevel()+"\n");
    }catch(IOException e){
      System.out.print("Exception");
    }
    }
}
