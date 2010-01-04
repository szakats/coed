package coed.base.data;

/**
 * Class representing a text portion of a document.
 * A portion is defined by its offset and its length.
 * @author Izso
 *
 */
public class TextPortion {
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
	
	public String toString(){
		return "offset="+offset+", length="+length;
	}
	
}
