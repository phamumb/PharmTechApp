package com.bhcc.app.pharmtech.view.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.model.Medicine;

import java.util.List;

/**
 * Created by Luat on 11/10/2017.
 */

public class PagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private static List<Medicine> mMedicineList;

    private static final String EXTRA_GENERIC_NAME ="generic_name";

    public static Intent newIntent(Context context,List<Medicine> mMedicines,String genericName)
    {
        Intent i = new Intent(context,PagerActivity.class);
        i.putExtra(EXTRA_GENERIC_NAME,genericName);
        mMedicineList = mMedicines;
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pager);
        mViewPager = (ViewPager)findViewById(R.id.medicine_card_view_pager);

        FragmentManager fm = getSupportFragmentManager();
        final String genericName = (String)getIntent().getSerializableExtra(EXTRA_GENERIC_NAME);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Medicine mMedicine = mMedicineList.get(position);
                Log.i("pagerAdapter","Generic Name: " + mMedicine.getGenericName());
                return CardFragment.newInstance(mMedicine.getGenericName());
            }

            @Override
            public int getCount() {
                return mMedicineList.size();
            }
        });

        // This loop scan through the List of crime and return position matches with crimeID.
        for (int i = 0; i < mMedicineList.size(); i++) {
            if (mMedicineList.get(i).getGenericName().equals(genericName)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
