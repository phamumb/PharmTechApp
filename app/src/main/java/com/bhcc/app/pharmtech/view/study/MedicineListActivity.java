package com.bhcc.app.pharmtech.view.study;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bhcc.app.pharmtech.data.MedicineLab;
import com.bhcc.app.pharmtech.data.model.Medicine;
import com.bhcc.app.pharmtech.view.SingleFragmentActivity;

/**
 * Created by Luat on 10/25/2017.
 */

public class MedicineListActivity extends SingleFragmentActivity implements MedicineListFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new MedicineListFragment();
    }

    public static Intent newIntent(Context context){
        Intent i = new Intent(context,MedicineListActivity.class);
        return i;
    }

    @Override
    public void onMedicineSelected(Medicine Medicine) {
        Intent intent = PagerActivity.newIntent(this, MedicineLab.get(this).getMedicines(),Medicine.getGenericName());
        startActivity(intent);
    }
}
