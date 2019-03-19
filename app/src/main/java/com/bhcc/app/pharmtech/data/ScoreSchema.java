package com.bhcc.app.pharmtech.data;

/**
 * Created by Luat on 10/31/2017.
 */

public class ScoreSchema {
    public static final class ScoreTable {
        public static final String NAME = "Scores";

        public static final class Cols {
            public static final String ID  = "_id";
            public static final String SCORE = "Score";
            public static final String TOTAL_QUESTION ="TotalQuestion";
            public static final String CORRECT = "Correct";
            public static final String INCORRECT = "Incorrect";
            public static final String DATE = "Date";
        }

        public static final int NUM_COLS = 6;
    }
}
