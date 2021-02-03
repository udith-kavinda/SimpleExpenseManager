package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    private Context context;

    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();
    }

    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO persistanceTransactionDAO = new PersistanceTransactionDAO(context);
        setTransactionsDAO(persistanceTransactionDAO);

        AccountDAO perSistanceAccountDAO = new PersistanceAccountDAO(context);
        setAccountsDAO(perSistanceAccountDAO);

        // dummy data
      //  Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
      //  Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
      //  DBHandler db = new DBHandler(context);
     //   getAccountsDAO().addAccount(dummyAcct1);
     //   getAccountsDAO().addAccount(dummyAcct2);
     //   db.addAccount(dummyAcct1);
      //  db.addAccount(dummyAcct2);
      //  getAccountsDAO().addAccount(dummyAcct1);
       // getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
