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

/**
 *
 * @author timwalsh300
 */
public class MileageFXController implements Initializable {

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
    private void chooseWorkingDirectory(ActionEvent event) {
        System.out.println("Launching file (directory) chooser...");
    }
    
    @FXML
    private void buildNewLog(ActionEvent event) {
        System.out.println("Launching log builder...");
    }
    
    @FXML
    private void exitApplication(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    private void addMileage(ActionEvent event) {
        System.out.println("Adding it up...");
        if (fromYear.getValue() != null && toMonth.getValue() != null
                && fromYear.getValue() != null && toYear.getValue() != null
                && fromDay.getValue() != null && toDay.getValue() != null) {

            try {
                MileageAdder adder = new MileageAdder(
                        Paths.get("temp").toAbsolutePath().getParent().toString(),
                        fromDay.getValue().toString(),
                        fromMonth.getValue().toString(),
                        fromYear.getValue().toString(),
                        toDay.getValue().toString(),
                        toMonth.getValue().toString(),
                        toYear.getValue().toString()
                );
                System.out.println("Total mileage is " + adder.getTotalMileage());
                System.out.println("Weekly mileage is " + adder.getWeeklyMileage());
            
            } catch (InvalidDateRangeException e1) {
                System.out.println(e1.getMessage());
                
            } catch (IOException e2) {
                System.out.println(e2.getMessage());
                
            } finally {
                
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO
    }

}
