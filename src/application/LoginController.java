package application;

import java.awt.Desktop;
import java.net.URI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML 
	private TextField codeaccess;
	
	    
	@FXML 
	protected void gmailLoginButtonAction(ActionEvent event) {
		try {
			System.out.println(Main.API.GetURL());
	    	URI uri = new URI(Main.API.GetURL());
	    	Desktop dt = Desktop.getDesktop();
	    	dt.browse(uri);
		}
	    catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	    
    @FXML
    protected void accessButtonAction(ActionEvent event) {
    	if (Main.API.Authenticate(codeaccess.getText())){
    		try {
    			Main.replaceSceneContent("Mail.fxml");
    		}
    		catch (Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    public LoginController(){
    	
    	
    }
    
    public void initialize(){
    	
    }
}
