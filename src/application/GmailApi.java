package application;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Profile;
import com.google.api.services.gmail.model.Thread;
import com.google.api.services.gmail.model.ListThreadsResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.http.Header;

public class GmailApi {

	// Check https://developers.google.com/gmail/api/auth/scopes for all
	// available scopes
	private static final Collection<String> SCOPE = Arrays
			.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email;https://www.googleapis.com/auth/gmail.modify;https://www.googleapis.com/auth/gmail.compose"
					.split(";"));
	private static final String APP_NAME = "Gmail API Quickstart";
	// Email address of the user, or "me" can be used to represent the currently
	// authorized user.
	private static final String USER = "me";
	// Path to the client_secret.json file downloaded from the Developer Console
	private static final String CLIENT_SECRET_PATH = "client_secret.json";

	private static GoogleClientSecrets clientSecrets;

	private String urlForToken = "";
	
	public GoogleAuthHelper Helper;

	HttpTransport httpTransport;
	JsonFactory jsonFactory;
	GoogleAuthorizationCodeFlow flow;
	Gmail service;
	Profile profile;
	
	public String getCurrentMail(){
		return profile.getEmailAddress();
	}

	public List<Message> getMails(String tag) {
		// Retrieve a page of Threads; max of 100 by default.
		try {
			List<String> labelIds = new ArrayList<String>();
			labelIds.add(tag);
			ListMessagesResponse threadsResponse = service.users().messages()
					.list(USER).setLabelIds(labelIds).execute();
			List<Message> messages = threadsResponse.getMessages();
			List<Message> result = new ArrayList<Message>();
			BatchRequest b = service.batch();
			JsonBatchCallback<Message> bc = new JsonBatchCallback<Message>() {

				@Override
				public void onSuccess(Message m, HttpHeaders responseHeaders)
						throws IOException {
					result.add(m);
				}

				@Override
				public void onFailure(GoogleJsonError e,
						HttpHeaders responseHeaders) throws IOException {

				}
			};

			// Print ID of each Thread.
			for (Message message : messages) {
				service.users().messages().get(USER, message.getId())
						.queue(b, bc);
			}
			b.execute();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Message getMessage(String messageId) {
		return null;
	}

	public void SentMessage() {

	}

	public String GetURL() {
		return urlForToken;
	}

	public Boolean Authenticate(String code) {
		// Generate Credential using retrieved code.
		try {
			GoogleTokenResponse response = flow.newTokenRequest(code)
					.setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI)
					.execute();
			GoogleCredential credential = new GoogleCredential()
					.setFromTokenResponse(response);

			// Create a new authorized Gmail API client
			service = new Gmail.Builder(httpTransport, jsonFactory, credential)
					.setApplicationName(APP_NAME).build();
			Helper = new GoogleAuthHelper(credential, service);
			profile = service.users().getProfile(USER).execute();
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	public GmailApi() {
		try {
			httpTransport = new NetHttpTransport();
			jsonFactory = new JacksonFactory();

			clientSecrets = GoogleClientSecrets.load(jsonFactory,
					new FileReader(CLIENT_SECRET_PATH));
			// Allow user to authorize via url.
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
					jsonFactory, clientSecrets, SCOPE)
					.setAccessType("online").setApprovalPrompt("auto").build();

			urlForToken = flow.newAuthorizationUrl()
					.setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}