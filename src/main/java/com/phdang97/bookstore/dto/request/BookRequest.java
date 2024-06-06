package com.phdang97.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    @NotBlank(message = "Book title should not be blank")
    private String title;

    @NotNull(message = "Book sections should not be null")
    @Positive(message = "Book sections should be greater than zero")
    private Integer sectionId;

    @NotNull(message = "Book category should not be null")
    @Positive(message = "Book category should be greater than zero")
    private Integer categoryId;

    @NotNull(message = "Book actual price should not be null")
    @PositiveOrZero(message = "Book actual price should be zero or more")
    private Double actualPrice;


    @NotNull(message = "Book discount price should not be null")
    @PositiveOrZero(message = "Book discount price should be zero or more")
    private Double discountPrice;


    @NotNull(message = "Book number of pages should not be null")
    @Positive(message = "Book number of pages should be greater than zero")
    private Integer numberOfPages;

    @PositiveOrZero(message = "Number of books in the stock should not be a negative value")
    private Integer booksInStock;


    @PositiveOrZero(message = "Number of books to be printed should not be a negative value")
    private Integer booksToBePrinted;

    @Size(message = "Book description should not exceed 5000 character",max = 5000)
    private String description;

    @PositiveOrZero(message = "Sold books should not be a negative number")
    private Integer soldBooks;
}
