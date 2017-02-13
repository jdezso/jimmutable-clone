package org.kane.base.immutability.decks;

import java.util.Collection;
import java.util.List;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.serialization.Equality;
import org.kane.base.serialization.ValidationException;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

abstract public class StandardImmutableDeckArrayList extends StandardImmutableObject
{
	private FieldArrayList contents;

	public StandardImmutableDeckArrayList(Collection contents)
	{
		this.contents = new FieldArrayList(this,contents);
	}
	
	abstract public Class getOptionalValidationType(Class default_value);

	@Override
	public void normalize() 
	{	
	}

	public void validate() 
	{
		Validator.containsNoNulls(contents);
		
		Class c = getOptionalValidationType(null);
		if ( c != null )
		{
			for ( StandardImmutableObject obj : (List<StandardImmutableObject>)contents )
			{
				if ( !c.isInstance(obj) ) 
					throw new ValidationException("Expceted contents to be of type: "+c);
			}
		}
	}
	
	public int hashCode() 
	{
		return contents.hashCode();
	}

	public List getSimpleContents() { return contents; }
	
	public boolean equals(Object obj) 
	{
		if ( obj == null ) return false;
		if ( !(obj instanceof StandardImmutableDeckArrayList) ) return false;
		
		StandardImmutableDeckArrayList other = (StandardImmutableDeckArrayList)obj;
		
		// This test is required to prevent any two empty decks from being equal...
		if ( !Equality.optionalEquals(getOptionalValidationType(null), other.getOptionalValidationType(null)) ) return false;
		
		return getSimpleContents().equals(other.getSimpleContents());
	}
	
	public int compareTo(Object o) 
	{
		if ( !(o instanceof StandardImmutableDeckArrayList) ) return 0;
		StandardImmutableDeckArrayList other = (StandardImmutableDeckArrayList)o;
		
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
}
