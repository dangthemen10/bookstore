package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.dto.response.BookListingResponse;
import com.phdang97.bookstore.entity.Book;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
  Optional<Book> findBookResponseById(Integer id);

  Optional<Book> findBookByTitle(String title);

  @Query(
      value =
          """
            select new com.phdang97.bookstore.dto.response.BookListingResponse(b.id, b.title, b.actualPrice, b.discountPrice, b.bookThumbnail)
            from Book b where (b.section.id=:sectionId or :sectionId is null ) and
            (b.category.id=:categoryId or :categoryId is null)
            order by
            case when :orderBy = 'POPULARITY' then b.soldCopies
                else b.id
            end desc
            """)
  Page<BookListingResponse> filterBooks(
      Integer sectionId, Integer categoryId, String orderBy, Pageable pageable);

  @Query(
      value =
          """
            SELECT new com.phdang97.bookstore.dto.response.BookListingResponse(b.id, b.title, b.actualPrice,b.discountPrice,b.bookThumbnail)
            FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))
          """)
  Page<BookListingResponse> findBooksByTitle(String title, Pageable pageable);

  @Query(
      value =
          """
            select b.copiesInStock from Book b where b.id=:id
            """)
  Integer getCopiesInStockById(Integer id);
}
