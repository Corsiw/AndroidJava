package com.example.reader.application;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.reader.infrastructure.database.BookDao;
import com.example.reader.infrastructure.database.BookDatabase;
import com.example.reader.model.Book;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookRepository {
    private final BookDao bookDao;
    private final ExecutorService executorService;

    public BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        bookDao = db.bookDao();
        executorService = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<Book>> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<Book> getBookById(int id) {
        return bookDao.getBookById(id);
    }

    public LiveData<List<Book>> getFavoriteBooks() {
        return bookDao.getFavoriteBooks();
    }

    public LiveData<List<Book>> getRecentBooks() {
        return bookDao.getRecentBooks();
    }

    // --- СОРТИРОВКА ---

    public LiveData<List<Book>> getBooksSortedByTitle() {
        return bookDao.getBooksSortedByTitle();
    }

    public LiveData<List<Book>> getBooksSortedByAuthor() {
        return bookDao.getBooksSortedByAuthor();
    }

    public LiveData<List<Book>> getBooksSortedByDate() {
        return bookDao.getBooksSortedByDate();
    }

    // --- ПОИСК И ФИЛЬТРАЦИЯ ---

    public LiveData<List<Book>> searchBooks(String query) {
        // Добавляем проценты для SQL оператора LIKE
        return bookDao.searchBooks("%" + query + "%");
    }

    public LiveData<List<Book>> getBooksByFormat(String format) {
        return bookDao.getBooksByFormat(format);
    }

    // --- ОПЕРАЦИИ ЗАПИСИ (EXECUTOR) ---

    public void insert(Book book) {
        executorService.execute(() -> bookDao.insert(book));
    }

    public void update(Book book) {
        executorService.execute(() -> bookDao.update(book));
    }

    public void delete(Book book) {
        executorService.execute(() -> bookDao.delete(book));
    }

    public void updateFavoriteStatus(int id, boolean isFavorite) {
        executorService.execute(() -> bookDao.updateFavoriteStatus(id, isFavorite));
    }

    public void updateProgress(int id, int page) {
        executorService.execute(() -> bookDao.updateProgress(id, page));
    }

    public void updateTotalPages(int id, int pages_count) {
        executorService.execute(() -> bookDao.updateTotalPages(id, pages_count));
    }

    public void updateCoverPath(int id, String coverPath) {
        executorService.execute(() -> bookDao.updateCover(id, coverPath));
    }
}