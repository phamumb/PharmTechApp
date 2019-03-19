package com.bhcc.app.pharmtech.view.favorites;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bhcc.app.pharmtech.view.SingleFragmentActivity;

/**
 * Created by Luat on 10/31/2017.
 */

public class FavoriteActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new FavoriteFragment();
    }

    public static Intent newIntent(Context context){
        Intent i = new Intent(context,FavoriteActivity.class);
        return i;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
