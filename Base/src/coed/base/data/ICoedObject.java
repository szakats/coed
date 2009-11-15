package coed.base.data;


public interface ICoedObject extends IVersionedPart, ICollabPart {
	
	public String getPath();
	public boolean isFile();
}
