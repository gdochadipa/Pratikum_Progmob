package co.ocha.pratikum_progmob.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBBooks extends SQLiteOpenHelper {
    //InnerClass, untuk mengatur artibut seperti Nama Tabel, nama-nama kolom dan Query
    public static abstract class MyColumns implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabel = "books";
        public static final String id = "id";
        public static final String title = "title";
        public static final String description = "description";
        public static final String writer = "writer";
        public static final String cover = "cover";
        public static final String language = "language";
        public static final String price = "price";
        public static final String stock = "stock";
    }

    private static final String NamaDatabse = "unpi.db";
    private static final int VersiDatabase = 1;

    //Query yang digunakan untuk membuat Tabel
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+MyColumns.NamaTabel+
            "("+MyColumns.id+" TEXT PRIMARY KEY, "+MyColumns.title+" TEXT NOT NULL, "+MyColumns.description+
            " TEXT NOT NULL, "+MyColumns.writer+" TEXT NOT NULL, "+MyColumns.cover+
            " TEXT NOT NULL, "+MyColumns.language+" TEXT NOT NULL, "+MyColumns.price+
            " TEXT NOT NULL, "+MyColumns.stock+" TEXT NOT NULL)";

    //Query yang digunakan untuk mengupgrade Tabel
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+MyColumns.NamaTabel;

    public DBBooks(Context context) {
        super(context, NamaDatabse, null, VersiDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
