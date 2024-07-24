package com.rafaa.catalog_service.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {

    private static Validator validator;

    @BeforeAll
    static void global_set_up(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void when_all_fields_correct_then_validation_succeeds(){
        Book book = new Book("1234567890", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void when_isbn_not_defined_then_validation_fails() {
        Book book = new Book("", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The book ISBN must be defined.")
                .contains("The ISBN format must be valid.");
    }

    @Test
    void when_isbn_defined_but_incorrect_then_validation_fails() {
        Book book = new Book("a234567890", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    void when_title_is_not_defined_then_validation_fails() {
        Book book = new Book("1234567890", "", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book title must be defined.");
    }

    @Test
    void when_author_is_not_defined_then_validation_fails() {
        Book book = new Book("1234567890", "Title", "", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book author must be defined.");
    }

    @Test
    void when_price_is_not_defined_then_validation_fails() {
        Book book = new Book("1234567890", "Title", "Author", null);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be defined.");
    }

    @Test
    void when_price_defined_but_zero_then_validation_fails() {
        Book book = new Book("1234567890", "Title", "Author", 0.0);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

    @Test
    void when_price_defined_but_negative_then_validation_fails() {
        Book book = new Book("1234567890", "Title", "Author", -9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

}