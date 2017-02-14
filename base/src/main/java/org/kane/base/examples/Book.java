package org.kane.base.examples;

import java.util.Collection;
import java.util.List;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.serialization.Equality;
import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.Normalizer;
import org.kane.base.serialization.StandardObject;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * An example of a proper implementation of a StandardImmutableObject
 * 
 * @author jim.kane
 *
 */
@XStreamAlias("book")
final public class Book extends StandardImmutableObject
{
	private String title; // required, upper-case
	private int page_count; // required, must be 0 or greater
	
	private String isbn; // optional
	
	private BindingType binding; // required

	private FieldArrayList<String> authors;
	
	// builder constructor...
	private Book(Builder builder)
	{
		// building constructor.  Builder will call complete...
		this.authors = new FieldArrayList();
	}
	
	// copy constructor...
	public Book(String title, int page_count, String isbn, BindingType binding, Collection<String> authors)
	{
		// building copy constructor.  Builder will call complete...
		this.title = title;
		this.page_count = page_count;
		this.isbn = isbn;
		this.binding = binding;
		
		this.authors = new FieldArrayList(authors);
		
		complete();
	}

	/**
	 * Normalize the book object (convert the title to upper case)
	 */
	public void normalize() 
	{
		title = Normalizer.upperCase(title);
	}
	
	/**
	 * Validate the object 
	 */
	public void validate()
	{
		Validator.notNull(title);
		Validator.min(page_count, 0);
		
		Validator.notNull(binding);
		
		Validator.containsNoNulls(authors);
	}
	
	/**
	 * Freeze the object
	 */
	public void freeze()
	{
		authors.freeze();
	}
	
	public String getSimpleTitle() { return title; } 
	public int getSimplePageCount() { return page_count; }
	
	public BindingType getSimpleBinding() { return binding; }
	public List<String> getSimpleAuthors() { return authors; }
	
	public boolean hasISBN() { return isbn != null; }
	public String getOptionalISBN(String default_value)
	{
		if ( isbn == null ) return default_value;
		return isbn;
	}

	public int compareTo(Object o) 
	{
		if ( !(o instanceof Book) ) return 0;
		
		return getSimpleTitle().compareTo(((Book)o).getSimpleTitle());
	}

	public int hashCode() 
	{
		return getSimpleTitle().hashCode();
	}

	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Book) ) return false;
		
		Book other = (Book)obj;
		
		if ( !getSimpleTitle().equals(other.getSimpleTitle()) ) return false;
		if ( getSimplePageCount() != other.getSimplePageCount() ) return false;
		
		if ( !Equality.optionalEquals(isbn, other.isbn) ) return false;
		
		if ( getSimpleBinding() != other.getSimpleBinding() ) return false;
		
		if ( !getSimpleAuthors().equals(other.getSimpleAuthors()) ) return false;
		
		return true;
	}
	
	static public class Builder
	{
		private Book under_construction;
		
		public Builder()
		{
			under_construction = new Book(this);
		}
		
		public Builder(Book starting_point)
		{
			under_construction = (Book)starting_point.deepMutableCloneForBuilder();
		}
		
		public void setTitle(String title) 
		{ 
			under_construction.assertNotComplete(); 
			under_construction.title = title;
		}
		
		public void setPageCount(int count)
		{
			under_construction.assertNotComplete(); 
			under_construction.page_count = count;
		}
		
		public void setISBN(String isbn)
		{
			under_construction.assertNotComplete();
			under_construction.isbn = isbn;
		}
		
		public void addAuthor(String author)
		{
			under_construction.assertNotComplete();
			under_construction.authors.add(author);
		}
		
		public void setBindingType(BindingType type)
		{
			under_construction.assertNotComplete();
			under_construction.binding = type;
		}
		
		public Book create()
		{
			under_construction.complete();
			return under_construction;
		}
	}
	
	static public void main(String args[]) throws Exception
	{
		Builder b = new Builder();
		
		b.setTitle("Of Mice and Men");
		b.setPageCount(911);
		b.setISBN("234234234234");
		b.addAuthor("John Steinbeck");
		b.addAuthor("Ed Weisman");
		b.setBindingType(BindingType.PAPER_BACK);
		
		Book book = b.create();
		
		System.out.println(JavaCodeUtils.toJavaStringLiteral(book));
	}
}

