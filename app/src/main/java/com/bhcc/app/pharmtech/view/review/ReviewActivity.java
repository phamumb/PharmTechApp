package com.bhcc.app.pharmtech.view.review;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.model.Score;
import com.bhcc.app.pharmtech.view.SingleFragmentActivity;

/**
 * Created by Luat on 10/26/2017.
 */

public class ReviewActivity extends SingleFragmentActivity implements ReviewDetailFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new ReviewListFragment();
    }

    public static Intent newItent(Context context) {
        return new Intent(context, ReviewActivity.class);
    }

    @Override
    public void onScoreUpdate(Score score) {
        Intent review = newItent(this);
        startActivity(review);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
