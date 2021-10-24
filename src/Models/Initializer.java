package Models;

import Controllers.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public class Initializer extends Login {

    public static ObservableList<TransactionsHistory> HistoryList = FXCollections.observableArrayList();

    public static void init (Label account_user, Text account_balance, Text balance_deposit, Text balance_withdrawal, Connection connect) throws SQLException {

        final String q = "SELECT * FROM accounts a LEFT JOIN customers c ON c.customer_id = a.customer_id WHERE acc_id = '" + _accountID + "'";

        try ( Statement _fetch = connect.createStatement() ) {
            ResultSet res = _fetch.executeQuery(q);
            while ( res.next() ) {
                account_user.setText( res.getString("username") );
                account_balance.setText( "$" + res.getString( "balance") );
            }
        }

        final String cash_in = "SELECT SUM(deposit) AS CASH_IN FROM deposits WHERE acc_id = '" + _accountID + "'";
        try ( ResultSet r = connect.createStatement().executeQuery(cash_in) ) { while ( r.next() ) CASH_IN = r.getDouble("CASH_IN"); balance_deposit.setText("+$" + CASH_IN); }

        final String w = "SELECT SUM(withdrawal) AS WITHDRW FROM withdrawals WHERE acc_id = '" + _accountID + "'";
        try ( ResultSet r = connect.createStatement().executeQuery(w) ) { while ( r.next() ) WITHDRW = r.getDouble("WITHDRW"); }

        final String t = "SELECT SUM(amount) AS TRANS FROM transfers WHERE acc_from = '" + _accountID + "'";
        try ( ResultSet r = connect.createStatement().executeQuery(t) ) { while ( r.next() ) TRANS = r.getDouble("TRANS"); }

        CASH_OUT = WITHDRW + TRANS;
        balance_withdrawal.setText("-$" + CASH_OUT);
    }


    public static void init_settings (TextField account_username, TextField account_password, Connection connect) throws SQLException {

        final String q = "SELECT username, password FROM customers WHERE customer_id = '" + _customerID + "'";

        try ( Statement _fetch = connect.createStatement() ) {
            ResultSet res = _fetch.executeQuery(q);
            while ( res.next() ) {
                account_username.setText( res.getString("username") );
                account_password.setText( res.getString("password") );
            }
        }

    }

    public static void init_transactions (Label account_user, Connection connect) throws SQLException {

        final String q = "SELECT username FROM customers WHERE customer_id = '" + _customerID + "'";

        try ( Statement _fetch = connect.createStatement() ) {
            ResultSet res = _fetch.executeQuery(q);
            while ( res.next() ) account_user.setText( res.getString("username") );
        }

    }

    public static void init_accounts (Label account_user, Label account_holder, Label account_card, Text account_balance, ListView<String> account_history, ListView<String> account_transactions, Connection connect) throws SQLException {

        final String q = "SELECT * FROM accounts a LEFT JOIN customers c ON c.customer_id = a.customer_id WHERE acc_id = '" + _accountID + "'";

        try ( Statement _fetch = connect.createStatement() ) {
            ResultSet res = _fetch.executeQuery(q);
            while ( res.next() ) {
                account_user.setText( res.getString("username") );
                account_holder.setText( res.getString("firstname") + " " + res.getString("lastname") );
                account_card.setText( res.getString("card_number") );
                account_balance.setText("$" + res.getDouble("balance") );
            }
        }

        final String __SQL = "SELECT * FROM history WHERE acc_id = '" + _accountID + "'";

        try {
            Statement __stmt = connect.createStatement(); ResultSet r = __stmt.executeQuery(__SQL);

            while ( r.next() ) {
                _historyMessage = r.getString("message");
                _historyStatus = r.getString("status");
                _historyDate = r.getString("date");
                _historyTime = r.getString("time");
                account_history.getItems().addAll( Collections.singleton(_historyMessage) );
            }
        } catch (SQLException error) { error.printStackTrace(); }


        final String _SQL = "SELECT DISTINCT d.transac_id D_ID, d.deposit DEP, d.balance D_BALANCE, d.date D_DATE, d.time D_TIME, w.transac_id W_ID, " +
                "w.withdrawal WITHD, w.balance W_BALANCE, w.date W_DATE, w.time W_TIME, t.transfer_id T_ID, t.acc_from T_FROM, t.acc_to T_TO, " +
                "t.amount T_AMOUNT, t.balance T_BALANCE, t.date T_DATE, t.time T_TIME FROM accounts a JOIN transfers t on a.card_number = t.acc_from " +
                "FULL JOIN deposits d on d.cust_id = a.customer_id FULL JOIN withdrawals w ON d.acc_id = w.acc_id WHERE d.acc_id = '" + _accountID + "'";

        try ( Statement _stmt = connect.createStatement() ) {
            ResultSet rs = _stmt.executeQuery(_SQL);

            while ( rs.next() ) {
                _depositID = rs.getString("D_ID");
                _deposit = rs.getString("DEP");
                _depositBalance = rs.getString("D_BALANCE");
                _depositDate = rs.getString("D_DATE");
                _depositTime = rs.getString("D_TIME");

                _withdrawalID = rs.getString("W_ID");
                _withdrawal = rs.getString("WITHD");
                _withdrawalBalance = rs.getString("W_BALANCE");
                _withdrawalDate = rs.getString("W_DATE");
                _withdrawalTime = rs.getString("W_TIME");

                _transferID = rs.getString("T_ID");
                _accountFrom = rs.getString("T_FROM");
                _accountTo = rs.getString("T_TO");
                _transferAmount = rs.getString("T_AMOUNT");
                _transferBalance = rs.getString("T_BALANCE");
                _transferDate = rs.getString("T_DATE");
                _transferTime = rs.getString("T_TIME");

                String dep = "Deposit: $" + _deposit + " \tdate: " + _depositDate + "\ttime: " + _depositTime;
                String with = "Withdrawal: $" + _withdrawal + " \tdate: " + _withdrawalDate + "\ttime: " + _withdrawalTime;
                String trans = "Transferred $" + _transferAmount + " to " + _accountTo + " . current balance: " + _transferBalance;

                if (_deposit != null) {
                    account_transactions.getItems().addAll( Collections.singleton(dep) );

                    HistoryList.add ( new TransactionsHistory( _depositID, "Deposit", _deposit, _depositBalance, _depositDate, _depositTime) );
                }

                if (_withdrawal != null) {
                    account_transactions.getItems().addAll( Collections.singleton(with) );

                    HistoryList.add ( new TransactionsHistory( _withdrawalID, "Withdrawal", _withdrawal, _withdrawalBalance, _withdrawalDate, _withdrawalTime) );
                }
                if (_transferAmount != null) {
                    account_transactions.getItems().addAll( Collections.singleton(trans) );

                    HistoryList.add ( new TransactionsHistory( _transferID, "Transfer", _transferAmount, _transferBalance, _transferDate, _transferTime) );
                }

            }

        }

    }
}
