module com.example.projet_formation {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;


    opens com.example.projet_formation to javafx.fxml;
    exports com.example.projet_formation;
    exports com.example.projet_formation.Modele;
    opens com.example.projet_formation.Modele to javafx.fxml;
}