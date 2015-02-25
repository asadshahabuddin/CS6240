/*
    Author: Asad Shahabuddin
    File: App.java
    Details: Flight Delay Prediction Application.
    Email ID: asad808@ccs.neu.edu
*/

package com.prediction.flightdelay.FlightDelayPredictionApp;

/* Import list */
import java.io.File;
import java.util.List;
import java.util.Calendar;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.GregorianCalendar;
import java.io.FileNotFoundException;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.evaluation.Auc;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class App
{
    private static ArrayList<Calendar> holidayList;
    private OnlineLogisticRegression olr;
    private Observation observation;

    static
    {
        /* List of holidays from 1993 through 2000 */
        holidayList = new ArrayList<Calendar>();

        /* 1993 */
        holidayList.add(new GregorianCalendar(1993, 1,  1));
    	holidayList.add(new GregorianCalendar(1993, 1,  18));
    	holidayList.add(new GregorianCalendar(1993, 2,  15));
    	holidayList.add(new GregorianCalendar(1993, 5,  31));
    	holidayList.add(new GregorianCalendar(1993, 7,  4));
    	holidayList.add(new GregorianCalendar(1993, 7,  5));
    	holidayList.add(new GregorianCalendar(1993, 9,  6));
    	holidayList.add(new GregorianCalendar(1993, 10, 11));
    	holidayList.add(new GregorianCalendar(1993, 11, 11));
    	holidayList.add(new GregorianCalendar(1993, 11, 25));
    	holidayList.add(new GregorianCalendar(1993, 12, 24));
    	holidayList.add(new GregorianCalendar(1993, 12, 25));
    	holidayList.add(new GregorianCalendar(1993, 12, 31));
    	
    	/* 1994 */
    	holidayList.add(new GregorianCalendar(1994, 1,  1));
    	holidayList.add(new GregorianCalendar(1994, 1,  17));
    	holidayList.add(new GregorianCalendar(1994, 2,  21));
    	holidayList.add(new GregorianCalendar(1994, 5,  30));
    	holidayList.add(new GregorianCalendar(1994, 7,  4));
    	holidayList.add(new GregorianCalendar(1994, 9,  5));
    	holidayList.add(new GregorianCalendar(1994, 10, 10));
    	holidayList.add(new GregorianCalendar(1994, 11, 11));
    	holidayList.add(new GregorianCalendar(1994, 11, 24));
    	holidayList.add(new GregorianCalendar(1994, 12, 25));
    	holidayList.add(new GregorianCalendar(1994, 12, 26));
    	
    	/* 1995 */
    	holidayList.add(new GregorianCalendar(1995, 1,  1));
    	holidayList.add(new GregorianCalendar(1995, 1,  2));
    	holidayList.add(new GregorianCalendar(1995, 1,  16));
    	holidayList.add(new GregorianCalendar(1995, 2,  20));
    	holidayList.add(new GregorianCalendar(1995, 5,  29));
    	holidayList.add(new GregorianCalendar(1995, 7,  4));
    	holidayList.add(new GregorianCalendar(1995, 9,  4));
    	holidayList.add(new GregorianCalendar(1995, 10, 9));
    	holidayList.add(new GregorianCalendar(1995, 11, 10));
    	holidayList.add(new GregorianCalendar(1995, 11, 11));
    	holidayList.add(new GregorianCalendar(1995, 11, 23));
    	holidayList.add(new GregorianCalendar(1995, 12, 25));
    	
    	/* 1996 */
    	holidayList.add(new GregorianCalendar(1996, 1,  1));
    	holidayList.add(new GregorianCalendar(1996, 1,  15));
    	holidayList.add(new GregorianCalendar(1996, 2,  19));
    	holidayList.add(new GregorianCalendar(1996, 5,  27));
    	holidayList.add(new GregorianCalendar(1996, 7,  4));
    	holidayList.add(new GregorianCalendar(1996, 9,  2));
    	holidayList.add(new GregorianCalendar(1996, 10, 14));
    	holidayList.add(new GregorianCalendar(1996, 11, 11));
    	holidayList.add(new GregorianCalendar(1996, 11, 28));
    	holidayList.add(new GregorianCalendar(1996, 12, 25));
    	
    	/* 1997 */
    	holidayList.add(new GregorianCalendar(1997, 1,  1));
    	holidayList.add(new GregorianCalendar(1997, 1,  20));
    	holidayList.add(new GregorianCalendar(1997, 2,  17));
    	holidayList.add(new GregorianCalendar(1997, 5,  26));
    	holidayList.add(new GregorianCalendar(1997, 7,  4));
    	holidayList.add(new GregorianCalendar(1997, 9,  1));
    	holidayList.add(new GregorianCalendar(1997, 10, 13));
    	holidayList.add(new GregorianCalendar(1997, 11, 11));
    	holidayList.add(new GregorianCalendar(1997, 11, 27));
    	holidayList.add(new GregorianCalendar(1997, 12, 25));
    	
    	/* 1998 */
    	holidayList.add(new GregorianCalendar(1998, 1,  1));
    	holidayList.add(new GregorianCalendar(1998, 1,  19));
    	holidayList.add(new GregorianCalendar(1998, 2,  16));
    	holidayList.add(new GregorianCalendar(1998, 5,  25));
    	holidayList.add(new GregorianCalendar(1998, 7,  3));
    	holidayList.add(new GregorianCalendar(1998, 7,  4));
    	holidayList.add(new GregorianCalendar(1998, 9,  7));
    	holidayList.add(new GregorianCalendar(1998, 10, 12));
    	holidayList.add(new GregorianCalendar(1998, 11, 11));
    	holidayList.add(new GregorianCalendar(1998, 11, 26));
    	holidayList.add(new GregorianCalendar(1998, 12, 25));
    	
    	/* 1999 */
    	holidayList.add(new GregorianCalendar(1999, 1,  1));
    	holidayList.add(new GregorianCalendar(1999, 1,  18));
    	holidayList.add(new GregorianCalendar(1999, 2,  15));
    	holidayList.add(new GregorianCalendar(1999, 5,  31));
    	holidayList.add(new GregorianCalendar(1999, 7,  4));
    	holidayList.add(new GregorianCalendar(1999, 7,  5));
    	holidayList.add(new GregorianCalendar(1999, 9,  6));
    	holidayList.add(new GregorianCalendar(1999, 10, 11));
    	holidayList.add(new GregorianCalendar(1999, 11, 11));
    	holidayList.add(new GregorianCalendar(1999, 11, 25));
    	holidayList.add(new GregorianCalendar(1999, 12, 24));
    	holidayList.add(new GregorianCalendar(1999, 12, 25));
    	holidayList.add(new GregorianCalendar(1999, 12, 31));
    	
    	/* 2000 */
    	holidayList.add(new GregorianCalendar(2000, 1,  1));
    	holidayList.add(new GregorianCalendar(2000, 1,  17));
    	holidayList.add(new GregorianCalendar(2000, 2,  21));
    	holidayList.add(new GregorianCalendar(2000, 5,  29));
    	holidayList.add(new GregorianCalendar(2000, 7,  4));
    	holidayList.add(new GregorianCalendar(2000, 9,  4));
    	holidayList.add(new GregorianCalendar(2000, 10, 9));
    	holidayList.add(new GregorianCalendar(2000, 11, 10));
    	holidayList.add(new GregorianCalendar(2000, 11, 11));
    	holidayList.add(new GregorianCalendar(2000, 11, 23));
    	holidayList.add(new GregorianCalendar(2000, 12, 25));
    }

    /* Constructor */
    public App()
    {
        olr = new OnlineLogisticRegression(2, 11, new L1());
        observation = null;
    }

    /* Main method */
    public static void main(String[] args)
    {
        /* Perform input validation */
        if(!validateInput(args))
        {
            System.exit(-1);
        }

        /* Calculate start time */
        long startTime = System.nanoTime();

        /* Output to console */
        System.out.println();
        System.out.println("=======================");
        System.out.println("FLIGHT DELAY PREDICTION");
        System.out.println("=======================");

	App logisticRegression = new App();

	/* Train a model */
        logisticRegression.train(args[0]);
        startTime = elapsedTime(startTime, "Model has been trained");

	/* Predict on the basis of my model */
	logisticRegression.predict(args[1], args[2]);
        startTime = elapsedTime(startTime, "Prediction completed");

	/* Calculate the accuracy of my predictions */
	logisticRegression.calcAccuracy(args[3], args[2]);
        elapsedTime(startTime, "Correctness validation completed");
    }

    /* Input validation */
    public static boolean validateInput(String[] args)
    {
        if(args.length < 4)
        {
            System.out.println("USAGE:   App <training file> <predict file> " +
                               "<output file> <assessment file>");
            System.out.println("EXAMPLE: App data.csv predict.csv output check.csv");
            return false;
        }
        return true;
    }

    /*
    (1) Output time elapsed since last checkpoint
    (2) Return start time for this checkpoint
    */
    public static long elapsedTime(long startTime, String message)
    {
        if(message != null)
        {
            System.out.println(message);
        }
        long elapsedTime = (System.nanoTime() - startTime) / 1000000000;
        System.out.println("Elapsed time: " + elapsedTime + " second(s)");
        return System.nanoTime();
    }

    /* Calculate number of days from the nearest holiday */
    public int daysFromNearestHoliday(String year, String month, String day)
    {
	Calendar calendar = new GregorianCalendar(Integer.valueOf(year),
				 		  Integer.valueOf(month),
				 		  Integer.valueOf(day));
	int diff = -1;
	int minDiff = Integer.MAX_VALUE;

	for(Calendar holiday : holidayList)
	{
	    diff = Math.abs((int)((calendar.getTimeInMillis() - holiday.getTimeInMillis()) / (1000 * 60 * 60 * 24)));	
	    if(diff == 0)
	    {
	        return diff;
	    }
	   
            if(diff < minDiff)
	    {
	        minDiff = diff;
	    }
	}
		
        return minDiff;
    }

    /* Extract hour of day from a string representing time */
    public int hourFromTime(String time)
    {
        return Integer.valueOf(String.format("%4s", time).substring(0, 2).trim());
    }
    
    /* Train a model */
    public void train(String fileName)
    {
        System.out.println(">Training a model...");        

        File file = new File(fileName);
        if(!file.exists())
        {
            return;
        }

        String line = "";
        FileReader fr = null;
        BufferedReader br = null;

        try
        {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            while((line = br.readLine()) != null)
            {
                trainHelper(line.split(","));
            }
        }
        catch(FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally
        {
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
    }

    /* Train a model - Helper function */
    public void trainHelper(String[] values)
    {
        if(values.length != 63)
        {
            System.out.println("[warning] Weird data separation");
            System.out.println("   [echo] There are " + values.length + " columns");
            return;
        }

        observation = new Observation(values);
	olr.train(observation.getActual(), observation.getVector());
    }

    /* Predict using the specified Logistic Regression model */
    public void predict(String inputFile, String outputFile)
    {
        System.out.println();
        System.out.println(">Predicting arrival delays...");

        String line = "";
	StringBuilder sb = new StringBuilder();
	BufferedReader br = null;
	BufferedWriter bw = null;
	Vector result = null;

	try
	{
	    FileReader fr = new FileReader(new File(inputFile));
	    FileWriter fw = new FileWriter(new File(outputFile));
	    br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);

	    while((line = br.readLine()) != null)
	    {
	        observation = new Observation(line.split(","));
		result = olr.classifyFull(observation.getVector());

		if(result.get(0) >= 0.5)
		{
		    sb.append("0\n");
		}
		else
		{
		    sb.append("1\n");
		}
	    }
	    bw.write(sb.toString());
	}
	catch(FileNotFoundException fnfe)
	{
	    fnfe.printStackTrace();
	}
	catch(IOException ioe)
	{
	    ioe.printStackTrace();
	}
	finally
	{
	    if(br != null)
	    {
	        try
		{
		    br.close();
		}
		catch(IOException ioe)
		{
		    ioe.printStackTrace();
	        }
	    }
	    if(bw != null)
	    {
	        try
		{
		    bw.close();
		}
		catch(IOException ioe)
		{
		    ioe.printStackTrace();
		}
	    }
	}
    }

    /* Calculate the accuracy of my prediction */
    public void calcAccuracy(String inputFile1, String inputFile2)
    {
        System.out.println();
        System.out.println("Calculating accuracy of prediction...");

        String line = "";
	BufferedReader br1 = null;
	BufferedReader br2 = null;
	int matchCount = 0;
	int totalCount = 0;

	try
	{
	    FileReader fr1 = new FileReader(inputFile1);
	    FileReader fr2 = new FileReader(inputFile2);
	    br1 = new BufferedReader(fr1);
	    br2 = new BufferedReader(fr2);

	    while((line = br1.readLine()) != null)
	    {
	        if(line.split(",")[46].equals(br2.readLine()))
		{
		    matchCount++;
		}
		totalCount++;
	    }
	}
	catch(FileNotFoundException fnfe)
	{
	    fnfe.printStackTrace();
	}
	catch(IOException ioe)
	{
	    ioe.printStackTrace();
	}
	finally
	{
	    if(br1 != null)
	    {
	        try
		{
		    br1.close();
		}
		catch(IOException ioe)
		{
		    ioe.printStackTrace();
		}
	    }
	    if(br2 != null)
	    {
	        try
		{
		    br2.close();
		}
		catch(IOException ioe)
		{
		    ioe.printStackTrace();
		}
	    }
	}
	
	System.out.println("Accuracy(%): " + (float)(matchCount * 100) / totalCount);
    }

    /* Observation class */
    class Observation
    {
        private DenseVector vector = new DenseVector(11);
	private int actual;

	public Observation(String[] values)
	{
	    ConstantValueEncoder interceptEncoder = new ConstantValueEncoder("intercept");
	    StaticWordValueEncoder encoder = new StaticWordValueEncoder("feature");

	    interceptEncoder.addToVector("1", vector);
	    vector.set(0, Double.valueOf(values[33]));                /* Departure delay */
	    vector.set(1, Double.valueOf(values[2]));                 /* Month */
	    vector.set(2, Double.valueOf(values[3]));                 /* Day of Month */
	    vector.set(3, Double.valueOf(values[4]));                 /* Day of Week */
            vector.set(4, Double.valueOf(hourFromTime(values[31])));  /* Hour of Day - Departure */
	    /* Number of days from the nearest holiday */
	    vector.set(5, Double.valueOf(daysFromNearestHoliday(values[0], values[2], values[3])));
            vector.set(6, Double.valueOf(hourFromTime(values[42])));  /* Hour of Day - Arrival */

	    encoder.addToVector(values[14], vector);                  /* Origin */
	    encoder.addToVector(values[6], vector);                   /* Carrier */
	    encoder.addToVector(values[24], vector);                  /* Destination */

	    this.actual = Integer.valueOf(values[46]);
	}

	/* Getter methods : BEGIN */
	public Vector getVector()
	{
	    return vector;
	}

	public int getActual()
	{
	    return actual;
	}
	/* Getter methods : END */
    }
}
/* End of App.java */
