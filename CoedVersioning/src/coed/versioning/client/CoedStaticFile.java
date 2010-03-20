package coed.versioning.client;

import coed.base.data.ICoedFile;
import coed.base.data.IVersionedFilePart;

public class CoedStaticFile implements IVersionedFilePart {
	private StaticVersioner ver;
	private ICoedFile obj;
	
	public CoedStaticFile(ICoedFile obj, StaticVersioner ver) {
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
	public ICoedFile getParent() {
		return obj;
	}

}
