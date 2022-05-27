module mileage {
    requires javafx.controls;
    requires javafx.fxml;
    opens mileage to javafx.fxml;
    exports mileage;
}
