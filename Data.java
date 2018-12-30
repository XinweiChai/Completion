/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;
import java.io.*;
import java.util.Vector;

/**
 *
 * @author RoshanMayCry
 */
public final class Data {
    private Vector<Sort> sorts;
    private Vector<Action> actions;
    private Vector<Process> processes;
    private Vector<Process> initialState;
    private Vector<Process> sequence;
    private Vector<Relation> relations;
    /*unreachableProcess1 contains unreachable processes without related actions.
    (Actions whose bounce equals these processes), case 1 in the pdf*/
    private Vector<Process> unreachableProcess1;
    /*unreachableProcess1 contains unreachable processes with related actions
    case 2 in the pdf*/
    private Vector<ProcessWithActions> unreachableProcess2;
    private double defaultRate;
    
    public int readRate(Vector<String> word,int cursor){
        if ((word.get(cursor).equals("directive"))&&(word.get(cursor+1).equals("default_rate"))){
            defaultRate=Integer.parseInt(word.get(cursor+2));
            return cursor+3;
        }
        return cursor;
    }
    
    public int readProcess(Vector<String> word,int cursor){
        if (word.get(cursor).equals("process")){
            sorts.add(new Sort(word.get(cursor+1),Integer.parseInt(word.get(cursor+2))));
            return readProcess(word,cursor+3);
        }
        else return cursor;
    }

    public int readAction(Vector<String> word,int cursor){
        if (word.get(cursor+2).equals("->"))
            for (Sort i : sorts)
                for (Sort j : sorts)
                    if ((word.get(cursor).equals(i.getSortName()))&&((word.get(cursor+3).equals(j.getSortName())))){
                        Process hitter = i.getProcesses().get(Integer.parseInt(word.get(cursor+1)));
                        Process target = j.getProcesses().get(Integer.parseInt(word.get(cursor+4)));
                        Process bounce = j.getProcesses().get(Integer.parseInt(word.get(cursor+5)));
                        actions.add(new Action(hitter,target,bounce));
                        return readAction(word, cursor+6);
                    }
        return cursor;
    }
    public int readInitialState(Vector<String> word,int cursor){
        for (Sort i : sorts){
            if (word.get(cursor+1).endsWith(",")){
                if (i.getSortName().equals(word.get(cursor))){
                    initialState.add(new Process(i,Integer.parseInt(word.get(cursor+1).substring(0,word.get(cursor+1).length()-1))));
                    return readInitialState(word, cursor+2);
                }
            }
            else if (i.getSortName().equals(word.get(cursor))){
                initialState.add(new Process(i,Integer.parseInt(word.get(cursor+1))));
                if (cursor==word.size()-2){
                    return cursor+1;
                }
                return cursor+2;
            }
        }
        return cursor;
    }
    public int readSequence(Vector<String> word,int cursor){
        for (Sort i : sorts){
            if (i.getSortName().equals(word.get(cursor))){
                sequence.add(new Process(i,Integer.parseInt(word.get(cursor+1))));
                if (cursor==word.size()-2){
                    return cursor+1;
                }
                return readSequence(word, cursor+2);
            }
        }
        return cursor;
    }
    public int readRelations(Vector<String> word,int cursor){
        for (Sort i : sorts)
            for (Sort j : sorts){
                if ((i.getSortName().equals(word.get(cursor)))&&(j.getSortName().equals(word.get(cursor+1)))){
                    if (word.get(cursor+2).equals("-"))
                        relations.add(new Relation(i,j,false));
                    else if (word.get(cursor+2).equals("+"))
                        relations.add(new Relation(i,j,true));
                    else return cursor;
                    if (cursor==word.size()-3){
                        return cursor+2;
                    }
                return readRelations(word, cursor+3);
            }
        }
        return cursor;
    }
    public void construct(){
        sorts = new Vector<>();
        actions = new Vector<>();
        processes = new Vector<>();
        initialState = new Vector<>();
        sequence = new Vector<>();
        relations = new Vector<>();
        unreachableProcess1 = new Vector<>();
        unreachableProcess2 = new Vector<>();
    }
    /*Serves to read the whole file but there are some errors, and I didn't use it in the main program*/
    public String readFileContent(String fileName) throws IOException{
        File file = new File(fileName);
        BufferedReader bf = new BufferedReader(new FileReader(file));
  
        String content = "";
        StringBuilder sb = new StringBuilder();
        while(content != null){
            content = bf.readLine();
            if(content == null){
                break;
            }
            sb.append(content.trim());
        }
        bf.close();
        return sb.toString();
    }
    
