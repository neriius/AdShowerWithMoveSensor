package com.nerius.taurus.javafxConfig;

import com.nerius.taurus.screens.MainController;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {
    private final FxWeaver fxWeaver;

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) { //(1)
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) { //(2)
        Stage stage = event.stage;
        FxControllerAndView<MainController, Node> load = fxWeaver.load(MainController.class);
        load.getController().setPrimaryStage(stage);
        Scene scene = new Scene((Parent) load.getView().get()); //(3)
        stage.setScene(scene);
        stage.setX(0);
        stage.setWidth(Screen.getPrimary().getBounds().getWidth());
        stage.setY(-500);//-350
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

    }



}
