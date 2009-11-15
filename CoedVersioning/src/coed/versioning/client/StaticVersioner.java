package coed.versioning.client;

import coed.base.common.ICoedVersioner;
import coed.base.common.IVersionedObject;

public class StaticVersioner implements ICoedVersioner {
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	class CoedStaticObject implements IVersionedObject {

		@Override
		public boolean checkoutFile() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean commitFile() {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
}
