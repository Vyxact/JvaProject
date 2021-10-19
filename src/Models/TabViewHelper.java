package Models;

import Controllers.Login;
import Database.Connect;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;


public abstract class TabViewHelper extends Login {

    public static void table_view (TableView<TransactionsHistory> transactions_view) { transactions_view.setItems(HistoryList); }

    public static void table_refresh (TableView<TransactionsHistory> transactions_view) throws SQLException {

        final String _SQL = "SELECT d.transac_id DEP_ID, d.deposit DEP, d.balance DEP_BALANCE, d.date DEP_DATE, d.time DEP_TIME, w.transac_id WITH_ID, " +
                "w.withdrawal WITHD, w.balance WITH_BALANCE, w.date WITH_DATE, w.time WITH_TIME, t.transfer_id TRANS_ID, t.amount TRANS_AMOUNT," +
                "t.balance TRANS_BALANCE, t.date TRANS_DATE, t.time TRANS_TIME FROM deposits d FULL JOIN withdrawals w ON d.acc_id = w.acc_id " +
                "FULL JOIN transfers t on d.acc_id = t.acc_from WHERE d.acc_id = '" + _accountID + "'";

        Connection conn = new Connect().connect();

        try (PreparedStatement _stmt = conn.prepareStatement(_SQL); ResultSet rs = _stmt.executeQuery() ) {
            HistoryList.clear();

            while ( rs.next() ) {

                if ( rs.getString("DEP") != null) {
                    HistoryList.add (
                            new TransactionsHistory (
                                rs.getString("DEP_ID"),
                                "Deposit",
                                rs.getString("DEP"),
                                rs.getString("DEP_BALANCE"),
                                rs.getString("DEP_DATE"),
                                rs.getString("DEP_TIME")
                            )
                    );
                    transactions_view.setItems(HistoryList);
                }

                if ( rs.getString("WITHD") != null) {
                    HistoryList.add (
                            new TransactionsHistory (
                                    rs.getString("WITH_ID"),
                                    "Withdawal",
                                    rs.getString("WITHD"),
                                    rs.getString("WITH_BALANCE"),
                                    rs.getString("WITH_DATE"),
                                    rs.getString("WITH_TIME")
                            )
                    );
                    transactions_view.setItems(HistoryList);
                }

                if ( rs.getString("TRANS_AMOUNT") != null) {
                    HistoryList.add (
                            new TransactionsHistory (
                                    rs.getString("TRANS_ID"),
                                    "Transfer",
                                    rs.getString("TRANS_AMOUNT"),
                                    rs.getString("TRANS_BALANCE"),
                                    rs.getString("TRANS_DATE"),
                                    rs.getString("TRANS_TIME")
                            )
                    );
                    transactions_view.setItems(HistoryList);
                }

            }

            // add refresh table-view fade out
        }
    }

    public static void table_search (TableView<TransactionsHistory> transactions_view, TextField searching) {
        FilteredList<TransactionsHistory> filteredData = new FilteredList<>(HistoryList, b -> true);

        searching.textProperty().addListener(
            ( (observableValue, oldValue, newValue) ->
                filteredData.setPredicate( TransactionsHistory ->
                    {
                        if ( newValue.isEmpty() || newValue.isBlank() ) return true;

                        String searchKeyword = newValue.toLowerCase();

                        if ( TransactionsHistory.getTrans_id().toLowerCase().contains(searchKeyword) ) return true;
                        else if ( TransactionsHistory.getTrans_type().toLowerCase().contains(searchKeyword) ) return true;
                        else if ( TransactionsHistory.getTrans_amount().toLowerCase().contains(searchKeyword) ) return true;
                        else if ( TransactionsHistory.getTrans_balance().toLowerCase().contains(searchKeyword) ) return true;
                        else if ( TransactionsHistory.getTrans_date().toLowerCase().contains(searchKeyword) ) return true;
                        else return TransactionsHistory.getTrans_time().toLowerCase().contains(searchKeyword);
                    }
                )
            )
        );

        SortedList<TransactionsHistory> sortData = new SortedList<>(filteredData);
        sortData.comparatorProperty().bind( transactions_view.comparatorProperty() );

        transactions_view.setItems(sortData);
    }

}
