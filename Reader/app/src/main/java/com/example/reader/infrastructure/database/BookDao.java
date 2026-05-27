package com.example.reader.infrastructure.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.reader.model.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM books WHERE id = :id")
    LiveData<Book> getBookById(int id);
    
    @Query("SELECT * FROM books ORDER BY dateAdded DESC")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM books WHERE isFavorite = 1 ORDER BY title ASC")
    LiveData<List<Book>> getFavoriteBooks();

    // --- СОРТИРОВКА ---
    @Query("SELECT * FROM books ORDER BY title ASC")
    LiveData<List<Book>> getBooksSortedByTitle();
    
    @Query("SELECT * FROM books ORDER BY author ASC")
    LiveData<List<Book>> getBooksSortedByAuthor();
    
    @Query("SELECT * FROM books ORDER BY dateAdded DESC")
    LiveData<List<Book>> getBooksSortedByDate();

    // --- ФИЛЬТРАЦИЯ И ПОИСК ---
    
    @Query("SELECT * FROM books WHERE title LIKE :searchQuery OR author LIKE :searchQuery")
    LiveData<List<Book>> searchBooks(String searchQuery);
    
    @Query("SELECT * FROM books WHERE format = :format")
    LiveData<List<Book>> getBooksByFormat(String format);
    
    @Query("SELECT * FROM books WHERE category = :category")
    LiveData<List<Book>> getBooksByCategory(String category);

    // --- СПЕЦИФИЧНЫЕ ЗАПРОСЫ ---

    // Получить недавно открытые книги (те, где currentPage > 0)
    @Query("SELECT * FROM books WHERE currentPage > 0 ORDER BY dateAdded DESC LIMIT 5")
    LiveData<List<Book>> getRecentBooks();

    // Быстрое переключение статуса избранного по ID
    @Query("UPDATE books SET isFavorite = :favorite WHERE id = :id")
    void updateFavoriteStatus(int id, boolean favorite);

    // Обновление прогресса чтения
    @Query("UPDATE books SET currentPage = :page WHERE id = :id")
    void updateProgress(int id, int page);

    @Query("UPDATE books SET totalPages = :pages_count WHERE id = :id")
    void updateTotalPages(int id, int pages_count);
    
    @Query("UPDATE books SET coverPath = :coverPath WHERE id = :id")
    void updateCover(int id, String coverPath);
}