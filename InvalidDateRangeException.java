/*
 * This exception is thrown by the MileageAdder class if the given date
 * range is invalid, such as "to" being earlier than "from."
 */
package mileage;

public class InvalidDateRangeException extends Exception {

    public InvalidDateRangeException() {
        
    }
    
    public InvalidDateRangeException(String msg) {
        super(msg);
    }
}
