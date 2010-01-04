 package coed.base.config;

 /**
  * Interface for defining the functionalities of a Config.
  * A Config is an object, that reads certain options from a 
  * config file, and can retrieve these.
  * @author Neobi008
  *
  */
public interface ICoedConfig {
	
	public String getString(String key);
	public Integer getInt(String key);
	public Double getDouble(String key);

	public void setString(String key, String value);
	public void setInt(String key, int value);
	public void setDouble(String key, double value);
}
