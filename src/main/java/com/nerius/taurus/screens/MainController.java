package com.nerius.taurus.screens;

import com.nerius.taurus.db.entities.Ad;
import com.nerius.taurus.db.repository.AdRepository;
import com.nerius.taurus.models.SerialPortReader;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@FxmlView
@Controller
@Log4j2
public class MainController {

    public AnchorPane rootPane;
    public ListView adsListView;
    @Autowired
    private SerialPortReader serialPortReader;

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private FxWeaver fxWeaver;

    @Setter
    private Stage primaryStage;

    public void initialize() {
        serialPortReader.startPort(new SerialPortReader.OnSensorAnswerReceived() {
            @Override
            public void onHumanCame() {
                log.info("Human came show ad");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        playAudio("/greating.mp3");
                        moveStageDown(primaryStage);
                    }
                });
            }

            @Override
            public void onHumanGoOut() {
                log.info("Human went close ad");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        playAudio("/bye.mp3");
                        moveStageUp(primaryStage);
                    }
                });
            }
        });
        Iterable<Ad> all = adRepository.findAll();


        all.forEach(ad -> {
            FxControllerAndView<AdViewItem, Node> load = fxWeaver.load(AdViewItem.class);
            load.getController().initAd(ad);
            adsListView.getItems().add(load.getView().get());
        });


    }

    private void playAudio(String resourcePath) {
        Thread audioThread = new Thread(() -> {
            try (InputStream audioStream = getClass().getResourceAsStream(resourcePath)) {
                AdvancedPlayer player = new AdvancedPlayer(audioStream);
                player.play();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        });
        audioThread.start();

    }


    private void moveStageDown(Stage stage) {
        double startY = stage.getY();
        double endY = startY + stage.getHeight();

        stage.setIconified(false);
        stage.toFront();
        animateStage(stage, startY, endY);
    }

    private void moveStageUp(Stage stage) {
        double startY = stage.getY();
        double endY = startY - stage.getHeight();
        stage.toFront();

        animateStage(stage, startY, endY);
    }

    private void animateStage(Stage stage, double fromY, double toY) {
        AnimationTimer timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                double elapsedSeconds = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                double deltaY = (toY - fromY) * elapsedSeconds;

                if ((toY > fromY && stage.getY() < toY) || (toY < fromY && stage.getY() > toY)) {
                    stage.setY(stage.getY() + deltaY);
                } else {
                    stage.setY(toY);
                    if (fromY > toY) {
                        stage.setIconified(true);
                    }
                    stop();
                }
            }
        };
        timer.start();
    }
}
