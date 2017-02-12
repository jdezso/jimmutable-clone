package org.kane.base.serialization.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;
import org.kane.base.serialization.ValidationException;

/**
 * This is a utility class that makes it easy to create a properly behaved 
 * list field of an StandardImmutableObject.
 * 
 * What's hard about that? Well, you need to have a list that is mutable (when
 * the owning object is mutable) and then immutable when the owning object is
 * Immutable. Using Collection.unmodifiableList is clunky...
 * StandardImmutableFieldArrayList takes care of the heavy lifting for you!
 * 
 * @author jim.kane
 *
 * @param <T>
 */
abstract public class AbstractFieldList<T> implements List<T>
{
	/**
	 * This object will be null when the field is serialized from either XML and
	 * JSON (null = immutable) and will be "set" otherwise (via one of the
	 * constructors)
	 */
	transient private StandardImmutableObject parent;
	
	private List<T> mutable_contents;
	
	abstract protected List createNewMutableListInstance();
	abstract protected List createFieldSubList(StandardImmutableObject parent, Collection<T> objs);
	
	public AbstractFieldList()
	{
		this(null);
	}
	
	public AbstractFieldList(StandardImmutableObject parent)
	{
		this.parent = parent;
		mutable_contents = createNewMutableListInstance();
	}
	
	public AbstractFieldList(StandardImmutableObject parent, Iterable<T> objs)
	{
		this(parent);
		
		if ( objs != null )
		{
			for ( T obj : objs )
			{
				add(obj);
			}
		}
	}


	
	public void assertNotComplete()
	{
		if ( parent == null ) // an unset parent can only occour when this object was created via xstream de-serialization... Therfore, the object is not mutable...
			throw new ImmutableException();
		else 
			parent.assertNotComplete();
	}
	
	
	public List<T> subList(int fromIndex, int toIndex)
	{
		return createFieldSubList(parent, mutable_contents.subList(fromIndex, toIndex));
	}
	
	public int size() { return mutable_contents.size(); }
	public boolean isEmpty() { return mutable_contents.isEmpty(); }
	public boolean contains(Object o) { return mutable_contents.contains(o); }
	public Object[] toArray() { return mutable_contents.toArray();	}
	public <T> T[] toArray(T[] a) { return mutable_contents.toArray(a); }
	public boolean containsAll(Collection<?> c) { return mutable_contents.containsAll(c); }
	public T get(int index) { return mutable_contents.get(index); }
	
	

	public boolean add(T e) 
	{
		assertNotComplete();
		return mutable_contents.add(e);
	}
	
	public boolean remove(Object o) 
	{
		assertNotComplete();
		return mutable_contents.remove(o);
	}

	public boolean addAll(Collection<? extends T> c) 
	{
		assertNotComplete();
		return mutable_contents.addAll(c);
	}


	
	public boolean addAll(int index, Collection<? extends T> c) 
	{
		assertNotComplete();
		return mutable_contents.addAll(index,c);
	}


	
	public boolean removeAll(Collection<?> c) 
	{
		assertNotComplete();
		return mutable_contents.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) 
	{
		assertNotComplete();
		return mutable_contents.retainAll(c);
	}


	
	public void clear() 
	{
		assertNotComplete();
		mutable_contents.clear();
	}

	
	public T set(int index, T element) 
	{
		assertNotComplete();
		return mutable_contents.set(index,element);
	}


	
	public void add(int index, T element) 
	{
		assertNotComplete();
		mutable_contents.add(index,element);
	}

	public T remove(int index) 
	{
		assertNotComplete();
		return mutable_contents.remove(index);
	}

	public int indexOf(Object o) { return mutable_contents.indexOf(o); }
	public int lastIndexOf(Object o) { return mutable_contents.lastIndexOf(o); }


	public Iterator<T> iterator() { return new MyListIterator(mutable_contents.listIterator()); }
	public ListIterator<T> listIterator() { return new MyListIterator(mutable_contents.listIterator()); }
	public ListIterator<T> listIterator(int index) { return new MyListIterator(mutable_contents.listIterator(index)); }
	

	public int hashCode() 
	{
		return mutable_contents.hashCode();
	}


	public boolean equals(Object obj) 
	{
		if (!(obj instanceof List) ) return false;
		
		List other = (List)obj;
		
		if ( other.size() != size() ) return false;
		
		for ( int i = 0; i < size(); i++ )
		{
			if ( !get(i).equals(other.get(i)) ) 
				return false;
		}
		
		return true;
	}

	public String toString() 
	{
		return super.toString();
	}

	private class MyListIterator implements ListIterator<T>
	{
		private ListIterator<T> itr;
		
		public MyListIterator(ListIterator<T> itr)
		{
			this.itr = itr;
		}

		
		public boolean hasNext() { return itr.hasNext(); }
		public T next() { return (T)itr.next(); }

		
		public boolean hasPrevious() { return itr.hasPrevious(); }
		public T previous() { return itr.previous(); }
		public int nextIndex() { return itr.nextIndex(); }
		public int previousIndex() { return itr.previousIndex(); }

		
		public void remove() 
		{
			assertNotComplete();
			itr.remove();
		}

		
		public void set(T e) 
		{
			assertNotComplete();
			itr.set(e);
		}

		
		public void add(T e) 
		{
			assertNotComplete();
			itr.add(e);
		}
	}
	
	
}
