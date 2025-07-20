package app;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private Button btnSearch;

    @FXML
    private ComboBox<String> cmBoxSearchBy;

    @FXML
    private TextArea resultTxt;

    @FXML
    private TextField txtSearch;

    private final DB2Test db = new DB2Test();
    private String lastSearchType = null;
    private String lastInput = null;

    @FXML
    public void initialize() {
        cmBoxSearchBy.setItems(FXCollections.observableArrayList("ID", "Model", "Manufacturer"));
        cmBoxSearchBy.setValue("ID");
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchType = cmBoxSearchBy.getValue();
        String input = txtSearch.getText().trim();

        if (input.isEmpty()) {
            resultTxt.setText("Моля, въведете стойност за търсене.");
            return;
        }

        String query = switch (searchType) {
            case "ID" -> "SELECT * FROM COFFEES WHERE CODE = '" + input + "'";
            case "Model" -> "SELECT * FROM COFFEES WHERE NAME LIKE '%" + input + "%'";
            case "Manufacturer" -> (
                    "SELECT C.* FROM COFFEES C " +
                            "JOIN MANUFACTURER M ON C.MANUFACTURER_CODE = M.CODE " +
                            "WHERE M.NAME LIKE '%" + input + "%'"
            );
            default -> null;
        };

        if (query == null) {
            resultTxt.setText("Невалиден критерий за търсене.");
            return;
        }

        lastSearchType = searchType;
        lastInput = input;

        db.openConnection();
        String rs = db.selectToString(query, 9);
        db.closeConnection();

        if (rs == null || rs.isEmpty()) {
            resultTxt.setText("Няма намерени елементи.");
        } else {
            resultTxt.setText(rs);
        }
    }
    @FXML
    void btnAddOnAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addCoffee.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Добави ново кафе");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRemoveMatchingOnAction(ActionEvent event) {
        if (lastSearchType == null || lastInput == null) {
            resultTxt.setText("Няма предходно търсене, което може да се изтрие.");
            return;
        }

        String deleteQuery = switch (lastSearchType) {
            case "ID" -> "DELETE FROM COFFEES WHERE CODE = '" + lastInput + "'";
            case "Model" -> "DELETE FROM COFFEES WHERE NAME LIKE '%" + lastInput + "%'";
            case "Manufacturer" -> (
                    "DELETE FROM COFFEES WHERE MANUFACTURER_CODE IN (" +
                            "SELECT CODE FROM MANUFACTURER WHERE NAME LIKE '%" + lastInput + "%')"
            );
            default -> null;
        };

        if (deleteQuery == null) {
            resultTxt.setText("Неуспешно генериране на заявка за изтриване.");
            return;
        }

        db.openConnection();
        db.delete(deleteQuery); // използваме вече съществуващия метод
        db.closeConnection();

        resultTxt.setText("Успешно изтрити резултати от базата.");
        lastInput = null;
        lastSearchType = null;
    }

    @FXML
    void cmBoxSearchByOnAction(ActionEvent event) {

    }
    @FXML
    void btnExitOnAction(ActionEvent event) {
        System.exit(0); // или Platform.exit(); ако ползваш javafx.application.Platform
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        resultTxt.clear();
    }
}
