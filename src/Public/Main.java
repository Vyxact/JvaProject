package Public;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start (Stage stage) throws IOException {
        stage.setTitle("E-Banking");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/withdrawal.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void main (String[] args) { launch(args); }
}
