package mediaplayer;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MediaController implements Initializable{
    //@FXML
    //ImageView shuffleButton;

    @FXML
    Label shuffleLabel, repeatLabel;

    @FXML
    ListView<String> albumList;

    @FXML
    Label titleLabel, artistLabel, genreLabel, lengthLabel, dateLabel, ratingLabel, volumeLabel;

    @FXML
    Slider volumeSlider;

    @FXML
    private ListView<SongModel> listView;

    private PlayListModel listModel;

    @FXML
    Label bot_title, bot_author, bot_length;

    @FXML
    ImageView main_image, bot_rating_img, playButton;

    @FXML
    Slider progressSlider;

    boolean isPlaying, timerInitialized;
    Timer myTimer;

    @FXML
    protected void onShuffleClicked(MouseEvent event) {
        toggleLabel(shuffleLabel);
        //System.out.println("On shuffle clicked");
        //event.consume();
    }

    @FXML
    protected void onRepeatClicked(MouseEvent event) {
        toggleLabel(repeatLabel);
    }

    private void toggleLabel(Label label) {
        if (label.getText().equalsIgnoreCase("off"))
            label.setText("On");
        else
            label.setText("Off");
    }

    @FXML
    protected void onListClicked(MouseEvent event) {
        //System.out.println("clicked on " + albumList.getSelectionModel().getSelectedItem());
        titleLabel.setText(listView.getSelectionModel().getSelectedItem().getTitle());
        bot_title.setText(listView.getSelectionModel().getSelectedItem().getTitle());
        artistLabel.setText(listView.getSelectionModel().getSelectedItem().getArtist());
        bot_author.setText(listView.getSelectionModel().getSelectedItem().getArtist());
        genreLabel.setText(listView.getSelectionModel().getSelectedItem().getGenre());
        lengthLabel.setText(listView.getSelectionModel().getSelectedItem().getLength());
        bot_length.setText(listView.getSelectionModel().getSelectedItem().getLength());
        dateLabel.setText("" + listView.getSelectionModel().getSelectedItem().getDate());
        ratingLabel.setText("" + listView.getSelectionModel().getSelectedItem().getRating() + " star");
        setRatingImage(listView.getSelectionModel().getSelectedItem().getRating());
        setMusicImage(listView.getSelectionModel().getSelectedItem().getImagePath());
        if (timerInitialized)
            onTrackChanged();
        resetProgressSlider();
    }

    private void setRatingImage(int rating) {
        if (rating == 1) {
            bot_rating_img.setImage(new Image("mediaplayer/images/1-stars.png"));
        } else if (rating == 2) {
            bot_rating_img.setImage(new Image("mediaplayer/images/2-stars.png"));
        } else if (rating == 3) {
            bot_rating_img.setImage(new Image("mediaplayer/images/3-stars.png"));
        } else if (rating == 4) {
            bot_rating_img.setImage(new Image("mediaplayer/images/4-stars.png"));
        } else if (rating == 5) {
            bot_rating_img.setImage(new Image("mediaplayer/images/5-stars.png"));
        } else {
            bot_rating_img.setImage(new Image("mediaplayer/images/0-stars.png"));
        }
    }

    private void setMusicImage(String imagePath) {
        main_image.setImage(new Image(imagePath));
    }

    private void resetProgressSlider() {
        progressSlider.adjustValue(0);
    }

    @FXML
    protected void onPlayClicked(MouseEvent event) {
        if (!isPlaying) {
            playButton.setImage(new Image("mediaplayer/images/pause-button.png"));
            isPlaying = true;
            myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Your database code here
                    progressSlider.increment();
                }
            }, 1000, 1000);
            timerInitialized = true;
        } else {
            playButton.setImage(new Image("mediaplayer/images/playbutton.png"));
            isPlaying = false;
            myTimer.cancel();
            myTimer.purge();
            timerInitialized = false;
        }
    }

    private void onTrackChanged() {
        playButton.setImage(new Image("mediaplayer/images/playbutton.png"));
        isPlaying = false;
        myTimer.cancel();
        myTimer.purge();
    }

    @FXML
    protected void onRewindClicked(MouseEvent event) {
        for (int i = 0; i < 3; i++)
            progressSlider.decrement();
    }

    @FXML
    protected void onFastClicked(MouseEvent event) {
        for (int i = 0; i < 3; i++)
            progressSlider.increment();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        ObservableList<String> names = FXCollections.observableArrayList(
//                "Album 1", "Album 2", "Album 3", "Album 4", "Album 5", "Album 6", "Album 7");
//        albumList.setItems(names);

        this.listModel = new PlayListModel();
        ObservableList<SongModel> songs = FXCollections.observableArrayList(
                new SongModel("Space Oddity", "David Bowie", "Classic Rock", 3.52, LocalDate.of(1969,7,11), 3,
                        "mediaplayer/images/space-oddity.jpg"),
                new SongModel("Somebody To Love", "Freddie Mercury", "Classic Rock", 3.25, LocalDate.of(1976,11,12), 5,
                        "mediaplayer/images/somebody-to-love.jpg"),
                new SongModel("Light My Fire", "The Doors", "Rock", 3.55, LocalDate.of(1996,8,4), 3,
                        "mediaplayer/images/light-my-fire.jpg"),
                new SongModel("Cold, Cold Heart", "Hank Williams", "Country", 2.05, LocalDate.of(1951,2,20), 2,
                        "mediaplayer/images/cold-cold-heart.jpg"),
                new SongModel("I’m a Believer", "Neil Diamond", "Rock", 3.56, LocalDate.of(1966,7,20), 5,
                        "mediaplayer/images/im-a-believer.jpg"),
                new SongModel("Royals", "Joel Little", "Electropop", 3.45, LocalDate.of(2013,6,3), 4,
                        "mediaplayer/images/royals.jpg"),
                new SongModel("Hotel California", "Don Felder", "Soft Rock", 4.25, LocalDate.of(1977,2,15), 5,
                        "mediaplayer/images/hotel-california.jpg"),
                new SongModel("In My Life", "The Beatles", "Rock", 4.35, LocalDate.of(1965,10,20), 3,
                        "mediaplayer/images/in-my-life.jpg"),
                new SongModel("The Boxer", "Paul Simon", "Country", 4.25, LocalDate.of(1968,11,5), 4,
                        "mediaplayer/images/the-boxer.png"),
                new SongModel("Space Oddity", "David Bowie", "Classic Rock", 3.52, LocalDate.of(1969,7,11), 3,
                        "mediaplayer/images/space-oddity.jpg"),
                new SongModel("Somebody To Love", "Freddie Mercury", "Classic Rock", 3.25, LocalDate.of(1976,11,12), 5,
                        "mediaplayer/images/somebody-to-love.jpg"),
                new SongModel("Light My Fire", "The Doors", "Rock", 3.55, LocalDate.of(1996,8,4), 3,
                        "mediaplayer/images/light-my-fire.jpg"),
                new SongModel("Cold, Cold Heart", "Hank Williams", "Country", 2.05, LocalDate.of(1951,2,20), 2,
                        "mediaplayer/images/cold-cold-heart.jpg"),
                new SongModel("I’m a Believer", "Neil Diamond", "Rock", 3.56, LocalDate.of(1966,7,20), 5,
                        "mediaplayer/images/im-a-believer.jpg"),
                new SongModel("Royals", "Joel Little", "Electropop", 3.45, LocalDate.of(2013,6,3), 4,
                        "mediaplayer/images/royals.jpg")
                //new SongModel("In My Life", "The Beatles", "Rock", 4.50, LocalDate.of(1965,10,20), 3)
        );
        listView.setItems(songs);

        listView.setCellFactory(param -> new ListCell<SongModel>() {
            @Override
            protected void updateItem(SongModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getSongView() == null) {
                    setText(null);
                } else {
                    setText(item.getSongView());
                }
            }
        });

        volumeLabel.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        volumeSlider.valueProperty()
                )
        );
        isPlaying = false;
        timerInitialized = false;
        progressSlider.setBlockIncrement(5); //default is 10
    }
}
