package com.marklogic.performanceExamples.util;

import java.util.Iterator;
import java.util.List;

public class Stats {
	public static long average(List<Long> input){
		long total = 0;
		Iterator<Long> i = input.iterator(); 
		while(i.hasNext()){
			total = total + i.next();
		}
		
		return total/input.size();
	}
	
	public static long stdev(List<Long> input){
		long average = average(input);
		long total = 0;
		Iterator<Long> i = input.iterator(); 
		while(i.hasNext()){
			total = total + (long)java.lang.Math.pow(i.next()-average, 2);
		}
		return (long) java.lang.Math.sqrt(total/input.size());
	}
}
