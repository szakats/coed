package coed.base.data;

import java.io.Serializable;

/**
 * TODO: javadoc
 * @author Neobi
 *
 */
public class CoedProject implements Serializable { 
   
   private String name;
   private String[] collaborators;
   private CoedFile[] files;
   
   public CoedProject(String name){
	   this.name=name;
	   this.collaborators=null;
   }
   
   public CoedProject(String name, String[] collaborators){
	   this.name=name;
	   this.collaborators=collaborators;
   }
   
   public String getName(){
	   return name;
   }
   
   public void setName(String name){
	   this.name=name;
   }
   
   public String[] getCollaborators(){
	   return this.collaborators;
   }
   
   public void setCollaborators(String[] collaborators){
	   this.collaborators=collaborators;
   }
   
   public CoedFile[] getFiles(){
	   return files;
   }
   
   public void setFiles(CoedFile[] files){
	   this.files=files;
   }
}