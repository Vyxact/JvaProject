package Public;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DepositScreen {
    static Stage root_deposit;

    protected static void getStage (Stage stage) { root_deposit = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( DepositScreen.class.getResource(fxml) ) );
        Main.scene = new Scene(Main.root);
        root_deposit.setScene(Main.scene);
        root_deposit.show();
    }
}
