package coed.plugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import coed.base.config.Config;
import coed.base.config.ICoedConfig;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.plugin.base.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String versioner,host,port,user;
		ICoedConfig config=null;
		try {
			config=new Config("../.coed/config.ini");
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		versioner=config.getString("versioner.type");
		host=config.getString("server.host");
		port=config.getString("server.port");
		user=config.getString("user.name");
		store.setDefault(PreferenceConstants.P_STRING1,versioner);
		store.setDefault(PreferenceConstants.P_STRING2,host);
		store.setDefault(PreferenceConstants.P_STRING3,port);
		store.setDefault(PreferenceConstants.P_STRING4,user);
	}

}
