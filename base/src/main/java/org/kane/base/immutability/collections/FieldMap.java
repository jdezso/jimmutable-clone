package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * An implementation of a {@link Collection} that begins life as mutable but can,
 * at any time, be "{@link #freeze() frozen}" (made immutable). In other
 * words, a wrapper for a {@link Collection} that implements {@link Field}.
 * 
 * <p>This class is designed to be extended. Most of the <em>collection hierarchy</em>
 * is already wrapped as part of the standard <em>jimmutable</em> library, but further
 * extensions should go quickly as the base class does nearly all of the work. However,
 * extension implementors should take time to carefully understand the immutability
 * principles involved and to write careful unit tests to make sure that the implementations
 * are as strictly immutable as possible.
 * 
 * @author Jim Kane
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
abstract public class FieldMap<K,V> implements Map<K,V>, Field
{
	transient volatile private boolean is_frozen;
	
	/*
	 * Never access _contents_ directly.
	 * Use getContents so that SubList (and future) inheritance works
	 * correctly.
	 */
	private Map<K,V> contents = createNewMutableInstance();
	
	/**
	 * Get the mutable contents of the {@link Map} that this object wraps.
	 * 
	 * <p>This is the main interface between the {@link Field} specification
	 * that this implementation enforces and the {@link Map} that it wraps.
	 * 
	 * @return The <em>mutable</em> map that this object wraps
	 */
	protected Map<K,V> getContents() { return contents; }

	/**
	 * Instantiate a <em>new</em>, <em>mutable</em> {@link Map}.
	 * This allows sub-classes to control the {@link Map} implementation
	 * that is used (e.g. {@link HashMap}, {@link TreeMap}, etc.).
	 *  
	 * @return The new {@link Map} instance
	 */
	abstract protected Map<K,V> createNewMutableInstance();
	
	
	/**
	 * Default constructor (for an empty map)
	 */
	public FieldMap()
	{
		is_frozen = false;
	}
	
	/**
     * Constructs a collection containing the elements of the specified {@link Map},
     * in the order they are returned by the {@link Iterable#iterator() iterator}.
     *
     * @param objs The {@code Map} whose elements are to be placed into this map
     * 
     * @throws NullPointerException if the specified {@code Map} is {@code null}
	 */
	public FieldMap(Map<K,V> initial_values)
	{
		this();
		
		if ( initial_values != null )
			contents.putAll(initial_values);
	}
	
	@Override
	public void freeze() { is_frozen = true; }
	
	@Override
	public boolean isFrozen()  { return is_frozen; }
	
	@Override
	public int size() { return getContents().size(); }
	
	@Override
	public boolean isEmpty() { return getContents().isEmpty(); }
	
	@Override
	public boolean containsKey(Object key) { return getContents().containsKey(key); }
	
	@Override
	public boolean containsValue(Object value) { return getContents().containsValue(value); }
	
	@Override
	public V get(Object key) { return getContents().get(key); }

	@Override
	public V put(K key, V value)
	{
		assertNotFrozen();
		return getContents().put(key, value);
	}
	
	@Override
	public V remove(Object key)
	{
		assertNotFrozen();
		return getContents().remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		assertNotFrozen();
		getContents().putAll(m);
	}
	
	@Override
	public void clear()
	{
		assertNotFrozen();
		getContents().clear();
	}
	
	@Override
	public Set<K> keySet()
	{
		return new InnerSet<>(getContents().keySet());
	}

	@Override
	public Collection<V> values()
	{
		return new InnerCollection<>(getContents().values());
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		return new InnerSet<>(getContents().entrySet());
	}

	@Override
	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Map) ) return false;
		
		Map<?, ?> other = (Map<?, ?>)obj;
		
		if ( size() != other.size() ) return false;
		
		return entrySet().equals(other.entrySet());
	}
	
	@Override
	public String toString() 
	{
		return getContents().toString();
	}
	
	private class InnerCollection<E> extends FieldCollection<E>
	{
		private Collection<E> inner_contents;
		
		private InnerCollection(Collection<E> collection)
		{
			inner_contents = collection;
		}

		@Override
		protected Collection<E> getContents()
		{
			return inner_contents;
		}

		@Override
		public void freeze()
		{
			FieldMap.this.freeze();
		}

		@Override
		public boolean isFrozen()
		{
			return FieldMap.this.isFrozen();
		}
	}
	
	private class InnerSet<E> extends InnerCollection<E> implements Set<E>
	{
		private InnerSet(Collection<E> collection)
		{
			super(collection);
		}
	}
}
