package coed.versioning.client;

import coed.base.data.ICoedObject;
import coed.base.data.IVersionedObject;

public class CoedStaticFile implements IVersionedObject {
	private StaticVersioner ver;
	private ICoedObject obj;
	
	public CoedStaticFile(ICoedObject obj, StaticVersioner ver) {
		this.obj = obj;
		this.ver = ver;
	}

	@Override
	public boolean checkout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ICoedObject getParent() {
		return obj;
	}

}
