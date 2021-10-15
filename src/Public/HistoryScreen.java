package Public;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HistoryScreen {
    static Stage root_history;

    protected static void getStage (Stage stage) { root_history = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( HistoryScreen.class.getResource(fxml) ) );
        Main.scene = new Scene(Main.root);
        root_history.setScene(Main.scene);
        root_history.show();
    }
}
