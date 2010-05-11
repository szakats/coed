package coed.plugin.text.locks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.data.ICoedFile;
import coed.base.data.TextPortion;
import coed.plugin.base.Controller;

public class BasicTextLockManager implements ITextLockManager {
	private static Logger logger = Logger.getLogger(BasicTextLockManager.class.toString()); 
	private Integer delay;
	private HashMap<ICoedFile,ArrayList<TextPortion>> activeLocks;
	private HashMap<ICoedFile,ArrayList<TextPortion>> waitingLocks;
	
	public BasicTextLockManager(Integer releaseDelay) {
		this.delay=releaseDelay;
		activeLocks = new HashMap<ICoedFile, ArrayList<TextPortion>>();
		waitingLocks = new HashMap<ICoedFile, ArrayList<TextPortion>>();
	}

	@Override
	public TextPortion requestLock(IDocument doc, ICoedFile coedo, Integer lineNr) {
		
		TextPortion newLock = getLockZone(doc,lineNr);
		if (newLock != null)
		{
		logger.info("Lock for line "+lineNr+" is: "+newLock);
		
		synchronized (activeLocks){
			if (!activeLocks.containsKey(coedo)) activeLocks.put(coedo, new ArrayList<TextPortion>());
			else 
			synchronized (waitingLocks) {
				if (waitingLocks.get(coedo)!=null && waitingLocks.get(coedo).contains(newLock)) {					
					waitingLocks.get(coedo).remove(newLock);
					activeLocks.get(coedo).add(newLock);
					
					logger.info("Lock "+newLock+" was removed from dying list.");
				}
			}
		}
			
		if (!activeLocks.get(coedo).contains(newLock)){
			activeLocks.get(coedo).add(newLock);
		}
		try {
			if (coedo.requestLock(newLock).get()) {
				logger.info("Lock SUCCESS on: "+newLock);
				return newLock;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		logger.info("Lock FAIL on: "+newLock);
		return null;
	}

	private TextPortion getLockZone(IDocument doc, int lineNr) {
		
		TextPortion result;
		try {
			result = new TextPortion(doc.getLineOffset(lineNr),doc.getLineLength(lineNr));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public void findAndReleaseLocks(ICoedFile coedo, TextPortion zone) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void releaseLock(ICoedFile coedo, TextPortion ticket) {
		if (delay==0) {
			logger.info("Lock is RELEASED for: "+ticket);
			coedo.releaseLock(ticket);
		} else {
			synchronized (activeLocks) {
				activeLocks.get(coedo).remove(ticket);
			}
			Releaser releaser = new Releaser(coedo,ticket);
			releaser.run();
		}
	}
	
	private class Releaser implements Runnable {
		
		private ICoedFile coedo;
		private TextPortion ticket;

		public Releaser(ICoedFile coedo, TextPortion ticket){
			this.coedo=coedo;
			this.ticket=ticket;
		}
		
		@Override
		public void run() {
			logger.info("Lock is being released for: "+ticket+" in "+delay+"ms");
			
			if (!waitingLocks.containsKey(coedo)) waitingLocks.put(coedo, new ArrayList<TextPortion>());
			waitingLocks.get(coedo).add(ticket);
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			synchronized (activeLocks) {
				synchronized (waitingLocks) {
					if (activeLocks.get(coedo).contains(ticket)){
						
						logger.info("Lock SURVIVES: "+ticket);
						waitingLocks.get(coedo).remove(ticket);
						
					} else {
						
						logger.info("Lock is RELEASED for: "+ticket);
						coedo.releaseLock(ticket);
						
					}
				}
			}
		}
	}

}
