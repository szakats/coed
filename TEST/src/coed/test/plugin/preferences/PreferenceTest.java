package coed.test.plugin.preferences;

import coed.plugin.preferences.ConfigModifier;

public class PreferenceTest {
	
	public PreferenceTest(){
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigModifier m = new ConfigModifier();
		System.out.println("Setting config editors");
		System.out.println("versioner=static ; user=pista; port=1234; host=localhost");
		System.out.println("Set user to jozsi");
		m.setFieldEditors();
		System.out.println("User now is jozsi. Check config file!");
		

	}

}
