package ict376.murdoch.edu.au.braid;

import java.io.Serializable;

/**
 * Book class
 * Created by Taaqif on 24/10/2017.
 */

public class Book implements Serializable {
    private int id;
    private String title;
    private String ISBN;
    private String cover;
    private String authors;
    private String publisher;
    private String publishedDate;
    private String addedDate;
    private int rating;
    private int totalPages;
    private int currentPages;

    public Book(int id, String title, String isbn, String cover, String authors, String publisher, String publishedDate, String addedDate, int rating, int totalPages, int currentPages) {
        this.id = id;
        this.title = title;
        this.ISBN = isbn;
        this.cover = cover;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.addedDate = addedDate;
        this.rating = rating;
        this.totalPages = totalPages;
        this.currentPages = currentPages;
    }

    public int getID() {
        return id;
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

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String categories) {
        this.authors = categories;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String toString(){
        return (
        this.id + ", "+
        this.title + ", "+
        this.ISBN + ", "+
        this.cover + ", "+
        this.authors + ", "+
        this.publisher + ", "+
        this.publishedDate + ", "+
        this.addedDate + ", "+
        this.rating + ", "+
        this.totalPages + ", "+
        this.currentPages + ", ");
    }
}