    public Data(String filePath){
        construct();
        String line;
        String[] words = null;
        Vector<String> allWords = new Vector<>();
        try{
           //String s = readFileContent(filePath);
           /*I tried to read the words in the whole file then put them into a String but there are some emtpy String
            like "" in the words, which caused errors in the filereading.
            If possible could you tell me how to fix this problem?*/
           BufferedReader buf = new BufferedReader(new FileReader(filePath));
           while ((line=buf.readLine())!=null){
                words=line.split(" |\n|\r");
                for (int i=0;i<words.length;i++)
                    allWords.add(words[i]);
           }
           /*This pointer indicates the position of reading*/
            int i=0;
            i=readRate(allWords,i);
            i=readProcess(allWords,i);
            i=readAction(allWords,i);
            if (allWords.get(i).equals("initial_state"))
                i=readInitialState(allWords,i+1);        
            if (allWords.get(i).equals("sequence"))
                i=readSequence(allWords,i+1);   
            if (allWords.get(i).equals("relation"))
                i=readRelations(allWords,i+1);
            /*If i is not at EOF, there must be some errors in filereading.*/
            if (i!=allWords.size()-1)
                throw new IOException();
        }
        catch(IOException e){
            System.out.print("Input file error");
        }	
        
        
    }
    /*Data for testing*/
    public Data() {
        construct();
        Sort sort1 = new Sort("a",1);
        Sort sort2 = new Sort("b",1);
        
        this.sorts.add(sort1);
        this.sorts.add(sort2);
        Process process1 = new Process(sort1,0);
        Process process2 = new Process(sort1,1);
        Process process3 = new Process(sort2,0);
        Process process4 = new Process(sort2,1);
        Action action1 = new Action(process1,process4,process3);
        Action action2 = new Action(process2,process3,process4);
        Action action3 = new Action(process3,process1,process2);
        Action action4 = new Action(process4,process2,process1);
        
        this.actions.add(action1);
        /*Actions removed, we want to see if the program can find these actions.*/
        //this.actions.add(action2);
        //this.actions.add(action3);
        this.actions.add(action4);
        
        this.initialState.add(process1);
        this.initialState.add(process3);
        
        this.sequence.add(process4);
        this.sequence.add(process2);
        
        Relation relation1 = new Relation(sort1,sort2,true);
        Relation relation2 = new Relation(sort2,sort1,false);
        this.relations.add(relation1);
        this.relations.add(relation2);
        this.unreachableProcess1= new Vector<>();
        this.unreachableProcess2= new Vector<>();
        /*plus infinity*/
        defaultRate=1f/0f;
    }

    public Vector<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(Vector<Sort> sorts) {
        this.sorts = sorts;
    }

    public Vector<Action> getActions() {
        return actions;
    }

    public void setActions(Vector<Action> actions) {
        this.actions = actions;
    }

    public Vector<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(Vector<Process> processes) {
        this.processes = processes;
    }

    public Vector<Process> getInitialState() {
        return initialState;
    }

    public void setInitialState(Vector<Process> initialState) {
        this.initialState = initialState;
    }

    public Vector<Process> getSequence() {
        return sequence;
    }

    public void setSequence(Vector<Process> sequence) {
        this.sequence = sequence;
    }

    public Vector<Relation> getRelations() {
        return relations;
    }

    public void setRelations(Vector<Relation> relations) {
        this.relations = relations;
    }

    public Vector<Process> getUnreachableProcess1() {
        return unreachableProcess1;
    }

    public void setUnreachableProcess1(Vector<Process> unreachableProcess1) {
        this.unreachableProcess1 = unreachableProcess1;
    }

    public Vector<ProcessWithActions> getUnreachableProcess2() {
        return unreachableProcess2;
    }

    public void setUnreachableProcess2(Vector<ProcessWithActions> unreachableProcess2) {
        this.unreachableProcess2 = unreachableProcess2;
    }
    
    public void printActions(){
        for (Action i : actions){
            i.printAction();
        }
    }
}
