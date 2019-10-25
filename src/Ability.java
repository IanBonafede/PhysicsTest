

import java.util.ArrayList;
import org.jbox2d.dynamics.Body;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian Bonafede
 */
public class Ability {
    private boolean active;
    private boolean needsGround;
    private boolean hold;
    private boolean sameKeys;
    
    private float seconds;
    
    private int HPcost;
    private int MPcost;
    private int SPcost;
    
    private ArrayList keysNeed;
    private ArrayList antikeysNeed;
    
    private boolean needs;
    private boolean antineeds;
    
    private Movement movement;
    
    private String type;
    
    
    
    
    
    
    
    public Ability(ArrayList keysNeeded, ArrayList antikeysNeeded, boolean needground, boolean hol, int hpc, int mpc, int spc, Movement m, float s) {
        active = false;
        
        keysNeed = new ArrayList();
        keysNeed = keysNeeded;
        
        antikeysNeed = new ArrayList();
        antikeysNeed = antikeysNeeded;
        
        needsGround = needground;
        hold = hol;
        
        HPcost = hpc;
        MPcost = mpc;
        SPcost = spc;
        
        movement = new Movement();
        movement = m;
        
        seconds = s;
        
        type = movement.getType();
        
    }
    
    
    public void activate(boolean onGround, ArrayList lastKeysPressed, ArrayList newKeysPressed, int HPLeft, int MPLeft, int SPLeft, Body body) {
        
        System.out.println("step 1");
        
        //check that the conditions are right
        if(onGround == needsGround && HPLeft >= HPcost && MPLeft >= MPcost && SPLeft >= SPcost) {
            
            System.out.println("step 2");
            
            //check to see if the need keys are being pressed
            needs = true;
            for(int i = 0; i < keysNeed.size(); i++) {
                    
                if(!newKeysPressed.contains(keysNeed.get(i)))
                    needs = false;
                    
            }
            
            
            
            
            //check to see if the anti-need keys are not being pressed
            antineeds = true;
            for(int i = 0; i < antikeysNeed.size(); i++) {
                    
                if(newKeysPressed.contains(antikeysNeed.get(i)))
                    antineeds = false;
                    
            }
            
            
            
            if(needs && antineeds) {
                System.out.println("step 3");
                
                if(hold) {
                    movement.doMovement(body);
                    System.out.println("step 4");
                }
                
                //if you cant hold it
                if(!hold) {
                    //if the last keys pressed are not the same activate
                    if(lastKeysPressed.size() == newKeysPressed.size()) {
                        sameKeys = true;
                        
                        
                        for(int i = 0; i < newKeysPressed.size(); i++) {
                            
                            if(newKeysPressed.get(i) != lastKeysPressed.get(i))
                                sameKeys = false;
                            
                        }
                        
                        
                        if(!sameKeys) {
                            movement.doMovement(body);
                            active = true;
                            System.out.println("step 4");
                        }
                     
                    }
                    
                    if(lastKeysPressed.size() != newKeysPressed.size()) {
                        sameKeys = false;
                        
                        
                        movement.doMovement(body);
                        active = true;
                        System.out.println("step 4");
                     
                    }
                }
            
//            if(!needs || !antineeds) {
//                active = false;
//            }
            
            
            
            
            
            }
        }
        
//        else
//            active = false;
            
            
            
            
        System.out.println(active);
    }
    
    
    public boolean isActive() {
        return active;
    }
    
    
    
    
    public int getHPCost() {
        return HPcost;
    }
    public int getMPCost() {
        return MPcost;
    }
    public int getSPCost() {
        return SPcost;
    }
}
