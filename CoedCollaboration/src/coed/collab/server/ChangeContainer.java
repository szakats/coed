/**
 * 
 */
package coed.collab.server;

import java.util.Vector;

/**
 * @author Neobi008
 * Container with generic parameters, that is synchronized, and
 * we can access the elements by the indices.
 */
public class ChangeContainer<T> {
	
	private Vector<T> container = new Vector<T>();
	
	public ChangeContainer(){
		container = new Vector<T>();
	}
	
	public void add(int index, T element){
		container.add(index, element);
	}
	
	public T remove(int index){
		return container.remove(index);
	}
	
	public T elementAt(int index){
		return container.elementAt(index);
	}
	
	public int size(){
		return container.size();
	}

}
