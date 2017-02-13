package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.HashSet;

import org.kane.base.immutability.StandardImmutableObject;

public class FieldHashSet<T> extends FieldSet<T>
{
	public FieldHashSet()
	{
		super();
	}
	
	public FieldHashSet(StandardImmutableObject parent)
	{
		super(parent);
	}
	
	public FieldHashSet(StandardImmutableObject parent, Iterable<T> objs)
	{
		super(parent,objs);
	}
	
	protected Collection<T> createNewMutableInstance()
	{
		return new HashSet();
	}

}