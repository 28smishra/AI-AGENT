package main.java.core_framework;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger {


	public static enum LogLevel {
	    INFO,
	    ERROR,
	    PASS,
	    FAIL
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String getDateTimeStamp()
	{
		String dateTimeStamp="";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		dateTimeStamp=simpleDateFormat.format(new Date());
		return dateTimeStamp;
	}
	
	public static void writeConsoleLog(LogLevel loglevel, String logMessage)
	{
		switch(loglevel)
		{
		case INFO:
			System.out.println("["+getDateTimeStamp()+"] INFO: "+logMessage);
			break;
		case ERROR:
			System.out.println("["+getDateTimeStamp()+"] ERROR: "+logMessage);
			break;
		case PASS:
			System.out.println("["+getDateTimeStamp()+"] PASS: "+logMessage);
			break;
		case FAIL:
			System.out.println("["+getDateTimeStamp()+"] FAIL: "+logMessage);
			break;
		}
	}

}
