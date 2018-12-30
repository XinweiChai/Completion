/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author RoshanMayCry
 */
public class Sort {
    private String sortName;
    private int processMaxLevel;
    private boolean observable;
    private Vector<Process> processes = new Vector<>();
    public Sort(String sortName, int processMaxLevel) {
        this.sortName = sortName;
        this.processMaxLevel = processMaxLevel;
        this.observable=false;
        generateProcess();
    }

    public Sort(String sortName, int processMaxLevel0, boolean observable) {
        this.sortName = sortName;
        this.processMaxLevel = processMaxLevel;
        this.observable = observable;
        generateProcess();
    }
    
    /*Possible processes for a sort*/
    public void generateProcess(){
             for (int j=0;j<=processMaxLevel;j++)
                 processes.add(new Process(this,j));
    }
    public String getSortName() {
        return sortName;
    }

    public int getProcessMaxLevel() {
        return processMaxLevel;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public void setProcessMaxLevel(int processMaxLevel) {
        this.processMaxLevel = processMaxLevel;
    }

    public boolean isObservable() {
        return observable;
    }

    public void setObservable(boolean observable) {
        this.observable = observable;
    }

    public Vector<Process> getProcesses() {
        return processes;
    }
    public boolean equals(String s){
        return(s.equals(sortName));
    }
     public void filePrintSort(BufferedWriter buf){
        try{
           buf.append("process "+sortName+" "+processMaxLevel+" ");
    }catch(IOException e){
      System.out.print("Exception");
    }
    }
}
