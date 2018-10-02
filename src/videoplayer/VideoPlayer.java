/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;  

/**
 *
 * @author Rhys
 */
public class VideoPlayer extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Videoplay.fxml"));
        Parent root = loader.load();
        
        VideoController controller = (VideoController)loader.getController();
        
        controller.init(primaryStage);
        
        Scene scene = new Scene(root); 
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
    
}
