package Public;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AccountScreen {
    static Stage root_account;

    protected static void getStage (Stage stage) { root_account = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( AccountScreen.class.getResource(fxml) ) );
        Main.scene = new Scene(Main.root);
        root_account.setScene(Main.scene);
        root_account.show();
    }
}
