/**
 * 
 */
package coed.plugin.base;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.data.CoedFile;
import coed.base.data.CoedProject;
import coed.plugin.views.IFileTree;
import coed.plugin.views.IUserList;

/**
 * Interface to which CoedTextEditors will register, and will be manipulated through.
 * TODO: clean up/ rewrite comments !!!
 * @author Izso
 *
 */
public interface IPluginController {
	final public static String ACTION_COMMIT="COED_commit";
	final public static String ACTION_COMMIT_LIVE="COED_commit_live";
	final public static String ACTION_CHECKOUT="COED_checkout";

	/**
	 * Adds this editor to the list of monitorized editors.
	 * Replaces it's contents with the current live version of the document. 
	 * If there is no live version of it, the contents will remain unchanged.
	 * 
	 * @param texte
	 */
	public void startCollabFor(AbstractDecoratedTextEditor texte);
	
	/**
	 * Ends live session for the given document.
	 * If the user is the last user to close a document or the initiating user
	 * he/she is prompted to save/commit the document 
	 * @param texte
	 */
	public void endCollabFor(AbstractDecoratedTextEditor texte);
	
	/**
	 * Attaches the given "live files tree" to the controller.
	 * This means that the controller will update it if new files appear on the
	 * collaborative server, etc. 
	 * @param filet
	 * @return success
	 */
	public boolean attachFileTree(IFileTree filet);
	
	/**
	 * Detaches a tree from the controller
	 * @param filet
	 */
	public void detachFileTree(IFileTree filet);
	
	/**
	 * Attaches a list which will display online/all users in the collaborative system.
	 * @param userl
	 * @return success
	 */
	public boolean attachUserList(IUserList userl);
	
	/**
	 * Detaches a user list from the controller
	 * @param userl
	 */
	public void detachUserList(IUserList userl);
	
	/**
	 * Method for requesting a given action for a file.
	 * This method will be invoked by menus/buttons in the context menu, button bar
	 * @param action
	 * @param file
	 * @return success
	 */
	public boolean requestVersionAction(String action, CoedFile file);
	
	/**
	 * Method for requesting a given action for a project.
	 * This method will be invoked by menus/buttons in the context menu, button bar
	 * @param action
	 * @param project
	 * @return
	 */
	public boolean requestVersionAction(String action, CoedProject project);
	
	/**
	 * This is a method for asking for information about a given file.
	 * The method is invoked with an incomplete CoedFile (having only path and project), 
	 * and it will complete it and return it as a result.
	 * @param file
	 * @return
	 */
	public CoedFile requestInfo(CoedFile file);
	
	/**
	 * This is a method for asking for information about a given file.
	 * The method is invoked with an incomplete CoedProject, 
	 * and it will complete it and return it as a result.
	 * @param project
	 * @return
	 */
	public CoedProject requestInfo(String project);
	
	/**
	 * Lists all users on the coed server
	 * @return
	 */
	public String[] getAllUsers();
	
	/**
	 * Lists users which work on a given live document.
	 * @param file
	 * @return
	 */
	public String[] getCollabUsers(CoedFile file);
	
}
