package Models;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

public interface ScreenTransition {
    static void fadeIn (@NotNull Pane root) {
        DoubleProperty opacity = root.opacityProperty();
        Timeline fadeIn =
            new Timeline (
                new KeyFrame ( Duration.ZERO, new KeyValue(opacity, 0.8) ),
                new KeyFrame ( new Duration(800), new KeyValue(opacity, 1.0) )
            );
        fadeIn.play();
    }
}
