package controllers.controlledMovementWithEntitiesDisplayJava;

import controllers.ControlledMovementWithEntitiesDisplayController;
import java.util.TimerTask;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dc_ca
 */
public class MyTimerTask extends TimerTask{
    public MyTimerTask(){}
    @Override
    public void run() {
        try{
            ControlledMovementWithEntitiesDisplayController.attemptUpdateAll();
        }
        catch(Exception e){
            
        }
    }
    
}
