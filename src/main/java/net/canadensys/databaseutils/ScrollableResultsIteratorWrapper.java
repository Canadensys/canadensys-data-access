package net.canadensys.databaseutils;

import java.util.Iterator;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * Iterator<T> implementation of ScrollableResults to avoid exposing ScrollableResults directly to other layers.
 * @author canadensys
 *
 */
public class ScrollableResultsIteratorWrapper<T> implements Iterator<T> {
	private static final int DEFAULT_FLUSH_LIMIT = 1000;
	
	private ScrollableResults sr;
	private T next = null;
	private Session session;
	private int count = 0;
	
	public ScrollableResultsIteratorWrapper(ScrollableResults sr, Session session){
		this.sr = sr;
		this.session = session;
	}
	
	/**
	 * ScrollableResults does not provide a hasNext method, implemented here for Iterator interface.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasNext() {
		if(sr.next()){
			//we remember the element
			next = (T)sr.get()[0];
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		T toReturn = null;
		//next variable could be null because the last element was sent or because we iterate on the next()
		//instead of hasNext()
		if(next == null){
			//if we can retrieve an element, do it
			if(sr.next()){
				toReturn = (T)sr.get()[0];
			}
		}
		else{ //the element was fetched by hasNext, return it
			toReturn = next;
			next = null;
		}
		
		//if we are at the end, close the result set
		if(toReturn == null){
			sr.close();
		}
		else{ //clear memory to avoid memory leak
			if(count == DEFAULT_FLUSH_LIMIT){
				session.flush();
				session.clear();
				count=0;
			}
			count++;
		}
		return toReturn;
	}

	/**
	 * Unsupported Operation for this implementation.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
