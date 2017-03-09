package org.kane.base.examples.book;

import java.util.Collection;
import java.util.Collections;

import org.kane.base.examples.book.BookDeckList.Builder;
import org.kane.base.immutability.collections.FieldHashSet;
import org.kane.base.immutability.collections.FieldSet;
import org.kane.base.immutability.decks.StandardImmutableListDeck;
import org.kane.base.immutability.decks.StandardImmutableSetDeck;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("book-set")
final public class BookDeckSet extends StandardImmutableSetDeck<BookDeckSet, Book>
{
	private FieldHashSet<Book> books = new FieldHashSet<>();
	
	
	private BookDeckSet(Builder builder)
	{
	}
	
	public BookDeckSet()
	{
		this(Collections.emptySet());
	}
	
	public BookDeckSet(Collection<Book> books)
	{
		if ( books != null )
		{
			this.books.addAll(books);
		}
		
		complete();
	}
	
	public void normalize() 
	{	
	}
	
	public void validate() 
	{
		Validator.containsNoNulls(books);
		Validator.containsOnlyInstancesOf(Book.class, books);
	}
	
	public FieldSet<Book> getSimpleContents() { return books; }

    @Override
    public Builder getBuilder()
    {
        return new Builder(this);
    }

    final static public class Builder extends StandardImmutableSetDeck.Builder<BookDeckSet, Book>
    {
        public Builder()
        {
//            super((Function<Builder, BookDeckSet>) BookDeckSet::new);
            under_construction = new BookDeckSet(this);
        }
        
        public Builder(BookDeckSet starting_point)
        {
            super(starting_point);
        }
    }
}