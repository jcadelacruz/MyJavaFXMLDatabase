/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers.controlledMovementDisplayJava;

/**
 *
 * @author dc_ca
 */
public interface Movable {
    public Integer[] getPosition();
    public boolean getIsPermeable();
    public String getIconFileName();
    public void setX(int i);
    public void setY(int i);
}
