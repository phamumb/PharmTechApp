package com.bhcc.app.pharmtech.view.quiz;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.ScoreLab;
import com.bhcc.app.pharmtech.data.model.Score;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Date;

/**
 * Created by LuatPham on 10/26/2017.
 */

public class SummaryFragment extends android.support.v4.app.Fragment {
    private ArcProgress progressBar;
    private TextView mCorrectText;
    private TextView mIncorrectText;
    private TextView mTimeTextView;
    private TextView mDateText;
    private static String ARG_TOTAL = "total";
    private static String ARG_CORRECT = "correct";

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.fragment_summary,null);

        double correct = (int)getArguments().getSerializable(ARG_CORRECT);
        double total = (int)getArguments().getSerializable(ARG_TOTAL);
        double incorrect = total -correct;
        double percentFinished = ((correct/total)*100);

        Log.i("Summary","Testing Percent: " + percentFinished+"Correct: " + correct+",Total: "+total);

        mCorrectText = (TextView)view.findViewById(R.id.correct_score);
        mIncorrectText = (TextView)view.findViewById(R.id.incorrect_score);
        mDateText = (TextView) view.findViewById(R.id.date_text_view);
        mTimeTextView = (TextView)view.findViewById(R.id.time_text_view);

        progressBar = (ArcProgress)view.findViewById(R.id.arc_progress);
        progressBar.setProgress((int)percentFinished);

        String dateFormat="EEE ,MMMM dd, yyyy";
        String dateString = DateFormat.format(dateFormat,new Date()).toString();
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        int seconds = dt.getSeconds();
        String curTime = hours + ":" + minutes + ":" + seconds;

        mCorrectText.setText((int)correct+" Correct");
        mIncorrectText.setText((int)(total-correct)+" Incorrect");
        mDateText.setText(dateString);
        mTimeTextView.setText(curTime);

        Score score = new Score(percentFinished,(int)total,(int)correct,(int)incorrect,dt);
        ScoreLab.get(getActivity()).addScore(score);

        return view;
    }

    public static SummaryFragment createFragment(int correct,int totalQuestion)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TOTAL,totalQuestion);
        args.putSerializable(ARG_CORRECT,correct);
        SummaryFragment fragment = new SummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
