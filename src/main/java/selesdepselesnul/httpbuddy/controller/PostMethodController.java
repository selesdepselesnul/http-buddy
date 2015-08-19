package selesdepselesnul.httpbuddy.controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PostMethodController implements Initializable {

	@FXML
	private TextField keyFieldTextField;

	@FXML
	private TextField valueFieldTextField;

	@FXML
	private TextField keyFileTextField;

	@FXML
	private Button valueFileButton;

	@FXML
	private TableView<Pair<String, String>> fieldTableView;

	@FXML
	private TableColumn<Pair<String, String>, String> keyFieldTableColumn;

	@FXML
	private TableColumn<Pair<String, String>, String> valueFieldTableColumn;

	@FXML
	private TableView<Pair<String, File>> fileTableView;

	@FXML
	private TableColumn<Pair<String, File>, String> keyFileTableColumn;

	@FXML
	private TableColumn<Pair<String, File>, File> valueFileTableColumn;

	@FXML
	private Button postButton;

	private String url;

	private HttpBuddyController httpBuddyController;

	private Stage stage;

	@FXML
	private ComboBox<String> postInputComboBox;

	@FXML
	private TextArea requestBodyTextArea;

	@FXML
	private TitledPane fieldTitledPane;

	@FXML
	Button addingFieldButton;

	private File openedFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.keyFieldTableColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
		this.valueFieldTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		this.fieldTableView.getColumns().setAll(keyFieldTableColumn, valueFieldTableColumn);

		this.keyFileTableColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
		this.valueFileTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		this.fileTableView.getColumns().setAll(keyFileTableColumn, valueFileTableColumn);
	}

	@FXML
	public void handleAddingPairButton() {
		String key = this.keyFieldTextField.getText();
		if (key != null) {
			if (!key.equals("")) {
				this.fieldTableView.getItems().add(new Pair<String, String>(key, this.valueFieldTextField.getText()));
				clearTextField();
			}
		}

		this.postButton.setDisable(false);
	}

	@FXML
	public void handleAddingFieldButton() {
		this.postButton.setDisable(false);
		this.fieldTableView.getItems()
				.add(new Pair<>(this.keyFieldTextField.getText(), this.valueFieldTextField.getText()));
		this.keyFieldTextField.clear();
		this.valueFieldTextField.clear();
	}

	@FXML
	public void handleAddingFileButton() {
		this.postButton.setDisable(false);
		this.fileTableView.getItems().add(new Pair<>(this.keyFileTextField.getText(), this.openedFile));
		this.keyFileTextField.clear();
		this.valueFileButton.setDisable(true);
	}

	@FXML
	public void handleTypingRequestBody() {
		if (this.requestBodyTextArea.getText() != null) {
			if (!this.requestBodyTextArea.getText().equalsIgnoreCase("")) {
				this.fieldTableView.getItems().clear();
				this.fileTableView.getItems().clear();
				this.postButton.setDisable(false);
			} else {
				this.postButton.setDisable(true);
			}
		}
	}

	private void clearTextField() {
		this.keyFieldTextField.clear();
		this.valueFieldTextField.clear();
	}

	@FXML
	public void handlePostButton() {
		try {
			HttpRequestWithBody httpRequestWithBody = Unirest.post(this.url);
			if (!this.fieldTableView.getItems().isEmpty() || !this.fileTableView.getItems().isEmpty()) {
				Map<String, Object> mapField = new HashMap<>();
				if (!this.fieldTableView.getItems().isEmpty()) {
					mapField.putAll(this.fieldTableView.getItems().stream()
							.collect(Collectors.toMap(Pair::getKey, Pair::getValue)));
				}
				if (!this.fileTableView.getItems().isEmpty()) {
					mapField.putAll(this.fileTableView.getItems().stream()
							.collect(Collectors.toMap(Pair::getKey, Pair::getValue)));
				}
				this.httpBuddyController.outputResponseMessage(httpRequestWithBody.fields(mapField).asString());
			} else {
				this.httpBuddyController
						.outputResponseMessage(httpRequestWithBody.body(this.requestBodyTextArea.getText()).asString());

				this.httpBuddyController.outputRequestHeaderMessage(httpRequestWithBody.getHeaders());
			}
			this.stage.close();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setMainController(HttpBuddyController httpBuddyController) {
		this.httpBuddyController = httpBuddyController;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleSelectInComboBox() {
		if (this.postInputComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("request body")) {
			this.disableField(true);
		} else {
			this.disableField(false);
		}
	}

	private void disableField(boolean isDisable) {
		this.keyFieldTextField.setDisable(isDisable);
		this.valueFieldTextField.setDisable(isDisable);
		this.requestBodyTextArea.setDisable(!isDisable);
	}

	@FXML
	public void handleKeyFileRealesed() {
		if (this.keyFileTextField.getText() != null) {
			if (!this.keyFileTextField.getText().equalsIgnoreCase("")) {
				this.valueFileButton.setDisable(false);
			} else {
				this.valueFileButton.setDisable(true);
			}
		} else {
			this.valueFileButton.setDisable(true);
		}
	}

	@FXML
	public void handleChoosingFile() {
		FileChooser fileChooser = new FileChooser();
		this.openedFile = fileChooser.showOpenDialog(this.stage);
	}

}