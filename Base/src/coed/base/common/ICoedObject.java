package coed.base.common;

public interface ICoedObject extends IVersionedPart, ICollabPart {
	
	public String getPath();
	public boolean isFile();
}
