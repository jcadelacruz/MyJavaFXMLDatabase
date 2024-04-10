/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import controllers.controlledMovementDisplayJava.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * FXML Controller class
 *
 * @author dc_ca
 */
public class ControlledMovementDisplayController implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private Button btnrn;
    private GridPane gridPane;
    private ArrayList<ArrayList<ImageView>> imageViews;
    private int ROWCNT, COLCNT, ROWSIZE, COLSIZE, camX = 0, camY=0;
    private Navigable currLoc;
    private String MOVABLE_IMGS_PATH, LOCATION_IMGS_PATH;
    private Movable user;
    
    //display
        //set location
    public void setLocation(Navigable l){
        currLoc = l;
    }
        //display
    public void updateScreen(){
        //set the empty
        setBackground(currLoc);
        //set the not empty
        for(Movable m : currLoc.getContents()){
            //access pos
            int x = m.getPosition()[0];
            int y = m.getPosition()[1];
            //if Movable is within cam constraints
            if((camX <= x) && (x < (COLCNT + camX))){ if((camY <= y) && (y < (ROWCNT + camY))){
                ImageView iv = imageViews.get(x-camX).get(y-camY);
                try{
                    Image img = new Image(getClass().getResourceAsStream(MOVABLE_IMGS_PATH + m.getIconFileName()));
                    iv.setImage(img);
                }
                catch(NullPointerException e){
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setHue(-0.7);
                    iv.setEffect(colorAdjust);
                }
            }}
        }
    }
    public void setBackground(Navigable l){
        Image img;
        try{
            img = new Image(getClass().getResourceAsStream(LOCATION_IMGS_PATH + l.getBGImgFileName()));
        }
        catch(NullPointerException e){
            img = new Image(getClass().getResourceAsStream(""));//default image
        }
        for(ArrayList<ImageView> a : imageViews){ for(ImageView iv : a){
            iv.setImage(img);
            iv.setEffect(null);
        }}
    }
        //functionality
            //player only
    @FXML public void inputKey(KeyEvent e){
        KeyCode keyCode = e.getCode();
        //example for moving player with keys
        if(keyCode.equals(KeyCode.W)){//directions: NESW = 0123
            int direction = 0;
            if(checkOpenSpace(user, direction)) movePlayer(direction);
            //commenceTurn();//add this if you want turns to move every time you move
        }
        if(keyCode.equals(KeyCode.D)){
            int direction = 1;
            if(checkOpenSpace(user, direction)) movePlayer(direction);
        }
        if(keyCode.equals(KeyCode.S)){
            int direction = 2;
            if(checkOpenSpace(user, direction)) movePlayer(direction);
        }
        if(keyCode.equals(KeyCode.A)){
            int direction = 3;
            if(checkOpenSpace(user, direction)) movePlayer(direction);
        }
        //example for other function with keyboard
        if(keyCode.getName().equals("E")){
            
        }
        updateScreen();
    }
    private void movePlayer(int direction){
        int limit = 2;
        if(direction==0||direction==2) limit = (ROWCNT)/2;
        else limit = (COLCNT)/2;
        
        int key = checkMovableDistanceFromEdge(user, direction);
        if(checkMovableDistanceFromEdge(user, direction+2)<limit) key = 1;
        
        moveMovablePos(user, direction);
        if(key>limit) moveCamPos(direction);//at far position, limit+1 or more away from edge
    }
    private void moveCamPos(int direction){
        switch(limitDirection(direction)){
            case 0:
                camY--;
                break;
            case 1:
                camX++;
                break;
            case 2:
                camY++;
                break;
            case 3:
                camX--;
                break;
        }
    }
            //general
    private int limitDirection(int direction){
        int dir = direction;
        if(direction>3){
            int subtract = (direction)/4;
            dir = direction - (subtract*4);
        }
        return dir;
    }
    private boolean checkOpenSpace(Movable r, int direction){
        boolean open = true;
        System.out.println(checkMovableDistanceFromEdge(r, direction));
        if(checkMovableDistanceFromEdge(r, direction)<=0) return false;
        
        moveMovablePos(r, direction);
        for(Movable s : currLoc.getContents()){
            if( (s.getPosition()[0] == r.getPosition()[0]) && (s.getPosition()[1] == r.getPosition()[1]) && (s!=r)){
                if(!s.getIsPermeable()){
                    open = false;
                }
            }
        }
        moveMovablePos(r, direction+2);
        
        return open;
    }
    private int checkMovableDistanceFromEdge(Movable u, int direction){
        int distance = 0;
        switch(limitDirection(direction)){
            case 0://north
                System.out.println("going north");
                distance = u.getPosition()[1];
                break;
            case 1://east
                System.out.println("going east");
                distance = currLoc.getSize()[0] - (u.getPosition()[0]);
                break;
            case 2://south
                distance = currLoc.getSize()[1] - (u.getPosition()[1]);
                break;
            case 3://west
                distance = u.getPosition()[0];
                break;
            default:
                //System.out.println("Direction not found");
        }
        
        return distance;
    }
    private void moveMovablePos(Movable s, int direction){
        Integer[] pos = s.getPosition();
        
        switch(limitDirection(direction)){
            case 0:
                s.setY(pos[1]-1);
                break;
            case 1:
                s.setX(pos[0]+1);
                break;
            case 2:
                s.setY(pos[1]+1); 
                break;
            case 3:
                s.setX(pos[0]-1);
                break;
        }
    }
            //turn system
    /*
    public void commenceTurn(){
        for(Movable s : currLoc.getContents()){
            if(s!=user){
                int speed = s.getSpeed();
                if(speed<user.getSpeed()){//speed is slower
                    if(turn%user.getSpeed()==0){
                        commenceSpatialTurn(s);
                    }
                }
                else{//speed is equal to or faster
                    for(int i = 0; i<(speed/u.getSpeed()); i++){
                        commenceSpatialTurn(s);
                    }
                }
            }
        }
        u.turnUpdate();
        
        //display
        updateScreen();
    }
    private void commenceSpatialTurn(Spatial s){
        int agro = -1;
        String ag = s.getAggression();
        //determining agro
        if(ag.equals("RANDOM")) agro = 0;
        if(ag.equals("CHASE")) agro = 2;
        //turn update (regen, 
        s.turnUpdate();
        //movement
        switch(agro){//s.getAggression()){
            case 0://random move, no atk
                double d = Math.random() * 4;
                int dir = (int) d;
                if(checkOpenSpace(s, dir)) moveMovablePos(s, dir);
                break;
            default:
                System.out.println("aggression not found");
        }
        //attack
        boolean userIsDead = false;
        if(isPlayerNearby(s)) userIsDead = s.attackAndGetIsDead(Spatial.getUser());
        switch(agro){
            case 2:
                if(isPlayerNearby(s)) userIsDead = s.attackAndGetIsDead(Spatial.getUser());
                break;
            default:
        }
        if(userIsDead) spatialDeath(Spatial.getUser());
    }
    private void spatialDeath(Spatial s){
        ArrayList<Spatial> contents = Location.getCurrentLocation().getContents();
        contents.remove(s);
        if(s==Spatial.getUser()){
            //death stuff if player
        }
    }
    private boolean isPlayerNearby(Spatial s){
        boolean nearby = false;
        Spatial u = Spatial.getUser();
        int[] uPos = u.getPosition(), sPos = s.getPosition();
        
        if( ((sPos[0]==(uPos[0]-1)) || (sPos[0]==(uPos[0]+1))) && (uPos[1]==sPos[1]) ){
            nearby = true;
        }
        if( ((sPos[1]==(uPos[1]-1)) || (sPos[1]==(uPos[1]+1))) && (uPos[0]==sPos[0]) ){
            nearby = true;
        }
        return nearby;
    }*/
    //initializing gridPane
    private void intializeGridPane(){
        //creating + placing grid pane
        gridPane = new GridPane();
        borderPane.setCenter(gridPane);
        
        setImageViews();
        setGridConstraints();
    }
    private void setImageViews(){
        //imageviews arraylist
        imageViews = new ArrayList<>();
        for(int i = 0; i<COLCNT; i++){
            imageViews.add(new ArrayList<>());
            for(int j = 0; j<ROWCNT; j++){
                ImageView iv = new ImageView();
                iv.setPreserveRatio(true);
                iv.setFitWidth(COLSIZE); 
                iv.setFitHeight(ROWSIZE); 
                
                imageViews.get(i).add(iv);
                gridPane.add(iv, i, j);
            }
        }
    }
    private void setGridConstraints(){
        //setting column and row sizes of gridpane
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        for(int i=0; i<ROWCNT; i++) gridPane.getRowConstraints().add(new RowConstraints(ROWSIZE));
        for(int i=0; i<COLCNT; i++) gridPane.getColumnConstraints().add(new ColumnConstraints(COLSIZE));
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ROWCNT = 5;
        COLCNT = 5;
        ROWSIZE = 100;
        COLSIZE = 100;
        MOVABLE_IMGS_PATH = "/imgs/movables/";//example
        LOCATION_IMGS_PATH = "";//example
        intializeGridPane();
            //examples
            Embe loc = new Embe(1,1);
            user = new Embe(1,1);//initialize user here, or when you initialize currLoc, make sure at center pos
            user.setY(ROWCNT/2);
            user.setX(COLCNT/2);
            loc.addContents(user);//make sure user is in currLoc
            loc.addContents(new Embe(5,4));
            loc.addContents(new Embe(0,0));
            loc.addContents(new Embe(0,2));
            setLocation(loc);
            updateScreen();
    }
    
}
