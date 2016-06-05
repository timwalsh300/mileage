This is a very personal application that serves two purposes.

First, it supports my habit of keeping details of my daily workouts in text log-files. I've been doing this in a fairly standard format since 2004, entering a line of text for each day. This automates the most common tasks I need to do: creating a new blank log-file for each 
year, and then adding up my running mileage over some period of time.

Second, this has been a chance for me, as an aspiring programmer, to get hands-on practice writing clean Java code and using libraries for IO, regular expressions, time, and 
user interfaces. I began development in Windows using Java SE 7 and Swing. I later re-wrote everything in Arch Linux from scratch to be cleaner code and use the Date and Time 
API in Java 8. Finally, I refactored the application again to use JavaFX.

After opening the application, the first step is to select the working directory which has (or should have) the log-files. Then you can add new log-files or add up mileage. 
The name of each file (if you want to add up mileage) must be "YYYY.txt" The format for each line must be of the following...

Mmm DD: xx.xx mile(s)
