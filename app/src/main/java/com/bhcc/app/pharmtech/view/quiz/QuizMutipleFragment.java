package com.bhcc.app.pharmtech.view.quiz;

import android.animation.StateListAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.MedicineLab;
import com.bhcc.app.pharmtech.data.MedicineSchema;
import com.bhcc.app.pharmtech.data.model.Medicine;
import com.bhcc.app.pharmtech.data.model.Question;
import com.bhcc.app.pharmtech.view.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Luat on 11/13/2017.
 */

public class QuizMutipleFragment extends Fragment {
    private TextView mDoneQuestionText;
    private ProgressBar mProgressBar;
    private TextView mQuestionText;
    private Button mNextButton;
    private Button[] mButtons;
    private LinearLayout mQuestionLayout;
    private String fileName;

    // Data List
    private List<Question> mQuestionList;
    private List<Medicine> mMedicines;

    // Lists
    private String[] topicList;
    private String[] fieldList;
    private static int numQuiz;
    private final int NUM_CHOICES = 4;

    // variables
    private int index;
    private int correct;
    private int randomCorrect;

    // Bundle argument id
    private static final String EXTRA_TOPIC_LIST = "extra: topic list";
    private static final String EXTRA_FIELD_LIST = "extra: field list";
    private static final String EXTRA_NUM_QUIZ = "extra: num quiz";

