package com.bhcc.app.pharmtech.view.review;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.ScoreLab;
import com.bhcc.app.pharmtech.data.model.Score;
import com.bhcc.app.pharmtech.view.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static android.support.v7.widget.helper.ItemTouchHelper.*;

/**
 * Created by LuatPham on 11/11/2017.
 */

public class ReviewListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ScoreAdapter mScoreAdapter;
    // Lists
    private List<String> dateList;
    public static List<String> fileNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // To register the fragment to receive menu callbacks.
        // set up lists
        dateList = new ArrayList<>();
        fileNames = new ArrayList<>();

        // get data from the file
        // each review file's name
        File reviewInfo = new File(getActivity().getFilesDir(), MainActivity.fileName);
        try {
            Scanner fileInput = new Scanner(reviewInfo);
            while (fileInput.hasNextLine()) {
                // add to file name list
                String temp = fileInput.nextLine();
                fileNames.add(temp);
                Log.i("test5", temp);

                // modify date & time
                StringBuilder stringBuilder = new StringBuilder(temp);
                stringBuilder.insert(2, '/');
                stringBuilder.insert(5, '/');
                stringBuilder.insert(11, ':');
                stringBuilder.insert(14, ':');
                stringBuilder.replace(8, 9, " ");
                temp = stringBuilder.toString();

                // add to date list
                Log.i("test5", temp);
                dateList.add(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("test5", "Error\n");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.recycler_view));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
// set up the adapter w/ new lists
        ScoreLab scoreLab = ScoreLab.get(getActivity());
        List<Score> scores = scoreLab.get(getActivity()).getScores();
        mScoreAdapter = new ScoreAdapter(scores);
        mRecyclerView.setAdapter(mScoreAdapter);
    }

    // =====================  ViewHolder =================================//

    private class ScoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mScoreTextView;
        private TextView mCorrectTextView;
        private TextView mPassFailText;
        private TextView mTimeText;
        private TextView mDate;
        private View mPassFailColor;
        private Score mScore;

        /**
         * Constructor
         *
         * @param v
         */
        public ScoreHolder(View v) {
            super(v);
            itemView.setOnClickListener(this);  //set onclick listener to the override method
            // link variables to widgets
            mScoreTextView = (TextView) v.findViewById(R.id.score_text);
            mCorrectTextView = (TextView) v.findViewById(R.id.correct_text);
            mTimeText = (TextView) v.findViewById(R.id.time_text);
            mPassFailText = (TextView) v.findViewById(R.id.pass_fail_text);
            mDate = (TextView) v.findViewById(R.id.date_text);
            mPassFailColor = (View) v.findViewById(R.id.pass_failt_color);
        }

        /**
         * To set up each holder w/ data
         *
         * @param mScore
         */
        public void bindScore(final Score mScore) {
            // get a medicine from an argument and set a medicine id and name to text views
            this.mScore = mScore;
            mScoreTextView.setText((int) mScore.getScore() + "%");
            mCorrectTextView.setText(mScore.getCorrect() + " out of " + mScore.getTotalQuestion());

            String dateFormat = "MMMM dd,yyyy";
            String dateString = DateFormat.format(dateFormat, mScore.getDate()).toString();
            mDate.setText(dateString);

            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
            String time = localDateFormat.format(mScore.getDate());
            mTimeText.setText(time);

            if (mScore.getScore() < 70) {
                mPassFailText.setText("Failed");
                mPassFailColor.setBackgroundColor(Color.RED);
            }

            Log.i("TestID",""+mScore.getId());
        }


        /**
         * To set up OnClickListener to each holder
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            ReviewDetailFragment fragment =
                    ReviewDetailFragment.newInstance(fileNames.get(getPosition()),mScore.getDate(),getAdapterPosition());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, "ReviewListFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }


// =====================  Adapter ================================= //

    private class ScoreAdapter extends RecyclerView.Adapter<ReviewListFragment.ScoreHolder> {

        // medicine list
        private List<Score> Scores;

        /**
         * Constructor
         *
         * @param scores
         */
        public ScoreAdapter(List<Score> scores) {
            this.Scores = scores;
        }

        /**
         * To create a holder
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.fragment_report_detail, parent, false);
            return new ScoreHolder(view);
        }

        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Score score = Scores.get(position);
            holder.bindScore(score);
        }

        /**
         * get size of the list
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return Scores.size();
        }

        public void remove(int position) {
            ScoreLab.get(getActivity()).deleteScore(Scores.get(position));
            Scores.remove(position);
        }

        public void setScores(List<Score> list) {
            Scores = list;
        }
    }

}
