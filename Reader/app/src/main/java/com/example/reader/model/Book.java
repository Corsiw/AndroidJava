package com.example.reader.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "books")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String author;
    private String filePath;
    private String format;
    private String category;

    private int currentPage;
    private int totalPages;

    private long dateAdded;
    private boolean isFavorite;

    private String coverPath;

    public Book(String title, String author, String filePath, String format, int totalPages) {
        this.title = title;
        this.author = author;
        this.filePath = filePath;
        this.format = format;
        this.dateAdded = System.currentTimeMillis();
        this.currentPage = 0;
        this.isFavorite = false;
        if (totalPages > 0) {
            this.totalPages = totalPages;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    @Ignore
    public int getProgressPercentage() {
        if (totalPages <= 0) return 0;
        return (int) (((float) currentPage / totalPages) * 100);
    }
}