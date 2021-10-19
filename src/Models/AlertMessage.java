package Models;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

public interface AlertMessage {

    static void message (@NotNull Text text) {
        DoubleProperty opacity = text.opacityProperty();
        Timeline fadeOut =
                new Timeline (
                        new KeyFrame ( Duration.ZERO, new KeyValue(opacity, 1) ),
                        new KeyFrame ( Duration.seconds(40), new KeyValue(opacity, 0) )
                );
        fadeOut.play();
    }

}
