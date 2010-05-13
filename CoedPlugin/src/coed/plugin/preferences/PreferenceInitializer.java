package coed.plugin.preferences;

import org.eclipse.core.resources.ResourcesPlugin;
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
		String configLocation=ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
		ICoedConfig config=null;
		try {
			config = new Config(configLocation+"\\.coed\\config.ini");
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		store.setDefault(PreferenceConstants.P_STRING1,config.getString("user.name"));
		store.setDefault(PreferenceConstants.P_STRING2,"");
		store.setDefault(PreferenceConstants.P_STRING3,config.getString("server.host"));
		store.setDefault(PreferenceConstants.P_STRING4,config.getString("server.port"));
		
		
	}

}
