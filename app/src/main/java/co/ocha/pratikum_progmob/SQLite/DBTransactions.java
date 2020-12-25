package co.ocha.pratikum_progmob.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBTransactions extends SQLiteOpenHelper {
    //InnerClass, untuk mengatur artibut seperti Nama Tabel, nama-nama kolom dan Query
    public static abstract class MyColumns implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabel = "transactions";
        public static final String id = "id";
        public static final String user_id = "user_id";
        public static final String address_id = "address_id";
        public static final String total = "total";
        public static final String timeout = "timeout";
        public static final String status = "status";
    }

    private static final String NamaDatabse = "transactions.db";
    private static final int VersiDatabase = 1;

    //Query yang digunakan untuk membuat Tabel
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+ DBTransactions.MyColumns.NamaTabel+
            "("+ DBTransactions.MyColumns.id+" TEXT PRIMARY KEY, "+ DBTransactions.MyColumns.user_id+" TEXT NOT NULL, "+ DBTransactions.MyColumns.address_id+
            " TEXT NOT NULL, "+ MyColumns.total+" TEXT NOT NULL, "+ MyColumns.timeout+" TEXT NOT NULL, "+ MyColumns.status+" TEXT NOT NULL)";

    //Query yang digunakan untuk mengupgrade Tabel
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ DBTransactions.MyColumns.NamaTabel;

    public DBTransactions(Context context) {
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
