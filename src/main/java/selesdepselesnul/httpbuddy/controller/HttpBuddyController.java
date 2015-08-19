package selesdepselesnul.httpbuddy.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import selesdepselesnul.httpbuddy.app.util.HttpGetMethod;
import selesdepselesnul.httpbuddy.app.util.HttpMethod;
import javafx.scene.control.ComboBox;

public class HttpBuddyController implements Initializable {

	@FXML
	private Text statusText;

	@FXML
	private MenuItem methodComboBox;

	@FXML
	private TextField urlTextField;

	@FXML
	private TextArea resHeaderTextArea;

	@FXML
	private TextArea reqHeaderTextArea;

	@FXML
	private TextArea bodyTextArea;

	@FXML
	private TextField keyTextField;

	@FXML
	private TextField valueTextField;

	@FXML
	private ComboBox<String> requestHeaderField;

	@FXML
	private TextField headerFieldValueTField;

	private Map<String, String> headerFieldMap;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.headerFieldMap = new HashMap<>();
		this.requestHeaderField.setPromptText("header field");
		this.requestHeaderField.getItems().setAll("Accept-Charset", "Accept-Encoding", "Accept-Language",
				"Authorization", "Except", "From", "Host", "If-Match", "If-Modified-Since", "If-None-Match", "If-Range",
				"If-Unmodified-Since", "Max-Forwards", "Proxy-Authorization", "Range", "Referer", "TE", "User-Agent");
		Unirest.setDefaultHeader("User-Agent", "Http-Buddy");
	}

	@FXML
	public void handleGetMethod() {
		try {
			this.reqHeaderTextArea.clear();
			this.resHeaderTextArea.clear();
			GetRequest getRequest = Unirest.get(this.urlTextField.getText());
			getRequest.headers(this.headerFieldMap);
			outputResponseMessage(getRequest.asString());
			getRequest.getHeaders().forEach((k, v) -> this.reqHeaderTextArea.appendText(k + " : " + v + "\n"));
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handlePostMethod() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			Stage stage = new Stage();
			fxmlLoader.load(ClassLoader.getSystemResourceAsStream("selesdepselesnul/httpbuddy/view/PostMethod.fxml"));
			PostMethodController postMethodController = (PostMethodController) fxmlLoader.getController();
			postMethodController.setStage(stage);
			postMethodController.setUrl(this.urlTextField.getText());
			postMethodController.setMainController(this);
			stage.setScene(new Scene(fxmlLoader.getRoot()));
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleDeleteMethod() {
		try {
			Unirest.delete(this.urlTextField.getText()).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public void outputRequestHeaderMessage(Map<String, List<String>> headerMessage) {
		this.reqHeaderTextArea.clear();
		StringBuilder stringBuilder = new StringBuilder();
		headerMessage.forEach((k, v) -> stringBuilder.append(k + " : " + v + "\n"));
		this.reqHeaderTextArea.setText(stringBuilder.toString());
	}

	public void outputResponseMessage(HttpResponse<String> httpResponse) {
		this.resHeaderTextArea.clear();
		this.bodyTextArea.setText(httpResponse.getBody());
		httpResponse.getHeaders().forEach((k, v) -> this.resHeaderTextArea.appendText(k + " : " + v + "\n"));
		this.statusText.setText(httpResponse.getStatus() + " " + httpResponse.getStatusText());
	}

	@FXML
	public void handleHeadMethod() {
		try {
			GetRequest getRequest = Unirest.get(this.urlTextField.getText());
			getRequest.header("User-Agent", "http-buddy");
			getRequest.asString().getHeaders()
					.forEach((k, v) -> this.resHeaderTextArea.appendText(k + " : " + v + "\n"));
			this.statusText.setText(getRequest.asString().getStatusText() + " " + getRequest.asString().getStatus());
			getRequest.getHeaders().forEach((k, v) -> this.reqHeaderTextArea.appendText(k + " : " + v + "\n"));
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleAddingHeaderField() {
		this.reqHeaderTextArea
				.appendText(this.requestHeaderField.getValue() + ": [" + this.headerFieldValueTField.getText() + "]\n");
		this.headerFieldMap.putIfAbsent(this.requestHeaderField.getValue(), this.headerFieldValueTField.getText());
		this.requestHeaderField.getSelectionModel().clearSelection();
		this.headerFieldValueTField.clear();
	}

}