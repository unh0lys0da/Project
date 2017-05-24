package rnd.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bedragen.db";
    public static final String TABLE_NAME = "Bedragen";
    //Column names:
    public static final String COLUMN_0_ID = "ID";
    public static final String COLUMN_1_BEDRAG = "bedrag";
    public static final String COLUMN_2_UITOFIN = "uitOfIn";
    public static final String COLUMN_3_CATEGORIE = "categorie";
    public static final String COLUMN_4_JAAR = "jaar";
    public static final String COLUMN_5_MAAND = "maand";
    public static final String COLUMN_6_DAG = "dag";


    public DatabaseHelper(Context context) {
        //creates database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        //Creates table
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                        " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_1_BEDRAG + " REAL," +
                        COLUMN_2_UITOFIN + " TEXT," +
                        COLUMN_3_CATEGORIE + " TEXT," +
                        COLUMN_4_JAAR + " INTEGER," +
                        COLUMN_5_MAAND + " INTEGER," +
                        COLUMN_6_DAG + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Adds amount to table
    public boolean addAmount(double bedrag, String uitofin, String cat, int jaar, int maand, int dag) {
        //get database in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create values for a new row
        ContentValues values = new ContentValues();
        values.put(COLUMN_1_BEDRAG, bedrag);
        values.put(COLUMN_2_UITOFIN, uitofin);
        values.put(COLUMN_3_CATEGORIE, cat);
        values.put(COLUMN_4_JAAR, jaar);
        values.put(COLUMN_5_MAAND, maand);
        values.put(COLUMN_6_DAG, dag);

        // Insert the new row, returning the primary key value of the new row or -1 if failed
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) return false; //Adding row failed
        else return true; //Adding row succeeded
    }

    //Returns cursor with all columns for given month in year
    public Cursor getMaand(int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String maand = String.valueOf(month);
        String jaar = String.valueOf(year);
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_5_MAAND + " = \"" + maand + "\" AND " +
                COLUMN_4_JAAR + " = \"" + jaar + "\"", null);
        return data;
    }

    public Cursor getMaandJaar() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT DISTINCT " + COLUMN_5_MAAND + "," + COLUMN_4_JAAR + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_4_JAAR, null);
        return data;
    }
    //Returns cursor with sum of bedrag and categorie for all inkomsten or uitgaven
    public Cursor getUitIn(String uitIn) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT sum(" + COLUMN_1_BEDRAG + "), " + COLUMN_3_CATEGORIE + " FROM " + TABLE_NAME + " WHERE "
                + COLUMN_2_UITOFIN + " = \"" + uitIn + "\" GROUP BY " + COLUMN_3_CATEGORIE, null);
        return data;
    }

    public Cursor getUitInMonthYear(String uitIn, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sum(" + COLUMN_1_BEDRAG + "), " + COLUMN_3_CATEGORIE + " FROM " + TABLE_NAME + " WHERE "
                + COLUMN_2_UITOFIN + " = \"" + uitIn + "\"" + " AND " + COLUMN_5_MAAND + " = " + month + " AND " + COLUMN_4_JAAR + " = " + year +  " GROUP BY " + COLUMN_3_CATEGORIE;
        Cursor data = db.rawQuery("SELECT sum(" + COLUMN_1_BEDRAG + "), " + COLUMN_3_CATEGORIE + " FROM " + TABLE_NAME + " WHERE "
                + COLUMN_2_UITOFIN + " = \"" + uitIn + "\"" + " AND " + COLUMN_5_MAAND + " = " + month + " AND " + COLUMN_4_JAAR + " = " + year +  " GROUP BY " + COLUMN_3_CATEGORIE, null);
        System.out.println(query);
        return data;
    }

    public Cursor getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT " + COLUMN_3_CATEGORIE + " FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}