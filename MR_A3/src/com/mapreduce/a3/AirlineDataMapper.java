package com.mapreduce.a3;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AirlineDataMapper extends Mapper<LongWritable, Text, Text, FlightInformation>{
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
	{
		
		String[] columns = value.toString().split(",");
		context.write(new Text(columns[14]), new FlightInformation(columns[2], columns[3], columns[4], columns[8], columns[14], columns[23], columns[54]));
		
	}

}
