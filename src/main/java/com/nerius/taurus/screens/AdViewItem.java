package com.nerius.taurus.screens;

import com.nerius.taurus.db.entities.Ad;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@FxmlView
@Component
@Scope("prototype")
public class AdViewItem {
    public AnchorPane imageAnchorPane;
    public Label nameLabel;
    public Label descriptionLabel;
    public Label priceLabel;

    public void initialize() {

    }

    public void initAd(Ad ad) {
        Image image = new Image(new ByteArrayInputStream(ad.getImage()));
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,    // повторение: нет
                BackgroundRepeat.NO_REPEAT,    // повторение: нет
                BackgroundPosition.DEFAULT,    // позиция фона
                new BackgroundSize(
                        BackgroundSize.AUTO,    // ширина
                        BackgroundSize.AUTO,    // высота
                        true,                   // сохранить соотношение сторон
                        true,                   // центрировать
                        true,
                        false// изменить размеры
                )
        );
        Background background = new Background(backgroundImage);
        imageAnchorPane.setBackground(background);
        nameLabel.setText(ad.getGoodName());
        descriptionLabel.setText(ad.getDescription());
        priceLabel.setText(String.valueOf(ad.getPrice()));
    }
}
