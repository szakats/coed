package coed.test.base.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import coed.base.util.CoedFuture;
import coed.base.util.IFutureListener;

/**
 * Test case for the implementation of futures
 * @author szakats
 *
 */
public class FutureTest  {
	
  @Before public void setUp() {
   
  }
  
  CoedFuture<String> f;
  Boolean gotValue;
  Boolean gotError;
  String result;
  Throwable error;
  
  void initGot() {
	  gotValue = false;
	  gotError = false;
	  result = null;
	  error = null;
  }
  
  class ListenerTest implements IFutureListener<String> {
	@Override
	public void got(String r) {
		gotValue = true;
		result = r;
	}

	@Override
	public void caught(Throwable e) {
		gotError = true;
		error = e;
	}
  }

  @Test public void testResult() {
	  f = new CoedFuture<String>();
	  assertTrue(!f.isDone());
	  f.addListener(new ListenerTest());
	  
	  class TestThread extends Thread {
		  public String res = null;
		  public void run() {
			try {
				res = f.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
	  }
	  
	  TestThread t = new TestThread();
	  t.start();
	  
	  initGot();
	  String r = "good";
	  f.set(r);
	  assertTrue(f.isDone() && gotValue && !gotError && result == r);
	  try {
		assertTrue(f.get() == r);
	  } catch (Exception e) {
		e.printStackTrace();
		fail();
	  }
	  
	  try {
		Thread.sleep(1000);
	  } catch (InterruptedException e) {
		e.printStackTrace();
	  }
	  
	  assertTrue(t.res == r);
  }
  
  @Test public void testError() {
	  f = new CoedFuture<String>();
	  assertTrue(!f.isDone());
	  f.addListener(new ListenerTest());
	  
	  class TestThread extends Thread {
		  public Exception x = null;
		  public void run() {
			try {
				f.get();
			} catch (Exception e) {
				x = e;
			}
		  }
	  }
	  
	  TestThread t = new TestThread();
	  t.start();
	  
	  initGot();
	  Exception e = new Exception(); 
	  f.throwEx(e);
	  assertTrue(f.isDone() && !gotValue && gotError && error == e);
	  try {
		  f.get();
		  fail();
	  } catch(ExecutionException x) {
		  assertTrue(x.getCause() == e);
	  } catch(Exception x) {
		  x.printStackTrace();
		  fail();
	  }
	  
	  try {
		Thread.sleep(1000);
	  } catch (InterruptedException x) {
		x.printStackTrace();
      }
		  
	  assertTrue(t.x instanceof ExecutionException && t.x.getCause() == e);
  }
}
