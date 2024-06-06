package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.assembler.BookAssembler;
import com.phdang97.bookstore.dto.request.BookRequest;
import com.phdang97.bookstore.dto.response.BookListingResponse;
import com.phdang97.bookstore.entity.Book;
import com.phdang97.bookstore.entity.Category;
import com.phdang97.bookstore.entity.Section;
import com.phdang97.bookstore.enums.ImageType;
import com.phdang97.bookstore.enums.SortBy;
import com.phdang97.bookstore.exception.ImageException;
import com.phdang97.bookstore.repository.BookRepository;
import com.phdang97.bookstore.service.BookService;
import com.phdang97.bookstore.service.CategoryService;
import com.phdang97.bookstore.service.MultipartFileService;
import com.phdang97.bookstore.service.SectionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
  @Autowired private final BookRepository bookRepository;

  @Autowired private final SectionService sectionService;

  @Autowired private final CategoryService categoryService;

  @Autowired private final MultipartFileService multipartFileService;

  @Override
  public Book getBookDetails(Integer bookId) {
    return getBookById(bookId);
  }

  @Override
  public Book getBookResponse(Integer bookId) {
    Optional<Book> bookOptional = bookRepository.findBookResponseById(bookId);
    return bookOptional.orElseThrow(
        () ->
            new EntityNotFoundException(String.format("The book with id %s is not found", bookId)));
  }

  @Override
  public Page<BookListingResponse> getBooksBySectionAndCategory(
      Integer sectionId, Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    if (SortBy.convert(sortBy) == SortBy.DATE) {
      return bookRepository.filterBooks(sectionId, categoryId, SortBy.DATE.name(), pageable);
    } else {
      return bookRepository.filterBooks(sectionId, categoryId, SortBy.POPULARITY.name(), pageable);
    }
  }

  @Override
  public Page<BookListingResponse> getBooksByTitle(
      String title, Integer pageNumber, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return bookRepository.findBooksByTitle(title, pageable);
  }

  @Override
  public Book addBook(BookRequest request, MultipartFile bookCover, MultipartFile bookThumbnail) {
    String title = request.getTitle();
    Integer sectionId = request.getSectionId();
    Integer categoryId = request.getCategoryId();
    validateBookDuplicate(title);
    Section section = sectionService.getSectionById(sectionId);
    Category category = categoryService.getCategoryByNameAndSectionName(categoryId, sectionId);
    if (bookCover.isEmpty() || bookThumbnail.isEmpty()) {
      throw new ImageException("book cover image and book thumbnail image should not be null");
    }
    String bookCoverName = multipartFileService.uploadImage(bookCover, ImageType.COVER);
    String bookThumbnailName = multipartFileService.uploadImage(bookThumbnail, ImageType.THUMBNAIL);
    Book newBook =
        new BookAssembler()
            .toBook(request, title, section, category, bookCoverName, bookThumbnailName);
    bookRepository.save(newBook);
    return newBook;
  }

  //  @Override
  //  public Book updateBook(
  //      Integer bookId, BookRequest request, MultipartFile bookCover, MultipartFile bookThumbnail)
  // {
  //    Book book = getBookById(bookId);
  //
  //    String title = request.getTitle();
  //    if (!title.equals(book.getTitle())) {
  //      validateBookDuplicate(title);
  //    }
  //    book.setTitle(title);
  //
  //    Section newSection = sectionService.getSectionById(request.getSectionId());
  //    Category newCategory =
  //        categoryService.getCategoryByNameAndSectionName(
  //            request.getCategoryId(), request.getSectionId());
  //    Section section = book.getSection();
  //    Category category = book.getCategory();
  //
  //    if (!section.getName().equals(newSection.getName())
  //        || !category.getName().equals(newCategory.getName())) {
  //      book.setSection(newSection);
  //      book.setCategory(newCategory);
  //    }
  //
  //    if (!bookCover.isEmpty()) {
  //      multipartFileService.deleteImage(book.getBookCover());
  //      book.setBookCover(multipartFileService.uploadImage(bookCover, ImageType.COVER));
  //    }
  //
  //    if (!bookThumbnail.isEmpty()) {
  //      multipartFileService.deleteImage(book.getBookCover());
  //      book.setBookThumbnail(multipartFileService.uploadImage(bookCover, ImageType.THUMBNAIL));
  //    }
  //
  //    book.setActualPrice(request.getActualPrice());
  //    book.setDiscountPrice(request.getDiscountPrice());
  //    book.setNumberOfPages(request.getNumberOfPages());
  //    book.setCopiesInStock(request.getBooksInStock() == null ? 1 : request.getBooksInStock());
  //    if (request.getBooksToBePrinted() != null) {
  //      book.setCopiesToBePrinted(request.getBooksToBePrinted());
  //    }
  //
  //    if (request.getDescription() != null) {
  //      book.setDescription(request.getDescription());
  //    }
  //
  //    if (request.getSoldBooks() != null) {
  //      book.setSoldCopies(request.getSoldBooks());
  //    }
  //
  //    bookRepository.save(book);
  //    return book;
  //  }

  @Override
  public Book updateBook(
      Integer bookId, BookRequest request, MultipartFile bookCover, MultipartFile bookThumbnail) {
    Book book = getBookById(bookId);

    updateBookDetails(book, request);

    updateBookImages(book, bookCover, bookThumbnail);

    bookRepository.save(book);
    return book;
  }

  @Override
  public String deleteBook(Integer bookId) {
    Book book = getBookById(bookId);
    multipartFileService.deleteImage(book.getBookCover());
    multipartFileService.deleteImage(book.getBookThumbnail());
    bookRepository.deleteById(bookId);
    return "Book deleted successfully!";
  }

  private void updateBookDetails(Book book, BookRequest request) {
    String newTitle = request.getTitle();
    validateAndSetBookTitle(book, newTitle);

    Section newSection = sectionService.getSectionById(request.getSectionId());
    Category newCategory =
        categoryService.getCategoryByNameAndSectionName(
            request.getCategoryId(), request.getSectionId());

    updateBookSectionAndCategory(book, newSection, newCategory);

    book.setActualPrice(request.getActualPrice());
    book.setDiscountPrice(request.getDiscountPrice());
    book.setNumberOfPages(request.getNumberOfPages());
    book.setCopiesInStock(request.getBooksInStock() == null ? 1 : request.getBooksInStock());

    if (request.getBooksToBePrinted() != null) {
      book.setCopiesToBePrinted(request.getBooksToBePrinted());
    }

    if (request.getDescription() != null) {
      book.setDescription(request.getDescription());
    }

    if (request.getSoldBooks() != null) {
      book.setSoldCopies(request.getSoldBooks());
    }
  }

  private void validateAndSetBookTitle(Book book, String newTitle) {
    if (!newTitle.equals(book.getTitle())) {
      validateBookDuplicate(newTitle);
      book.setTitle(newTitle);
    }
  }

  private void updateBookSectionAndCategory(Book book, Section newSection, Category newCategory) {
    Section section = book.getSection();
    Category category = book.getCategory();

    if (!section.getName().equals(newSection.getName())
        || !category.getName().equals(newCategory.getName())) {
      book.setSection(newSection);
      book.setCategory(newCategory);
    }
  }

  private void updateBookImages(Book book, MultipartFile bookCover, MultipartFile bookThumbnail) {
    if (!bookCover.isEmpty()) {
      deleteAndSetBookCoverImage(book, bookCover);
    }

    if (!bookThumbnail.isEmpty()) {
      deleteAndSetBookThumbnailImage(book, bookThumbnail);
    }
  }

  private void deleteAndSetBookCoverImage(Book book, MultipartFile bookCover) {
    deleteBookImage(book.getBookCover());
    book.setBookCover(multipartFileService.uploadImage(bookCover, ImageType.COVER));
  }

  private void deleteAndSetBookThumbnailImage(Book book, MultipartFile bookThumbnail) {
    deleteBookImage(book.getBookThumbnail());
    book.setBookThumbnail(multipartFileService.uploadImage(bookThumbnail, ImageType.THUMBNAIL));
  }

  private void deleteBookImage(String imagePath) {
    if (imagePath != null) {
      multipartFileService.deleteImage(imagePath);
    }
  }

  @Override
  public Book getBookById(Integer bookId) {
    Optional<Book> bookOptional = bookRepository.findById(bookId);
    return bookOptional.orElseThrow(
        () ->
            new EntityNotFoundException(String.format("The book with id %s is not found", bookId)));
  }

  private void validateBookDuplicate(String bookTitle) {
    Optional<Book> optionalBook = bookRepository.findBookByTitle(bookTitle);
    if (optionalBook.isPresent()) {
      throw new EntityExistsException(
          String.format("Book with title '%s' is already in the system.", bookTitle));
    }
  }

  @Override
  public void validateBookAvailability(Integer bookId, Integer quantity, String bookTitle) {
    Integer copiesInStock = bookRepository.getCopiesInStockById(bookId);
    if (copiesInStock == null || copiesInStock <= 0) {
      throw new IllegalArgumentException(
          String.format("The book with title '%s' is not in stock", bookTitle));
    } else if (copiesInStock < quantity) {
      throw new IllegalArgumentException(
          String.format(
              "The book with title '%s' does not have enough copies in stock", bookTitle));
    }
  }
}
