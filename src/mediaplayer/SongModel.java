package mediaplayer;

import java.time.LocalDate;

/**
 * Created by student on 3/5/17.
 */
public class SongModel {
    String title;
    String artist;
    String genre;
    double length;
    LocalDate date;
    int rating;
    String imagePath;

    public SongModel(String title, String artist, String genre, double length, LocalDate date, int rating, String img) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.length = length;
        this.date = date;
        this.rating = rating;
        this.imagePath = img;
    }

    public String getTitle() { return this.title; }
    public String getArtist() { return this.artist; }
    public String getGenre() { return this.genre; }
    public String getLength() {
        String str = Double.toString(this.length);
        return str.charAt(0) + "min " + str.substring(2) + "sec";
    }
    public LocalDate getDate() { return this.date; }
    public int getRating() { return this.rating; }
    public String getSongView() { return this.title + " -- " + this.artist; }
    public String getImagePath() { return this.imagePath; }
}
