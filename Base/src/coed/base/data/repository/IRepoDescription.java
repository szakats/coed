package coed.base.data.repository;

import coed.base.data.*;

/**
 * Generic interface for repository book-keeper classes.
 * They keep all information about files associated with a given project, and
 * also the details of the project;
 * @author Izso
 *
 */
public interface IRepoDescription {
	
	public boolean hasFile(CoedFile file);
	
	public boolean addFile(CoedFile file);
	
	public boolean removeFile(CoedFile file);
	
	public CoedFile[] getAllFiles();
			
	public CoedProject getProject();
	
}