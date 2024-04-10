/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import controllers.controlledMovementDisplayJava.*;
import java.util.ArrayList;

/**
 *
 * @author dc_ca
 */
public class Embe implements Navigable, Movable{
    private ArrayList<Movable> cont = new ArrayList<>();
    private Integer[] pos = {0, 0};

    public Embe(int x, int y){
        pos[0] = x;
        pos[1] = y;
        cont = new ArrayList<>();
    }
    @Override
    public String getBGImgFileName() {
        return "/controllers/note.png";
    }

    @Override
    public ArrayList<Movable> getContents() {
        return cont;
    }

    @Override
    public void addContents(Movable m) {
        cont.add(m);
    }

    @Override
    public Integer[] getSize() {
        Integer[] res = {5,6};
        return res;
    }

    @Override
    public Integer[] getPosition() {
        return pos;
    }

    @Override
    public boolean getIsPermeable() {
        return false;
    }

    @Override
    public String getIconFileName() {
        return "bla";
    }

    @Override
    public void setX(int i) {
        pos[0] = i;
    }

    @Override
    public void setY(int i) {
        pos[1] = i;
    }
    
}
