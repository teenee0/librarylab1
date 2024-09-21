package com.example.librarylab1.Controllers;

import com.example.librarylab1.models.Books;
import com.example.librarylab1.repo.BooksRepository;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final BooksRepository booksRepository;

    public MainController(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Главная страница");
        return "greeting";
    }

    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("title", "Поддержка");
        return "support";
    }
    @GetMapping("/books")
    public String books(Model model) {
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("title", "Страница книг");
        return "books";
    }
    @GetMapping("/books/filter")
    public String getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            Model model) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortByDate = Sort.by(sortDirection, "give_date");

        List<Books> books;

        if (title != null || author != null || start_date != null || end_date != null || studentName != null) {
            books = booksRepository.findByParams(title, author, start_date, end_date, studentName, sortByDate);
        } else {
            books = booksRepository.findAll(sortByDate);
        }

        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/stats")
    public String getBooksStats(Model model) {
        List<Object[]> stats = booksRepository.findBookIssueStats();

        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (Object[] row : stats) {
            dates.add(row[0].toString());
            counts.add((Long) row[1]);
        }

        model.addAttribute("dates", dates);
        model.addAttribute("counts", counts);
        return "books_stats";
    }

    @GetMapping("/bookadd")
    public String bookadd(Model model) {
        return "bookadd";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/bookadd")
    public String bookaddPost(@RequestParam String title,
                              @RequestParam String author,
                              @RequestParam String publisher,
                              @RequestParam LocalDate give_date,
                              @RequestParam String studentName,
                              @RequestParam(required = false) LocalDate date_take,
                              Model model) {
        Books book = new Books(title, author, publisher, give_date, studentName, date_take);
        booksRepository.save(book);
        return "redirect:/books";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/books/{id}")
    public String bookById(@PathVariable("id") long id, Model model) {
        if(!booksRepository.existsById(id)) {
            return "redirect:/books";
        }
        Optional<Books> book = booksRepository.findById(id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("books", res);
        return "bookDetails";
    }
    @PostMapping("/books/save")
    public String bookEdit(@RequestParam("id") long id,
                           @RequestParam String title,
                           @RequestParam String author,
                           @RequestParam String publisher,
                           @RequestParam LocalDate give_date,
                           @RequestParam String studentName,
                           @RequestParam(required = false) LocalDate date_take,
                           Model model) {
        Books book = booksRepository.findById(id).orElseThrow();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setGive_date(give_date);
        book.setStudentName(studentName);

        if (date_take != null) {
            book.setDate_take(date_take);
        } else {
            book.setDate_take(null);
        }

        booksRepository.save(book);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/remove")
    public String bookDelete(@PathVariable("id") long id, Model model) {
        Books book = booksRepository.findById(id).orElseThrow();
        booksRepository.delete(book);
        return "redirect:/books";

    }
}

