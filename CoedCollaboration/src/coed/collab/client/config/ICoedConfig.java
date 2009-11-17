 package coed.collab.client.config;

 /**
  * Interface for defining the functionalities of a Config.
  * A Config is an object, that reads certain options from a 
  * config file, and can retrieve these.
  * @author Neobi008
  *
  */
public interface ICoedConfig {
	
	public String getString(String key);
	public int getInt(String key);
	public double getDouble(String key);

}
