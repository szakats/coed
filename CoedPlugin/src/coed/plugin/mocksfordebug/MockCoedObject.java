package coed.plugin.mocksfordebug;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Display;

import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.ICoedObject;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.CoedFuture;
import coed.base.util.IFuture;

public class MockCoedObject implements ICoedObject {
	private String path;
	private CoedFileLine change = new CoedFileLine(1, null, "");
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
	}

	@Override
	public IFuture<String[]> getActiveUsers() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		String[] s = {"111","22222","3333"};
		CoedFuture<String[]> fs = new CoedFuture<String[]>();
		fs.set(s);
		return fs;
	}

	@Override
	public IFuture<CoedFileLine[]> getChanges() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		
		System.out.println("SENGING CHANGES...");
		CoedFileLine[] cfl={change}; //{new CoedFileLine(1, s, "")};
		CoedFuture<CoedFileLine[]> cflf = new CoedFuture<CoedFileLine[]>();
		cflf.set(cfl);
		return cflf;
	}

	@Override
	public void goOffline() {
		// TODO Auto-generated method stub
		System.out.println("GOING OFFLINE "+path);
	}

	@Override
	public IFuture<Boolean> goOnline() {
		// TODO Auto-generated method stub
		System.out.println("GOING ONLINE "+path);
		CoedFuture<Boolean> bf =new CoedFuture<Boolean>();
		bf.set(true);
		return bf;
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
		change=new CoedFileLine(1, line.getText(), "");
		(new Changer(this)).start();
		return true;
	}
	
	class Changer extends Thread {
		private MockCoedObject outer;
		
		public Changer(MockCoedObject co){
			outer=co;
		}
		
		public void run(){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listeners.get(0).update(outer);
			
		}
	}

	@Override
	public IFuture<String> getCurrentContent() {
		// TODO Auto-generated method stub
		return null;
	}

}
