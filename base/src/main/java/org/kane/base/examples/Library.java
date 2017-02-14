package org.kane.base.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kane.base.immutability.decks.StandardImmutableDeckArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class serves as a "deck of books" (or a Library... he he)
 * 
 * This class is the model immutable deck pattern in the StandardObject pattern
 * 
 * @author jim.kane
 *
 */

@XStreamAlias("library")
public class Library extends StandardImmutableDeckArrayList
{
	private Library(Builder builder)
	{
		super(Collections.EMPTY_LIST);
	}
	
	public Library(Collection<Book> books)
	{
		super(books);
		
		complete();
	}
	
	public Class getOptionalValidationType(Class default_value) { return Book.class; }
	public List<Book> getSimpleBooks() { return getSimpleContents(); }
	
	static public class Builder
	{
		private Library under_construction;
		
		public Builder()
		{
			under_construction = new Library(this);
		}
		
		public Builder(Library starting_point)
		{
			under_construction = (Library)starting_point.deepMutableCloneForBuilder();
		}

		public void addBook(Book book)
		{
			under_construction.assertNotComplete();
			under_construction.getSimpleContents().add(book);
		}
		
		public Library create()
		{
			under_construction.complete();
			
			return under_construction;
		}
	}
}
