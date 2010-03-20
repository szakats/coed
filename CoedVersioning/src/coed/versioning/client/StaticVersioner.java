package coed.versioning.client;

import coed.base.comm.ICoedVersionerPart;
import coed.base.data.ICoedFile;
import coed.base.data.IVersionedFilePart;

public class StaticVersioner implements ICoedVersionerPart {
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return STATIC;
	}

	@Override
	public IVersionedFilePart makeVersionedFile(ICoedFile obj) {
		return new CoedStaticFile(obj, this);
	}
	
}
