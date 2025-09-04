package iterator.book;

import java.util.Set;
import java.util.TreeSet;

public class BookCollection {
	private Set<Book> books=new TreeSet<>();
	public void addBook(Book book) {
		books.add(book);
	}

	public Set<Book> getBooks() {
		return books;
	}
}
