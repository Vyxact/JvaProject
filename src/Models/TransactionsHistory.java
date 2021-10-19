package Models;

public class TransactionsHistory {

    String trans_id, trans_type, trans_amount, trans_balance, trans_date, trans_time;

    public TransactionsHistory (String trans_id, String trans_type, String trans_amount, String trans_balance, String trans_date, String trans_time) {
        this.trans_id = trans_id;
        this.trans_type = trans_type;
        this.trans_amount = trans_amount;
        this.trans_balance = trans_balance;
        this.trans_date = trans_date;
        this.trans_time = trans_time;
    }

    public String getTrans_id () { return trans_id; }
    public void setTrans_id (String trans_id) { this.trans_id = trans_id; }

    public String getTrans_type () { return trans_type; }
    public void setTrans_type (String trans_type) { this.trans_type = trans_type; }

    public String getTrans_amount () { return trans_amount; }
    public void setTrans_amount (String trans_amount) { this.trans_amount = trans_amount; }

    public String getTrans_balance () { return trans_balance; }
    public void setTrans_balance (String trans_balance) { this.trans_balance = trans_balance; }

    public String getTrans_date () { return trans_date; }
    public void setTrans_date (String trans_date) { this.trans_date = trans_date; }

    public String getTrans_time () { return trans_time; }
    public void setTrans_time (String trans_time) { this.trans_time = trans_time; }
}
