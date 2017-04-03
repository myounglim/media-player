package mediaplayer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Created by student on 4/1/17.
 */
public class PlayListModel {
    private ObservableList<SongModel> playList = FXCollections.observableArrayList();
    private ObjectProperty<SongModel> currentlyPlaying = new SimpleObjectProperty<>(null);

    public ObjectProperty<SongModel> currentPlayingProperty() {
        return currentlyPlaying;
    }

    public final SongModel getCurrentSong() {
        return currentPlayingProperty().get();
    }

    public final void setCurrentlyPlaying(SongModel song) {
        currentPlayingProperty().set(song);
    }

    public ObservableList<SongModel> getSongList() {
        return playList;
    }

    public void addToList(SongModel song) {
        playList.add(song);
    }

    public void removeFromList(SongModel song) {
        playList.remove(song);
    }
}
