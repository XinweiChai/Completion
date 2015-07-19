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
public class Completion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        //Data data = new Data("/home/chai/Desktop/loopTest.ph");
        Data data = new Data("C:\\Users\\RoshanMayCry\\Desktop\\source\\PHex.ph");
        //Data data = new Data();
        Model model = new Model(data);
        //model.run(5);
        //model.reachIn(5);
        //model.setObservable();
        //if(!model.tryCompletion(10))
          //  System.out.println("No solution");
        model.compPhReach();
    }
    
}
