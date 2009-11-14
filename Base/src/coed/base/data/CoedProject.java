package coed.base.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TODO: javadoc
 * @author Neobi
 *
 */
public class CoedProject implements Serializable { 
   
   private String name;
   private String[] collaborators;
   private ArrayList<CoedFile> files; 
   
   public CoedProject(String name){
	   this.name=name;
	   this.collaborators=null;
	   this.files = new ArrayList<CoedFile>();
   }
   
   public boolean addFile(CoedFile file){
	   boolean res = true;
	   if ( files.contains(file) )
		   res = false;
	   else
		   this.files.add(file);
	   return res;
   }
   
   public void removeFile(CoedFile file){
	   if (files.contains(file))
		   files.remove(file);
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
	   return (CoedFile[])files.toArray();
   }
   
   public void setFiles(CoedFile[] files){
	   for (int i=0; i<files.length; i++)
		   this.files.add(files[i]);
   }
   
   public boolean equals(Object proj){
	   return this.name.equals(proj);
   }
}