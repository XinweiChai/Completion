/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package completion;

/**
 *
 * @author RoshanMayCry
 */
public class Relation {
    private Sort hitter;
    private Sort target;
    private boolean regulation;

    public Relation(Sort hitter, Sort target, boolean regulation) {
        this.hitter = hitter;
        this.target = target;
        this.regulation = regulation;
    }

    public Sort getHitter() {
        return hitter;
    }

    public void setHitter(Sort hitter) {
        this.hitter = hitter;
    }

    public Sort getTarget() {
        return target;
    }

    public void setTarget(Sort target) {
        this.target = target;
    }

    public boolean getRegulation() {
        return regulation;
    }

    public void setRegulation(boolean regulation) {
        this.regulation = regulation;
    }
    
}
