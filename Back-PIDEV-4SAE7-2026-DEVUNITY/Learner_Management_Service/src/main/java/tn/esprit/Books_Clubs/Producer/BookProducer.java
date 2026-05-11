package tn.esprit.Books_Clubs.Producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.Config.RabbitMQConfig;
import tn.esprit.Books_Clubs.DTO.BookDTO;
import tn.esprit.Books_Clubs.entities.Book;
import tn.esprit.repositories.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookProducer {

    private final RabbitTemplate rabbitTemplate;
    private final BookRepository bookRepository;

    public void sendBook(BookDTO bookDTO) {
        log.info("Sending Book to RabbitMQ: {}", bookDTO.getTitle());
        rabbitTemplate.convertAndSend(RabbitMQConfig.BOOK_QUEUE, bookDTO);
    }

    public void syncAllBooks() {
        log.info("Synchronizing all existing books to Elasticsearch...");
        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            BookDTO dto = BookDTO.builder()
                    .bookId(book.getBookId())
                    .title(book.getTitle())
                    .isbn(book.getIsbn())
                    .status(book.getStatus() != null ? book.getStatus().name() : null)
                    .salePrice(book.getSalePrice())
                    .authorName(book.getAuthor() != null ? book.getAuthor().getName() : null)
                    .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
                    .build();
            sendBook(dto);
        }
        log.info("Synchronization of {} books completed.", books.size());
    }
}
