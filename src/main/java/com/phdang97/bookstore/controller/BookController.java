package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.assembler.BookAssembler;
import com.phdang97.bookstore.dto.request.BookRequest;
import com.phdang97.bookstore.dto.response.BookDetailsResponse;
import com.phdang97.bookstore.dto.response.BookListingResponse;
import com.phdang97.bookstore.dto.response.BookResponse;
import com.phdang97.bookstore.entity.Book;
import com.phdang97.bookstore.service.BookService;
import com.phdang97.bookstore.service.MultipartFileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired private final BookService bookService;

  private final MultipartFileService multipartFileService;

  @GetMapping("/{id}")
  public BookResponse getBook(@PathVariable(name = "id") Integer bookId) {
    Book book = bookService.getBookResponse(bookId);
    return new BookAssembler().toBookResponse(book);
  }

  @GetMapping("/details/{id}")
  public BookDetailsResponse getBookDetails(@PathVariable(name = "id") Integer bookId) {
    Book book = bookService.getBookDetails(bookId);
    return new BookAssembler().toDetailsResponse(book);
  }

  @GetMapping("/category")
  public Page<BookListingResponse> getBooksBySectionAndCategory(
      @RequestParam(value = "sectionId", required = false) Integer sectionId,
      @RequestParam(value = "categoryId", required = false) Integer categoryId,
      @RequestParam(value = "page") @PositiveOrZero Integer pageNumber,
      @RequestParam(value = "size") @Positive() Integer pageSize,
      @RequestParam(value = "orderby", required = false) String sortBy) {
    return bookService.getBooksBySectionAndCategory(
        sectionId, categoryId, pageNumber, pageSize, sortBy);
  }

  @GetMapping("/title")
  public Page<BookListingResponse> getBooksByTitle(
      @RequestParam(name = "title") String title,
      @RequestParam(value = "page") @PositiveOrZero Integer pageNumber,
      @RequestParam(value = "size") @Positive() Integer pageSize) {
    return bookService.getBooksByTitle(title, pageNumber, pageSize);
  }

  @GetMapping("/image")
  public ResponseEntity<byte[]> getImage(@RequestParam("path") String imagePath) {
    byte[] image = multipartFileService.downloadImage(imagePath);
    MediaType mediaType = multipartFileService.getContentType(imagePath);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(mediaType);
    return new ResponseEntity<>(image, headers, HttpStatus.OK);
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(HttpStatus.CREATED)
  public BookDetailsResponse addBook(
      @RequestPart("book") @Valid BookRequest request,
      @RequestPart("cover") MultipartFile bookCover,
      @RequestPart("thumbnail") MultipartFile bookThumbnail) {
    Book book = bookService.addBook(request, bookCover, bookThumbnail);
    return new BookAssembler().toDetailsResponse(book);
  }

  @PutMapping(
      value = {"/{id}"},
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(HttpStatus.OK)
  public BookDetailsResponse updateBook(
      @RequestPart("book") @Valid BookRequest request,
      @RequestPart(value = "cover", required = false) MultipartFile bookCover,
      @RequestPart(value = "thumbnail", required = false) MultipartFile bookThumbnail,
      @PathVariable(name = "id") @PositiveOrZero Integer id) {
    Book book = bookService.updateBook(id, request, bookCover, bookThumbnail);
    return new BookAssembler().toDetailsResponse(book);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public String deleteBook(@PathVariable(name = "id") @PositiveOrZero Integer id) {
    return bookService.deleteBook(id);
  }
}
