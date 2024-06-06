package com.phdang97.bookstore.assembler;

import com.phdang97.bookstore.dto.request.BookRequest;
import com.phdang97.bookstore.dto.response.BookDetailsResponse;
import com.phdang97.bookstore.dto.response.BookListingResponse;
import com.phdang97.bookstore.dto.response.BookResponse;
import com.phdang97.bookstore.entity.Book;
import com.phdang97.bookstore.entity.Category;
import com.phdang97.bookstore.entity.Section;
import org.springframework.stereotype.Component;

@Component
public class BookAssembler {
  public BookDetailsResponse toDetailsResponse(Book book) {
    BookListingResponse listingDTO =
        BookListingResponse.builder()
            .id(book.getId())
            .title(book.getTitle())
            .actualPrice(book.getActualPrice())
            .discountPrice(book.getDiscountPrice())
            .bookThumbnail(book.getBookThumbnail())
            .build();
    return BookDetailsResponse.builder()
        .bookListingResponse(listingDTO)
        .description(book.getDescription())
        .numberOfPages(book.getNumberOfPages())
        .coverImage(book.getBookCover())
        .inStock(book.getCopiesInStock() != null && book.getCopiesInStock() <= 0)
        .build();
  }

  public Book toBook(
      BookRequest request,
      String title,
      Section section,
      Category category,
      String bookCoverName,
      String bookThumbnailName) {
    return Book.builder()
        .title(title)
        .actualPrice(request.getActualPrice())
        .discountPrice(request.getDiscountPrice())
        .numberOfPages(request.getNumberOfPages())
        .copiesInStock(request.getBooksInStock() == null ? 1 : request.getBooksInStock())
        .copiesToBePrinted(request.getBooksToBePrinted())
        .description(request.getDescription())
        .soldCopies(request.getSoldBooks())
        .bookCover(bookCoverName)
        .bookThumbnail(bookThumbnailName)
        .section(section)
        .category(category)
        .build();
  }

  public BookResponse toBookResponse(Book book) {
    return BookResponse.builder()
        .id(book.getId())
        .title(book.getTitle())
        .actualPrice(book.getActualPrice())
        .discountPrice(book.getDiscountPrice())
        .bookThumbnail(book.getBookThumbnail())
        .description(book.getDescription())
        .numberOfPages(book.getNumberOfPages())
        .bookCover(book.getBookCover())
        .copiesInStock(book.getCopiesInStock())
        .soldCopies(book.getSoldCopies())
        .copiesToBePrinted(book.getCopiesToBePrinted())
        .creationDate(book.getCreationTime())
        .updatedDate(book.getLastUpdatedTime())
        .section(book.getSection())
        .category(book.getCategory())
        .build();
  }
}
