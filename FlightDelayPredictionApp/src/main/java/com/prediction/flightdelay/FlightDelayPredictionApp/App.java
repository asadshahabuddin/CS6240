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
    private static ArrayList<Calendar> holidayList2012;

    static
    {
        /* List of holidays in 2012 */
        /* TODO: Add holidays for all years listed in the actual input file */
        holidayList2012 = new ArrayList<Calendar>();
        holidayList2012.add(new GregorianCalendar(2012, 1,  1));
        holidayList2012.add(new GregorianCalendar(2012, 1,  2));
        holidayList2012.add(new GregorianCalendar(2012, 2,  20));
        holidayList2012.add(new GregorianCalendar(2012, 5,  28));
        holidayList2012.add(new GregorianCalendar(2012, 7,  4));
        holidayList2012.add(new GregorianCalendar(2012, 9,  3));
        holidayList2012.add(new GregorianCalendar(2012, 10, 8));
        holidayList2012.add(new GregorianCalendar(2012, 11, 11));
	holidayList2012.add(new GregorianCalendar(2012, 11, 12));
	holidayList2012.add(new GregorianCalendar(2012, 11, 22));
	holidayList2012.add(new GregorianCalendar(2012, 11, 25));
    }

    /* Main method */
    public static void main(String[] args)
    {
        /* Calculate start time */
        long startTime = System.nanoTime();

        /* Output to console */
        System.out.println("=======================");
        System.out.println("FLIGHT DELAY PREDICTION");
        System.out.println("=======================");

	App logisticRegression = new App();

	/* Load the input data */
	List<Observation> trainingData = logisticRegression.parseInputFile("input/data.csv");

	/* Train a model */
        OnlineLogisticRegression olr = logisticRegression.train(trainingData);

	/* Predict on the basis of my model */
	logisticRegression.predict(olr, "input/predict.csv", "output");

	/* Calculate the accuracy of my predictions */
	logisticRegression.calcAccuracy("input/check.csv", "output");

        /* Print elapsed time since last checkpoint */
        long runningTime = (System.nanoTime() - startTime) / 1000000000;
        System.out.println("\nRunning time: " + runningTime + " second(s)");
    }

    /* Calculate number of days from the nearest holiday */
    public int daysFromNearestHoliday(String year, String month, String day)
    {
	Calendar calendar = new GregorianCalendar(Integer.valueOf(year),
				 		  Integer.valueOf(month),
				 		  Integer.valueOf(day));
	int diff = -1;
	int minDiff = Integer.MAX_VALUE;

	for(Calendar holiday : holidayList2012)
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

    /* Parse input data from file */
    public List<Observation> parseInputFile(String inputFile)
    {
        List<Observation> result = new ArrayList<Observation>();
        BufferedReader br = null;
	String line = "";

	try
	{
	    /* Load the file which contains training data */
	    br = new BufferedReader(new FileReader(new File(inputFile)));

	    /* Prepare observation data */
	    while ((line = br.readLine()) != null)
	    {
	        String[] values = line.split(",");
		if(values[49].equals("0"))
		{
		    result.add(new Observation(values));
	        }
	    }
	}
	catch (FileNotFoundException fnfe)
	{
	    fnfe.printStackTrace();
	}
	catch (IOException ioe)
	{
	    ioe.printStackTrace();
	}
	finally
	{
	    if (br != null)
	    {
		try
		{
		    br.close();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	    }
	}
		
	return result;
    }

    /* Train a model */
    public OnlineLogisticRegression train(List<Observation> trainData)
    {
	OnlineLogisticRegression olr = new OnlineLogisticRegression(2, 10, new L1());

	/* Train the model using 30 passes */
	for (int pass = 0; pass < 50; pass++)
	{
	    for (Observation observation : trainData)
	    {
	        olr.train(observation.getActual(), observation.getVector());
	    }

	    /* Every 10 passes check the accuracy of the trained model */
	    if (pass % 10 == 0)
	    {
	        Auc eval = new Auc(0.5);
		for (Observation observation : trainData)
		{
		    eval.add(observation.getActual(),
		    olr.classifyScalar(observation.getVector()));
		}
                System.out.format("Pass: %2d, Learning rate: %2.4f, Accuracy: %2.4f\n",
				  pass, olr.currentLearningRate(), eval.auc());
	    }
	}
		
	return olr;
    }

    /* Predict using the specified Logistic Regression model */
    public void predict(OnlineLogisticRegression olr, String inputFile, String outputFile)
    {
        String line = "";
	StringBuilder sb = new StringBuilder();
	BufferedReader br = null;
	BufferedWriter bw = null;
	Observation newObservation = null;
	Vector result = null;

	try
	{
	    FileReader fr = new FileReader(new File(inputFile));
	    FileWriter fw = new FileWriter(new File(outputFile));
	    br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);

	    while((line = br.readLine()) != null)
	    {
	        newObservation = new Observation(line.split(","));
		result = olr.classifyFull(newObservation.getVector());

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
	
	System.out.println();
	System.out.println("Accuracy (%): " + (float)matchCount / totalCount);
    }

    /*
    public void testModel(OnlineLogisticRegression olr)
    {
        Observation newObservation = new Observation(new String[]
						     {"family", "10", "100000", "0" });
	Vector result = olr.classifyFull(newObservation.getVector());

	System.out.println();
	System.out.println("=======");
	System.out.println("TESTING");
	System.out.println("=======");
	System.out.format("Probability of being on time (0) = %.3f\n",
			  result.get(0));
	System.out.format("Probability of being late (1)    = %.3f\n",
		          result.get(1));
    }
    */

    /* Observation class */
    class Observation
    {
        private DenseVector vector = new DenseVector(10);
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
            vector.set(4, Double.valueOf(hourFromTime(values[31])));  /* Hour of Day */
	    /* Number of days from the nearest holiday */
	    vector.set(5, Double.valueOf(daysFromNearestHoliday(values[0], values[2], values[3])));

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
