module com.example.chartassign1javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.chartassign1javafx to javafx.fxml;
    exports com.example.chartassign1javafx;
}