/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import controllers.controlledMovementWithEntitiesDisplayJava.MyTimerTask;
import controllers.controlledMovementWithEntitiesDisplayJava.Movable;
import controllers.controlledMovementWithEntitiesDisplayJava.Navigable;
import controllers.controlledMovementWithEntitiesDisplayJava.Entity;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Timer;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Has Navigable or a location 
 * -> Has camera movement to increase space
 * Has Movable which are objects that can move in Navigable
 * -> Has Entity which are Movable that are connected to create a functional hit box
 * Has a user entity that can be moved by controllers
 * Uses threads
 * -> Has press and hold key movement
 * -> Has update functions
 *
 * @author dc_ca
 */
public class ControlledMovementWithEntitiesDisplayController implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private Button btnrn;
    @FXML private Text texto;
    private GridPane gridPane;
    private ArrayList<ArrayList<ImageView>> imageViews;
    private int ROWCNT, COLCNT, ROWSIZE, COLSIZE, camX = 0, camY=0, TICKRATE, turn, placeholderStat = 0;
    private Integer[] playerDir = {-1,-1,-1,-1};
    private Navigable currLoc;
    private String MOVABLE_IMGS_PATH, LOCATION_IMGS_PATH;
    private Entity user;
    private static ArrayList<ControlledMovementWithEntitiesDisplayController> displays = new ArrayList<>();
    
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
                    colorAdjust.setHue(m.getHue());
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
        if(keyCode.equals(KeyCode.W))addMovementKey(0);//directions: NESW = 0123
        if(keyCode.equals(KeyCode.D))addMovementKey(1);
        if(keyCode.equals(KeyCode.S))addMovementKey(2);
        if(keyCode.equals(KeyCode.A))addMovementKey(3);
        //example for other function with keyboard
        if(keyCode.equals(KeyCode.E)){
            setStageSize();
        }
        if(keyCode.equals(KeyCode.P)){
            for(Movable m : currLoc.getContents()){
                if(areNear(user, m, 1)) attemptAttack(user, m);
            }
        }
        updateScreen();
    }
    @FXML public void keyRelease(KeyEvent e){
        KeyCode keyCode = e.getCode();
        if(keyCode.equals(KeyCode.W))removeMovementKey(0);
        if(keyCode.equals(KeyCode.D))removeMovementKey(1);
        if(keyCode.equals(KeyCode.S))removeMovementKey(2);
        if(keyCode.equals(KeyCode.A))removeMovementKey(3);
    }
    private void addMovementKey(KeyCode k){
        if(k.equals(KeyCode.W))addMovementKey(0);
        if(k.equals(KeyCode.D))addMovementKey(1);
        if(k.equals(KeyCode.S))addMovementKey(2);
        if(k.equals(KeyCode.A))addMovementKey(3);
    }
    private void removeMovementKey(KeyCode k){
        if(k.equals(KeyCode.W))removeMovementKey(0);
        if(k.equals(KeyCode.D))removeMovementKey(1);
        if(k.equals(KeyCode.S))removeMovementKey(2);
        if(k.equals(KeyCode.A))removeMovementKey(3);
    }
    private void removeMovementKey(int unDir){
        ArrayList<Integer> pd = new ArrayList<Integer>(Arrays.asList(playerDir));
        int index = pd.indexOf(unDir);
        unshiftIntArrayAtIndex(index, playerDir, -1);
    }
    private void pushIntArray(Integer[] ia, int add){
        //
        int len = ia.length;
        for(int i=0; i<len-1; i++){
            //System.out.println("  moving "+ia[len-(2+i)] + " from " + (len-(2+i)) + " to " + (len-(1+i)));
            ia[len-(1+i)] = ia[len-(2+i)];
        }
        ia[0] = add;
    }
    private void unshiftIntArrayAtIndex(int index, Integer[] ia, int add){
        int len = ia.length;
        for(int i=0; i<(len-index)-1; i++){
            ia[index+i] = ia[index+i+1];
        }
        ia[len-1] = add;
    }
    private void addMovementKey(int newDir){
        ArrayList<Integer> pd = new ArrayList<Integer>(Arrays.asList(playerDir));
        if(!pd.contains(newDir)) pushIntArray(playerDir,newDir);
    }
    private void movePlayer(int direction){
        //System.out.println("moving "+direction);
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
    private boolean areOfSameEntity(Movable s, Movable r){
        if((r.getEntity()==null)||(s.getEntity()==null)||(s.getEntity()!=r.getEntity())) return false;
        else return true;
    }
    private boolean isEntity(Movable s){//add this to Movable?
        boolean entity = false;
        try{
            Entity e = (Entity) s;
            entity = true;
        }
        catch(ClassCastException exc){
            entity = false;
        }
        return entity;
    }
                //positional
    private boolean checkOpenSpace(Entity e, int direction){
        boolean result = true;
        int count=0;
        for(Movable m : e.getContents()){
            if(!checkOpenSpace(m, direction)) count++;
        }
        if(count!=0) result = false;
        return result;
    }
    private boolean checkOpenSpace(Movable r, int direction){
        boolean open = true;
        if(checkMovableDistanceFromEdge(r, direction)<=0) return false;
        
        moveMovablePos(r, direction);
        for(Movable s : currLoc.getContents()){
            if( (s.getPosition()[0] == r.getPosition()[0]) && (s.getPosition()[1] == r.getPosition()[1]) && (s!=r)){
                if(!s.getIsPermeable() && (!areOfSameEntity(s,r))){
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
                distance = u.getPosition()[1];
                break;
            case 1://east
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
    private boolean areNear(Movable a, Movable b, int distance){
        if(a==b) return false;
        Integer[] apos = a.getPosition(), bpos = b.getPosition();
        boolean withinX = false, withinY = false;
        
        if(bpos[0]-distance<=apos[0]&&apos[0]<=bpos[0]+distance){
            withinX = true;
        }
        if(bpos[1]-distance<=apos[1]&&apos[1]<=bpos[1]+distance){
            withinY = true;
        }
        if(withinX&&withinY) return true;
        else return false;
    }
                //movement
    private void moveMovablePos(Movable s, int direction){
        //if part of an entity, then js affect the entity, not the single movable
        if(s.getEntity()!=null&&!isEntity(s)){
            moveMovablePos(s.getEntity(), direction);
            return;
        }
        
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
    public void attemptMove(Movable s, int dir){
        if(isEntity(s)){
            if(checkOpenSpace((Entity)s, dir)) moveMovablePos((Entity)s, dir);
        }
        else{
            if(checkOpenSpace(s, dir)) moveMovablePos(s, dir);
        }
    }
    private int findDirectionPointingTo(Movable chaser, Movable object){
        return findDirectionPointingTo(chaser.getPosition(), object.getPosition());
    }
    private int findDirectionPointingTo(Movable chaser, Integer opos[]){
        return findDirectionPointingTo(chaser.getPosition(), opos);
    }
    private int findDirectionPointingTo(Integer cpos[], Movable object){
        return findDirectionPointingTo(cpos, object.getPosition());
    }
    private int findDirectionPointingTo(Integer[] cpos, Integer[] opos){
        int dir = 0;//north=0,east=1, etc. 
        
        if(opos[0]<cpos[0]) dir = 3;
        else if(opos[0]>cpos[0]) dir = 1;
        if(opos[1]<cpos[1]) dir = 0;
        else if(opos[1]>cpos[1]) dir = 2;
        
        return dir;//prioritizes y axis over x axis
    }
                //attack
    private void attemptAttack(Movable attacker, Movable victim){
        boolean attackHits = true;
        if(attackHits&&(!areOfSameEntity(attacker,victim))) attack(attacker, victim);
    }
    private void attack(Movable a, Movable v){
        //knockback
        int strength = 3;
        for(int i=0; i<strength;i++){
            attemptMove(v, findDirectionPointingTo(a, v));
        }
        //damage
    }
            //turn system
    public void update(){
        //damage
        for(Movable m : currLoc.getContents()){
            boolean nearby = false;
            for(Movable u : user.getContents()){
                if(areNear(u, m, 1)&&!areOfSameEntity(u, m)){
                    nearby = true;
                }
            }
            if(nearby){
                placeholderStat++;
                texto.setText("HP: " + placeholderStat);
            }
        }
        
        //movement
        for(Movable s : currLoc.getContents()){
            int speed = s.getSpeed();
            if(turn%speed==0){
                if(s==user){
                    if(playerDir[0]!=-1&&checkOpenSpace(user, playerDir[0])) movePlayer(playerDir[0]);
                }
                else commenceMovableTurn(s);
            }
        }
        updateTurn();
        
        //display
        updateScreen();
    }
    public void updateTurn(){
        turn++;
        if(turn==TICKRATE) turn = 0;
    }
    private void commenceMovableTurn(Movable s){//BEHAVIORS
        /*Movable j = s;
        s = checkIfEntity(s);
        if(s==null) s=j;*/
        
        int agro = -1;
        String ag = s.getAggression();
        //determining agro
        if(ag.equals("RANDOM")) agro = 0;
        if(ag.equals("CHASE")) agro = 2;
        //movement
        int dir = 0;
        switch(agro){//s.getAggression()){
            case 0://random move, no atk
                double d = Math.random() * 4;
                dir = (int) d;
                attemptMove(s, dir);
                break;
            case 2:
                dir = findDirectionPointingTo(s, user);
                attemptMove(s, dir);
                break;
            case 3://chase pathfinding with guaranteed success
                //INCOMPLETE
                dir = 0;
                attemptMove(s, dir);
                break;
            default:
                //System.out.println("aggression not found");
        }
        //attack
        /*boolean userIsDead = false;
        if(isPlayerNearby(s)) userIsDead = s.attackAndGetIsDead(Spatial.getUser());
        switch(agro){
            case 2:
                if(isPlayerNearby(s)) userIsDead = s.attackAndGetIsDead(Spatial.getUser());
                break;
            default:
        }
        if(userIsDead) spatialDeath(Spatial.getUser());*/
    }
    public boolean getCanUpdate(){//havent made anything for this :/
        return true;
    }
    public static void attemptUpdateAll(){
        for(ControlledMovementWithEntitiesDisplayController d : displays){
            if(d.getCanUpdate()) d.update();
        }
    }
    public void makeTimer(){
        Timer timer = new Timer("delay", true);
        timer.scheduleAtFixedRate(new MyTimerTask(), 100, 1000/TICKRATE);
    }
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
        /*VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.prefWidthProperty().bind(borderPane.widthProperty());
        box.prefHeightProperty().bind(borderPane.heightProperty());*/
        borderPane.setCenter(gridPane);
        //box.getChildren().add(gridPane);
        
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
    int stageSizeX, stageSizeY;
    public Integer[] getStageSize(){
        Integer[] res = {stageSizeX, stageSizeY};
        return res;
    }
    private void setStageSize(){
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setWidth(getStageSize()[0]);
        stage.setHeight(getStageSize()[1]);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displays.add(this);
        ROWCNT = 40;
        COLCNT = 40;
        stageSizeX = 800;
        stageSizeY = stageSizeX+60;
        int smaller = (stageSizeX<stageSizeY) ? stageSizeX:stageSizeY;
        ROWSIZE = (smaller-20)/ROWCNT;
        COLSIZE = (smaller-20)/COLCNT;
        MOVABLE_IMGS_PATH = "/imgs/movables/";//example
        LOCATION_IMGS_PATH = "";//example
        TICKRATE = 12;
        intializeGridPane();
            //examples
            Navigable loc = new Navigable(ROWCNT-1,COLCNT-1);
            user = new Entity(1,3,-0.7,"",1,new Movable(0,3,-0.7),new Movable(0,4,-0.7),new Movable(1,4,-0.7),new Movable(1,3,-0.7), new Movable(3,3,-0.7), new Movable(3,0,-0.7), new Movable(1,0,-0.7));//initialize user here, or when you initialize currLoc, make sure at center pos
            //user = new Movable(1,1,-0.7);
            user.setY(ROWCNT/2);
            user.setX(COLCNT/2);
            loc.addContents(user, true);//make sure user is in currLoc
            loc.addContents(new Entity(0,5,0.7,"CHASE",4,new Movable(0,6, 0.7),new Movable(1,6, 0.7),new Movable(1,5, 0.7)), true);
            loc.addContents(new Entity(15,5,0.7,"RANDOM",4,new Movable(15,6, 0.7),new Movable(16,6, 0.7),new Movable(16,5, 0.7)), true);
            //loc.addContents(new Movable(5,5, 0.3,"",12));
            //loc.addContents(new Movable(9,4, 0.6,"",12));
            //loc.addContents(new Movable(0,0, 0.5,"RANDOM", 1));
            //loc.addContents(new Movable(0,2, 0.9,"CHASE", 3));
            setLocation(loc);
            updateScreen();
            makeTimer();
    }
    
}
