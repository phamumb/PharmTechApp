package com.bhcc.app.pharmtech.view.filter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bhcc.app.pharmtech.view.SingleFragmentActivity;

/**
 * Created by Luat on 10/30/2017.
 */

public class FilterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new FilterFragment();
    }

    public static Intent newIntent(Context context)
    {
        return new Intent(context,FilterActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
