package coed.collab.client.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import coed.base.data.exceptions.InvalidConfigFileException;


public class Config implements ICoedConfig {
	
	/**
	 * settings is the hashmap containing the settings from the config file
	 */
	Map<String, String> settings = new HashMap<String, String>(); 
	
	public Config(String path) throws InvalidConfigFileException { 
		try { 

			   FileReader fr = new FileReader(path);
		       BufferedReader br = new BufferedReader(fr);

		       String record = new String();
		       String key = new String();
		       String value = new String();
		       
		       while ((record = br.readLine()) != null) {
		    	   int ind;
		    	   //reading key-value pairs from file, and putting into map
		    	   if ((ind = record.indexOf('=')) != -1){
		    	   	  key = record.substring(0, ind).trim();
		    	   	  value = record.substring(ind+1).trim();
		              settings.put(key, value);
		    	   }
		       } 
		 } catch (IOException e) { 
		       throw new InvalidConfigFileException();
		 }

	}
	
	public String getString(String key) {
		return settings.get(key);
	}
	
	public int getInt(String key) {
		return new Integer(settings.get(key));
	}
	
	public double getDouble(String key) {
		return new Double(settings.get(key));
	}
	
	/*public static void main (String args[]){
		Config c = new Config("D:\\University\\III YEAR\\1st Semester\\SE\\CoED\\coed\\CoedCollaboration\\src\\coed\\collab\\client\\config\\myfile.txt");
		System.out.println(c.getString("host"));
	}*/
}
