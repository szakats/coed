package coed.plugin.mocksfordebug;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Display;

import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.ICoedObject;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;

public class MockCoedObject implements ICoedObject {
	private String path;
	private ArrayList<IFileObserver> listeners;
	
	public MockCoedObject(String path){
		this.path=path;
		listeners=new ArrayList<IFileObserver>();
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	@Override
	public boolean isFile() {
		// TODO Auto-generated method stub
		return true;
	}

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

	@Override
	public void addChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		listeners.add(fileObserver);
		new Changer(this).start();
	}

	@Override
	public String[] getActiveUsers() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		String[] s = {"111","22222","3333"};
		return s;
	}

	@Override
	public CoedFileLine[] getChanges() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		String[] s = {"xxxx","yyyy"};
		CoedFileLine[] cfl={new CoedFileLine(1, s, "")};
		return cfl;
	}

	@Override
	public void goOffline() {
		// TODO Auto-generated method stub
		System.out.println("GOING OFFLINE "+path);
	}

	@Override
	public void goOnline() {
		// TODO Auto-generated method stub
		System.out.println("GOING ONLINE "+path);
	}

	@Override
	public boolean releaseLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		listeners.remove(fileObserver);
	}

	@Override
	public boolean requestLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return (lock.getLines()[0]<2) ? false : true;
	}

	@Override
	public boolean sendChanges(CoedFileLine line)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return true;
	}
	
	class Changer extends Thread {
		private MockCoedObject outer;
		
		public Changer(MockCoedObject co){
			outer=co;
		}
		
		public void run(){
			int i=0;
			for (i=0; i<2000000 && listeners.size()>0; i++) {
				listeners.get(0).update(outer);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
