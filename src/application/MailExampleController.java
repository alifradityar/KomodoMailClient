package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.mail.MessagingException;

import org.thehecklers.monologfx.MonologFX;
import org.thehecklers.monologfx.MonologFXBuilder;
import org.thehecklers.monologfx.MonologFXButton;
import org.thehecklers.monologfx.MonologFXButtonBuilder;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;
import com.sun.javafx.css.StyleManager;

public class MailExampleController {
	@FXML
	private ListView listView;

	@FXML
	private Label title;

	@FXML
	private Label emailSubject;
	@FXML
	private Label emailSR;
	@FXML
	private Label emailDate;
	@FXML
	private WebView emailWebView;

	@FXML
	private MenuButton menuButton;

	@FXML
	private VBox mailContent;
	@FXML
	private VBox mailCompose;

	@FXML
	private TextField recipientField;
	@FXML
	private TextField subjectField;
	@FXML
	private TextArea contentField;

	@FXML
	private Label currentMailLabel;
	
	@FXML
	private Button replyButton;
	
	@FXML
	private Button forwardButton;
	
	@FXML
	private Button decryptButton;
	
	@FXML
	private RadioButton useEncryptionRadio;
	
	@FXML
	private RadioButton useSignatureRadio;
	
	private String currentRawContent;
	
	private com.sun.mail.util.MailLogger logger;

	private List<Message> messages;
	ObservableList observableList = FXCollections.observableArrayList();

	public MailExampleController() {

	}

	@FXML
	protected void inboxButtonAction(ActionEvent event) {
		replyButton.setVisible(false);
		forwardButton.setVisible(false);
		decryptButton.setVisible(false);
		mailContent.setVisible(true);
		listView.setVisible(true);
		mailCompose.setVisible(false);
		loadInbox();
		menuButton.setText("INBOX");
	}

	@FXML
	protected void sentButtonAction(ActionEvent event) {
		replyButton.setVisible(false);
		forwardButton.setVisible(false);
		decryptButton.setVisible(false);
		mailContent.setVisible(true);
		listView.setVisible(true);
		mailCompose.setVisible(false);
		loadSentMail();
		menuButton.setText("SENT");
	}
	
	@FXML
	protected void draftButtonAction(ActionEvent event) {
		replyButton.setVisible(false);
		forwardButton.setVisible(false);
		decryptButton.setVisible(false);
		mailContent.setVisible(true);
		listView.setVisible(true);
		mailCompose.setVisible(false);
		loadDraft();
		menuButton.setText("DRAFT");
	}
	
	@FXML
	protected void spamButtonAction(ActionEvent event) {
		replyButton.setVisible(false);
		forwardButton.setVisible(false);
		decryptButton.setVisible(false);
		mailContent.setVisible(true);
		listView.setVisible(true);
		mailCompose.setVisible(false);
		loadSpam();
		menuButton.setText("SPAM");
	}
	
	@FXML
	protected void trashButtonAction(ActionEvent event) {
		replyButton.setVisible(false);
		forwardButton.setVisible(false);
		decryptButton.setVisible(false);
		mailContent.setVisible(true);
		listView.setVisible(true);
		mailCompose.setVisible(false);
		loadTrash();
		menuButton.setText("TRASH");
	}

	@FXML
	protected void composeButtonAction(ActionEvent event) {
		mailContent.setVisible(false);
		listView.setVisible(false);
		mailCompose.setVisible(true);
		menuButton.setText("COMPOSE");
	}
	
