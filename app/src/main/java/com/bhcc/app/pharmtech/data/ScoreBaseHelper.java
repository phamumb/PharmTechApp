package com.bhcc.app.pharmtech.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LuatPham on 11/11/2017.
 */

public class ScoreBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "scores.db";

    public ScoreBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ScoreSchema.ScoreTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ScoreSchema.ScoreTable.Cols.SCORE +", " +
                ScoreSchema.ScoreTable.Cols.TOTAL_QUESTION + ", " +
                ScoreSchema.ScoreTable.Cols.CORRECT + ", " +
                ScoreSchema.ScoreTable.Cols.INCORRECT + ", " +
                ScoreSchema.ScoreTable.Cols.DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
