package coed.collab.client;

import java.io.File;

public class Config  {
	private File file;
	private static final Config instance = new Config();
	
	public static Config getInstance() {
		return instance;
	}
	
	private Config() {
		
	}
	
	String getString(String key) {
		return "";
	}
	
	int getInt(String key) {
		return 0;
	}
	
	double getDouble(String key) {
		return 0.0;
	}
}
