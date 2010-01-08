package coed.base.util;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import coed.base.data.exceptions.CoedFutureException;

public class CoedFuture<T> implements IFuture<T> {
	private LinkedList<IFutureListener<T>> listeners;
	private LinkedList<IFutureErrorListener> errorListeners;
	private T result;
	private CoedFutureException exception;
	private boolean done;
	
	private static final TimeoutException timeoutException = new TimeoutException();
	
	public CoedFuture() {
		done = false;
	}
	
	public CoedFuture(T result) {
		this.result = result;
		done = true;
	}
	
	public CoedFuture(Throwable e) {
		this.exception = new CoedFutureException(e);
		done = true;
	}

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
		if(exception != null)
			throw exception;
		return result;
	}

	/**
	 * Waits if necessary for at most the given time for the computation to complete, 
	 * and then retrieves its result, if available. 
	 * @throws timeout - the maximum time to wait
	 * @throws unit - the time unit of the timeout argument 
	 * @throws CancellationException - if the computation was cancelled 
	 * @throws ExecutionException - if the computation threw an exception 
	 * @throws InterruptedException - if the current thread was interrupted while waiting 
	 * @throws TimeoutException - if the wait timed out
	 * @return the computed result 
	 */
	@Override
	public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		assert(unit != null);
		if(!isDone()) {
			this.wait(unit.toMillis(timeout));
			if(!isDone())
				throw timeoutException;
		}
		if(exception != null)
			throw exception;
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
	public synchronized void addListener(IFutureListener<T> listener) {
		assert(listener != null);
		if(!isDone()) {
			if(listeners == null)
				listeners = new LinkedList<IFutureListener<T>>();
			listeners.add(listener);
		} else if(exception == null)
			listener.got(result);
		else
			listener.caught(exception.getCause());
	}
	
	public synchronized void set(T result) {
		assert(!isDone());
		// result is allowed to be null
		this.result = result;
		done = true;
		notifyDone();
	}
	
	public synchronized void throwEx(Throwable e) {
		assert(!isDone());
		exception = new CoedFutureException(e);
		done = true;
		notifyDone();
	}
	
	public synchronized void chain(IFuture<T> future) {
		addListener(new IFutureListener<T>() {
			@Override
			public void got(T result) {
				set(result);
			}
			@Override
			public void caught(Throwable e) {
				throwEx(e);
			}
		});
	}
	
	private synchronized void notifyDone() {
		// notify all listeners
		if(exception != null) {
			if(listeners != null)
				for(IFutureListener<T> listener : listeners)
					listener.caught(exception.getCause());
			else if(errorListeners != null)
				for(IFutureErrorListener listener : listeners)
					listener.caught(exception.getCause());
		} else if(listeners != null)
			for(IFutureListener<T> listener : listeners)
				listener.got(result);
				
		listeners = null;
		errorListeners = null;
		// notify all waiting threads
		notifyAll();
	}

	@Override
	public void addErrorListener(IFutureErrorListener listener) {
		assert(listener != null);
		if(!isDone()) {
			if(errorListeners == null)
				errorListeners = new LinkedList<IFutureErrorListener>();
			errorListeners.add(listener);
		} else if(exception != null)
			listener.caught(exception.getCause());
	}
}
