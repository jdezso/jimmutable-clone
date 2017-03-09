package org.kane.base.immutability.decks;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldCollection;
import org.kane.base.immutability.decks.StandardImmutableListDeck.Builder;


abstract public class StandardImmutableDeck<T extends StandardImmutableDeck<T, E>, E> extends StandardImmutableObject<T> implements Iterable<E>
{
    abstract public FieldCollection<E> getSimpleContents();
    abstract public Builder<T, E> getBuilder();
    
    @Override
    public Iterator<E> iterator()
    {
        return getSimpleContents().iterator();
    }
    
    @Override
    public int compareTo(T other)
    {
        return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
    }

    @Override
    public void freeze()
    {
        getSimpleContents().freeze();
    }

    @Override
    public int hashCode()
    {
        return getSimpleContents().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! getClass().isInstance(obj)) return false;
        
        StandardImmutableDeck<?, ?> other = (StandardImmutableDeck<?, ?>) obj;
        
        return getSimpleContents().equals(other.getSimpleContents());
    }
    
    
    public T cloneAdd(E element)
    {
        Builder<T, E> builder = getBuilder();
        builder.getSimpleContents().add(element);
        return builder.create();
    }
    
    public T cloneRemove(Object obj)
    {
        Builder<T, E> builder = getBuilder();
        builder.getSimpleContents().remove(obj);
        return builder.create();
    }
    
    public T cloneAddAll(Collection<? extends E> elements)
    {
        Builder<T, E> builder = getBuilder();
        builder.getSimpleContents().addAll(elements);
        return builder.create();
    }
    
    public T cloneRetainAll(Collection<?> elements)
    {
        Builder<T, E> builder = getBuilder();
        builder.getSimpleContents().retainAll(elements);
        return builder.create();
    }
    
    public T cloneRemoveAll(Collection<?> elements)
    {
        Builder<T, E> builder = getBuilder();
        builder.getSimpleContents().removeAll(elements);
        return builder.create();
    }
    
    public T cloneClear()
    {
        Builder<T, E> builder = getBuilder();
        builder.getSimpleContents().clear();
        return builder.create();
    }
    
    
    abstract static public class Builder<T extends StandardImmutableDeck<T, E>, E>
    {
        protected T under_construction;
        
//        protected <B extends Builder<T, E>> Builder(Function<B, T> constructor)
//        {
//            under_construction = constructor.apply(this);
//        }
        public Builder()
        {
        }
        
        public Builder(T starting_point)
        {
            under_construction = starting_point.deepMutableCloneForBuilder();
        }
        
        public FieldCollection<E> getSimpleContents()
        {
            return under_construction.getSimpleContents();
        }
        
        public T create()
        {
            return under_construction.deepClone();
        }
    }
}
