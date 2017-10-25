package ict376.murdoch.edu.au.braid;

/**
 * Book class
 * Created by Taaqif on 24/10/2017.
 */

public class Book {
    private int id;
    private String title;
    private String ISBN;
    private String cover;
    private String categories;
    private int publisher_id;
    private String publishedDate;
    private String addedDate;
    private int rating;
    private int totalPages;
    private int currentPages;

    public Book(int id, String title, String isbn, String cover, String categories, int publisher_id, String publishedDate, String addedDate, int rating, int totalPages, int currentPages) {
        this.id = id;
        this.title = title;
        this.ISBN = isbn;
        this.cover = cover;
        this.categories = categories;
        this.publisher_id = publisher_id;
        this.publishedDate = publishedDate;
        this.addedDate = addedDate;
        this.rating = rating;
        this.totalPages = totalPages;
        this.currentPages = currentPages;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPages() {
        return currentPages;
    }

    public void setCurrentPages(int currentPages) {
        this.currentPages = currentPages;
    }
}
