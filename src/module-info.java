module LynkXapp {
	requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
	requires java.desktop;
	requires javafx.graphics;
    
    // Opens the 'application' package to javafx.fxml and javafx.graphics
    opens application to javafx.fxml, javafx.graphics;
}
