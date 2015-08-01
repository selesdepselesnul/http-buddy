package selesdepselesnul.httpbuddy;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class HttpBuddyController {

	@FXML
	private Text statusText;

	@FXML
	private MenuItem methodComboBox;

	@FXML
	private TextField urlTextField;

	@FXML
	private TextArea headerTextArea;

	@FXML
	private TextArea bodyTextArea;

	@FXML
	private TextField keyTextField;

	@FXML
	private TextField valueTextField;

	@FXML
	public void handleGetMethod() {
		try {
			HttpResponse<String> httpResponse = Unirest.get(this.urlTextField.getText()).asString();
			this.bodyTextArea.setText(httpResponse.getBody());
			httpResponse.getHeaders().forEach((k, v) -> this.headerTextArea.appendText(k + " : " + v + "\n"));
			this.statusText.setText(httpResponse.getStatus() + " " + httpResponse.getStatusText());
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handlePostMethod() {
	}

}