    public static QuizMutipleFragment newInstance(String[] topicList, String[] fieldList,
                                                  int numQuiz) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_TOPIC_LIST, topicList);
        bundle.putStringArray(EXTRA_FIELD_LIST, fieldList);
        bundle.putInt(EXTRA_NUM_QUIZ, numQuiz);
        QuizMutipleFragment quizFragment = new QuizMutipleFragment();
        quizFragment.setArguments(bundle);
        return quizFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File reviewInfo = new File(getActivity().getFilesDir(), MainActivity.fileName);
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(reviewInfo, true)));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyy_HHmmss");
            fileName = dateFormat.format(new Date()).toString();
            printWriter.append(fileName + "\n");
            printWriter.close();
        } catch (Exception ex) {
            Log.i("File","Could not create file");
        }

        topicList = getArguments().getStringArray(EXTRA_TOPIC_LIST);
        fieldList = getArguments().getStringArray(EXTRA_FIELD_LIST);
        numQuiz = getArguments().getInt(EXTRA_NUM_QUIZ, 0);
        mMedicines = findMedicinesQuiz(topicList);
        mQuestionList = new ArrayList<>();
        index = 0;
        correct=0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        mDoneQuestionText = (TextView) v.findViewById(R.id.done_question_text);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mQuestionText = (TextView) v.findViewById(R.id.question_text);
        mNextButton = (Button) v.findViewById(R.id.next_button);
        mQuestionLayout = (LinearLayout) v.findViewById(R.id.question_layout);
        mButtons = new Button[NUM_CHOICES];

        // Create button.
        for (int i = 0; i < NUM_CHOICES; i++) {
            mButtons[i] = new Button(getActivity());
            createButton(mButtons[i]);
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment index.
                if (index < numQuiz - 1) {
                    index++;
                    setEnable(true);
                    updateUI();
                }else{
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, SummaryFragment.createFragment(correct,numQuiz), "NewFragmentTag");
                    ft.commit();
                }
            }
        });

        updateUI();
        return v;
    }

    private void createButton(final Button button) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);
        button.setLayoutParams(params);
        button.setWidth(351);
        button.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setBackgroundColor(getResources().getColor(R.color.white));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = button.getText().toString();
                String correctText = mQuestionList.get(index).getCorrectAnswer();
                if (buttonText.equals(correctText)) {
                    button.setBackgroundColor(getResources().getColor(R.color.correct_color));
                    correct++;
                } else {
                    button.setBackgroundColor(getResources().getColor(R.color.incorrect_color));
                    mButtons[randomCorrect].setBackgroundColor(getResources().getColor(R.color.correct_color));
                }

                mQuestionList.get(index).setUserAnswer(button.getText().toString());
                mNextButton.setBackground(getResources().getDrawable(R.drawable.corner_radius_enable));
                mNextButton.setEnabled(true);
                setEnable(false);
            }
        });

        mQuestionLayout.addView(button);
    }

    private void saveToFile() {
        File file = new File(getActivity().getFilesDir(), fileName);
        try {
            PrintWriter printWriter = new PrintWriter(file);

            for (Question q : mQuestionList) {
                printWriter.write(q.getQuestion() + " ?\n");
                /*printWriter.write("Your Answer: " +
                        ((RadioButton) getView().findViewById(rgChoices[i].getCheckedRadioButtonId())).getText() + "\n");
                        */
                Log.i("WriteToFile",q.getQuestion());
                printWriter.write(q.getUserAnswer() + "\n");
                printWriter.write((q.getCorrectAnswer() + "\n"));
            }

            printWriter.close();
        } catch (Exception ex) {
        }
    }

    private void updateUI() {

        double percent = ((index + 1) / (double) numQuiz) * 100;
        mDoneQuestionText.setText("Question " + (index + 1) + "/" + numQuiz + "(" + (int) percent + "%)");
        mProgressBar.setProgress((int) percent);

        // Shuffle the List.
        Collections.shuffle(mMedicines);

        // Disable button and reset the view
        mNextButton.setBackground(getResources().getDrawable(R.drawable.corner_radius_disable));
        mNextButton.setEnabled(false);
        for (Button b : mButtons) {
            b.setBackgroundColor(Color.WHITE);
        }

        // Insert emtpy question object to List<Question>
        Question mQuestion = new Question();
        mQuestionList.add(mQuestion);

        // Random the question
        int randomField = generateRandom(fieldList.length);
        randomCorrect = generateRandom(NUM_CHOICES);
        Log.i("Random Field", fieldList[randomField]);

        String question = "What is the " + fieldList[randomField] + " of " +
                mMedicines.get(index).getGenericName() + "/" + mMedicines.get(index).getBrandName();

        mQuestionList.get(index).setQuestion(question);
        mQuestionText.setText(question);

        // Set correct answer for specific question list.
        ArrayList<String> choices = new ArrayList<>();
        String tempCorrect = randomIndex(fieldList[randomField],index,mMedicines.get(index));

        // Set text for random correct button.
        mButtons[randomCorrect].setText(tempCorrect);
        mQuestionList.get(index).setCorrectAnswer(tempCorrect);
        Log.i("Correct", "RandomCorrect: " + randomCorrect);
        choices.add(tempCorrect);

        //Set text for random incorrect button.
        for (int i = 0; i < NUM_CHOICES; i++) {
            String temp = "";
            if (i != randomCorrect) {
                do {
                    int random = generateRandom(MedicineLab.get(getActivity()).getMedicines().size());
                    temp = randomIndex(fieldList[randomField],random,MedicineLab.get(getActivity()).getMedicines().get(random));
                    mButtons[i].setText(temp);
                } while (choices.contains(temp));
                choices.add(temp);
            }
        }
    }

    private int generateRandom(int length) {
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt() % length);
        Log.i("Random Number", String.valueOf(randomNumber));
        return randomNumber;
    }

    private String randomIndex(String list, int random, Medicine medicine)
    {
        String temp ="";
        switch (list) {
            case MedicineSchema.MedicineTable.Cols.PURPOSE:
                temp = medicine.getPurpose();
                break;
            case MedicineSchema.MedicineTable.Cols.DEASCH:
                temp = medicine.getDeaSch();
                break;
            case MedicineSchema.MedicineTable.Cols.SPECIAL:
                temp = medicine.getSpecial();
                break;
            case MedicineSchema.MedicineTable.Cols.CATEGORY:
                temp = medicine.getCategory();
                break;
        }
        if (temp.equals("") || temp.equals("-")) {
            temp = "N/A";
        }
        return temp;
    }

    private void setEnable(boolean flag) {
        for (int i = 0; i < mQuestionLayout.getChildCount(); i++) {
            View child = mQuestionLayout.getChildAt(i);
            child.setEnabled(flag);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStopMulti","get called");
        saveToFile();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("OnPauseMulti","get called");
        saveToFile();
    }

    /**
     * to find medicines from the database
     *
     * @param topicList
     * @return List of medicines
     */
    private List<Medicine> findMedicinesQuiz(String[] topicList) {

        String whereArgs = "(";
        for (int i = 0; i < topicList.length; i++) {
            whereArgs += "?";
            if (i != topicList.length - 1)
                whereArgs += ",";
        }
        whereArgs += ")";

        List<Medicine> medicinesQuiz = MedicineLab.get(getActivity())
                .getSpecificMedicine("StudyTopic IN " + whereArgs, topicList, MedicineSchema.MedicineTable.Cols.GENERIC_NAME);
        return medicinesQuiz;
    }

}
