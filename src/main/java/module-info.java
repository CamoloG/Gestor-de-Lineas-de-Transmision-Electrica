module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    opens modelo to javafx.base;
    opens controlador to javafx.fxml;
    exports controlador;
}
