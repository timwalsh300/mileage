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

public class LogBuilder {

    private final Path newFile;
    private final String firstDayOfTheYearString;
    private final boolean leapYear, mustDeleteFile; 
    
    public LogBuilder(Path nf, String fdotys, boolean ly, boolean mdf) {
        newFile = nf;
        firstDayOfTheYearString = fdotys;
        leapYear = ly;
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
        
        // set a counter for days of the week based on the first day of the year
        // 1 if Sunday, 2 if Monday, etc.
        int dayOfTheWeek = 0;
        while (dayOfTheWeek == 0) {
            switch (firstDayOfTheYearString) {
                    case "Sunday" : dayOfTheWeek = 1;
                                    break;
                    case "Monday" : dayOfTheWeek = 2;
                                    break;
                    case "Tuesday" : dayOfTheWeek = 3;
                                    break;
                    case "Wednesday" : dayOfTheWeek = 4;
                                    break;
                    case "Thursday" : dayOfTheWeek = 5;
                                    break;
                    case "Friday" : dayOfTheWeek = 6;
                                    break;
                    case "Saturday" : dayOfTheWeek = 7;
                                    break;
            }
        }    
        System.out.println("Year begins on day number " + dayOfTheWeek + " of the week.");
        
        // establish an array of 12 values, one for each month, with the
        // number of days in each month, based on whether or not this is a
        // leap year for February
        int[] calendar;
        if (leapYear) {
            System.out.println("Building leap year.");
            calendar = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        }
        else {
            System.out.println("Building non-leap year.");
            calendar = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        }
        
        // set another counter to iterate through the days of each month; 
        // increment this counter until reaching the value for the first month,
        // printing that month and the date (counter value) each time; also 
        // increment the day of the week counter each time, adding a double-
        // space after reaching 7, and then resetting back to 1; upon reaching
        // the value for each month, reset the day-of-the-month counter back
        // to 1 and begin printing the next month and dates
        String currentMonth = " ";
        int monthCounter, currentDayOfTheMonth = -1;
        Charset charset = Charset.forName("US-ASCII");
        BufferedWriter writer= null;
        try {
            writer = Files.newBufferedWriter(newFile, charset);
        }
        catch (IOException e3){
            System.out.println("Error trying to open file for writing: " + e3);
        }
        for (monthCounter = 1; monthCounter <= 12; monthCounter++) {
            switch (monthCounter) {
                case 1 : currentMonth = "Jan ";
                    break;
                case 2 : currentMonth = "Feb ";
                    break;
                case 3 : currentMonth = "Mar ";
                    break;
                case 4 : currentMonth = "Apr ";
                    break;
                case 5 : currentMonth = "May ";
                    break;
                case 6 : currentMonth = "Jun ";
                    break;
                case 7 : currentMonth = "Jul ";
                    break;
                case 8 : currentMonth = "Aug ";
                    break;
                case 9 : currentMonth = "Sep ";
                    break;
                case 10 : currentMonth = "Oct ";
                    break;
                case 11 : currentMonth = "Nov ";
                    break;
                case 12 : currentMonth = "Dec ";
                    break;    
            }
            for (currentDayOfTheMonth = 1; currentDayOfTheMonth <= calendar[monthCounter - 1]; currentDayOfTheMonth++) {
                //append the file with "currentMonth currentDayOfTheMonth: "
                try {
                    writer.write(currentMonth);
                    if (currentDayOfTheMonth < 10)
                        writer.write("0");
                    writer.write(currentDayOfTheMonth + ": ");
                    writer.newLine();
                }
                catch (IOException e4){
                    System.out.println("Error trying to write to file: " + e4);
                }
                dayOfTheWeek++;
       
                if (dayOfTheWeek > 7) {
                    try {
                        writer.newLine();// put in the double-space
                    }
                    catch (IOException e5) {
                        System.out.println("Error trying to write to file: " + e5);
                    }
                    dayOfTheWeek = 1; // reset the counter
                }
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
