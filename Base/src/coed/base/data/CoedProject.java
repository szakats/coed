package coed.base.data;

/**
 * TODO: javadoc
 * @author Neobi
 *
 */
public class CoedProject { 
   
   private String name;
   private String[] collaborators;
   
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
   
}