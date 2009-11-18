package coed.base.data;

/**
 * A TextModification class represents a change to a text, whoch is identified by its
 * location and its content (also, an optional meta component for adding event info for ex. )
 * @author Izso
 *
 */
public class TextModification extends TextPortion {
	private String text;
	private String metaInfo;
	
	public TextModification(Integer offset, Integer length, String text) {
		super(offset, length);
		this.text=text;
		this.metaInfo = null;
	}
	
	public TextModification(Integer offset, Integer length, String text, String meta) {
		super(offset, length);
		this.text=text;
		this.metaInfo = meta;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
