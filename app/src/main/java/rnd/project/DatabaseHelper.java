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
    public static final String TABLE_NAME_CAT = "Categories";

    // Kolom namen:
    public static final String COLUMN_0_ID = "ID";
    public static final String COLUMN_1_BEDRAG = "bedrag";
    public static final String COLUMN_2_UITOFIN = "uitOfIn";
    public static final String COLUMN_3_CATEGORIE = "categorie";
    public static final String COLUMN_4_JAAR = "jaar";
    public static final String COLUMN_5_MAAND = "maand";
    public static final String COLUMN_6_DAG = "dag";

    // Kolom namen categories:
    public static final String CAT_COLUMN_0_ID = "ID";
    public static final String CAT_COLUMN_1_CATEGORIES = "categorie";

    public DatabaseHelper(Context context) {
        // Maakt database:
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // Maakt tabellen:
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

        CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME_CAT +
                        " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CAT_COLUMN_1_CATEGORIES + " TEXT)";
        db.execSQL(CREATE_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nog wat uitleg #aandacht
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nog wat uitleg #aandacht
        onUpgrade(db, oldVersion, newVersion);
    }

    // Voegt rij toe aan de tabel:
    public boolean addAmount(double bedrag, String uitofin, String cat, int jaar, int maand, int dag) {

        // Zet de database in write-modus
        SQLiteDatabase db = this.getWritableDatabase();

        // Vult de juiste gegevens in in de juiste kolom
        ContentValues values = new ContentValues();
        values.put(COLUMN_1_BEDRAG, bedrag);
        values.put(COLUMN_2_UITOFIN, uitofin);
        values.put(COLUMN_3_CATEGORIE, cat);
        values.put(COLUMN_4_JAAR, jaar);
        values.put(COLUMN_5_MAAND, maand);
        values.put(COLUMN_6_DAG, dag);

        //  Zet de nieuwe rij in de database, en returnt de primary key waarde van de nieuwe rij (en -1 als het niet is gelukt)
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) return false; //Adding row failed
        else return true; //Adding row succeeded
    }

    //Returnt cursor met alle kolommen voor de gegeven maand in jaar
    public Cursor getMaand(int month, int year) {

        SQLiteDatabase db = this.getReadableDatabase();
        String maand = String.valueOf(month);
        String jaar = String.valueOf(year);
        String query = "SELECT * FROM " +
                TABLE_NAME + " WHERE " +
                COLUMN_5_MAAND + " = \"" + maand + "\" AND " +
                COLUMN_4_JAAR + " = \"" + jaar + "\"";
        Cursor data = db.rawQuery(query, null);

        return data;
    }


    public Cursor getMaandJaar() {
        // Wat returnt deze precies? #aandacht
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT " +
                COLUMN_5_MAAND + "," +
                COLUMN_4_JAAR + " FROM " +
                TABLE_NAME + " WHERE " +
                COLUMN_2_UITOFIN + " != " + "\"empty\"" + " ORDER BY " +
                COLUMN_4_JAAR + " DESC";
        Cursor data = db.rawQuery(query, null);

        return data;
    }


    //Returnt cursor met som van bedrag en categorie voor alle inkomsten of uitgaven
    public Cursor getUitIn(String uitIn) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sum(" +
                COLUMN_1_BEDRAG + "), " +
                COLUMN_3_CATEGORIE + " FROM " +
                TABLE_NAME + " WHERE " +
                COLUMN_2_UITOFIN + " = \"" + uitIn + "\" GROUP BY " +
                COLUMN_3_CATEGORIE;
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getUitInMonthYear(String uitIn, int month, int year) {
        // Wat returnt deze precies? #aandacht
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sum(" +
                COLUMN_1_BEDRAG + "), " +
                COLUMN_3_CATEGORIE + " FROM " +
                TABLE_NAME + " WHERE " +
                COLUMN_2_UITOFIN + " = \"" + uitIn + "\"" + " AND " +
                COLUMN_5_MAAND + " = " + month + " AND " +
                COLUMN_4_JAAR + " = " + year + " AND " +
                COLUMN_2_UITOFIN + " != " + "\"empty\"" + " GROUP BY " +
                COLUMN_3_CATEGORIE;
        Cursor data = db.rawQuery(query, null);
        System.out.println(query);
        return data;
    }

    public Cursor getCategories() {
        // Deze haalt de categorieen op, zodat in het dropdown menu kan worden gekozen tot welke categorie
        // een bedrag behoort.
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT " +
                CAT_COLUMN_1_CATEGORIES + " FROM " +
                TABLE_NAME_CAT;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    /**
     * Queried Bedrag, Uit of In, Categorie en Dag WHERE month = month AND jaar = year.
     * @param month de maand om te querien
     * @param year het jaar om te querien
     * @return De resulterende query als Cursor
     */
    public Cursor getInUitAndDay(int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                COLUMN_1_BEDRAG + " , " +
                COLUMN_2_UITOFIN + " , " +
                COLUMN_3_CATEGORIE + " , " +
                COLUMN_6_DAG + " FROM " +
                TABLE_NAME + " WHERE " +
                COLUMN_5_MAAND + " = " + month + " AND " +
                COLUMN_4_JAAR + " = " + year + " AND " +
                COLUMN_3_CATEGORIE + " != " + "\"empty\"" + " ORDER BY " +
                COLUMN_6_DAG;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    /**
     * Voegt cat toe aan de database
     * @param cat De categorie om te toe te voegen aan de database
     * @return true als succesvol, anders false.
     */
    public boolean addCategory(String cat) {
        // Deze methode voegt een nieuwe invoer toe aan de database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAT_COLUMN_1_CATEGORIES,cat);
        long result = db.insert(TABLE_NAME_CAT, null, values);
        return (result != -1);
    }

    public boolean removeCategory(String cat) {
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(TABLE_NAME_CAT, CAT_COLUMN_1_CATEGORIES + " = " + cat, null) != -1);
    }
}