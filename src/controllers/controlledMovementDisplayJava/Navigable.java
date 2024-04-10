/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers.controlledMovementDisplayJava;

import java.util.ArrayList;

/**
 *
 * @author dc_ca
 */
public interface Navigable {
    public String getBGImgFileName();
    public ArrayList<Movable> getContents();
    public void addContents(Movable m);
    public Integer[] getSize();
}
