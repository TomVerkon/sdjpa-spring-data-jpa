package guru.springframework.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = { "guru.springframework.jdbc.dao" })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

	@Autowired
	BookRepository bookRepository;
	
	@Test
	void testBookNamedQuery() {
		Book book = bookRepository.byTitleJpa("Clean Code");
		assertNotNull(book);
	}
	
	@Test
	void testBookQueryNative() {
		Book book = bookRepository.findBookByTitleNativeQuery("Clean Code");
		assertNotNull(book);
	}
	
	@Test
	void testBookQueryNamed() {
		Book book = bookRepository.findBookByTitleWithQueryNamed("Clean Code");
		assertNotNull(book);
	}
	
	@Test
	void testBookQuery() {
		Book book = bookRepository.findBookByTitleWithQuery("Clean Code");
		assertNotNull(book);
	}
	
	@Test
	void testBookFuture() throws ExecutionException, InterruptedException {
		Future<Book> bookFuture = bookRepository.queryByTitle("Clean Code");
		
		Book book = bookFuture.get();
		assertNotNull(book);
	}

	@Test
	void testGetBookByNameNullParam() {
		assertNull(bookRepository.getByTitle(null));
	}

	@Test
	void testGetBookByNameNoException() {
		assertNull(bookRepository.getByTitle("Clean Cod"));
	}

	@Test
	void testGetBookByNameEmptyResultException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			bookRepository.readByTitle("Clean Cod");
		});
	}
	
	@Test
	void testBookStream() {
		AtomicInteger count = new AtomicInteger();
		bookRepository.findAllByTitleNotNull().forEach(book -> {
			count.incrementAndGet();
		});
		
		assertThat(count.get()).isGreaterThan(2);
	}

}
