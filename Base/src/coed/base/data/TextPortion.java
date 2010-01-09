package coed.base.data;

import java.io.Serializable;

/**
 * Class representing a text portion of a document.
 * A portion is defined by its offset and its length.
 * @author Izso
 *
 */
public class TextPortion implements Serializable {
	private Integer offset;
	private Integer length;
	
	public TextPortion(Integer offset, Integer length) {
		this.offset=offset;
		this.length=length;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
	
	public String toString() {
		return "offset=" + getOffset() + ",length=" + getLength();
	}
	
	public boolean equals(Object o){
		if (o instanceof TextPortion){
			if ( ((TextPortion)o).getOffset().equals(offset) && ((TextPortion)o).getLength().equals(length)) {
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		return offset*10000+length;
		
	}
}
