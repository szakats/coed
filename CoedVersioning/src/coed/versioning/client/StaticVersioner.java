package coed.versioning.client;

import coed.base.common.ICoedObject;
import coed.base.common.ICoedVersioner;
import coed.base.common.IVersionedObject;

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
