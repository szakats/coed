package coed.base.config;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import coed.base.data.exceptions.InvalidConfigFileException;

/**
 * Class that represents a Configurator object. It creates a map of certain 
 * configuration options read from a config file. The format of the config file should be 
 * the usual .ini . Access methods are provided.
 * 
 * @author Neobi008
 *
 */

public class Config implements ICoedConfig {
	
	/**
	 * settings is the hashmap containing the settings from the config file
	 */
	Map<String, String> settings = new HashMap<String, String>(); 
	private String path;
	
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
		       //if file not found, InvalidConfigFIleException is thrown
		 }
		 this.path = path;
	}
	
	public Config() {
		
	}
	
	public String getString(String key) {
		return settings.get(key);
	}
	
	public Integer getInt(String key) {
		return new Integer(settings.get(key));
	}
	
	public Double getDouble(String key) {
		return new Double(settings.get(key));
	}
	
	public void setString(String key, String value) {
		settings.put(key, value);
	}
	
	public void setInt(String key, int value) {
		settings.put(key, Integer.toString(value));
	}
	
	public void setDouble(String key, double value) {
		settings.put(key, Double.toString(value));
	}
	
	public void writeConfigFile(){
		Writer output = null;
	   // File file = new File("write.txt");
		try{
	    output = new BufferedWriter(new FileWriter(path));
		
	    Set<String> keyset = settings.keySet();
	    Iterator<String> it = keyset.iterator();
	    while (it.hasNext())
	    {
	    	String key = it.next();
	    	output.write(key+"="+settings.get(key));
	    }
	    output.close();
		}
		catch(Exception e){}
		
	}
}
