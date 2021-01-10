This is a very personal application that serves two purposes.

First, it supports my habit of keeping details of my daily workouts in text log files. I've been doing this in a fairly standard format since mid-2004, entering a line of text for each day. This automates the most common tasks I need to do: creating a new blank log file for each year, and then adding up my running mileage over some period of time.

Second, this has been a chance for me, as an aspiring programmer, to get hands-on practice writing clean Java code and using libraries for IO, regular expressions, time, and graphical user interfaces. I began development using Java 7 and Swing. I later re-wrote everything from scratch to be cleaner code and use the Date and Time API in Java 8. Finally, I refactored the application again to use JavaFX.

The name of each file (if you want to add up mileage) must be "YYYY.txt" The format for each line must be of the following...

Mmm DD: xx.xx mile(s) followed by arbitrary text about the workout or other things of interest
