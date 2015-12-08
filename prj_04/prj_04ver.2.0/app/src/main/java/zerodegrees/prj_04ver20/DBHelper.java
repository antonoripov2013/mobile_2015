package zerodegrees.prj_04ver20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    Context context;

    public DBHelper(Context context) {
        super(context, "dialogs.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table dialogs (" +
                "_id integer primary key autoincrement," +
                "user_id integer," +
                "body text," +
                "uid integer)");
        db.execSQL("create table friends (" +
                "_id integer primary key autoincrement," +
                "user_id integer," +
                "first_name text," +
                "last_name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
