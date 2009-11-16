package coed.plugin.views;

import coed.base.data.CoedObject;

/**
 * Interface for a view which will display files that are edited 
 * in a collaborative way. It is treeview, because one should see projects,
 * files in projects, and maybe some additional data for each file. 
 * @author Izso
 *
 */
public interface IFileTree {
	
	public void displayFileTree(CoedObject[] projects);
}
