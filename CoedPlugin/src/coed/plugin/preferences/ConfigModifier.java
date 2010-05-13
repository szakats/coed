package coed.plugin.preferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import coed.base.config.Config;
import coed.base.config.ICoedConfig;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.plugin.base.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class ConfigModifier
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private ICoedConfig config=null;
	private StringFieldEditor e1=null;
	private StringFieldEditor e2=null;
	private StringFieldEditor e3=null;
	private StringFieldEditor e4=null;
	
	public ConfigModifier() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Configuration options: ");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		/*addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, 
				"&Directory preference:", getFieldEditorParent()));
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_BOOLEAN,
				"&An example of a boolean preference",
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_CHOICE,
			"An example of a multiple-choice preference",
			1,
			new String[][] { { "&Choice 1", "choice1" }, {
				"Choice 2", "choice2" }
		}, getFieldEditorParent()));*/
		String configLocation=ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
		
		try {
			config = new Config(configLocation+"\\.coed\\config.ini");
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		e1=new StringFieldEditor(PreferenceConstants.P_STRING1, "Username", getFieldEditorParent());
		e2=new StringFieldEditor(PreferenceConstants.P_STRING2, "Password", getFieldEditorParent());
		e3=new StringFieldEditor(PreferenceConstants.P_STRING3, "Server Hostname", getFieldEditorParent());
		e4=new StringFieldEditor(PreferenceConstants.P_STRING4, "Server Port", getFieldEditorParent());
		
		addField(e4);		  
		addField(e2);			
		addField(e3);			
		addField(e1);			
	}

	/**
	 * Method used for testing
	 * Remove when program finished!
	 * Method sets the field editors
	 */
	public void setFieldEditors(){
		e1.setStringValue("jozsi");
		e2.setStringValue("pass");
		e3.setStringValue("localhost");
		e4.setStringValue("1234");
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public boolean performOk(){
		config.setString("user.password", e2.getStringValue());
		config.setString("server.host",e3.getStringValue());
		config.setString("server.port",e4.getStringValue());
		config.setString("user.name",e1.getStringValue());
		
	/*	 MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");

			digest.update(e1.getStringValue().getBytes());
		 	byte[] hash = digest.digest();
		 
		    config.writeConfigFile();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return true;
	}
	
	public void init(IWorkbench workbench) {
	}
	
}