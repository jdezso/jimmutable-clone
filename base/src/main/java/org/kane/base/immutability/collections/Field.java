package org.kane.base.immutability.collections;

import java.util.Collection;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;

/**
 * An interface to represent a member of a {@link StandardImmtuableObject} that
 * requires custom code to {@link StandardImmutableObject#freeze() freeze} it.
 * 
 * <p>{@code StandardImmutableObject} does not require that it's members implement
 * {@code Field}, but it is useful as a declarative interface as a way for a
 * developer to signal adherence to the behaviors defined by {@code Field}
 * (i.e. well-behaved {@link #freeze() freezing).
 * 
 * <p>Most notably, most {@link Collection} or {@link Map} implementations in the
 * JDK will have a matching wrapper that also implements {@code Field}.
 * 
 * @author Jim Kane
 */
public interface Field
{
	/**
	 * Make any changes to this object required to make this object
	 * immutable.
	 * 
	 * @see StandardImmutableObject#freeze()
	 */
	public void freeze();
	
	/**
     * Returns {@code true} if this object is {@link #freeze() frozen}.
     *
     * @return {@code true} if this object is {@link #freeze() frozen}
	 */
	public boolean isFrozen();
	
	/**
	 * Test to see if this object is {@link #freeze() frozen}
	 * 
	 * @throws ImmutableException if this {@link Field} is already {@link #isFrozen() frozen}
	 */
	default public void assertNotFrozen() 
	{ 
		if ( isFrozen() ) 
			throw new ImmutableException();
	}
}
