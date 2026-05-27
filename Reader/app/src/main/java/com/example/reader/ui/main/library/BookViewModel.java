package com.example.reader.ui.main.library;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.reader.application.BookRepository;
import com.example.reader.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private final BookRepository repository;

    // Состояния фильтрации и сортировки
    public enum FilterType {ALL, FAVORITE, RECENT, PDF, EPUB}

    public enum SortType {DEFAULT, TITLE, DATE}

    private final MutableLiveData<FilterType> currentFilter = new MutableLiveData<>(FilterType.ALL);
    private final MutableLiveData<SortType> currentSort = new MutableLiveData<>(SortType.DEFAULT);

    // Основной медиатор, на который будет подписан Fragment
    private final MediatorLiveData<List<Book>> booksToShow = new MediatorLiveData<>();
    private LiveData<List<Book>> currentSource = null;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);

        booksToShow.addSource(currentFilter, filter -> updateSource());
        booksToShow.addSource(currentSort, sort -> updateSource());

        updateSource();
    }

    private void updateSource() {
        if (currentSource != null) {
            booksToShow.removeSource(currentSource);
        }

        FilterType filter = currentFilter.getValue();

        if (filter == FilterType.FAVORITE) currentSource = repository.getFavoriteBooks();
        else if (filter == FilterType.RECENT) currentSource = repository.getRecentBooks();
        else if (filter == FilterType.PDF) currentSource = repository.getBooksByFormat("pdf");
        else if (filter == FilterType.EPUB) currentSource = repository.getBooksByFormat("epub");
        else currentSource = repository.getAllBooks();

        // Применяем СОРТИРОВКУ к результату фильтра
        booksToShow.addSource(currentSource, books -> {
            if (books == null) return;

            List<Book> sortedList = new ArrayList<>(books);
            SortType sort = currentSort.getValue();

            if (sort == SortType.TITLE) {
                sortedList.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
            } else if (sort == SortType.DATE) {
                sortedList.sort((b1, b2) -> Long.compare(b2.getDateAdded(), b1.getDateAdded()));
            }

            booksToShow.setValue(sortedList);
        });
    }

    public LiveData<List<Book>> getBooks() {
        return booksToShow;
    }

    public void setFilter(FilterType filter) {
        currentFilter.setValue(filter);
    }

    public void setSort(SortType sort) {
        currentSort.setValue(sort);
    }

    public LiveData<List<Book>> getAllBooks() {
        return repository.getAllBooks();
    }

    public LiveData<Book> getBookById(int id) {
        return repository.getBookById(id);
    }

    public LiveData<List<Book>> getFavoriteBooks() {
        return repository.getFavoriteBooks();
    }

    public LiveData<List<Book>> getRecentBooks() {
        return repository.getRecentBooks();
    }

    public LiveData<List<Book>> searchBooks(String query) {
        return repository.searchBooks(query);
    }

    public LiveData<List<Book>> getBooksSortedByTitle() {
        return repository.getBooksSortedByTitle();
    }

    public LiveData<List<Book>> getBooksSortedByDate() {
        return repository.getBooksSortedByDate();
    }

    public LiveData<List<Book>> getBooksByFormat(String format) {
        return repository.getBooksByFormat(format);
    }

    public void insert(Book book) {
        repository.insert(book);
    }

    public void update(Book book) {
        repository.update(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public void toggleFavorite(Book book) {
        repository.updateFavoriteStatus(book.getId(), !book.isFavorite());
    }

    public void updateReadingProgress(int bookId, int page) {
        repository.updateProgress(bookId, page);
    }

    public void updateTotalPages(int bookId, int total_pages) {
        repository.updateTotalPages(bookId, total_pages);
    }
    
    public void updateCoverPath(int bookId, String coverPath) {
        repository.updateCoverPath(bookId, coverPath);
    }
}