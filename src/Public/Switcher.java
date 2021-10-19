package Public;

import Models.ScreenTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Switcher {
    static Stage root_login;

    protected static void getStage (Stage stage) { root_login = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( Switcher.class.getResource(fxml) ) );
        ScreenTransition.fadeIn(Main.root);
        Main.scene = new Scene(Main.root);
        root_login.setScene(Main.scene);
        root_login.show();
    }
}
