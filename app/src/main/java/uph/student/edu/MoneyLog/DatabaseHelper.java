package uph.student.edu.MoneyLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION= 1;
    private static final String DATABASE_NAME = "database.db";

    //Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RECORDS = "records";

    //USER Columns
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private static final String USER_BALANCE= "balance";

    //RECORD Columns
    private static final String RECORD_ID = "id";
    private static final String RECORD_USERNAME = "username";
    private static final String RECORD_CATEGORY = "category";
    private static final String RECORD_POSITIVE = "positive";
    private static final String RECORD_AMOUNT = "amount";
    private static final String RECORD_DESCRIPTION = "description";
    private static final String RECORD_DATE = "date";

    //RECORD create table
    private static final String CREATE_TABLE_RECORDS = "CREATE TABLE " + TABLE_RECORDS + "("
            + RECORD_ID 	        + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + RECORD_USERNAME       + " TEXT,"
            + RECORD_CATEGORY 		+ " TEXT,"
            + RECORD_POSITIVE 		+ " BOOLEAN,"
            + RECORD_AMOUNT 		+ " INTEGER,"
            + RECORD_DESCRIPTION 	+ " TEXT,"
            + RECORD_DATE 			+ " TEXT"
            + ")";

    //RECORD create table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + USER_EMAIL 		+ " TEXT PRIMARY KEY,"
            + USER_PASSWORD 	+ " TEXT,"
            + USER_BALANCE		+ " INTEGER"
            + ")";

    SQLiteDatabase db;

    public DatabaseHelper(Context context)  {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + TABLE_RECORDS);
        db.execSQL("drop table if exists" + TABLE_USERS);

        onCreate(db);
    }

    //inserting in database
    public  boolean userRegister(String email,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        long ins = db.insert(TABLE_USERS, null, cv);
        System.out.println(ins);

        if(ins==-1) {
            return false;
        }
        else {
            return true;
        }
    }
    //checking the email and password
    public boolean userAuth(String email,String password){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] args = new String[]{email,password};

        Cursor cursor = db.rawQuery("select * from "+ TABLE_USERS + " where email=? and password=?", args);

        if(cursor.getCount()>0) return true;
        else return false;
    }

     /*************************
     ** READ RECORDS BY USER **
     *************************/

    public ArrayList<Records> readRecordData(String username) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Records> listOfRecords = new ArrayList<Records>();
        String selectQuery = "SELECT * FROM "+ TABLE_RECORDS + " WHERE "+ RECORD_USERNAME +"=?";
        String[] args = new String[]{username};

        Cursor c = db.rawQuery(selectQuery, args);

        System.out.println("CURSORRRRRRRRRRRR");

        if(c.moveToFirst()) {

            Records rcrd;

            boolean value;


            do {
                rcrd = new Records();
                rcrd.setRecords_ID(         c.getInt(       c.getColumnIndex(RECORD_ID)));
                rcrd.setRecord_Username(    c.getString(    c.getColumnIndex(RECORD_USERNAME)));
                rcrd.setCategory(           c.getString(    c.getColumnIndex(RECORD_CATEGORY)));
                value = c.getInt(                   c.getColumnIndex(RECORD_POSITIVE)) > 0;
                rcrd.setPositive(value);
                rcrd.setAmount(             c.getInt(       c.getColumnIndex(RECORD_AMOUNT)));
                rcrd.setDescription(        c.getString(    c.getColumnIndex(RECORD_DESCRIPTION)));
                rcrd.setDate(               c.getString(    c.getColumnIndex(RECORD_DATE)));
                listOfRecords.add(rcrd);

                System.out.println("CURSORRRRRRRRRRRR");

            } while (c.moveToNext());
        }

        return listOfRecords;
    }

