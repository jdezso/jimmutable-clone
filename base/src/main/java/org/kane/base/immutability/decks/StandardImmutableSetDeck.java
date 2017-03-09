package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldSet;


abstract public class StandardImmutableSetDeck<T extends StandardImmutableSetDeck<T, E>, E> extends StandardImmutableDeck<T, E>
{
	@Override
    abstract public FieldSet<E> getSimpleContents();
    
    
    abstract static public class Builder<T extends StandardImmutableSetDeck<T, E>, E> extends StandardImmutableDeck.Builder<T, E>
    {
        public Builder()
        {
            super();
        }
        
        public Builder(T starting_point)
        {
            super(starting_point);
        }
        
        public FieldSet<E> getSimpleContents()
        {
            return under_construction.getSimpleContents();
        }
        
        public T create()
        {
            return under_construction.deepClone();
        }
    }
}
