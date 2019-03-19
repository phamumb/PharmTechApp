package com.bhcc.app.pharmtech.view.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.MedicineLab;
import com.bhcc.app.pharmtech.data.model.Medicine;
import com.bhcc.app.pharmtech.view.study.PagerActivity;

import java.util.List;

/**
 * Created by Luat on 10/31/2017.
 */

public class FavoriteFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private MedicineAdapter medicineAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medicine_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.medicine_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    // Create option menu.

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setHasOptionsMenu(true);
    }
    //-------------------------------------------------------------------------------------------
    //                              UPDATE UI
    //--------------------------------------------------------------------------------------------

    /**
     * To update UI
     */
    private void updateUI() {
        // set up the adapter w/ new lists
        MedicineLab medicineLab = MedicineLab.get(getActivity());
        List<Medicine> medicines = medicineLab.getFavortieMedicines();
        medicineAdapter = new MedicineAdapter(medicines);
        mRecyclerView.setAdapter(medicineAdapter);
    }

    //-------------------------------------------------------------------------------------------
    //                              VIEW HOLDER CREATION
    //--------------------------------------------------------------------------------------------
    private class MedicineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // views
        private TextView idTextView;
        private TextView nameTextView;
        private CheckBox mFavoriteBox;

        // medicine
        private Medicine medicine;

        /**
         * Constructor
         *
         * @param itemView
         */
        public MedicineHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // link variables to widgets
            idTextView = (TextView) itemView.findViewById(R.id.medicine_id);
            nameTextView = (TextView) itemView.findViewById(R.id.medicine_name);
            mFavoriteBox = (CheckBox) itemView.findViewById(R.id.favorite_box);
        }

        public void bindMedicine(final Medicine medicine) {
            // get a medicine from an argument and set a medicine id and name to text views
            this.medicine = medicine;
            idTextView.setText(this.medicine.getGenericName());
            nameTextView.setText("( " + this.medicine.getBrandName() + " )");
            mFavoriteBox.setChecked(medicine.isFavorite());
            mFavoriteBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isFavorite = medicine.isFavorite();
                    if (isFavorite)
                        isFavorite = !isFavorite;
                    else
                        isFavorite = !isFavorite;
                    medicine.setFavorite(isFavorite);
                    MedicineLab.get(getContext()).updateCrime(medicine);
                }
            });
        }

        @Override
        public void onClick(View view) {
            // show toast notifying a view holder is clicked
            //Toast.makeText(getContext(), medicine.getGenericName() + " Clicked", Toast.LENGTH_SHORT).show();

            Intent i = PagerActivity.newIntent(getActivity(),
                    MedicineLab.get(getActivity()).getFavortieMedicines(),medicine.getGenericName());
            startActivity(i);
        }
    }

    //-------------------------------------------------------------------------------------------
    //                              MEDICINE ADAPTER CREATION
    //--------------------------------------------------------------------------------------------

    private class MedicineAdapter extends RecyclerView.Adapter<FavoriteFragment.MedicineHolder> {

        // medicine list
        private List<Medicine> medicines;

        /**
         * Constructor
         *
         * @param medicines
         */
        public MedicineAdapter(List<Medicine> medicines) {
            this.medicines = medicines;
        }

        /**
         * To create a holder
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public FavoriteFragment.MedicineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.fragment_medicine, parent, false);
            return new MedicineHolder(view);
        }

        @Override
        public void onBindViewHolder(MedicineHolder holder, int position) {
            Medicine medicine = medicines.get(position);
            holder.bindMedicine(medicine);
        }

        /**
         * To bind a holder
         * @param holder
         * @param position
         */
        /**
         * get size of the list
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return medicines.size();
        }
    }
}
