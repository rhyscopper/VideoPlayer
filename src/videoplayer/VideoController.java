/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoplayer;

import java.awt.Button;
import javafx.event.ActionEvent;
import java.io.File;
import java.net.URL; 
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable; 
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer; 
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Rhys
 */
public class VideoController implements Initializable {
    
    private Stage stage;            
    private static String MEDIA_URL = "";
    private Duration duration;   
    private static final double INIT_VALUE = 0;
    
    @FXML
    private MediaView MediaView;
    private MediaPlayer MediaPlayer;
    private Media Media; 
    private Button PlayButton;
    
    @FXML
    Slider VolumeSlider;
    
    @FXML
    Slider VideoSlider;
    
    @FXML
    Text StartTime;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VolumeSlider.valueProperty().addListener(new InvalidationListener(){
         
            @Override
            public void invalidated(Observable observable) {
                MediaPlayer.setVolume(VolumeSlider.getValue() / 100 ); 
            }

        }); 
      
        VideoSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (VideoSlider.isValueChanging()) {
                    
                    // multiply duration by percentage calculated by slider position
                    MediaPlayer.seek(duration.multiply(VideoSlider.getValue() / 100.0));
                   
                }
            }
        });
 
    }    
    
    void init(Stage stage) {
        this.stage = stage;
    }
    
    public void openFile(){
        FileChooser filechooser = new FileChooser();  
        filechooser.setInitialDirectory(new File(System.getProperty("user.home")));        
        File file = filechooser.showOpenDialog(stage);
        
        if(file != null) {
            System.out.println("Chosen file: " + file);
            
            MEDIA_URL = file.getAbsolutePath();  
            Media = new Media(new File(MEDIA_URL).toURI().toString());
            MediaPlayer = new MediaPlayer(Media);
            MediaView.setMediaPlayer(MediaPlayer);
            
            VolumeSlider.setValue(MediaPlayer.getVolume() * 100);
            System.out.println(MediaPlayer.getVolume() * 100);
            
            VideoSlider.setValue(INIT_VALUE); 
            System.out.println(MediaPlayer.getTotalDuration());
 
            MediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {

                    duration = MediaPlayer.getMedia().getDuration();
                    updateValues();

                }
            });
           
            MediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                     updateValues();
                }
            });    
             
        }
        
    } 
    
    protected void updateValues() {
        if (StartTime != null && VideoSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    
                    Duration currentTime = MediaPlayer.getCurrentTime();
                    StartTime.setText(formatTime(currentTime, duration));
                    VideoSlider.setDisable(duration.isUnknown());
                    if (!VideoSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !VideoSlider.isValueChanging()) {
                        VideoSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    } 
                }
            });
        }
    }
    
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
    
    @FXML
    private void PlayVideo(ActionEvent event)
    { 
        MediaPlayer.play();
        MediaPlayer.setRate(1);
    }
    
    @FXML
    private void PauseVideo(ActionEvent event)
    { 
        MediaPlayer.pause();
    }
    
    @FXML
    private void reload(ActionEvent event)
    { 
        MediaPlayer.seek(MediaPlayer.getStartTime());
        MediaPlayer.pause();
        updateValues();
    }

    
    @FXML
    
    private void last (ActionEvent event)
    { 
        MediaPlayer.seek(MediaPlayer.getTotalDuration());
        MediaPlayer.stop();
    }
    
    
}

