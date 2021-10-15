package Public;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Main extends Application {
    static Pane root;
    static Scene scene;
    String fxml = "/Views/login.fxml";

    @Override
    public void start (@NotNull Stage stage) throws Exception {
        stage.setTitle("E-Banking");
        root = FXMLLoader.load( Objects.requireNonNull( getClass().getResource(fxml) ) );
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        LoginScreen.getStage(stage);
    }
    public static void main (String[] args) { launch(args); }
}
