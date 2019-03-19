package com.bhcc.app.pharmtech.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.bhcc.app.pharmtech.data.model.Score;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Luat on 10/31/2017.
 */

public class ScoreCursorWrapper extends CursorWrapper {
    public ScoreCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Score getScore() {
        int id = getInt(getColumnIndex(ScoreSchema.ScoreTable.Cols.ID));
        double score = getDouble(getColumnIndex(ScoreSchema.ScoreTable.Cols.SCORE));
        int totalQuestion = getInt(getColumnIndex(ScoreSchema.ScoreTable.Cols.TOTAL_QUESTION));
        int correctQuestion = getInt(getColumnIndex(ScoreSchema.ScoreTable.Cols.CORRECT));
        int incorrectQuestion = getInt(getColumnIndex(ScoreSchema.ScoreTable.Cols.INCORRECT));
        long date = getLong(getColumnIndex(ScoreSchema.ScoreTable.Cols.DATE));
        Log.i("CursorWrapper",String.valueOf(date));
        Score mScore = new Score(score,totalQuestion,correctQuestion,incorrectQuestion,new Date(date));
        mScore.setId(id);
        return mScore;
    }
}
