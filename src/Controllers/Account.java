package Controllers;

import Public.LoginScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;

public class Account extends Login {
    @FXML
    private Label account_user, account_holder, account_card;

    @FXML
    private Text account_balance;

    @FXML
    private ListView<String> account_history, account_transactions;

    @Override
    public void initialize () {
        account_user.setText(_username);
        account_holder.setText(_firstname + " " + _lastname);
        account_card.setText(_cardNumber);
        account_balance.setText("$" + _accountBalance);
        account_history.getItems().addAll(_history);
        account_transactions.getItems().addAll(_transactions);
    }

    @FXML
    void depositBtn () throws IOException { LoginScreen.switcher("/Views/deposit.fxml"); }

    @FXML
    void withdrawalBtn () throws IOException { LoginScreen.switcher("/Views/withdrawal.fxml"); }

    @FXML
    void transferBtn () throws IOException { LoginScreen.switcher("/Views/transfer.fxml"); }

    @FXML
    void transactionsBtn () throws IOException { LoginScreen.switcher("/Views/transactions.fxml"); }

    @FXML
    void settingsBtn () throws IOException { LoginScreen.switcher("/Views/settings.fxml"); }

    @FXML
    void logoutBtn () throws IOException {
        _depositID = _deposit = _depositBalance = _depositDate = _depositTime = null;
        _withdrawalID = _withdrawal = _withdrawalBalance = _withdrawalDate = _withdrawalTime = null;
        _transferID =  _accountFrom =  _accountTo =  _transferAmount =  _transferBalance =  _transferDate =  _transferTime = null;
        _historyMessage = _historyStatus =  _historyDate =  _historyTime = null;

        LoginScreen.switcher("/Views/login.fxml");
    }
}
