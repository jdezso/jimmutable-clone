package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;

abstract public class FieldMap<K,V> implements Map<K,V>
{
	/**
	 * This object will be null when the field is serialized from either XML and
	 * JSON (null = immutable) and will be "set" otherwise (via one of the
	 * constructors)
	 */
	private StandardImmutableObject parent;
	
	private Map<K,V> contents;
	
	abstract protected Map<K,V> createNewMutableMapInstance();
	
	public FieldMap()
	{
		this(null);
		
	}
	
	public FieldMap(StandardImmutableObject parent)
	{
		this.parent = parent;
		contents = createNewMutableMapInstance();
	}
	
	public FieldMap(StandardImmutableObject parent, Map<K,V> initial_values)
	{
		this(parent);
		
		if ( initial_values != null )
			contents.putAll(initial_values);
	}
	
	public void assertNotComplete()
	{
		if ( parent == null ) // this should never happen, but... should it take place... default to immutable...
			throw new ImmutableException();
		else 
			parent.assertNotComplete();
	}
	
	public int size() { return contents.size(); }
	public boolean isEmpty() { return contents.isEmpty(); }
	public boolean containsKey(Object key) { return contents.containsKey(key); }
	public boolean containsValue(Object value) { return contents.containsValue(value); }
	public V get(Object key) { return contents.get(key); }

	
	public V put(K key, V value)
	{
		assertNotComplete();
		return contents.put(key, value);
	}

	
	public V remove(Object key)
	{
		assertNotComplete();
		return contents.remove(key);
	}

	
	public void putAll(Map<? extends K, ? extends V> m)
	{
		assertNotComplete();
		contents.putAll(m);
	}

	
	public void clear()
	{
		assertNotComplete();
		contents.clear();
	}

	
	public Set<K> keySet()
	{
		return (Set<K>)new MyKeySet();
	}

	
	public Collection<V> values()
	{
		return new MyValuesCollection();
	}

	
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return new MyEntrySet();
	}

	private class MyKeySet<K> extends FieldSet<K>
	{
		private MyKeySet()
		{
			super(parent);
		}
		
		protected Collection createNewMutableInstance()
		{
			return contents.keySet();
		}
	}
	
	private class MyEntrySet extends FieldSet
	{
		private MyEntrySet()
		{
			super(parent);
		}
		
		protected Set createNewMutableInstance()
		{
			return contents.entrySet();
		}
	}
	
	private class MyValuesCollection<V> extends FieldCollection<V>
	{
		private MyValuesCollection()
		{
			super(parent);
		}
		
		protected Collection<V> createNewMutableInstance()
		{
			return (Collection<V>)contents.values();
		}
	}

	
	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Map) ) return false;
		
		Map other = (Map)obj;
		
		if ( size() != other.size() ) return false;
		
		return entrySet().containsAll(other.entrySet());
	}

	
	public String toString() 
	{
		return contents.toString();
	}
	
	
}
