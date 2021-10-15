package Public;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WithdrawalScreen {
    static Stage root_withdrawal;

    protected static void getStage (Stage stage) { root_withdrawal = stage; }

    public static void switcher (String fxml) throws IOException {
        Main.root = FXMLLoader.load( Objects.requireNonNull( WithdrawalScreen.class.getResource(fxml) ) );
        Main.scene = new Scene(Main.root);
        root_withdrawal.setScene(Main.scene);
        root_withdrawal.show();
    }
}
