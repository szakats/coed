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
	
	class CoedStaticObject implements IVersionedObject {

		@Override
		public boolean checkout() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean commit() {
			// TODO Auto-generated method stub
			return true;
		}

	}

	@Override
	public IVersionedObject makeVersionedObject(ICoedObject obj) {
		return new CoedStaticFile(obj, this);
	}
	
}
