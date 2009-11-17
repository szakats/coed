package coed.base.data;


public interface ICollabObject extends ICollabPart {
	/**
	 * Get the ICoedObject which contains this object.
	 * @return the parent object
	 */
	public ICoedObject getParent();
}
