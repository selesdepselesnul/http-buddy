package selesdepselesnul.httpbuddy;

import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PostMethodController {

	@FXML
	private TableView<Map<String, String>> mapTableView;

	@FXML
	private TextField keyTextField;

	@FXML
	private TextField valueTextField;
	
	@FXML
	public void handleEnterTextField() {
		System.out.println("TESTTESTESTTESTTEST");
	}

}
