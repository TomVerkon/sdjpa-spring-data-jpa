package guru.springframework.jdbc.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import guru.springframework.jdbc.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	Optional<Author> findAuthorByFirstNameAndLastName(String firstname, String lastName);
}
