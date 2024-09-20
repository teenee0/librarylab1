package com.example.librarylab1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String author;
    private String publisher;
    private LocalDate give_date;  // Используем LocalDate для даты выдачи
    private LocalDate date_take;  // Используем LocalDate для даты возврата
    private String studentName;

    // Getters и Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getGive_date() {
        return give_date;
    }

    public void setGive_date(LocalDate give_date) {
        this.give_date = give_date;
    }

    public LocalDate getDate_take() {
        return date_take;
    }

    public void setDate_take(LocalDate date_take) {
        this.date_take = date_take;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Books() {}

    public Books(String title, String author, String publisher, LocalDate give_date, String studentName, LocalDate date_take) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.give_date = give_date;
        this.studentName = studentName;
        this.date_take = date_take;
    }
}
