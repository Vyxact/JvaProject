package Public;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RegisterScreen {
    static Stage root_register;

    protected static void getStage (Stage stage) { root_register = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( RegisterScreen.class.getResource(fxml) ) );
        Main.scene = new Scene(Main.root);
        root_register.setScene(Main.scene);
        root_register.show();
    }
}
