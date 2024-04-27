/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javaFXMLDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dc_ca
 */
public class MenuDisplayController implements Initializable {

    @FXML private HBox menuHBox;
    private ArrayList<FXMLLoader> fxmlFiles = new ArrayList<>();
    
    private FXMLLoader cd(String name){//cd stands for Create Display; it's a short hand to help constructing
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/displays/"+name+"Display.fxml"));
        return loader;
    }
    private void createButtons(){
        for(FXMLLoader l : fxmlFiles){
            //create button
                //file name
                String fileName = l.getLocation().getFile();
                int index = fileName.lastIndexOf("/");
                String shortenedFileName = fileName.substring(index + 1);
            Button b = new Button(shortenedFileName);
            b.setOnAction(event -> {
                try{
                    openFXML(l);
                }
                catch(IOException e){
                    System.out.println("file not found or error opening file");
                }
            });
            //add button to menudisplay
            menuHBox.getChildren().add(b);
        }
    }
    private void openFXML(FXMLLoader l) throws IOException{
        //get current display (scene + stage)
        Scene currentScene = menuHBox.getScene();
        Stage currentStage = (Stage) currentScene.getWindow();
        //get new display (scene)
        Parent root = l.load();
        Scene newScene = new Scene(root);
        //switch displays (scenes)
        currentStage.hide();
        currentStage.setScene(newScene);
        currentStage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //intialize array of fxml files 
        fxmlFiles.add(cd("ControlledMovement"));
        fxmlFiles.add(cd("ControlledMovementWithEntities"));
        //fxmlFiles.add(cd("Store"));
        //create buttons yeh
        createButtons();
    }    
    
}
