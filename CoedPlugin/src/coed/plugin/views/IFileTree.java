package coed.plugin.views;

import coed.base.data.ICoedObject;

/**
 * Interface for a view which will display files that are edited 
 * in a collaborative way. It is treeview, because one should see projects,
 * files in projects, and maybe some additional data for each file. 
 * @author Izso
 *
 */
public interface IFileTree {
	
	/**
	 * Method receives an array of ICoedObject files,
	 * extracts the path of the files and builds a file tree
	 * containing directories,subdirectories and the respective files
	 * @param files - array of coed objects
	 */
	public void displayFileTree(ICoedObject[] files);
	
	/**
	 * Method that receives a file and adds it to the file tree
	 * structure. If the structure does not exists, it is created.
	 * @param file - file that must be added to tree structure
	 */
	public void displayFile(ICoedObject file);
	
	/**
	 * Method that receives a file as argument and has to remove it
	 * from the file tree. If the file does not exists in the tree,
	 * nothing happens.
	 * @param file - file that must be removed
	 */
	public void removeFile(ICoedObject file);
	
	/**
	 * Method used to delete the file tree when user goes offline
	 */
	public void goOffline();
}
