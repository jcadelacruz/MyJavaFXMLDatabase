/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers.controlledMovementWithEntitiesDisplayJava;

import java.util.ArrayList;

/**
 *
 * @author dc_ca
 */
public class Navigable {
    private ArrayList<Movable> cont = new ArrayList<>();
    private Integer[] size = {0,0};

    public Navigable(){
        cont = new ArrayList<>();
    }
    public Navigable(int x, int y){
        cont = new ArrayList<>();
        Integer[] res = {x, y};
        size = res;
    }
    
    //getters
    public Integer[] getSize() {
        return size;
    }
    public String getBGImgFileName() {
        return "note.png";
    }
    public ArrayList<Movable> getContents() {
        return this.cont;
    }
    //setters
    public void addContents(Movable m) {
        cont.add(m);
    }
    public void addContents(Entity e, boolean t) {
        cont.add(e);
        cont.addAll(e.getContents());
    }
}
