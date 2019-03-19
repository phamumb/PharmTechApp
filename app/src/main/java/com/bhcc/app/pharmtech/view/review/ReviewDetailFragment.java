package com.bhcc.app.pharmtech.view.review;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.ScoreLab;
import com.bhcc.app.pharmtech.data.model.Question;
import com.bhcc.app.pharmtech.data.model.Score;
import com.bhcc.app.pharmtech.view.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDetailFragment extends Fragment {

    // Bundle argument id
    private static final String EXTRA_FILE_NAME = "extra: fileName";
    private static final String EXTRA_DATE = "extra_date";
    private static final String EXTRA_POSITION = "extra_position";
    private Score mScore;
    private RecyclerView mRecyclerView;
    private QuestionAdapter mAdapter;
    private List<Question> mQuestions;
    private int position;

    // file name
    private String fileName;

    private Callbacks mCallbacks;

    public interface Callbacks{
        void  onScoreUpdate(Score score);
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
    /**
     * To crate a new fragment w/ bundle arguments
     * @param fileName
     * @return ReviewDetailFragment
     */
    public static ReviewDetailFragment newInstance(String fileName, Date date, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_FILE_NAME, fileName);
        bundle.putSerializable(EXTRA_DATE,date);
        bundle.putSerializable(EXTRA_POSITION,position);
        ReviewDetailFragment reviewDetailFragment = new ReviewDetailFragment();
        reviewDetailFragment.setArguments(bundle);
        return reviewDetailFragment;
    }
    private void updateScore()
    {
        mCallbacks.onScoreUpdate(mScore);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestions = new ArrayList<>();
        // Read data from the file
        // Store each line to the string
        this.position = (int)getArguments().getSerializable(EXTRA_POSITION);
        fileName = getArguments().getSerializable(EXTRA_FILE_NAME).toString();
        Date date = (Date)getArguments().getSerializable(EXTRA_DATE);
        mScore = ScoreLab.get(getActivity()).getScore(date);
        File file = new File(getActivity().getFilesDir(), fileName);
        String question = "";
        String userAnswer = "";
        String correctAnswer = "";

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                question = scanner.nextLine();
                userAnswer = scanner.nextLine();
                correctAnswer = scanner.nextLine();
                Question mQuestion = new Question(question,userAnswer,correctAnswer);
                mQuestions.add(mQuestion);
            }
        }
        catch (Exception ex) {}
    }

    public ReviewDetailFragment() {
        // Required empty public constructor
    }


    /**
     * To set up views & read data from the file
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up views
        View view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.medicine_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Testing File output.
        /*for(int i =0;i<mQuestions.size();i++)
        {
            System.out.println("Question: " + mQuestions.get(i).getQuestion()+
            ",User Answer: " + mQuestions.get(i).getUserAnswer() +", Correct Answer: " + mQuestions.get(i).getCorrectAnswer());
        }*/
        setHasOptionsMenu(true);
        mAdapter = new QuestionAdapter(mQuestions);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_medicine_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_item:
                Log.i("ScoreDate","Date: " +mScore.getDate().getTime());
                ScoreLab.get(getActivity()).deleteScore(mScore);

                try {

                    // Delete the review file
                    File fileDeleted = new File(getActivity().getFilesDir(), fileName);
                    fileDeleted.delete();

                    // Remove file name from the list
                    ReviewListFragment.fileNames.remove(position);

                    // Write update file name list to the info file
                    File file = new File(getActivity().getFilesDir(), MainActivity.fileName);
                    PrintWriter printWriter = new PrintWriter(file);

                    for (String fileName : ReviewListFragment.fileNames) {
                        printWriter.write(fileName + "\n");
                    }

                    printWriter.close();


                }
                catch (Exception ex) {}

                updateScore();
                getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //-------------------------------------------------------------------------------------------
    //                              VIEW HOLDER CREATION
    //--------------------------------------------------------------------------------------------
    private class QuestionHolder extends RecyclerView.ViewHolder{

        // views
        private TextView mQuestionText;
        private TextView mUserAnswer;
        private TextView mCorrectAnswer;

        // medicine
        private Question mQuestion;

        /**
         * Constructor
         *
         * @param itemView
         */
        public QuestionHolder(View itemView) {
            super(itemView);
            // link variables to widgets
            mQuestionText = (TextView) itemView.findViewById(R.id.question_text);
            mUserAnswer = (TextView) itemView.findViewById(R.id.incorrect_text);
            mCorrectAnswer = (TextView) itemView.findViewById(R.id.correct_text);
        }

        public void bindQuestion(final Question question) {
            // get a medicine from an argument and set a medicine id and name to text views
            mQuestion = question;
            String userAnswer = mQuestion.getUserAnswer().toLowerCase();
            String correctAnswer = mQuestion.getCorrectAnswer().toLowerCase();
            mQuestionText.setText(mQuestion.getQuestion());
            if(userAnswer.equals(correctAnswer))
            {
                mUserAnswer.setVisibility(TextView.GONE);
            }else{
                mUserAnswer.setVisibility(TextView.VISIBLE);
                mUserAnswer.setText(mQuestion.getUserAnswer());
            }
            mCorrectAnswer.setText(mQuestion.getCorrectAnswer());
        }
    }

    //-------------------------------------------------------------------------------------------
    //                              MEDICINE ADAPTER CREATION
    //--------------------------------------------------------------------------------------------

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        // medicine list
        private List<Question> mQuestionList;

        /**
         * Constructor
         *
         * @param questions
         */
        public QuestionAdapter(List<Question> questions) {
            this.mQuestionList = questions;
        }

        /**
         * To create a holder
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.fragment_question, parent, false);
            return new QuestionHolder(view);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            Question question = mQuestionList.get(position);
            holder.bindQuestion(question);
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
            return mQuestionList.size();
        }
    }

}
