module com.ou.oubus {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires java.logging;
    requires spring.security.crypto;
    requires java.base;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.apache.pdfbox;
    requires javafx.swing;
    opens com.ou.oubus to javafx.fxml;
    opens com.ou.pojo to javafx.base;
    exports com.ou.oubus;
}
