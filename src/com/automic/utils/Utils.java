package com.automic.utils;

import java.util.Arrays;
import java.util.Random;

import com.uc4.api.MessageBox;
import com.uc4.api.Template;

public class Utils {

	public static boolean isInteger( String input ) {
	    try {
	        Integer.parseInt( input );
	        return true;
	    }
	    catch( Exception e ) {
	        return false;
	    }
	}
	
	public static String getErrorString(MessageBox msg){
		String s = "\t -- Error: "+msg.getText().toString();
		return s;
	}
	
	public static String getErrorString(String msg){
		String s = "\t -- Error: "+msg;
		return s;
	}
	
	public static String getWarningString(String s){
		return "\t %% Warning: "+s;
	}
	
	public static String getInfoString(String s){
		return "\t %% "+s;
	}
	
	public static String getDebugString(String s){
		return "\t %% DEBUG: "+s;
	}
	
	public static String getSuccessString(String s){
		return "\t ++ " + s;
	}
	
	public static void showProgressMsg() {
		getInfoString("Filtering Progress:");
	}
	public static void showNoObjectFoundMsg(String ObjectName) {
		getWarningString("=> No Object Was Found for name: "+ObjectName);
	}
	public static void showMatchingObjectMsg(String ObjectName, String ObjectTitle) {
		getSuccessString("  => Processing Matching Object: [ " + ObjectName +" | " + ObjectTitle +" ] ");
	}
	public static void displayErrorString(String s) {
		System.out.println(getErrorString(s));
	}
	public static void displayWarningString(String s) {
		System.out.println(getWarningString(s));
	}
	public static void displayInfoString(String s) {
		System.out.println(getInfoString(s));
	}
	public static void displaySuccessString(String s) {
		System.out.println(getSuccessString(s));
	}
	
	public static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
	
	public static String getXMLfromValuesForIAVARA(String attrkey, String description, String type) {
		// ex: 		<event_attributes><description>success field</description><key>success</key><type>BOOLEAN</type></event_attributes>
		String xmloutput = "";
		String[] IAInputTypes = {"BOOLEAN", "STRING","NUMBER"};
		String TYPE = type.toUpperCase();
		if(Arrays.asList(IAInputTypes).contains(TYPE)){
			xmloutput = "<event_attributes><description>";
			xmloutput = xmloutput + description + "</description><key>"+attrkey+"</key><type>"+TYPE+"</type></event_attributes>";
		}
		return xmloutput;
	}
}
