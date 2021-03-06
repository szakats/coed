package coed.plugin.mocksfordebug;

import java.util.ArrayList;

import coed.base.data.ICoedFile;
import coed.base.data.IFileChangeListener;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.util.CoedFuture;
import coed.base.util.IFuture;

public class MockCoedObject implements ICoedFile {
	private String path;
	private TextModification change = new TextModification(1, 0, "xcc");
	private ArrayList<IFileChangeListener> listeners;
	
	public MockCoedObject(String path){
		this.path=path;
		listeners=new ArrayList<IFileChangeListener>();
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
	public IFuture<Void> addChangeListener(IFileChangeListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
		return null;
	}

	@Override
	public IFuture<String[]> getActiveUsers() {
		// TODO Auto-generated method stub
		String[] s = {"111","22222","3333"};
		CoedFuture<String[]> fs = new CoedFuture<String[]>();
		fs.set(s);
		return fs;
	}

	@Override
	public IFuture<TextModification[]> getChanges() {
		// TODO Auto-generated method stub
		
		System.out.println("SENGING CHANGES...");
		TextModification[] cfl={change}; //{new CoedFileLine(1, s, "")};
		CoedFuture<TextModification[]> cflf = new CoedFuture<TextModification[]>();
		cflf.set(cfl);
		return cflf;
	}

	@Override
	public IFuture<Void> goOffline() {
		// TODO Auto-generated method stub
		System.out.println("GOING OFFLINE "+path);
		return null;
	}

	@Override
	public IFuture<String> goOnline(String s) {
		// TODO Auto-generated method stub
		System.out.println("GOING ONLINE "+path);
		CoedFuture<Boolean> bf =new CoedFuture<Boolean>();
		bf.set(true);
		return null;
	}

	@Override
	public IFuture<Void> removeChangeListener(IFileChangeListener listener) {
		// TODO Auto-generated method stub
		listeners.remove(listener);
		return null;
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
			listeners.get(0).hasChanges(outer);
			
		}
	}

	@Override
	public IFuture<String> getRemoteContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<Boolean> releaseLock(TextPortion lock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<Boolean> requestLock(TextPortion lock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line) {
		change=new TextModification(1, 0, line.getText(), "meta");
		(new Changer(this)).start();

		return null;
	}

}
