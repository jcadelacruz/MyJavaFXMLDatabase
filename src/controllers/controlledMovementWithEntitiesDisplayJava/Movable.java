/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers.controlledMovementWithEntitiesDisplayJava;

/**
 *
 * @author dc_ca
 */
public class Movable {
    protected Integer[] pos = {0, 0};
    private double hue;
    private int speed;
    private String aggression;
    private boolean isPermeable;
    protected Entity entity;
    
    public Movable(int x, int y, double h){
        pos[0] = x;
        pos[1] = y;
        hue = h;
        aggression = "";
        speed = 12;
        entity = null;
    }
    public Movable(int x, int y, double h, Entity e){
        pos[0] = x;
        pos[1] = y;
        hue = h;
        aggression = "";
        speed = 12;
        entity = e;
    }
    public Movable(int x, int y, double h, String a, int s){
        pos[0] = x;
        pos[1] = y;
        hue = h;
        aggression = a;
        speed = s;
        entity = null;
    }
    
    //getters
    public Integer[] getPosition(){
        return pos;
    }
    public boolean getIsPermeable(){
        return isPermeable;
    }
    public String getIconFileName(){
        return "";
    }
    public double getHue(){
        return hue;
    }
    public int getSpeed(){
        return speed;
    }
    public String getAggression(){
        return aggression;
    }
    public Entity getEntity(){
        return entity;
    }
    
    //setters
    public void setEntity(Entity e){
        entity = e;
    }
    public void setX(int i) {
        pos[0] = i;
    }
    public void setY(int i) {
        pos[1] = i;
    }
        //second set for entity movement fsr?
    public void changeX(int i) {
        pos[0] = i;
    }
    public void changeY(int i) {
        pos[1] = i;
    }
}
