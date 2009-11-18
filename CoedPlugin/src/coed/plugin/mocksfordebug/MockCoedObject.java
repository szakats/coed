package coed.plugin.mocksfordebug;

import java.util.ArrayList;

import coed.base.data.ICoedObject;
import coed.base.data.IFileObserver;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.CoedFuture;
import coed.base.util.IFuture;

public class MockCoedObject implements ICoedObject {
	private String path;
	private TextModification change = new TextModification(1, 0, "xcc");
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
	public IFuture<TextModification[]> getChanges() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		
		System.out.println("SENGING CHANGES...");
		TextModification[] cfl={change}; //{new CoedFileLine(1, s, "")};
		CoedFuture<TextModification[]> cflf = new CoedFuture<TextModification[]>();
		cflf.set(cfl);
		return cflf;
	}

	@Override
	public void goOffline() {
		// TODO Auto-generated method stub
		System.out.println("GOING OFFLINE "+path);
	}

	@Override
	public IFuture<ICoedObject[]> goOnline() {
		// TODO Auto-generated method stub
		System.out.println("GOING ONLINE "+path);
		CoedFuture<Boolean> bf =new CoedFuture<Boolean>();
		bf.set(true);
		return null;
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		listeners.remove(fileObserver);
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

	@Override
	public IFuture<String> goOnline(String contents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean releaseLock(TextPortion lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requestLock(TextPortion lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line)
			throws NotConnectedToServerException {
		change=new TextModification(1, 0, line.getText(), "meta");
		(new Changer(this)).start();

		return null;
	}

}
