package guru.springframework.jdbc.dao;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;

@Component
public class BookDaoImpl implements BookDao {

	private final BookRepository bookRepository;

	public BookDaoImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Book getById(Long id) {
		return bookRepository.getById(id);
	}

	@Override
	public Book saveNewBook(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Book updateBook(Book book) {
		Book stored = bookRepository.getById(book.getId());
		stored.setAuthorId(stored.getAuthorId());
		stored.setIsbn(book.getIsbn());
		stored.setPublisher(book.getPublisher());
		stored.setTitle(book.getTitle());
		return bookRepository.save(stored);
	}

	@Override
	public void deleteBookById(Long id) {
		bookRepository.deleteById(id);
	}

	@Override
	public Book findBookByTitle(String title) {
		return bookRepository.findBookByTitle(title).orElseThrow(EntityNotFoundException::new);
	}

}
