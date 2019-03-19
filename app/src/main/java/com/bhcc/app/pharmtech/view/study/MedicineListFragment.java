package com.bhcc.app.pharmtech.view.study;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.MedicineLab;
import com.bhcc.app.pharmtech.data.MedicineSchema;
import com.bhcc.app.pharmtech.data.model.Medicine;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MedicineListFragment extends Fragment {

    // Medicine list adapter
    private MedicineAdapter medicineAdapter;
    private int position;
    private Callbacks mCallbacks;

    // Medicine recycler view
    RecyclerView medicineListRecyclerView;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onMedicineSelected(Medicine Medicine);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public MedicineListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // To register the fragment to receive menu callbacks.
    }

    /**
     * To set up views
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        medicineListRecyclerView = (RecyclerView) view;
        medicineListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // update UI
        updateUI();

        return view;
    }

    /**
     * Added the SearchView interface to the app.
     * The purpose of having a search view is for the user
     * to be able to search for a certain drug in the list that
     * he/she wants to review.
     *
     * @param menu
     * @param menuInflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        // Set up view
        super.onCreateOptionsMenu(menu, menuInflater);

        menuInflater.inflate(R.menu.fragment_medicine_list_drawer, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        MenuItem sortItem = menu.findItem(R.id.sort);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        sortItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showRadioButtonDialog();
                updateUI();
                return false;
            }
        });
        // OnQuerySearchListener
        searchItem.setIcon(R.mipmap.ic_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * When the user submits the query.
             * @param s
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String s) {

                // if there is nothing in the serach bar
                // show every drugs
                if (s.length() == 0) {
                    MedicineLab.get(getContext())
                            .updateMedicineLab(null, null, MedicineSchema.MedicineTable.Cols.GENERIC_NAME);
                    updateUI();

                    return true;
                }

                // Where argument
                String[] args = new String[1];
                args[0] = s + "%";
                Log.i("Test", args[0]);

                // update the list
                MedicineLab.get(getContext())
                        .updateMedicineLab(MedicineSchema.MedicineTable.Cols.GENERIC_NAME + " LIKE ? ",
                                args, MedicineSchema.MedicineTable.Cols.GENERIC_NAME);

                // update UI
                updateUI();

                return true;
            }

            /**
             * When the user is typing in the search bar
             * @param s
             * @return
             */
            @Override
            public boolean onQueryTextChange(String s) {

                // if there is nothing in the serach bar
                // show every drugs
                if (s.length() == 0) {
                    MedicineLab.get(getContext())
                            .updateMedicineLab(null, null, MedicineSchema.MedicineTable.Cols.GENERIC_NAME);
                    updateUI();

                    return true;
                }

                // Where argument
                String[] args = new String[1];
                args[0] = s + "%";
                Log.i("Test", args[0]);

                // update the list
                MedicineLab.get(getContext())
                        .updateMedicineLab(MedicineSchema.MedicineTable.Cols.GENERIC_NAME + " LIKE ? ",
                                args, MedicineSchema.MedicineTable.Cols.GENERIC_NAME);

                // update UI
                updateUI();
                return true;
            }
        });
    }


    /**
     * To update UI
     */
    private void updateUI() {
// set up the adapter w/ new lists
        MedicineLab medicineLab = MedicineLab.get(getActivity());
        List<Medicine> medicines = medicineLab.getMedicines();
        medicineAdapter = new MedicineAdapter(medicines);
        medicineListRecyclerView.setAdapter(medicineAdapter);
    }

    // =====================  ViewHolder =================================//

    public class MedicineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);  //set onclick listener to the override method
            // link variables to widgets
            idTextView = (TextView) itemView.findViewById(R.id.medicine_id);
            nameTextView = (TextView) itemView.findViewById(R.id.medicine_name);
            mFavoriteBox = (CheckBox) itemView.findViewById(R.id.favorite_box);
        }

        /**
         * To set up each holder w/ data
         *
         * @param medicine
         */
        public void bindMedicine(final Medicine medicine) {
            // get a medicine from an argument and set a medicine id and name to text views
            this.medicine = medicine;
            idTextView.setText(this.medicine.getGenericName());
            mFavoriteBox.setChecked(medicine.isFavorite());
            nameTextView.setText(this.medicine.getBrandName());
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


        /**
         * To set up OnClickListener to each holder
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            // show toast notifying a view holder is clicked
            //Toast.makeText(getContext(), medicine.getGenericName() + " Clicked", Toast.LENGTH_SHORT).show();

            mCallbacks.onMedicineSelected(medicine);
        }
    }


// =====================  Adapter ================================= //

    private class MedicineAdapter extends RecyclerView.Adapter<MedicineHolder> {

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
        public MedicineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.fragment_medicine, parent, false);
            return new MedicineHolder(view);
        }

        /**
         * To bind a holder
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MedicineHolder holder, int position) {
            Medicine medicine = medicines.get(position);
            // bind a medicine to a view holder
            holder.bindMedicine(medicine);
        }

        /**
         * get size of the list
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return medicines.size();
        }

        public void setMedicines(List<Medicine> medicines) {
            this.medicines = medicines;
        }
    }

    private static final int ASCENDING_ID = 0;
    private static final int DESCENDING_ID = 1;

    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_sorting_dialog);

        List<String> stringList = new ArrayList<>();  // list to hold choices
        // add choices
        stringList.add("Ascending");
        stringList.add("Descending");

        // Radio group to hold radio buttons
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        // add radio buttons to radio group
        RadioButton rbAscending = new RadioButton(getActivity());
        rbAscending.setText(stringList.get(ASCENDING_ID));
        rbAscending.setId(ASCENDING_ID);
        rg.addView(rbAscending);

        RadioButton rbDescending = new RadioButton(getActivity());
        rbDescending.setText(stringList.get(DESCENDING_ID));
        rbDescending.setId(DESCENDING_ID);
        rg.addView(rbDescending);

        TextView tvOK = (TextView) dialog.findViewById(R.id.choose_sorting_ok_button);
        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = rg.getCheckedRadioButtonId();
                switch (id) {
                    case ASCENDING_ID:
                        MedicineLab.get(getActivity()).sortAscending();
                        Log.i("test3", MedicineLab.get(getActivity()).getMedicines().get(0).getGenericName());
                        break;
                    case DESCENDING_ID:
                        MedicineLab.get(getActivity()).sortDescending();
                        Log.i("test3", MedicineLab.get(getActivity()).getMedicines().get(0).getGenericName());
                        break;
                }
                updateUI();
                dialog.dismiss();
                // Reload current fragment

            }
        });

        dialog.show();
    }
}

