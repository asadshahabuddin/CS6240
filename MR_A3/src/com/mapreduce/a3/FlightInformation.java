package com.mapreduce.a3;

public class FlightInformation {
	
	private String month;
	private String dayOfMonth;
	private String dayOfWeek;
	private String carrier;
	private String origin; //source airport
	private String dest;   //destination airport
	private String distance;
	
	public FlightInformation(String month, String dayOfMonth, String dayOfWeek, String carrier, String origin, String dest, String distance)
	{
		this.month=month;
		this.dayOfMonth=dayOfMonth;
		this.dayOfWeek=dayOfWeek;
		this.carrier=carrier;
		this.origin=origin;
		this.dest=dest;
		this.distance=distance;
	}
	
	public String getMonth()
	{
		return month;
	}
	
	
	public String getDayOfMonth()
	{
		return dayOfMonth;
	}
	
	
	public String getDayOfWeek()
	{
		return dayOfWeek;
	}
	
	
	public String getCarrier()
	{
		return carrier;
	}
	

	public String getOrigin()
	{
		return origin;
	}
	
	
	public String getDestination()
	{
		return dest;
	}
	

	public String getDistance()
	{
		return distance;
	}
	
}
