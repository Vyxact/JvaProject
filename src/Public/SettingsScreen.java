package Public;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SettingsScreen {
    static Stage root_settings;

    protected static void getStage (Stage stage) { root_settings = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( SettingsScreen.class.getResource(fxml) ) );
        Main.scene = new Scene(Main.root);
        root_settings.setScene(Main.scene);
        root_settings.show();
    }
}
