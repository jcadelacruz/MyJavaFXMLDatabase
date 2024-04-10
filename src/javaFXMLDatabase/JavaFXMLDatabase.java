/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package javaFXMLDatabase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author dc_ca
 */
public class JavaFXMLDatabase extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        //display menu
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuDisplay.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e){
            System.out.println("\nerror opening MenuDisplay.fxml, probably error in the intializing of fxml files in menudisplay");
            //stage.close();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
