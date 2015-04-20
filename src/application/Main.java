package application;
	
import java.util.HashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;


public class Main extends Application {
	
	public static Stage primaryStage;
	public static GmailApi API;
    private AnchorPane rootLayout;
    private Set<String> stringSet;
    
    @FXML
    private ListView listView;
    
    
    ObservableList observableList = FXCollections.observableArrayList();
    
   
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Login.fxml"));
            rootLayout = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.initStyle(StageStyle.UNDECORATED);
			
			Main.primaryStage = primaryStage;
	        Main.primaryStage.setTitle("Komodo");

	        initRootLayout();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		API = new GmailApi();
		launch(args);
	}
	
	public static Parent replaceSceneContent(String fxml) throws Exception {
	        Parent page = (Parent) FXMLLoader.load(Main.class.getResource(fxml));
	        Scene scene = primaryStage.getScene();
	        if (scene == null) {
	            scene = new Scene(page, 700, 450);
	        } else {
	        	primaryStage.getScene().setRoot(page);
	        }
	        primaryStage.sizeToScene();
	        return page;
	    }
}
