package coed.versioning.client;

import coed.base.comm.ICoedVersioner;
import coed.base.data.ICoedObject;
import coed.base.data.IVersionedObject;

public class StaticVersioner implements ICoedVersioner {
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return STATIC;
	}

	@Override
	public IVersionedObject makeVersionedObject(ICoedObject obj) {
		return new CoedStaticFile(obj, this);
	}
	
}
