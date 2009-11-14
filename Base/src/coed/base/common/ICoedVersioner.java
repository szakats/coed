package coed.base.common;

import coed.base.data.CoedFile;
import coed.base.data.CoedProject;

/**
 * TODO: description
 * @author Neobi
 * 
 */
public interface ICoedVersioner {
	
	public final static String NULL = "null";
	public final static String GIT = "git";
	
   
   /**
	* TODO: javadoc
	* @return
   */
   public String getState();
   public String getType();

   public String getProjectList();
   public CoedProject getProjectInfo(String name);
   public boolean checkoutProject(CoedProject project);
   public boolean commitProject(CoedProject project);

   public CoedFile getFileInfo(String fileName);
   public boolean checkoutFile(CoedFile file);
   public boolean checkoutFiles(CoedFile[] files);
   public boolean commitFile(CoedFile file);
   public boolean commitFiles(CoedFile[] files);
   public CoedFile addFileToProject(String fileName, CoedProject project);
   public void removeFileFromProject(CoedFile file);
}