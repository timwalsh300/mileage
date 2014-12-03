/*
 * Instances of this class take a date range from MileageUI class, read the
 * through the log files between the specified dates, and perform the analysis.
 */
package mileage;

import java.io.*;
import java.util.Scanner;
import java.util.regex.*;

public class MileageAdder {
    
    private String fromDay, toDay;
    private final String fromMonth, fromYear, toMonth, toYear;
    private double daysInRange = -1, totalMiles = -1;
    private final Pattern datePattern = Pattern.compile("\\d\\d:");
    private final Pattern monthInitialCheck = Pattern.compile("\\D\\D\\D");
    private Pattern previousTokenPattern;
    private final String monthStringSequence = "JanFebMarAprMayJunJulAugSepOctNovDec";
    
    MileageAdder(String fd, String fm, String fy,
            String td, String tm, String ty) {
        
        int temp;
        
        fromYear = fy;
        fromMonth = fm.substring(0, 3);
        fromDay = fd;
        temp = getMonthDays(fromMonth, fromYear);
        if (Integer.valueOf(fromDay) > temp) { // assume that the user wants to select the final day of the month
            fromDay = Integer.toString(temp); // this will always be two digits, 28-31, so no need to pad it with 0
        }
        
        toYear = ty;
        toMonth = tm.substring(0, 3);
        toDay = td;
        temp = getMonthDays(toMonth, toYear);
        if (Integer.valueOf(toDay) > temp) { // assume that the user wants to select the final day of the month
            toDay = Integer.toString(temp); // this will always be two digits, 28-31, so no need to pad it with 0
        }
    }
    
    public double getTotalMileage() throws InvalidDateRangeException, IOException  {
        if (totalMiles == -1 || daysInRange == -1) { // haven't run through the logs yet to add this up
            doTheAddition();
        }
        return totalMiles;
    }
        
    public double getWeeklyMileage() throws InvalidDateRangeException, IOException {
        if (totalMiles == -1 || daysInRange == -1) { // haven't run through the logs yet to add this up
            doTheAddition();
        }
        return totalMiles / (daysInRange / 7);
    }
    
    private void doTheAddition() throws InvalidDateRangeException, IOException {
        validateDateRange(); // throws InvalidDateRangeException if necessary
        
        totalMiles = 0;
        daysInRange = 0;
        Scanner s = null;
        String previousToken, thisToken = " ";
        boolean enteredDateRange = false, reachedFinalDayInRange = false, passedFinalDay = false;
        
        String[] yearsInRange = new String[Integer.valueOf(toYear) - Integer.valueOf(fromYear) + 1];
        for (int i = 0; i < yearsInRange.length; i++) {
            yearsInRange[i] = Integer.toString(Integer.valueOf(fromYear) + i);
        }
        
        try {
            for (String y: yearsInRange) {
                s = new Scanner(new BufferedReader(new FileReader(MileageUI.workingDirectory.toString() + 
                        File.separator + y + ".txt")));
                System.out.println("Opening file: "
                        + MileageUI.workingDirectory.toString() + File.separator + y + ".txt");
                while (s.hasNext()) {
                    previousToken = thisToken;
                    thisToken = s.next();
                    
                    // check to see if scanner has reached the fromDate
                    if (!enteredDateRange &&
                            thisToken.equals(fromDay + ":") &&
                            previousToken.equals(fromMonth) &&
                            y.equals(fromYear)) {
                        enteredDateRange = true; // start incrementing miles, days
                    }
                    
                    // look for instances like "5.3 miles" or "1 mile,"
                    // to add up miles...
                    if (enteredDateRange &&
                            (thisToken.equals("mile") ||
                            thisToken.equals("miles") ||
                            thisToken.equals("mile,") ||
                            thisToken.equals("miles,"))) {
                        try {
                            totalMiles += Double.valueOf(previousToken);
                        }
                        catch (NumberFormatException e) {
                            System.out.println("Cannot get Double.valueOf() " + e.getMessage());
                        }
                    }
                    
                    // check to see if scanner has reached the toDate
                    if (thisToken.equals(toDay + ":") &&
                            previousToken.equals(toMonth) &&
                            y.equals(toYear)) {
                        reachedFinalDayInRange = true;
                    }
                    
                    // look for instances like "Mar 09:" to increment days
                    boolean lookingAtDate = false;
                    if (datePattern.matcher(thisToken).matches() && 
                            monthInitialCheck.matcher(previousToken).matches()) {
                        previousTokenPattern = Pattern.compile(previousToken);
                        lookingAtDate = previousTokenPattern.matcher(monthStringSequence).find();
                    }
                    
                    if (enteredDateRange && lookingAtDate){
                        if (reachedFinalDayInRange) {
                            // upon reaching the day after the final day, scanner can
                            // stop... if the final day is the last day of a year, it will stop
                            // without reaching this statement because the for-loop will be at 
                            // the end of the yearsInRange array...
                            // must skip this step exactly 1 time to capture miles on final day
                            if (passedFinalDay) {
                                return; // go the the finally block
                            }
                            else {
                                passedFinalDay = true;
                            }
                        }
                        daysInRange++;
                    }
                }
            }
        }
        finally {
            if (s != null) {
                s.close();
            }
        }
    }
    
    private void validateDateRange() throws InvalidDateRangeException {
        int fromDate, toDate;
        fromDate = Integer.valueOf(fromYear + getMonthDigitString(fromMonth) + fromDay);
        toDate = Integer.valueOf(toYear + getMonthDigitString(toMonth) + toDay);
        if (fromDate > toDate) {
            String msg = "Invalid date range... from-date " + fromDate + " is later than to-date " + toDate;
            throw new InvalidDateRangeException(msg);
        }
    }
    
    private String getMonthDigitString(String month) {
        String monthDigitString = " ";
        switch (month)
        {
            case "Jan": monthDigitString = "01";
                         break;
            case "Feb":  monthDigitString = "02";
                         break;
            case "Mar":  monthDigitString = "03";
                         break;
            case "Apr":  monthDigitString = "04";
                         break;
            case "May":  monthDigitString = "05";
                         break;
            case "Jun":  monthDigitString = "06";
                         break;
            case "Jul":  monthDigitString = "07";
                         break;
            case "Aug":  monthDigitString = "08";
                         break;
            case "Sep":  monthDigitString = "09";
                         break;
            case "Oct":  monthDigitString = "10";
                         break;
            case "Nov":  monthDigitString = "11";
                         break;
            case "Dec":  monthDigitString = "12";
                         break;
        }
        return monthDigitString;
    }
    
    private int getMonthDays(String month, String year) {
        int numDays = -1;
        switch (month)
        {
            case "Jan": case "Mar": case "May":
            case "Jul": case "Aug": case "Oct":
            case "Dec":
                numDays = 31;
                break;
            case "Apr": case "Jun":
            case "Sep": case "Nov":
                numDays = 30;
                break;
            case "Feb":
                if (((Integer.valueOf(year) % 4 == 0) && 
                     !(Integer.valueOf(year) % 100 == 0))
                     || (Integer.valueOf(year) % 400 == 0))
                    numDays = 29;
                else
                    numDays = 28;
                break;
            default:
                System.out.println("Invalid month.");
                break;
        }
        return numDays;
    }
    
}
