package coed.base.data;

/**
 * TODO: JAVADOC 
 * @author Neobi
 *
 */
public class CoedFileLine { 
	
   private Integer line;
   private String[] text;
   private String metaInfo; //meta-info containing user's name who made the modification
	
   public CoedFileLine(Integer lineNumber, String[] text, String metaInfo){
	   this.line = lineNumber;
	   this.text = text;
	   this.metaInfo = metaInfo;
   }
   public Integer getLine(){
	   	return this.line;
   }
   public String[] getText(){
	   return this.text;
   }
   public String getMetaInfo(){
	   return this.metaInfo;
   }
}