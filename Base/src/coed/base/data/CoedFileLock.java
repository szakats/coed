package coed.base.data;


/**
 * TODO: javadoc
 * @author Izso
 *
 */
public class CoedFileLock { //TODO: create real class instead of abstract
   public Integer[] lines;
   
   public CoedFileLock(Integer[] lines){
	   this.lines=lines;	   
   }
   
   public Integer[] getLines() {
		return lines;
	}

	public void setLines(Integer[] lines) {
		this.lines = lines;
	}
}