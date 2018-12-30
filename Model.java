/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author RoshanMayCry
 */
public class Model {
    private final Data data;
    private final Analysis analysis;
    public Model(Data data) {
        this.analysis = new Analysis(data);
        this.data = data;
    }
    
    public void run(int steps){
        analysis.goBySteps(steps);
    }
    
    /*Not really useful, it copies the result of 
    reachability(Vector<Process> state,Vector<Process> sequence, int steps)*/
    public int reachIn(int steps){
        int result=analysis.reachability(data.getInitialState(),data.getSequence(), steps);
        if (result==0){
            System.out.println("Yes");
            return result;
        }
        else{
            System.out.println("No");
            return result;
        }
        
    }
    /*Complete the network with at most 1 action, return true if the network is modified*/
    public boolean completeRandomly(){
        boolean modified=true;
        if (!data.getUnreachableProcess1().isEmpty()){
        for (Process unreachableProcess1 : data.getUnreachableProcess1()){
            Vector<Action> toBeAdded = analysis.addAction1(unreachableProcess1);
            int s=0;
            if ((s=toBeAdded.size())==1){
                data.getActions().add(toBeAdded.get(0));
                System.out.print("Action added :");
                toBeAdded.get(0).printAction();
            }
            else if ((toBeAdded.isEmpty())){
                System.out.print("No actions added for ");
                unreachableProcess1.printProcess();
                System.out.println();
                modified=false;
            }
            else{
                Random rand =new Random();
                int i=rand.nextInt(toBeAdded.size());
                data.getActions().add(toBeAdded.get(i));
                System.out.print("Action added :");
                toBeAdded.get(i).printAction();
                //System.out.println(i+" "+toBeAdded.size());
            }
            //data.printActions();
        }
        if (modified){
            data.getUnreachableProcess1().remove(0);
            return true;
        }
        else 
            return false;
        }
        else {
            for (ProcessWithActions unreachableProcess2 : data.getUnreachableProcess2()){
            Vector<Action> toBeAdded = analysis.addAction2(unreachableProcess2);
            int s=0;
            if ((s=toBeAdded.size())==1){
                data.getActions().add(toBeAdded.get(0));
                System.out.print("Action added :");
                toBeAdded.get(0).printAction();
            }
            else if ((toBeAdded.isEmpty())){
                System.out.print("No actions added for ");
                unreachableProcess2.getProcess().printProcess();
                System.out.println();
                modified=false;
            }
            else{
                Random rand =new Random();
                int i=rand.nextInt(toBeAdded.size());
                data.getActions().add(toBeAdded.get(i));
                System.out.print("Action added :");
                toBeAdded.get(i).printAction();
                //System.out.println(i+" "+toBeAdded.size());
            }
            //data.printActions();
        }
        if (modified){
            data.getUnreachableProcess2().remove(0);
            return true;
        }
        else 
            return false;
        }
    }
    /*Set sorts appearing in the sequence as observable(others as unobservable by default)*/
    public void setObservable(){
        for (Process i : data.getSequence()){
            i.getSort().setObservable(true);
        }
    }
    /*To avoid endless loop*/
    private int iterations=0;
    public boolean tryCompletion(int steps){
        setObservable();
        if (iterations>100)
            return false;
        switch (reachIn(steps)){
            case 1:{
                if (!completeRandomly())
                    return false;
                iterations++;
                return tryCompletion(steps);
            }
            case 2: 
                return false;
        } 
        data.printActions();
        System.out.println("Iterations "+iterations);
        return true;
    }
    public void phReach(Process p){
        analysis.classify(p);
        completeRandomly();
        String path="C:\\Users\\RoshanMayCry\\Desktop\\source\\result";
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try{
           FileWriter fw = new FileWriter(file);
           BufferedWriter buf = new BufferedWriter(fw);
           for (Sort i : data.getSorts())
               i.filePrintSort(buf);
           buf.append("\n");
           for (Action j : data.getActions())
               j.filePrintAction(buf);
           buf.close();
    }catch(IOException e){
      System.out.print("Exception");
    }
        
    }
    public void compPhReach() throws CloneNotSupportedException{
        UnderApproximation str2 = new UnderApproximation(data);
        if (str2.Reachability(str2.getSol(),new Vector<Process>()))
            System.out.println("Yes");
        else{
            OverApproximation str = new OverApproximation(data);
            if (str.Reachability(str.getSol(),new Vector<Process>()))
                System.out.println("Inconclusive");
            else{
                System.out.println("No");
                phReach(data.getSequence().get(0));
            }
        }
    }
}
