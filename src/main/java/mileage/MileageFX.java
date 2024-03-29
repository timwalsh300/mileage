/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mileage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author timwalsh300
 */
public class MileageFX extends Application {

    public static Path workingDirectory;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Mileage");
        Parent parent = (Parent) FXMLLoader.load(getClass().getResource("mileage_doc.fxml"));
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("mileagefxcss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        workingDirectory = Paths.get(System.getProperty("user.dir"));
        launch(args);
    }

}
