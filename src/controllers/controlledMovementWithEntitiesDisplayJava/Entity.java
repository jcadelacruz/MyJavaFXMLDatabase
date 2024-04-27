/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.controlledMovementWithEntitiesDisplayJava;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dc_ca
 */
public class Entity extends Movable{
    private ArrayList<Movable> vertices, contents;
    
    public Entity(int x, int y, double h, String a, int s, Movable... list){
        super(x, y, h, a, s);
        entity = this;
        this.initializeVertices(x,y,h,list);
        this.initializeContents(h);
    }
    public Entity(int x, int y, double h, Movable... list){
        super(x, y, h);
        entity = this;
        this.initializeVertices(x,y,h,list);
        this.initializeContents(h);
    }
    private void initializeVertices(int x, int y, double h, Movable... list){
        vertices = new ArrayList<>();
            vertices.add(new Movable(x,y,h));
            vertices.addAll(Arrays.asList(list));   
        for(Movable m:vertices){
            m.setEntity(this);
        }
    }
    private void initializeContents(double h){
        contents = new ArrayList<>();
            contents.addAll(vertices);
            
        for(int index = 0; index<vertices.size(); index++){
            
            for(int axis = 0; axis<2; axis++){//refers to the axis thats being modified
                
                Movable v1 = vertices.get(index), v2;
                if(index==vertices.size()-1) v2 = vertices.get(0);
                else v2 = vertices.get(index+1);
                    int v1pos = v1.getPosition()[axis], v2pos = v2.getPosition()[axis];
                    
                if(v1pos == v2pos) continue;
                boolean flipped = false;
                if(v1pos>v2pos){
                    flipped = true;
                    v1pos=-v1pos;
                    v2pos=-v2pos;
                }
                
                for(int a = 0; a<(v2pos-v1pos)-1; a++){//V1 MUST BE LOWER; V1 IS THE START V2 IS THE END
                    int addend = a+1;
                    if(flipped) addend = (-2*v1pos)-addend;
                    
                    if(axis==0) contents.add(new Movable(v1pos+addend, v1.getPosition()[1], h, this));
                    else if(axis==1) contents.add(new Movable(v1.getPosition()[0], v1pos+addend, h, this));
                }
            }
        }
    }
    public ArrayList<Movable> getContents(){
        return contents;
    }
    public ArrayList<Movable> getVertices(){
        return vertices;
    }
    @Override
    public void setX(int i) {
        int change = i-pos[0];
        pos[0] = i;
        for(Movable m : contents){
            m.changeX((m.getPosition()[0]+change));
        }
    }
    @Override
    public void setY(int i) {
        int change = i-pos[1];
        pos[1] = i;
        for(Movable m : contents){
            m.changeY((m.getPosition()[1]+change));
        }
    }
}
