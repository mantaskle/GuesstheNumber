package mkle.ktu.guessthenumber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardDatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;

    private final static String DATABASE_NAME = "leaderboard";

    private final static String DATA_TABLE_LEADERBOARD = "table_leaderboard";

    private final static String FIELD_NAME = "name";
    private final static String FIELD_RESULT = "result";
    private final static String FIELD_NUMBER = "number";
    private final static String FIELD_TURN = "turn";

    private final static String FIELD_ID = "id";

    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + DATA_TABLE_LEADERBOARD + " (" +
                FIELD_ID + " INTEGER PRIMARY KEY, " +
                FIELD_NAME + " TEXT," +
                FIELD_RESULT + " INTEGER," +
                FIELD_NUMBER + " INTEGER," +
                FIELD_TURN + " INTEGER" +
                ")";
        db.execSQL(sqlQuery);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "DROP TABLE IF EXISTS " + DATA_TABLE_LEADERBOARD + ";";
        db.execSQL(sqlQuery);
        onCreate(db);
    }

    public LeaderboardDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void recreateTable(SQLiteDatabase db) {
        String sqlQuery = "DROP TABLE IF EXISTS " + DATA_TABLE_LEADERBOARD + ";";
        db.execSQL(sqlQuery);
        onCreate(db);
    }


    public void addEntry(LeaderboardEntry entry) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FIELD_NAME, entry.getName());
        cv.put(FIELD_RESULT, entry.getResult());
        cv.put(FIELD_NUMBER, entry.getNumber());
        cv.put(FIELD_TURN, entry.getTotalTurns());

        db.insert(DATA_TABLE_LEADERBOARD, null, cv);
        db.close();
    }

    public LeaderboardEntry getEntry(int i) {
        SQLiteDatabase db = getReadableDatabase();

        LeaderboardEntry entry = new LeaderboardEntry();

        Cursor cursor = db.query(DATA_TABLE_LEADERBOARD,
                new String[]{FIELD_NAME, FIELD_RESULT, FIELD_NUMBER, FIELD_TURN},
                FIELD_ID + "=?",
                new String[]{Integer.toString(i)},
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                entry.setName(cursor.getString(0));
                entry.setResult(cursor.getInt(1));
                entry.setNumber(cursor.getInt(2));
                entry.setTotalTurns(cursor.getInt(3));

            }
            cursor.close();
        }
        db.close();
        return entry;
    }

    public List<LeaderboardEntry> getAllEntries() {
        List<LeaderboardEntry> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + DATA_TABLE_LEADERBOARD;

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    LeaderboardEntry entry = new LeaderboardEntry();
                    entry.setName(cursor.getString(1));
                    entry.setResult(cursor.getInt(2));
                    entry.setNumber(cursor.getInt(3));
                    entry.setTotalTurns(cursor.getInt(4));
                    result.add(entry);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        System.out.println("total entries: " + result.size());
        return result;
    }

    public void deleteEntry(int i) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(DATA_TABLE_LEADERBOARD, FIELD_ID + "=?", new String[]{Integer.toString(i)});

        db.close();
    }

    public void updateEntry(int i, LeaderboardEntry entry) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FIELD_NAME, entry.getName());
        cv.put(FIELD_RESULT, entry.getResult());
        cv.put(FIELD_NUMBER, entry.getNumber());
        cv.put(FIELD_TURN, entry.getTotalTurns());

        db.update(DATA_TABLE_LEADERBOARD, cv, FIELD_ID + "=?", new String[]{Integer.toString(i)});
        db.close();
    }

}
