package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Book;
import com.study.jpa.bookmanager.domain.Publisher;
import com.study.jpa.bookmanager.domain.Review;
import com.study.jpa.bookmanager.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void bookTest() {
        Book book = new Book();
        book.setName("Jpa 초격차 패키지");
        book.setName("패스트 캠퍼스");
        book.setAuthorId(1L);

        bookRepository.save(book);

        System.out.println(bookRepository.findAll());
    }

    @Test
    @Transactional
    void bookRelationTest(){
        Review result = givenBookAndReview();

        Users user = usersRepository.findById(3L).orElseThrow(RuntimeException::new);

        System.out.println("Review : " + user.getReviews());
        System.out.println("Book : " + user.getReviews().get(0).getBook());
        System.out.println("Publisher : " + user.getReviews().get(0).getBook().getPublisher());
    }

    private Review givenBookAndReview(){
        return givenReview(givenUser(), givenBook(givenPublisher()));
    }

    private Book givenBook(Publisher publisher){
        Book book = new Book();
        book.setName("JPA 초격차 패키지");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private Users givenUser(){
        return usersRepository.findById(3L).orElseThrow(RuntimeException::new);
    }

    private Review givenReview(Users user, Book book){
        Review review = new Review();
        review.setTitle("좋은 책");
        review.setContent("넘 좋아요");
        review.setScore(5.0f);
        review.setUser(user);
        review.setBook(book);

        return reviewRepository.save(review);
    }

    private Publisher givenPublisher(){
        Publisher publisher = new Publisher();
        publisher.setName("패스트 캠퍼스");

        return publisherRepository.save(publisher);
    }

}
