package com.android.study.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MySqliteHelper extends SQLiteOpenHelper {
    private static final String TAG = "MySqliteHelper";
    private static final String DATABASE_NAME = "DBStorage";
    private static final int DATABASE_VERSION = 2;

    private long mMaximumDatabaseSize = 5 * 10 * 1024 * 1024L;//50mb
    static SimpleDateFormat sDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private Context mContext;
    private SQLiteDatabase mDb;


    static final String TABLE_STORAGE = "default_table_storage";
    static final String COLUMN_KEY = "key";
    static final String COLUMN_VALUE = "value";
    static final String COLUMN_TIMESTAMP = "timestamp";
    static final String COLUMN_PERSISTENT = "persistent";

    private static final int SLEEP_TIME_MS = 30;

    private static final String STATEMENT_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s ("
            + COLUMN_KEY
            + " TEXT PRIMARY KEY,"
            + COLUMN_VALUE
            + " TEXT NOT NULL,"
            + COLUMN_TIMESTAMP
            + " TEXT NOT NULL,"
            + COLUMN_PERSISTENT
            + " INTEGER DEFAULT 0"
            + ")";


    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade...oldVersion: " + oldVersion + ", newVersion: " + newVersion);
    }

    private SQLiteDatabase getDatabase(String tableName) {
        ensureDatabase(tableName);
        return mDb;
    }

    /**
     * 添加数据
     *
     * @param tableName
     * @param key
     * @param value
     */
    public boolean addDataToTable(String tableName, String key, String value) {
        SQLiteDatabase database = getDatabase(tableName);
        if (database == null) {
            return false;
        }

        String sql = "INSERT OR REPLACE INTO " + tableName + " VALUES (?,?,?,?);";
        SQLiteStatement statement = null;
        String timeStamp = sDateFormatter.format(new Date());
        try {
            statement = database.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, key);
            statement.bindString(2, value);
            statement.bindString(3, timeStamp);
            statement.bindLong(4, 0);
            statement.execute();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "DefaultWXStorage occurred an exception when execute setItem :" + e.getMessage());
            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 获取数据
     *
     * @param tableName
     * @param key
     * @return
     */
    public String getDataFromTable(String tableName, String key) {
        SQLiteDatabase database = getDatabase(tableName);
        if (database == null) {
            return null;
        }

        Cursor c = database.query(tableName,
                new String[]{COLUMN_VALUE},
                COLUMN_KEY + "=?",
                new String[]{key},
                null, null, null);
        try {
            if (c.moveToNext()) {
                ContentValues values = new ContentValues();
                //update timestamp
                values.put(COLUMN_TIMESTAMP, sDateFormatter.format(new Date()));
                int updateResult = getDatabase(tableName).update(tableName, values, COLUMN_KEY + "= ?", new String[]{key});

                Log.d(TAG, "update timestamp " + (updateResult == 1 ? "success" : "failed") + " for operation [getItem(key = " + key + ")]");
                return c.getString(c.getColumnIndex(COLUMN_VALUE));
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "DefaultWXStorage occurred an exception when execute getItem:" + e.getMessage());
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 删除数据
     *
     * @param tableName
     * @param key
     */
    public boolean deleteDataFromTable(String tableName, String key) {
        SQLiteDatabase database = getDatabase(tableName);
        if (database == null) {
            return false;
        }

        int count = 0;
        try {
            count = database.delete(tableName, COLUMN_KEY + "=?", new String[]{key});
        } catch (Exception e) {
            Log.e(TAG, "DefaultWXStorage occurred an exception when execute removeItem:" + e.getMessage());
            return false;
        }
        return count == 1;
    }

    /**
     * 删除表
     *
     * @param tableName
     */
    public boolean deleteTable(String tableName) {
        if (mDb == null) {
            return false;
        }

        try {
            mDb.execSQL("DROP TABLE IF EXISTS " + tableName);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 从表中获取所有key
     *
     * @param tableName
     * @return
     */
    public List<String> getAllKeysFromTable(String tableName) {
        SQLiteDatabase database = getDatabase(tableName);
        if (database == null) {
            return null;
        }

        List<String> result = new ArrayList<>();
        Cursor c = database.query(tableName, new String[]{COLUMN_KEY}, null, null, null, null, null);
        try {
            while (c.moveToNext()) {
                result.add(c.getString(c.getColumnIndex(COLUMN_KEY)));
            }
            return result;
        } catch (Exception e) {
            Log.e(TAG, "DefaultWXStorage occurred an exception when execute getAllKeys:" + e.getMessage());
            return result;
        } finally {
            c.close();
        }
    }

    private synchronized void ensureDatabase(String tableName) {
        if (mDb != null && mDb.isOpen()) {
            return;
        }

        try {
            // Sometimes retrieving the database fails. We do 2 retries: first without database deletion
            // and then with deletion.
            for (int tries = 0; tries < 2; tries++) {
                try {
                    if (tries > 0) {
                        //delete db and recreate
                        deleteDB();
                    }
                    mDb = getWritableDatabase();
                    break;
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
                // Wait before retrying.
                try {
                    Thread.sleep(SLEEP_TIME_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            if (mDb == null) {
                return;
            }

            createTableIfNotExists(mDb, tableName);

            mDb.setMaximumSize(mMaximumDatabaseSize);
        } catch (Throwable e) {
            mDb = null;
            Log.d(TAG, "ensureDatabase failed, throwable = " + e.getMessage());
        }

    }

    private boolean deleteDB() {
        closeDatabase();
        return mContext.deleteDatabase(DATABASE_NAME);
    }

    public synchronized void closeDatabase() {
        if (mDb != null && mDb.isOpen()) {
            mDb.close();
            mDb = null;
        }
    }

    private void createTableIfNotExists(@NonNull SQLiteDatabase db, String tableName) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'", null);
            if (cursor != null && cursor.getCount() > 0) {
                return;
            }

            String sqlCreateTable = String.format(STATEMENT_CREATE_TABLE, tableName);
            db.execSQL(sqlCreateTable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
