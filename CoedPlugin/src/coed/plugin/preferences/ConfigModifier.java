package coed.plugin.preferences;

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
				"C&hoice 2", "choice2" }
		}, getFieldEditorParent()));*/
		String configLocation=ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
		
		try {
			config = new Config(configLocation+"\\.coed\\config.ini");
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		e1=new StringFieldEditor(PreferenceConstants.P_STRING1, "versioner.type", getFieldEditorParent());
		e2=new StringFieldEditor(PreferenceConstants.P_STRING2, "server.host", getFieldEditorParent());
		e3=new StringFieldEditor(PreferenceConstants.P_STRING3, "server.port", getFieldEditorParent());
		e4=new StringFieldEditor(PreferenceConstants.P_STRING4, "user.name", getFieldEditorParent());
		addField(e1);		  
		addField(e2);			
		addField(e3);			
		addField(e4);			
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public boolean performOk(){
		config.setString("versioner.type",e1.getStringValue());
		config.setString("server.host",e2.getStringValue());
		config.setString("server.port",e3.getStringValue());
		config.setString("user.name",e4.getStringValue());
		return true;
	}
	
	public void init(IWorkbench workbench) {
	}
	
}