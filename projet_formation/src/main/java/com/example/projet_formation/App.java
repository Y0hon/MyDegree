package com.example.projet_formation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static Stage stage1;
    private static String repertoireImages = System.getProperty("user.dir")+"/projet_formation/src/main/resources/images/appicon.png";//Recupère le répertoire pour les csv

    @Override
    public void start(Stage stage) throws IOException {
        stage1 = stage;
        setRoot("menu",640,480);
        /*scene = new Scene(loadFXML("menu"), 640, 480);
        stage1.setScene(scene);
        stage1.centerOnScreen();
        stage1.show();*/
    }

    static void setRoot(String fxml, double x, double y) throws IOException {
        scene = new Scene(loadFXML(fxml), x, y);
        Image icon = new Image((repertoireImages));
        stage1.getIcons().add(icon);
        stage1.setScene(scene);
        stage1.centerOnScreen();
        stage1.show();
    }

    static void setRoot2(String fxml, double x, double y, String recup) throws IOException {
        //On récupère le FXMLLoader à partir du fxml
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        //On modifie le premier paramètre de loadFXML(fxml) à fxmlLoader.load()
        scene = new Scene(fxmlLoader.load(), x, y);

        //Pareil qu'avant
        stage1.setScene(scene);
        stage1.centerOnScreen();

        switch (fxml){
            case "parcours":
                //Récupère le controller choisi (MenuController)
                ParcoursController controllerParcours = fxmlLoader.getController();
                //Utilise la méthode test() présent dans le MenuControler avec un string en param
                controllerParcours.recupDonnees(recup);
                break;

            case "ues":
                //Récupère le controller choisi (MenuController)
                UEsController controllerUEs = fxmlLoader.getController();
                //Utilise la méthode test() présent dans le MenuControler avec un string en param
                controllerUEs.recupDonnees(recup);
                break;
        }


        //Pareil qu'avant
        stage1.show();

        //Plus qu'à mettre la methode test() dans le MenuControler (et recup le parameter dans un attribut)
        //Remplacer "ok" par un nouveau parameter (ex: l'id d'un UE)
        //Appeller la méthode dans le premier controller (ex dans EtudiantControler : setRoot2("UE", x, y, idUE))
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}