package com.bhcc.app.pharmtech.view.quiz;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bhcc.app.pharmtech.view.SingleFragmentActivity;

/**
 * Created by Luat on 10/26/2017.
 */

public class QuizActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new SelectQuizFragment();
    }

    public static Intent newIntent(Context context)
    {
        Intent i = new Intent(context,QuizActivity.class);
        return i;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
