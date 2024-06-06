package com.phdang97.bookstore.dto.response;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookListingResponse {
    private Integer id;

    private String title;

    private Double actualPrice;

    private Double discountPrice;

    private String bookThumbnail;
}
