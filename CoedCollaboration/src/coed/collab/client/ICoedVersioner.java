package coed.collab.client;

import coed.collab.data.*;

/**
 * TODO: description
 * @author Izso
 * 
 */
public interface ICoedVersioner {
   
   /**
	* TODO: javadoc
	* @return
   */
   public String getState();

   public String getProjectList();
   public CoedProject getProjectInfo(String name);
   public boolean checkoutProject(CoedProject project);
   public boolean commitProject(CoedProject project);

   public CoedFile getFileInfo();
   public boolean checkoutFile(CoedFile file);
   public boolean checkoutFiles(CoedFile[] files);
   public boolean commitFile(CoedFile file);
   public boolean commitFiles(CoedFile[] files);
   
}