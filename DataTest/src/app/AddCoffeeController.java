package app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddCoffeeController {

    @FXML
    private TextField txtCode, txtName, txtPrice;

    @FXML
    private ComboBox<String> cmbType, cmbManufacturer;

    private final DB2Test db = new DB2Test();

    @FXML
    public void initialize() {
        db.openConnection();


        // Зареждаме всички типове кафе и производители
        cmbType.setItems(FXCollections.observableArrayList(
                db.selectToList("SELECT CODE FROM TYPES_OF_COFFEE")
        ));
        cmbManufacturer.setItems(FXCollections.observableArrayList(
                db.selectToList("SELECT CODE FROM MANUFACTURER")
        ));

        db.closeConnection();
    }

    @FXML
    void btnConfirmOnAction() {
        String code = txtCode.getText().trim();
        String name = txtName.getText().trim();
        String price = txtPrice.getText().trim();
        String type = cmbType.getValue();
        String manu = cmbManufacturer.getValue();

        if (code.isEmpty() || name.isEmpty() || price.isEmpty() || type == null || manu == null) {
            showAlert("Моля, попълнете всички полета.");
            return;
        }

        String insert = String.format(
                "INSERT INTO COFFEES (CODE, NAME, PRICE, AVAILABILITY, DESCRIPTION, UNITS_PER_BOX, WEIGHT, COFFEE_TYPE_CODE, MANUFACTURER_CODE) " +
                        "VALUES ('%s', '%s', %s, 1, '', 1, 0.25, '%s', '%s')",
                code, name, price, type, manu
        );

        db.openConnection();
        db.insert(insert);
        db.closeConnection();

        showAlert("Кафето беше добавено успешно.");
        ((Stage) txtCode.getScene().getWindow()).close(); // Затвори прозореца
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
