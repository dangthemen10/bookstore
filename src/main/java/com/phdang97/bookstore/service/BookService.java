package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.BookRequest;
import com.phdang97.bookstore.dto.response.BookListingResponse;
import com.phdang97.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
  Book getBookById(Integer bookId);

  Book getBookResponse(Integer bookId);

  Book getBookDetails(Integer bookId);

  Page<BookListingResponse> getBooksBySectionAndCategory(
      Integer sectionId, Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy);

  Page<BookListingResponse> getBooksByTitle(String title, Integer pageNumber, Integer pageSize);

  Book addBook(BookRequest request, MultipartFile bookCover, MultipartFile bookThumbnail);

  Book updateBook(
      Integer bookId, BookRequest request, MultipartFile bookCover, MultipartFile bookThumbnail);

  String deleteBook(Integer bookId);

  void validateBookAvailability(Integer bookId, Integer quantity, String bookTitle);
}
