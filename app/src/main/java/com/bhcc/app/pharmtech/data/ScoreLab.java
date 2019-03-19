package com.bhcc.app.pharmtech.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhcc.app.pharmtech.data.model.Score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Luat on 10/31/2017.
 */

public class ScoreLab {
    private static ScoreLab sScoreLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static ScoreLab get(Context context){
        if(sScoreLab == null)
            sScoreLab = new ScoreLab(context);
        return sScoreLab;
    }

    private ScoreLab(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new ScoreBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Score score) {
        ContentValues values = new ContentValues();
        values.put(ScoreSchema.ScoreTable.Cols.SCORE,score.getScore());
        values.put(ScoreSchema.ScoreTable.Cols.TOTAL_QUESTION, score.getTotalQuestion());
        values.put(ScoreSchema.ScoreTable.Cols.CORRECT,score.getCorrect());
        values.put(ScoreSchema.ScoreTable.Cols.INCORRECT,score.getIncorrect());
        values.put(ScoreSchema.ScoreTable.Cols.DATE,score.getDate().getTime());
        return values;
    }

    private ScoreCursorWrapper queryScores(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ScoreSchema.ScoreTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new ScoreCursorWrapper(cursor);
    }

    private ScoreCursorWrapper rawQuery(String string){
        Cursor cursor = mDatabase.rawQuery(string,null);
        return new ScoreCursorWrapper(cursor);
    }

    public void updateScore(Score s){
        ContentValues values = getContentValues(s);
        mDatabase.update(ScoreSchema.ScoreTable.NAME,null,ScoreSchema.ScoreTable.Cols.DATE+ " = ?",
                new String[]{""+s.getDate().getTime()});
    }
    public Score getScore(Date date){
        ScoreCursorWrapper cursor = rawQuery("SELECT * FROM " +ScoreSchema.ScoreTable.NAME+" WHERE " +
        ScoreSchema.ScoreTable.Cols.DATE + " = " + date.getTime());

        Log.i("TestDate",String.valueOf(date.getTime()));
        try {
            if (cursor.getCount() == 0) {
                Log.i("Score","Did not find");
                return null;
            }
            Log.i("Score","find it");
            cursor.moveToFirst();
            return cursor.getScore();
        } finally {
            cursor.close();
        }
    }

    public void addScore(Score score)
    {
        ContentValues values = getContentValues(score);
        mDatabase.insert(ScoreSchema.ScoreTable.NAME, null, values);
    }

    public void deleteScore(Score s)
    {
        mDatabase.delete(ScoreSchema.ScoreTable.NAME,
                ScoreSchema.ScoreTable.Cols.DATE + " = " + s.getDate().getTime(),null );
    }

    public List<Score> getScores() {
        List<Score> scores = new ArrayList<>();
        ScoreCursorWrapper cursor = queryScores(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                scores.add(cursor.getScore());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return scores;
    }
}