//    VCCW

     /*************************
     * READ RECORD BY recordID*
     *************************/

    public Records readRecordDataByID(int recordID) {

        SQLiteDatabase db = this.getReadableDatabase();

        Records rcrd;

        String selectQuery = "SELECT * FROM "+ TABLE_RECORDS + " WHERE "+ RECORD_ID +"=?";

        String[] args = new String[]{recordID+""};
        Cursor c = db.rawQuery(selectQuery, args);

        rcrd = new Records();
        boolean value;
        rcrd.setRecords_ID(         c.getInt(       c.getColumnIndex(RECORD_ID)));
        rcrd.setRecord_Username(    c.getString(    c.getColumnIndex(RECORD_USERNAME)));
        rcrd.setCategory(           c.getString(    c.getColumnIndex(RECORD_CATEGORY)));
        value = c.getInt(                   c.getColumnIndex(RECORD_POSITIVE)) > 0;
        rcrd.setPositive(value);
        rcrd.setAmount(             c.getInt(       c.getColumnIndex(RECORD_AMOUNT)));
        rcrd.setDescription(        c.getString(    c.getColumnIndex(RECORD_DESCRIPTION)));
        rcrd.setDate(               c.getString(    c.getColumnIndex(RECORD_DATE)));

        return rcrd;
    }


     /*************************
     **     WRITE RECORDS    ** and Update Balance
     *************************/

    public boolean writeRecordData(Records rec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(RECORD_USERNAME,            rec.record_Username);
        cv.put(RECORD_CATEGORY,            rec.category);
        cv.put(RECORD_POSITIVE,            rec.positive);
        cv.put(RECORD_AMOUNT,              rec.amount);
        cv.put(RECORD_DESCRIPTION,         rec.description);
        cv.put(RECORD_DATE,                rec.date);

        long ins = db.insert(TABLE_RECORDS, null, cv);

        String username = rec.record_Username;
        int amount = rec.amount;

        int balBefore = getUserBalance(username);

        if(rec.positive) {
            editUserBalance(username, balBefore+amount);
        } else {
            editUserBalance(username, balBefore-amount);
        }


        if(ins==-1) {
            return false;
        }
        else {
            return true;
        }

    }


     /************************
     **     EDIT RECORDS    **
     ************************/

    public boolean editRecordData(Records newRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(RECORD_USERNAME,            newRecord.record_Username);
        cv.put(RECORD_CATEGORY,            newRecord.category);
        cv.put(RECORD_POSITIVE,            newRecord.positive);
        cv.put(RECORD_AMOUNT,              newRecord.amount);
        cv.put(RECORD_DESCRIPTION,         newRecord.description);
        cv.put(RECORD_DATE,                newRecord.date);

        long ins = db.update(TABLE_RECORDS, cv, RECORD_ID+" = ?", new String[]{newRecord.records_ID+""});

        if(ins==-1) {
            return false;
        }
        else {
            return true;
        }

    }

     /*************************
     **GET RECORDS BY CATEGORY*
     *************************/

    public ArrayList<Records> readRecordDataByCategory(String username, String category) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Records> listOfRecords = new ArrayList<Records>();
        String selectQuery = "SELECT * FROM "+ TABLE_RECORDS + " WHERE "+ RECORD_USERNAME +"=? and "+ RECORD_CATEGORY +"=?";
        String[] args = new String[]{username, category};

        Cursor c = db.rawQuery(selectQuery, args);

        if(c.moveToFirst()) {

            Records rcrd;
            boolean value;

            do {
                rcrd = new Records();
                rcrd.setRecords_ID(         c.getInt(       c.getColumnIndex(RECORD_ID)));
                rcrd.setRecord_Username(    c.getString(    c.getColumnIndex(RECORD_USERNAME)));
                rcrd.setCategory(           c.getString(    c.getColumnIndex(RECORD_CATEGORY)));
                value = c.getInt(                   c.getColumnIndex(RECORD_POSITIVE)) > 0;
                rcrd.setPositive(value);
                rcrd.setAmount(             c.getInt(       c.getColumnIndex(RECORD_AMOUNT)));
                rcrd.setDescription(        c.getString(    c.getColumnIndex(RECORD_DESCRIPTION)));
                rcrd.setDate(               c.getString(    c.getColumnIndex(RECORD_DATE)));

                listOfRecords.add(rcrd);
            } while (c.moveToNext());
        }

        return listOfRecords;
    }

     /*************************
     **   GET USER BALANCE   **
     *************************/

     public int getUserBalance(String username) {
         SQLiteDatabase db = this.getReadableDatabase();

         int balance = 0;

         String selectQuery = "SELECT * FROM "+ TABLE_USERS + " WHERE "+ USER_EMAIL +"=?";
         String[] args = new String[]{username};

         Cursor c = db.rawQuery(selectQuery, args);

         if(c.moveToFirst()) {
             balance = c.getInt(c.getColumnIndex(USER_BALANCE));
         }

         return balance;
     }

     /*************************
     **   EDIT USER BALANCE  **
     *************************/

    public boolean editUserBalance(String username, int newMoney) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_BALANCE,        newMoney);


        long ins = db.update(TABLE_USERS, cv, USER_EMAIL+" = ?", new String[]{username});

        if(ins==-1) {
            return false;
        }
        else {
            return true;
        }

    }




}