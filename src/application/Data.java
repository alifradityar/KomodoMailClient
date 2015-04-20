package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;

public class Data
{
    @FXML
    private VBox vBox;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    
    public Data()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MailItem.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Message message)
    {
    	List<MessagePartHeader> headers = message.getPayload().getHeaders();
    	for (MessagePartHeader header : headers){
    		if (header.getName().equals("From")){
    			label1.setText(header.getValue());
    		}
    		if (header.getName().equals("Date")){
    			label3.setText(header.getValue());
    		}
    	}
        
        label2.setText(message.getSnippet());
    }

    public VBox getBox()
    {
        return vBox;
    }
}