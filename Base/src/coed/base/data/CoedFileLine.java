package coed.base.data;

/**
 * TODO: JAVADOC 
 * @author Izso
 *
 */
public abstract class CoedFileLine { //TODO: create real class instead of abstract
   private Integer line;
   private String[] text;
	
   public CoedFileLine(Integer lineNumber, String[] text, String metaInfo){};
   public abstract Integer getLine();
   public abstract String[] getText();
}

//metaInfo --> user:jozsi; time:12:22:00;