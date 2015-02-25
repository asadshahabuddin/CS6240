>>INSTRUCTIONS
(1) Clone the project as is.

(2) Compile it using the following command:
    'mvn clean compile assembly:single'
    
    This command creates a binary from my source code and makes the JAR
    executable.

(3) From the project root directory, run the application as follows:
    'java -Xmx4g -jar target/<name of jar>.jar <training file> <predict file> <output file> <check file>'
    EXAMPLE: java -Xmx4g -jar
             target/FlightDelayPredictionApp-0.0.1-SNAPSHOT-jar-with-dependencies.jar
             /home/leo/Documents/Java/MapReduce_A3/input/all.csv
             /home/leo/Documents/Java/MapReduce_A3/input/predict.csv
             output
             /home/leo/Documents/Java/MapReduce_A3/input/check.csv

    (a) -Xmx4g is used to set your Java heap space to 4GB. If your PC has 8GB
        memory, you would use -Xmx8g instead.
    (b) all.csv is my training file.
    (c) predict.csv is the file used for predictive analysis.
    (d) output is the name of my output file which will written to the folder
        from where this application is executed. I ran it from project root
        and that's where the file named 'output' was written to my file system.
        Adjust it to your heart's content.
    (e) check.csv is the file against which prediction accuracy is compared.

>>TODO
(1) We need to convert my single-file program to a MapReduce application.

(2) Documentation is at 0%.
