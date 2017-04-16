package mediaplayer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.Pair;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
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
            if (progressSlider.getValue() == 100.0) progressSlider.adjustValue(0);
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

    @FXML
    protected void onAboutClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Media Player Info");
        alert.setHeaderText(null);
        String s = "Click on a song on the playlist to update the currently playing info!\n";
        s += "The controls on the lower pane are for playing/rewind/fast-forward and repeat/shuffle/volume";
        alert.setContentText(s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.show();
    }

    /**
     * Credit to http://code.makery.ch/blog/javafx-dialogs-official/ for most of the setup code for JavaFX dialogs
     * @param event
     */
    @FXML
    protected void onAddNewClicked(ActionEvent event) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New Song");
        dialog.setHeaderText("Add A New Song");

        // Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Title");
        TextField password = new TextField();
        password.setPromptText("Artist");
        TextField genreField = new TextField();
        genreField.setPromptText("Genre");
        TextField minField = new TextField();
        minField.setPromptText("Minutes (0-9)");
        TextField secField = new TextField();
        secField.setPromptText("Seconds (0-60)");
        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(
                "0",
                "1",
                "2",
                "3",
                "4",
                "5"
        );
        comboBox.getSelectionModel().select(3);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Artist:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Genre:"), 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(new Label("Length:"), 0, 3);
        grid.add(minField, 1, 3);
        grid.add(secField, 2, 3);
        grid.add(new Label("Rating:"), 0, 4);
        grid.add(comboBox, 1, 4);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
//        username.textProperty().addListener((observable, oldValue, newValue) -> {
//            loginButton.setDisable(newValue.trim().isEmpty());
//        });

        secField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                String title = username.getText();
                String artist = password.getText();
                String genre = genreField.getText();
                String min = minField.getText();
                String seconds = secField.getText();
                String rating = comboBox.getSelectionModel().getSelectedItem().toString();
                if (validInput(min, seconds, title, artist, genre)) {
                    String length = min + "." +  seconds;
                    double convertedDouble = Double.parseDouble(length);
                    int convertedRating = Integer.parseInt(rating);
                    SongModel newSong = new SongModel(title, artist, genre, convertedDouble, LocalDate.now(), convertedRating,
                            "mediaplayer/images/random-img.jpg");
                    listView.getItems().add(newSong);
                } else {
                    SongModel newSong = new SongModel("Try Again", "Bad Input", "Default Values", 3.50, LocalDate.now(), 3,
                            "mediaplayer/images/random-img.jpg");
                    listView.getItems().add(newSong);
                }
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });
    }

    private boolean validInput(String min, String secs, String title, String artist, String genre) {
        try {
            int minutes = Integer.parseInt(min);
            int seconds = Integer.parseInt(secs);
            if (minutes < 0 || minutes > 9) return false;
            if (title.isEmpty() || artist.isEmpty() || genre.isEmpty()) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @FXML
    protected void onExitClicked(ActionEvent event) {
        Platform.exit();
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
