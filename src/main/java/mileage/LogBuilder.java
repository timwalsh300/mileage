/*
 * Instances of this class provide functionality to build a new workout log file
 * given a filename, first day of the year, and whether or not it
 * is a leap year.
 */
package mileage;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class LogBuilder {

    private final Path newFile;
    private final int yearToBuild;
    private final boolean mustDeleteFile;
    DateTimeFormatter logFormat = DateTimeFormatter.ofPattern("MMM dd: ");
    
    public LogBuilder(Path nf, String yr, boolean mdf) {
        newFile = nf;
        yearToBuild = Integer.parseInt(yr);
        mustDeleteFile = mdf;
    }
    
    public boolean wasBuildSuccessful() {
        return Files.exists(newFile);
    }
    
    public void buildIt() {
        
        if (mustDeleteFile) {
            try {
                System.out.println("Deleting old file " + newFile.getFileName() + "...");
                Files.delete(newFile);
            }
            catch (IOException e1) {
                System.out.println("Error trying to delete file: " + e1);
            }
        }
        
        // create the new file
        try {
            System.out.println("Creating new file "+ newFile.getFileName() +"...");
            Files.createFile(newFile);
        }
        catch (IOException e2) {
            System.out.println("Error trying to create file: " + e2);
        }
        
        LocalDate dateToWrite = LocalDate.of(yearToBuild, Month.JANUARY, 1);
        System.out.println("Year begins on a " + dateToWrite.getDayOfWeek().toString());
        
        Charset charset = Charset.forName("US-ASCII");
        BufferedWriter writer= null;
        try {
            writer = Files.newBufferedWriter(newFile, charset);
        }
        catch (IOException e3){
            System.out.println("Error trying to open file for writing: " + e3);
        }

        while (dateToWrite.getYear() == yearToBuild) {
            //append the file with "Month DayOfTheMonth: "
            try {
                writer.write(dateToWrite.format(logFormat));
                writer.newLine();
                if (dateToWrite.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    writer.newLine();
                }
                dateToWrite = dateToWrite.plusDays(1);
            }
            catch (IOException e4){
                System.out.println("Error trying to write to file: " + e4);
            }
        }
        
        // close the text file, tell the user that the work is done
        try {
            writer.flush();
            writer.close();
        }
        catch (IOException e6) {
            System.out.println("Error trying to flush/close file: " + e6);
        }
        
        System.out.println("New workout log has been built successfully.");
        
    }
}
