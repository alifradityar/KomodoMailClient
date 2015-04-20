package application;

import javafx.scene.control.ListCell;
import com.google.api.services.gmail.model.Message;

public class ListViewCell extends ListCell<Message>
{
    @Override
    public void updateItem(Message message, boolean empty)
    {
        super.updateItem(message,empty);
        if(message != null)
        {
            Data data = new Data();
            data.setInfo(message);
            setGraphic(data.getBox());
        }
    }
}