package iterator;

import java.util.Iterator;

public class ClientV1 {
	public static void main(String[] args) {
		BookCollectionV2 bookCollection=new BookCollectionV2();
		bookCollection.addBook(new Book("c"));
		bookCollection.addBook(new Book("c++"));
		bookCollection.addBook(new Book("java"));
		bookCollection.addBook(new Book("python"));
		bookCollection.addBook(new Book("javascript"));
		for(int i=0;i<bookCollection.getBooks().size();i++) {
			/*
			* System.out.println(bookCollection.getBooks().get(i));
			* due to this we need to use iterator because if internal change client should not get
			* changed
			 *
			*
			* */
		}
		Iterator<Book> iterator=bookCollection.createIterator();
		while (iterator.hasNext()){
			System.out.println(iterator.next());
		}
	}
}
