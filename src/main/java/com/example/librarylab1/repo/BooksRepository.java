package com.example.librarylab1.repo;

import com.example.librarylab1.models.Books;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface BooksRepository extends JpaRepository<Books, Long> {
    @Query("SELECT b FROM Books b WHERE " +
            "(:title is null or b.title like %:title%) and " +
            "(:author is null or b.author like %:author%) and " +
            "(:studentName is null or b.studentName like %:studentName%) and " +
            "(:start_date is null or b.give_date >= :start_date) and " +
            "(:end_date is null or b.give_date <= :end_date)")
    List<Books> findByParams(@Param("title") String title,
                             @Param("author") String author,
                             @Param("start_date") LocalDate start_date,
                             @Param("end_date") LocalDate end_date,
                             @Param("studentName") String studentName,
                             Sort sort);
    @Query("SELECT b.give_date, COUNT(b) FROM Books b GROUP BY b.give_date")
    List<Object[]> findBookIssueStats();


}

