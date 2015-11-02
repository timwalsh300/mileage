/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mileage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.nio.file.Paths;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author timwalsh300
 */
public class MileageFXController implements Initializable {

    @FXML
    private Label header;
    @FXML
    private ComboBox fromYear;
    @FXML
    private ComboBox fromMonth;
    @FXML
    private ComboBox fromDay;
    @FXML
    private ComboBox toYear;
    @FXML
    private ComboBox toMonth;
    @FXML
    private ComboBox toDay;
    @FXML
    private Button closeButton;
    @FXML
    private TextField fileNameField;
    @FXML
    private TextField yearToBuildField;
    @FXML
    private Label buildCompleteLabel;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;
    @FXML
    private Canvas drawArea;

    private DirectoryChooser directoryChooser;
    private Path workingDirectory;
    private Path newFile;
    private GraphicsContext gc;

    @FXML
    private void chooseWorkingDirectory(ActionEvent event) {
        System.out.println("Launching directory chooser.");
        File temp = directoryChooser.showDialog(header.getScene().getWindow());
        if (temp != null) {
            workingDirectory = Paths.get(temp.getAbsolutePath());
        }
        System.out.println("Working directory is now " + workingDirectory.toString());
    }

    @FXML
    private void openLogBuilder(ActionEvent event) throws IOException {
        System.out.println("Launching log builder.");
        Stage logBuilderStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logbuilder_doc.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("mileagefxcss.css").toExternalForm());
        logBuilderStage.setScene(scene);
        logBuilderStage.setTitle("Log Builder");
        logBuilderStage.initModality(Modality.APPLICATION_MODAL);
        logBuilderStage.initOwner(header.getScene().getWindow());
        buildCompleteLabel.setVisible(false);
        fileNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            buildCompleteLabel.setVisible(false);
        });
        logBuilderStage.showAndWait();
    }

    @FXML
    private void closeLogBuilder(ActionEvent event) {
        System.out.println("Closing log builder.");
        closeButton.getScene().getWindow().hide();
    }

    @FXML
    private void exitApplication(ActionEvent event) {
        System.out.println("Exiting the application.");
        System.exit(0);
    }

    @FXML
    private void buildNewLog(ActionEvent event) throws IOException {
        System.out.println("Starting to build the new log.");
        if (!fileNameField.getText().isEmpty() && !yearToBuildField.getText().isEmpty()) {
            //capture selections/text from other fields in the GUI
            String filenameString = fileNameField.getText() + ".txt";

            //check if filename exists, and prompt for overwrite if it does
            newFile = Paths.get(workingDirectory.toString() + File.separator + filenameString);
            if (Files.exists(newFile)) {
                System.out.println("File named " + filenameString + " already exists in the current working directory.");
                // pop-up box to notify and ask for overwrite permission
                //wait for next action from pop-up window
                Stage confirmOverwriteStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("overwrite_doc.fxml"));
                loader.setController(this);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("mileagefxcss.css").toExternalForm());
                confirmOverwriteStage.setScene(scene);
                confirmOverwriteStage.setTitle("Confirm Overwrite");
                confirmOverwriteStage.initModality(Modality.APPLICATION_MODAL);
                confirmOverwriteStage.initOwner(closeButton.getScene().getWindow());
                confirmOverwriteStage.showAndWait();
            } else {
                System.out.println("Building " + newFile.toString());
                //instantiate LogBuilder with final argument of "false"
                //and call buildIt()
                LogBuilder logBuilder = new LogBuilder(newFile, yearToBuildField.getText(), false);
                logBuilder.buildIt();
                if (logBuilder.wasBuildSuccessful()) {
                    System.out.println("Success");
                    buildCompleteLabel.setText("Success.");
                    buildCompleteLabel.setVisible(true);
                } else {
                    System.out.println("Fail.");
                    buildCompleteLabel.setText("Fail.");
                    buildCompleteLabel.setVisible(true);
                }
            }
        }
    }

    @FXML
    private void overwriteAction(ActionEvent event) {

        if (event.getSource() == yesButton) {
            //if yes, instantiate a LogBuilder object with final argument of "true"
            //and call buildIt();
            System.out.println("Overwriting the file to build " + newFile.toString());
            LogBuilder logBuilder = new LogBuilder(newFile, yearToBuildField.getText(), true);
            logBuilder.buildIt();
            if (logBuilder.wasBuildSuccessful()) {
                System.out.println("Success");
                buildCompleteLabel.setText("Success.");
                buildCompleteLabel.setVisible(true);
            } else {
                System.out.println("Fail.");
                buildCompleteLabel.setText("Fail.");
                buildCompleteLabel.setVisible(true);
            }
            yesButton.getScene().getWindow().hide();
        } else if (event.getSource() == noButton) {
            //if no, close the pop-up window and
            //highlight the fileNameField in pink until the user changes it
            System.out.println("Canceling the build.");
            noButton.getScene().getWindow().hide();
        }
    }

    @FXML
    private void addMileage(ActionEvent event) {
        System.out.println("Preparing to add.");
        gc.clearRect(0, 0, 600, 100);
        if (fromYear.getValue() != null && toMonth.getValue() != null
                && fromYear.getValue() != null && toYear.getValue() != null
                && fromDay.getValue() != null && toDay.getValue() != null) {

            try {
                MileageAdder adder = new MileageAdder(
                        workingDirectory.toString(),
                        fromDay.getValue().toString(),
                        fromMonth.getValue().toString(),
                        fromYear.getValue().toString(),
                        toDay.getValue().toString(),
                        toMonth.getValue().toString(),
                        toYear.getValue().toString()
                );
                System.out.println("Total mileage is " + adder.getTotalMileage());
                System.out.println("Weekly mileage is " + adder.getWeeklyMileage());
                
                //gc.setFill(Color.BLUE);
                //gc.fillRect(10, 10, 580, 50);
                if (adder.getWeeklyMileage() > 20) {
                    gc.setFill(Color.BLUE);
                }
                else if (adder.getWeeklyMileage() > 15) {
                    gc.setFill(Color.GREEN);
                    
                }
                else if (adder.getWeeklyMileage() > 10) {
                    gc.setFill(Color.YELLOW);
                    
                }
                else if (adder.getWeeklyMileage() > 5) {
                    gc.setFill(Color.ORANGE);
                    
                }
                else {
                    gc.setFill(Color.RED);  
                }
                gc.fillRect(10, 10, (adder.getWeeklyMileage() * 580) / 25, 25);
                gc.setFill(Color.WHITESMOKE);
                gc.fillText("Average weekly mileage was " + (int)adder.getWeeklyMileage() + " miles.", 30, 60);
                gc.fillText("Total mileage was " + (int)adder.getTotalMileage() + " miles.", 30, 80);

            } catch (InvalidDateRangeException | IOException e) {
                System.out.println(e.getMessage());

            } finally {

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        workingDirectory = Paths.get("temp").toAbsolutePath().getParent();
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(workingDirectory.toFile());
        directoryChooser.setTitle("Choose your working directory.");
        gc = drawArea.getGraphicsContext2D();
    }

}
