package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String DB_NAME= "180629K";
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_TRANSACTION = "trans";

    private static  final String ID = "id";
    private static  final String ACCOUNT_NUMBER = "acc_no";
    private static  final String BANK = "bank";
    private static  final String ACCOUNT_HOLDER = "holder";
    private static  final String INITIAL_BALANCE = "balance";

    private static  final String ID2 = "id";
    private static  final String DATE = "date";
    private static  final String TYPE = "type";
    private static  final String AMOUNT = "amount";

    public List<Account> account;
    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


    public DBHandler( @Nullable  Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String TABLE_ACCOUNT_QUERY = "CREATE TABLE "+TABLE_ACCOUNT+" "+
                "("
                +ID+" INTEGER PRIMARY KEY "+"AUTOINCREMENT,"
                +ACCOUNT_NUMBER+" TEXT,"
                +BANK+ " TEXT,"
                +ACCOUNT_HOLDER+" TEXT,"
                +INITIAL_BALANCE+" DOUBLE" +
                ");";

        String TABLE_TRANSACTION_QUERY = "CREATE TABLE "+TABLE_TRANSACTION+" "+
                "("
                +ID+" INTEGER PRIMARY KEY "+"AUTOINCREMENT,"
                +DATE+" STRING,"
                +ACCOUNT_NUMBER+ " TEXT,"
                +TYPE+" TEXT,"
                +AMOUNT+" DOUBLE" +
                ");";

        sqLiteDatabase.execSQL(TABLE_ACCOUNT_QUERY);
        sqLiteDatabase.execSQL(TABLE_TRANSACTION_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_ACCOUNT_QUERY = "DROP TABLE IF EXISTS "+ TABLE_ACCOUNT;
        String DROP_TRANSACTION_QUERY = "DROP TABLE IF EXISTS "+ TABLE_TRANSACTION;

        sqLiteDatabase.execSQL(DROP_ACCOUNT_QUERY);
        sqLiteDatabase.execSQL(DROP_TRANSACTION_QUERY);

        onCreate(sqLiteDatabase);

    }

    public void addAccount(Account account){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ACCOUNT_NUMBER, account.getAccountNo());
        contentValues.put(BANK, account.getBankName());
        contentValues.put(ACCOUNT_HOLDER, account.getAccountHolderName());
        contentValues.put(INITIAL_BALANCE, account.getBalance());


        sqLiteDatabase.insert(TABLE_ACCOUNT, null, contentValues );
        sqLiteDatabase.close();
    }

    public Account getAccount(String accNo){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_ACCOUNT+" WHERE "+ACCOUNT_NUMBER +" = ?",new String[]{accNo});
        Account account = null;
        if(data.getCount() == 0){
            return account;
        }else{
            while(data.moveToNext()){
                String accountNo = data.getString(1);
                String bankName = data.getString(2);
                String accountHolderName = data.getString(3);
                double balance = data.getDouble(4);
                account = new Account(accountNo,bankName,accountHolderName,balance);
            }
            return account;
        }
    }

    public ArrayList<Account> getAllAccounts(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_ACCOUNT,null);
        ArrayList<Account> accountList=new ArrayList<>();
        if(data.getCount()==0){
            return accountList;
        }else{

            while(data.moveToNext()){
                String accountNo = data.getString(1);
                String bankName = data.getString(2);
                String accountHolderName = data.getString(3);
                double balance = data.getDouble(4);
                accountList.add(new Account(accountNo,bankName,accountHolderName,balance));
            }
            return accountList;
        }
    }


    public void removeAccount(String acc_no){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_ACCOUNT, ACCOUNT_NUMBER +"=?", new String[]{acc_no});
        sqLiteDatabase.close();
    }

    public boolean updateAccount(Account account){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NUMBER, account.getAccountNo());
        contentValues.put(BANK, account.getBankName());
        contentValues.put(ACCOUNT_HOLDER, account.getAccountHolderName());
        contentValues.put(INITIAL_BALANCE, account.getBalance());
        long result = sqLiteDatabase.update(TABLE_ACCOUNT,contentValues,ACCOUNT_NUMBER+"= ?",new String[]{account.getAccountNo()});
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public void addTransaction(Transaction trans){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE, formatter.format(trans.getDate()));
        contentValues.put(ACCOUNT_NUMBER, trans.getAccountNo());
        contentValues.put(TYPE, trans.getExpenseType().toString());
        contentValues.put(AMOUNT, trans.getAmount());

        sqLiteDatabase.insert(TABLE_TRANSACTION, null, contentValues );
        sqLiteDatabase.close();
    }

    public ArrayList<Transaction> getTransactions(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_TRANSACTION,null);
        return ArrayTransactions(data);
    }

    public ArrayList<Transaction> getTransactions(int limit){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_TRANSACTION +" ORDER BY " + ID+ " DESC LIMIT "+ limit,null);
       // Cursor data = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_TRANSACTION+" DESC LIMIT "+limit,null);
        return ArrayTransactions(data);
    }



    private ArrayList<Transaction> ArrayTransactions(Cursor data){

        ArrayList<Transaction> transactionList=new ArrayList<>();
        DateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        if(data.getCount()==0){
            return transactionList;
        }else{

            while(data.moveToNext()){
                String accountNo = data.getString(2);
                Date date = new Date();
                ExpenseType expenseType = ExpenseType.valueOf(data.getString(3));
                try {
                    date =  format.parse(data.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                double amount = data.getDouble(4);
                transactionList.add(new Transaction(date,accountNo,expenseType,amount));
            }
            return transactionList;
        }
    }
}
