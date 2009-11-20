package coed.base.util;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CoedFuture<T> implements IFuture<T> {
	private LinkedList<IFutureListener<T>> listeners;
	private T result;
	private boolean done = false;
	
	private static final TimeoutException timeoutException = new TimeoutException();

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized T get() throws InterruptedException, ExecutionException {
		if(!isDone()) {
			this.wait();
			assert(isDone());
		}
		
		return result;
	}

	@Override
	public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		assert(unit != null);
		if(!isDone()) {
			this.wait(unit.toMillis(timeout));
			if(!isDone())
				throw timeoutException;
		}
		
		return result;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized boolean isDone() {
		return done;
	}

	@Override
	public synchronized void add(IFutureListener<T> listener) {
		assert(listener != null);
		if(!isDone()) {
			if(listeners == null)
				listeners = new LinkedList<IFutureListener<T>>();
			listeners.add(listener);
		} else
			listener.got(result);
	}
	
	public synchronized void set(T result) {
		assert(!isDone());
		// result is allowed to be null
		this.result = result;
		done = true;
		notifyListeners();
		notifyAll();
	}
	
	public synchronized void chain(IFuture<T> future) {
		add(new IFutureListener<T>() {
			@Override
			public void got(T result) {
				set(result);
			}
		});
	}
	
	private synchronized void notifyListeners() {
		if(listeners != null)
			for(IFutureListener<T> listener : listeners)
				listener.got(result);
		listeners = null;
	}
}
