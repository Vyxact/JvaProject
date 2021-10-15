package Public;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TransferScreen {
    static Stage root_transfer;

    protected static void getStage (Stage stage) { root_transfer = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( TransferScreen.class.getResource(fxml) ) );
        Main.scene = new Scene(Main.root);
        root_transfer.setScene(Main.scene);
        root_transfer.show();
    }
}
