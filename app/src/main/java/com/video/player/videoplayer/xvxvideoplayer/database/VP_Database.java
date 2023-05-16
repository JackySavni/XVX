package com.video.player.videoplayer.xvxvideoplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.video.player.videoplayer.xvxvideoplayer.model.VP_HideData;

import java.util.ArrayList;
import java.util.List;

public class VP_Database extends SQLiteOpenHelper {
    private static final String VP_COLUMN_HIDE_NAME = "hide_name";
    private static final String VP_COLUMN_HIDE_PATH = "hide_path";
    private static final String VP_DATABASE_NAME = "hide_video.db";
    private static final int VP_DATABASE_VERSION = 1;
    private static final String VP_TABLE_HIDE = "Hide";

    public VP_Database(Context context) {
        super(context, VP_DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        String CREATEHIDETABLE = "CREATE TABLE Hide ( hide_name TEXT PRIMARY KEY, hide_path TEXT  ) ";
        sQLiteDatabase.execSQL(CREATEHIDETABLE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        String DROPHIDETABLE = "DROP TABLE IF EXISTS Hide";
        sQLiteDatabase.execSQL(DROPHIDETABLE);
        onCreate(sQLiteDatabase);
    }

    public void addHide(VP_HideData hide_Data) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String replace = hide_Data.getVp_name().replace("'", "@#");
        String replace2 = hide_Data.getVp_path().replace("'", "@#");
        contentValues.put(VP_COLUMN_HIDE_NAME, replace);
        contentValues.put(VP_COLUMN_HIDE_PATH, replace2);
        writableDatabase.insert(VP_TABLE_HIDE, null, contentValues);
        writableDatabase.close();
    }

    public int getID() {
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT * FROM Hide", null);
        if (rawQuery == null) {
            return 0;
        }
        rawQuery.moveToFirst();
        int count = rawQuery.getCount();
        rawQuery.close();
        return count;
    }

    public List<VP_HideData> getAllHide() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from Hide", null);
        if (rawQuery.moveToFirst()) {
            do {
                VP_HideData hide_Data = new VP_HideData();
                String replace = rawQuery.getString(rawQuery.getColumnIndexOrThrow(VP_COLUMN_HIDE_NAME)).replace("@#", "'");
                String replace2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(VP_COLUMN_HIDE_PATH)).replace("@#", "'");
                hide_Data.setVp_name(replace);
                hide_Data.setVp_path(replace2);
                arrayList.add(hide_Data);
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        readableDatabase.close();
        return arrayList;
    }

    public VP_HideData getHideData(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        VP_HideData hide_Data = null;
        Cursor rawQuery = readableDatabase.rawQuery("select * from Hide where hide_name='" + str + "'", null);
        if (rawQuery.moveToFirst()) {
            String replace = rawQuery.getString(rawQuery.getColumnIndexOrThrow(VP_COLUMN_HIDE_NAME)).replace("@#", "'");
            String replace2 = rawQuery.getString(rawQuery.getColumnIndexOrThrow(VP_COLUMN_HIDE_PATH)).replace("@#", "'");
            hide_Data = new VP_HideData();
            hide_Data.setVp_name(replace);
            hide_Data.setVp_path(replace2);
        }
        rawQuery.close();
        readableDatabase.close();
        return hide_Data;
    }

    public void deleteHide(String str) {
        getWritableDatabase().delete(VP_TABLE_HIDE, "hide_name = ?", new String[]{str});
    }
}
