package com.project.practice.service;

import com.project.practice.model.Book;
import com.project.practice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found for this id :: " + id));
        
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setYear(bookDetails.getYear());
        book.setPdf(bookDetails.getPdf());
        book.setImg(bookDetails.getImg());
        book.setCategories(bookDetails.getCategories());
        book.setMembership(bookDetails.getMembership());
        book.setRating(bookDetails.getRating());
        book.setUpdated(bookDetails.getUpdated());
        book.setDescription(bookDetails.getDescription());
        return bookRepository.save(book); 
    }
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}