package co.ocha.pratikum_progmob.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBCarts extends SQLiteOpenHelper {
    //InnerClass, untuk mengatur artibut seperti Nama Tabel, nama-nama kolom dan Query
    public static abstract class MyColumns implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabel = "carts";
        public static final String id = "id";
        public static final String user_id = "user_id";
        public static final String book_id = "book_id";
        public static final String qty = "qty";
        public static final String status = "status";
    }

    private static final String NamaDatabse = "carts.db";
    private static final int VersiDatabase = 1;

    //Query yang digunakan untuk membuat Tabel
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+ DBCarts.MyColumns.NamaTabel+
            "("+ DBCarts.MyColumns.id+" TEXT PRIMARY KEY, "+ DBCarts.MyColumns.user_id+" TEXT NOT NULL, "+ MyColumns.book_id+
            " TEXT NOT NULL, "+ DBCarts.MyColumns.qty+" TEXT NOT NULL, "+ MyColumns.status+
            " TEXT NOT NULL)";

    //Query yang digunakan untuk mengupgrade Tabel
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ DBCarts.MyColumns.NamaTabel;

    public DBCarts(Context context) {
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
