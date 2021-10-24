package Controllers;

import Models.Initializer;
import Public.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class Account extends Login {
    @FXML
    private Label account_user, account_holder, account_card;

    @FXML
    private Text account_balance;

    @FXML
    private ListView<String> account_history, account_transactions;

    public void initialize () throws SQLException { Initializer.init_accounts ( account_user, account_holder, account_card, account_balance, account_history, account_transactions, connect() ); }

    @FXML
    void accountBtn () throws IOException { Switcher.switcher("/Views/account.fxml"); }

    @FXML
    void depositBtn () throws IOException { Switcher.switcher("/Views/deposit.fxml"); }

    @FXML
    void withdrawalBtn () throws IOException { Switcher.switcher("/Views/withdrawal.fxml"); }

    @FXML
    void transferBtn () throws IOException { Switcher.switcher("/Views/transfer.fxml"); }

    @FXML
    void transactionsBtn () throws IOException { Switcher.switcher("/Views/transactions.fxml"); }

    @FXML
    void settingsBtn () throws IOException { Switcher.switcher("/Views/settings.fxml"); }

    @FXML
    private void logoutBtn () throws IOException, SQLException { disconnect(); Switcher.switcher("/Views/login.fxml"); }
}
