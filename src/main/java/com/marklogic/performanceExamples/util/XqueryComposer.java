package com.marklogic.performanceExamples.util;

public class XqueryComposer {
	public static String composeXquery(String[] input){
		String output = "";
		
		for(int i = 0; i < input.length; i++){
			output = output + " " + input[i];
		}
		
		return output;
	}
}