	@FXML
	protected void replyButtonAction(ActionEvent event) {
		
	}
	@FXML
	protected void forwardButtonAction(ActionEvent event) {
	}
	@FXML
	protected void decryptButtonAction(ActionEvent event) {
		
		try {
			String encrypted = currentRawContent;
			System.out.println(encrypted);
			EmailController emailController = new EmailController();
			String plainText = emailController.decryptButton(encrypted);
			System.out.println(plainText);
			emailWebView.getEngine().loadContent(plainText);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@FXML
	protected void sendButtonAction(ActionEvent event) {
		try {
			String email = Main.API.getCurrentMail();
			System.out.println(email);
			if (useEncryptionRadio.isSelected()){
				System.out.println("use encryption");
				EmailController emailController = new EmailController();
				String encrypted = emailController.encryptButton(contentField.getText());
				System.out.println(encrypted);
				Main.API.Helper.sendMessage(recipientField.getText(), email,
						subjectField.getText(), encrypted);
			}
			else {
				Main.API.Helper.sendMessage(recipientField.getText(), email,
						subjectField.getText(), contentField.getText());
			}
			MonologFXButton mlb = MonologFXButtonBuilder.create()
					.defaultButton(true)
					.type(MonologFXButton.Type.OK).build();
			MonologFX mono = MonologFXBuilder.create().modal(true)
					.message("Your Email Has Been Sent Successfully")
					.titleText("Email Sent!").button(mlb)
					.buttonAlignment(MonologFX.ButtonAlignment.RIGHT).build();
			recipientField.setText("");
			contentField.setText("");
			subjectField.setText("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	protected void exitButtonAction(ActionEvent event) {
		Platform.exit();
	}

	public void initialize() {
		loadInbox();
		currentMailLabel.setText("Current Email : " + Main.API.getCurrentMail());
	}

	public void loadSentMail() {
		messages = Main.API.getMails("SENT");
		observableList.setAll(messages);
		listView.setItems(observableList);
		listView.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<Message>>() {
			@Override
			public ListCell<Message> call(ListView<String> listView) {
				return new ListViewCell();
			}
		});
	}

	public void loadInbox() {
		messages = Main.API.getMails("INBOX");
		observableList.setAll(messages);
		listView.setItems(observableList);
		listView.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<Message>>() {
			@Override
			public ListCell<Message> call(ListView<String> listView) {
				return new ListViewCell();
			}
		});
	}
	
	public void loadDraft() {
		messages = Main.API.getMails("DRAFT");
		observableList.setAll(messages);
		listView.setItems(observableList);
		listView.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<Message>>() {
			@Override
			public ListCell<Message> call(ListView<String> listView) {
				return new ListViewCell();
			}
		});
	}
	
	public void loadSpam() {
		messages = Main.API.getMails("SPAM");
		observableList.setAll(messages);
		listView.setItems(observableList);
		listView.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<Message>>() {
			@Override
			public ListCell<Message> call(ListView<String> listView) {
				return new ListViewCell();
			}
		});
	}
	
	public void loadTrash() {
		messages = Main.API.getMails("TRASH");
		observableList.setAll(messages);
		listView.setItems(observableList);
		listView.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<Message>>() {
			@Override
			public ListCell<Message> call(ListView<String> listView) {
				return new ListViewCell();
			}
		});
	}

	@FXML
	private void mailListClickAction(MouseEvent mouseEvent) {
		Message message = (Message) listView.getSelectionModel()
				.getSelectedItem();

		Map<String, String> map = Main.API.Helper.getMessageDetails(message
				.getId());
		// Iterator entries = map.entrySet().iterator();
		// while (entries.hasNext()) {
		// Entry thisEntry = (Entry) entries.next();
		// Object key = thisEntry.getKey();
		// Object value = thisEntry.getValue();
		// System.out.println(key + " " + value);
		// }
		List<MessagePartHeader> headers = message.getPayload().getHeaders();
		String type = "";
		String content = "";
		for (MessagePartHeader header : headers) {
			// if (header.getName().equals("Subject")){
			// emailSubject.setText(header.getValue());
			// }
			if (header.getName().equals("From")) {
				emailSR.setText(header.getValue());
				content = header.getValue();
			}
			// if (header.getName().equals("Date")){
			// emailDate.setText(header.getValue());
			// }
			// if (header.getName().equals("Content-Type")){
			// type = header.getValue();
			// }
		}

		emailSubject.setText(map.get("subject"));
		//emailSR.setText(map.get("from"));
		emailDate.setText(map.get("time"));
		// emailContent.setText(map.get("body"));
		emailWebView.getEngine().loadContent(map.get("body"));
		currentRawContent = map.get("body");
		replyButton.setVisible(true);
		forwardButton.setVisible(true);
		decryptButton.setVisible(true);
	}

	public void setListView() {
		// stringSet = new HashSet<String>();
		// stringSet.add("String 1");
		// stringSet.add("String 2");
		// stringSet.add("String 3");
		// stringSet.add("String 4");
		// observableList.setAll(stringSet);
		// listView.setItems(observableList);
		// listView.setCellFactory(new Callback<ListView<String>,
		// javafx.scene.control.ListCell<Thread>>()
		// {
		// @Override
		// public ListCell<Thread> call(ListView<String> listView)
		// {
		// return new ListViewCell();
		// }
		// });
	}

}