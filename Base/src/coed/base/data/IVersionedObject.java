package coed.base.data;


public interface IVersionedObject extends IVersionedPart {
	/**
	 * Get the ICoedObject which contains this object.
	 * @return the parent object
	 */
	public ICoedObject getParent();
}
